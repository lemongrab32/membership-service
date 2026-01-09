package com.github.lemongrab32.config;

import com.github.lemongrab32.controller.dto.NotificationRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс конфигурации продюсера Kafka
 */
@Configuration
public class KafkaProducerConfig {

	@Value("${spring.kafka.bootstrap-servers: localhost:9092}")
	private String bootstrapServers;

	/**
	 * Бин фабрики продюсеров с указанными настройками
	 * @return фабрику продюсеров
	 */
	@Bean
	public ProducerFactory<String, NotificationRequest> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);          // доступные узлы брокеров
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // сериализатор для ключей сообщений
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // сериализатор для значений сообщений

		return new DefaultKafkaProducerFactory<>(config);
	}

	/**
	 * Бин шаблона, получающего продюсера из указанной фабрики для отправки сообщения
	 * @return шаблон
	 */
	@Bean
	public KafkaTemplate<String, NotificationRequest> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
