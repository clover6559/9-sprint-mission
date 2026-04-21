package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
            "discodeit.storage.type=s3",
            "aws.region=ap-northeast-2",
            "aws.credentials.access-key=dummy",
            "aws.credentials.secret-key=dummy",
            "aws.s3.bucket=test-bucket"
        })
class S3BinaryContentStorageTest {

    @Autowired(required = false)
    private BinaryContentStorage storage;

    @Test
    @DisplayName("S3 저장소 빈 등록 확인")
    void testBeanRegistration() {
        Assertions.assertThat(storage).isNotNull();
        Assertions.assertThat(storage).isInstanceOf(S3BinaryContentStorage.class);
    }
}
