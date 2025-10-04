package com.sia.pastebin.amazonAws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AWSService {
    private static final String bucket = "pasterbin-bucket";
    @Autowired S3Client s3Client;
    @Autowired S3AsyncClient asyncClient;

    public String uploadFile(String content) throws IOException{
        String key = UUID.randomUUID().toString();
        log.info("in method uploadFile");
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build(), RequestBody.fromBytes(content.getBytes(StandardCharsets.UTF_8)));
        return key;
    }

    public CompletableFuture<String> uploadFileAsync(String content) throws IOException{
        String key = UUID.randomUUID().toString();
        return asyncClient.putObject(
                PutObjectRequest.builder().bucket(bucket).key(key).build(),
                AsyncRequestBody.fromBytes(content.getBytes(StandardCharsets.UTF_8))
        ).thenApply(res -> key);
    }

    public String download(String key) throws IOException {
        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build())) {
            return new BufferedReader(new InputStreamReader(s3Object)).lines().collect(Collectors.joining("\n"));
        } catch (NoSuchKeyException e) {
            throw new IOException("File not found in S3 with key: " + key, e);
        }
    }

    public void deleteObject(String key){
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
