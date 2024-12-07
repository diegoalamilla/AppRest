package com.example.apprest.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucketName}")
    private String bucketName;

    @GetMapping("/test")
    public String test() {
        return "Access Key: " + accessKeyId + "\n" + "Secret Access Key: " + secretAccessKey + "\n" + "Region: " + region + "\n" + "Bucket Name: " + bucketName;
    }
}