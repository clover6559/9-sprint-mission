package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
public class AWSS3Test {

    private final String bucketName;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public AWSS3Test() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
        }

        this.bucketName = props.getProperty("AWS_S3_BUCKET");
        String accessKey = props.getProperty("AWS_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_SECRET_KEY");
        Region region = Region.of(props.getProperty("AWS_REGION"));

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Test
    @DisplayName("S3 파일 업로드 테스트")
    void uploadTest() throws IOException {
        String key = "test-folder/hello.txt";
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".txt");
        java.nio.file.Files.writeString(tempFile, "Hello S3!");

        PutObjectRequest request =
                PutObjectRequest.builder().bucket(bucketName).key(key).build();

        s3Client.putObject(request, RequestBody.fromFile(tempFile));
    }

    @Test
    @DisplayName("S3 파일 다운로드 테스트")
    void downloadTest() throws IOException {
        String key = "test-folder/hello.txt";
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("download", ".txt");
        java.nio.file.Files.writeString(tempFile, "Download Me!");

        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(), RequestBody.fromFile(tempFile));

        GetObjectRequest request =
                GetObjectRequest.builder().bucket(bucketName).key(key).build();
        try {
            s3Client.getObject(request);
        } catch (software.amazon.awssdk.services.s3.model.NoSuchKeyException e) {
            log.error("S3에 파일이 존재하지 않습니다. Key: {}", key);
            throw e;
        }
    }

    @Test
    @DisplayName("Presigned URL 생성 테스트")
    void presignedUrlTest() {
        String key = "test-folder/hello.txt";

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder().bucket(bucketName).key(key).build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();

        String url = s3Presigner.presignGetObject(presignRequest).url().toString();
        Assertions.assertNotNull(url);
    }

    @AfterEach
    void tearDown() {
        String key = "test-folder/hello.txt";

        DeleteObjectRequest deleteRequest =
                DeleteObjectRequest.builder().bucket(bucketName).key(key).build();

        s3Client.deleteObject(deleteRequest);
    }
}
