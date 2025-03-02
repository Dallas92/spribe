package com.example.spribe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Set;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Set<Long>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Set<Long>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());

        // Use JSON serializer for values (serializes Set<Long>)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Set up for hash keys and values (optional)
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}

