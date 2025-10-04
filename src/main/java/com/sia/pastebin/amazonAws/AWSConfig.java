package com.sia.pastebin.amazonAws;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public StaticCredentialsProvider credentials(){
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }
    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(credentials())
                .build();
    }
    @Bean
    public S3AsyncClient s3AsyncClient(){
        return S3AsyncClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(credentials())
                .build();
    }
}