package com.sprint.mission.discodeit.storage.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "discodeit.storage.s3")
public class AwsProperties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;

    public Credentials getCredentials() {
        return new Credentials(accessKey, secretKey);
    }

    @Getter
    public static class Credentials {

        private final String accessKey;
        private final String secretKey;

        public Credentials(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }
    }
}
