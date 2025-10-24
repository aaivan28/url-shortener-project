package com.github.aaivan28.cache.redis.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
public class RedisConfiguration implements CachingConfigurer {

    private static final int BATCH_SIZE = 1000;

    private final CacheProperties cacheProperties;

    @Bean
    CacheManager cacheManager(final RedisConnectionFactory connectionFactory) {
        final RedisCacheConfiguration defaultCacheConfig = this.getRedisCacheConfiguration(this.cacheProperties);

        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.scan(BATCH_SIZE)));

        if (this.cacheProperties.getRedis().isEnableStatistics()) {
            redisCacheManagerBuilder = redisCacheManagerBuilder.enableStatistics();
        }

        return redisCacheManagerBuilder.build();
    }

    private RedisCacheConfiguration getRedisCacheConfiguration(final CacheProperties cacheProperties) {

        final CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        RedisCacheConfiguration redisConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(this.getValueSerializer());

        if (redisProperties.getTimeToLive() != null) {
            redisConfig = redisConfig.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            redisConfig = redisConfig.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {
            redisConfig = redisConfig.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            redisConfig = redisConfig.disableKeyPrefix();
        }

        return redisConfig;
    }

    private RedisSerializationContext.SerializationPair<Object> getValueSerializer() {
        final RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer();
        return RedisSerializationContext.SerializationPair.fromSerializer(serializer);
    }
}
