package com.github.lemongrab32.config;

import com.github.lemongrab32.controller.dto.CalculationResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofHours(2))
			.disableCachingNullValues()
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair
					.fromSerializer(new Jackson2JsonRedisSerializer<>(CalculationResponse.class)));

		return RedisCacheManager.builder()
			.cacheDefaults(redisCacheConfiguration)
			.build();
	}

}
