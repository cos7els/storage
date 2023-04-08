package org.cos7els.storage.config;

import io.github.rctcwyvrn.blake3.Blake3;
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
    @Value("${STORAGE_ENDPOINT}")
    private String minioEndpoint;
    @Value("${STORAGE_ACCESS_KEY}")
    private String minioAccessKey;
    @Value("${STORAGE_SECRET_KEY}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }

    @Bean
    public Blake3 blake3 () {
        return Blake3.newInstance();
    }
}