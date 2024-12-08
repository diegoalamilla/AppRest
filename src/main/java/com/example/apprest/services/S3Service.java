package com.example.apprest.services;


import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.apprest.config.AwsConfig;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Service
public class S3Service {

    @Autowired
    private AwsConfig awsConfig;
    
    @Bean
    private AmazonS3 s3Client() {
        AWSCredentials awsCredentials = new BasicSessionCredentials(awsConfig.getAccessKeyId(), awsConfig.getSecretAccessKey(), awsConfig.getSessionToken());
        return AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }

    public String uploadFile(MultipartFile file, String key) {
        AmazonS3 s3Client = s3Client();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
    
            PutObjectRequest objectRequest = new PutObjectRequest(
                awsConfig.getBucketName(),
                key,
                file.getInputStream(),
                objectMetadata
            );
            s3Client.putObject(
                objectRequest
            );
    
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo el archivo a S3: " + e.getMessage());
        }
    
        return s3Client.getUrl(awsConfig.getBucketName(), key).toString();
    }}
