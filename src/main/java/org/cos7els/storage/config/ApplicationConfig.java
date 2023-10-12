package org.cos7els.storage.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableJpaRepositories(basePackages = "org.cos7els.storage.repository")
@EnableAspectJAutoProxy
public class ApplicationConfig {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.key.access}")
    private String accessKey;
    @Value("${minio.key.secret}")
    private String secretKey;
    @Value("${minio.bucket.photo}")
    private String photosBucket;
    @Value("${minio.bucket.thumbnail}")
    private String thumbnailsBucket;

    @Bean
    public MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(photosBucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(photosBucket).build());
        }
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(thumbnailsBucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(thumbnailsBucket).build());
        }
        return minioClient;
    }
}