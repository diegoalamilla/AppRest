package com.example.apprest.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;

import com.example.apprest.config.AwsConfig;
import com.example.apprest.models.SesionesAlumnos;




@Service
public class DynamoService {
    
    @Autowired
    AwsConfig awsConfig;

    @Bean
    public AmazonDynamoDB dynamoClient(){

         AWSCredentials awsCredentials = new BasicSessionCredentials(awsConfig.getAccessKeyId(), awsConfig.getSecretAccessKey(), awsConfig.getSessionToken());
        
        return AmazonDynamoDBClientBuilder.standard().
        withRegion("us-east-1").
        withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).
        build();



    }



    public boolean loginSession(SesionesAlumnos sessionData){
        AmazonDynamoDB dynamoDbClient = dynamoClient();

        try{
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDbClient);
        mapper.save(sessionData);
        return true;
        }catch(Exception e){
            return false;
        }

    }


    public boolean verifySession(String sessionString){

        SesionesAlumnos session = getSession(sessionString);
        if (session == null){
            return false;
        }
        if (session.getActive() == false){
            return false;
        }
       return true;
    }

    public void logoutSession(String sessionString){
        AmazonDynamoDB dynamoDbClient = dynamoClient();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDbClient);
        SesionesAlumnos session = getSession(sessionString);
        session.setActive(false);
        mapper.save(session);
        
    }

    public SesionesAlumnos getSession(String sessionString){
        AmazonDynamoDB dynamoDbClient = dynamoClient();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDbClient);

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            scanExpression.addFilterCondition(
                "sessionString", 
                new Condition()
                    .withComparisonOperator("EQ")
                    .withAttributeValueList(new AttributeValue().withS(sessionString))
            );

            List<SesionesAlumnos> result = mapper.scan(SesionesAlumnos.class, scanExpression);

            if(result.size() == 0){
                return null;
            }else{
                return result.get(0);
            }

    }
}
