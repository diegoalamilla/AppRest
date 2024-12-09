package com.example.apprest.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;




@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsConfig {
   
    private String accessKeyId;
    private String secretAccessKey;
    private String sessionToken;
    private String region;
    private String bucketName;
    private String topicArn;
    private String tableArn;
    
    public String getAccessKeyId() {
        return accessKeyId;
    }
    public String getSecretAccessKey() {
        return secretAccessKey;
    }
    public String getRegion() {
        return region;
    }
    public String getBucketName() {
        return bucketName;
    }
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    public String getSessionToken() {
        return sessionToken;
    }
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }
    public String getTableArn() {
        return tableArn;
    }
    public void setTableArn(String tableArn) {
        this.tableArn = tableArn;
    }
    

    
    
}
