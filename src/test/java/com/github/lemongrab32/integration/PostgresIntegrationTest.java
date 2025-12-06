package com.github.lemongrab32.integration;

import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.TariffRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostgresIntegrationTest {

	private static final PostgreSQLContainer postgresContainer =
		new PostgreSQLContainer("postgres:18-alpine")
			.withDatabaseName("membership-service")
			.withUsername("testUser")
			.withPassword("testPassword");

	@Autowired
	private TariffRepository tariffRepository;

	private final Tariff tariff = Instancio.create(Tariff.class);

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		postgresContainer.start();

		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.hikari.username", postgresContainer::getUsername);
		registry.add("spring.datasource.hikari.password", postgresContainer::getPassword);
		registry.add("client.payment.url", () -> "localhost:8083/payments");
	}

	@AfterAll
	static void afterAll() {
		postgresContainer.stop();
	}

	@Test
	@DisplayName("")
	public void saveTariff() {
		tariffRepository.save(tariff);

		var saved = tariffRepository.findById(tariff.getId());

		assertTrue(saved.isPresent());
		assertEquals(tariff, saved.get());
	}

}
