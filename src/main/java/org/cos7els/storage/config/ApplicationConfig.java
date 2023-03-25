package org.cos7els.storage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.cos7els.storage.repository")
@EnableAspectJAutoProxy
public class ApplicationConfig {
}
