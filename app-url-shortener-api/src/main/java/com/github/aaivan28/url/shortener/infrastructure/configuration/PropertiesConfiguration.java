package com.github.aaivan28.url.shortener.infrastructure.configuration;

import com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.properties.UrlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({UrlProperties.class})
public class PropertiesConfiguration {
}
