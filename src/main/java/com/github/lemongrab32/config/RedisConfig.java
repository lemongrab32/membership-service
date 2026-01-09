package com.github.lemongrab32.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Класс конфигурации для менеджера кэша на базе Redis
 */
@Configuration
public class RedisConfig {

	/**
	 * Бин для управления кэшем на базе Redis
	 * @param redisConnectionFactory настроенная фабрика подключений
	 * @return менеджер кэша
	 */
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofHours(2))                                      // время жизни кэша
			.disableCachingNullValues()
			.serializeKeysWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new StringRedisSerializer()))                   // сериализатор для ключей
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair
					.fromSerializer(new GenericJackson2JsonRedisSerializer())); // сериализатор для значений

		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(redisCacheConfiguration)
			.build();
	}

}
