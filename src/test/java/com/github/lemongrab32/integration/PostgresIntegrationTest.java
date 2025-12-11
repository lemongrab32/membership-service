package com.github.lemongrab32.integration;

import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.TariffRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties= {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration"
})
@ActiveProfiles("test")
public class PostgresIntegrationTest {

	private static final PostgreSQLContainer postgresContainer =
		new PostgreSQLContainer("postgres:18-alpine")
			.withDatabaseName("membership-service")
			.withUsername("testUser")
			.withPassword("testPassword");

	@Autowired
	private TariffRepository tariffRepository;

	private final Tariff tariff = Tariff.builder()
		.name("testTariff")
		.basePrice(2400.0)
		.clientType(ClientType.PRIVATE)
		.clientCategory(ClientCategory.ADULT)
		.build();

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		postgresContainer.start();

		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.hikari.username", postgresContainer::getUsername);
		registry.add("spring.datasource.hikari.password", postgresContainer::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}

	@AfterAll
	static void afterAll() {
		postgresContainer.stop();
	}

	@Test
	@DisplayName("")
	public void saveTariff() {
		var saved = tariffRepository.save(tariff);

		var found = tariffRepository.findById(saved.getId());

		assertTrue(found.isPresent());
		assertEquals(saved, found.get());
	}

}
