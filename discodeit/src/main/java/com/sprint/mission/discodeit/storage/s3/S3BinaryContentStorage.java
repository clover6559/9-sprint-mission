package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucket;

    public S3BinaryContentStorage(AwsProperties props) {
        this.accessKey = props.getCredentials().getAccessKey();
        this.secretKey = props.getCredentials().getSecretKey();
        this.region = props.getRegion();
        this.bucket = props.getBucket();
    }

    private S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String key = binaryContentId.toString();
        getS3Client()
                .putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromBytes(bytes));
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String key = binaryContentId.toString();
        return getS3Client()
                .getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto metaData) {
        String presignedUrl = generatePresignedUrl(metaData.id().toString(), metaData.contentType());

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl))
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .responseContentType(contentType)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        }
    }
}
