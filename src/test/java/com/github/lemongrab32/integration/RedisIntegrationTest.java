package com.github.lemongrab32.integration;

import com.github.lemongrab32.client.PaymentServiceClient;
import com.github.lemongrab32.controller.dto.CalculationResponse;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.NotificationRequest;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.repository.MembershipRepository;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.MembershipConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties= {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
public class RedisIntegrationTest {

	private static final String redisUsername = "membership-service-user";
	private static final String redisPassword = "membership-service-pass";

	private static final GenericContainer<?> redisContainer =
		new GenericContainer<>("redis:alpine3.22").withExposedPorts(6379)
			.withEnv("REDIS_USER", redisUsername).withEnv("REDIS_USER_PASSWORD", redisPassword);

	@Autowired
	private MembershipService membershipService;

	@Autowired
	private RedisCacheManager redisCacheManager;

	@MockitoBean
	private MembershipRepository membershipRepository;
	@MockitoBean
	private MembershipConfigRepository membershipConfigRepository;
	@MockitoBean
	private TariffService tariffService;
	@MockitoBean
	private PaymentServiceClient paymentServiceClient;
	@MockitoBean
	private KafkaTemplate<String, NotificationRequest> kafkaTemplate;

	private final MembershipRequest request = new MembershipRequest(
		UUID.randomUUID(), ClientCategory.ADULT, ClientType.PRIVATE,
		1, 3, null, null
	);

	private Tariff tariff;
	private final Map<String, Object> props = new HashMap<>();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		redisContainer.start();

		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
		registry.add("spring.data.redis.user", () -> redisUsername);
		registry.add("spring.data.redis.password", () -> redisPassword);
		registry.add("client.payment.url", () -> "localhost:8083/payments");
	}

	@BeforeEach
	public void setUp() {
		tariff = Tariff.builder()
			.id(request.tariffId())
			.name("testName")
			.clientCategory(ClientCategory.ADULT)
			.clientType(ClientType.PRIVATE)
			.basePrice(2400.0)
			.build();

		props.put(MembershipConfig.PRIVATE_MID_DISCOUNT, 0.05);
		props.put(MembershipConfig.PRIVATE_MAX_DISCOUNT, 0.1);
		props.put(MembershipConfig.ENTERPRISE_MID_DISCOUNT, 0.05);
		props.put(MembershipConfig.ENTERPRISE_MAX_DISCOUNT, 0.1);
		props.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_BOTTOM, 10000);
		props.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_TOP, 50000);
	}

	@AfterAll
	public static void afterAll() {
		redisContainer.stop();
	}

	@Test
	@DisplayName("")
	void cacheCalculationResponse() {
		Mockito.when(tariffService.getTariffById(request.tariffId()))
			.thenReturn(tariff);
		Mockito.when(membershipConfigRepository.getProperties())
			.thenReturn(props);

		membershipService.calculate(request);
		CalculationResponse response = membershipService.calculate(request);
		String key = request.category().toString() +
			request.type().toString() +
			request.donation() +
			request.months() +
			request.hours() +
			request.tariffId();

		Cache cache = redisCacheManager.getCache("CALC_CACHE");
		assertNotNull(cache);
		CalculationResponse cached = cache.get(key, CalculationResponse.class);

		assertNotNull(cached);
		assertEquals(response, cached);
	}

}
