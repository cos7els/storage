package org.cos7els.storage.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.cos7els.storage.repository")
@EnableAspectJAutoProxy
@PropertySource("classpath:storage.properties")
public class ApplicationConfig {
    @Value("${storage_endpoint}")
    private String endpoint;
    @Value("${storage_access_key}")
    private String accessKey;
    @Value("${storage_secret_key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient
                .builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}