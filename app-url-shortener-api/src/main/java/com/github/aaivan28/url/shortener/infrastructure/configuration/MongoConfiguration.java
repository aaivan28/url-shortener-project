package com.github.aaivan28.url.shortener.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence")
public class MongoConfiguration {
}
