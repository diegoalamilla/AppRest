package com.example.apprest.services;



import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import com.example.apprest.config.AwsConfig;

@Service
public class SNSService {

    @Autowired
    AwsConfig awsConfig;

    @Bean
    public SnsClient snsClient() {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(
            awsConfig.getAccessKeyId(),
            awsConfig.getSecretAccessKey(),
            awsConfig.getSessionToken()
        );
    return SnsClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
        
    }

    public boolean sendEmail(String message, String subject) {
        SnsClient snsClient = snsClient();
        String topicArn = awsConfig.getTopicArn();
        try {

            PublishRequest request = PublishRequest.builder()
            .subject(subject)
            .message(  message )
            .topicArn(topicArn)
            .build();

            PublishResponse response = snsClient.publish(request);
          
            return response.sdkHttpResponse().isSuccessful();
           
        } catch (SnsException e) {
           return false;
        }
        
    }

    
}
