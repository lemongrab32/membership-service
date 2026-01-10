package com.github.lemongrab32.integration;

import com.github.lemongrab32.client.PaymentServiceClient;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.NotificationRequest;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.model.Membership;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.repository.MembershipRepository;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.MembershipConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.kafka.KafkaContainer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties= {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
@ActiveProfiles("test")
public class KafkaIntegrationTest {

	private static final KafkaContainer kafkaContainer =
		new KafkaContainer("apache/kafka:4.0.1");

	@Autowired
	private MembershipService membershipService;

	@MockitoBean
	private MembershipRepository membershipRepository;
	@MockitoBean
	private MembershipConfigRepository membershipConfigRepository;
	@MockitoBean
	private TariffService tariffService;
	@MockitoBean
	private PaymentServiceClient paymentServiceClient;
	@MockitoBean
	private RedisCacheManager cacheManager;

	private final MembershipRequest request = new MembershipRequest(
		UUID.randomUUID(), "ADULT", "PRIVATE",
		1, 3, null, null
	);

	private KafkaConsumer<String, NotificationRequest> consumer;

	private Tariff tariff;
	private Membership membership;
	private final Map<String, Object> calcProps = new HashMap<>();

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		kafkaContainer.start();

		registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);

		AdminClient
			.create(Collections.singletonMap(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers()))
			.createTopics(Collections.singleton(new NewTopic("notifications", 1, (short) 1)));
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

		membership = Membership.builder()
			.id(1L)
			.clientId(request.clientId())
			.startDate(LocalDate.now())
			.isActive(true)
			.finalPrice(2400.0)
			.tariffId(tariff.getId())
			.build();

		calcProps.put(MembershipConfig.PRIVATE_MID_DISCOUNT, 0.05);
		calcProps.put(MembershipConfig.PRIVATE_MAX_DISCOUNT, 0.1);
		calcProps.put(MembershipConfig.ENTERPRISE_MID_DISCOUNT, 0.05);
		calcProps.put(MembershipConfig.ENTERPRISE_MAX_DISCOUNT, 0.1);
		calcProps.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_BOTTOM, 10000);
		calcProps.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_TOP, 50000);

		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "notifications");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		consumer = new  KafkaConsumer<>(
			props, new StringDeserializer(),
			new JsonDeserializer<>(NotificationRequest.class)
			);

		consumer.subscribe(Collections.singletonList("notifications"));
	}

	@AfterEach
	public void tearDown() {
		kafkaContainer.stop();
	}

	@Test
	@DisplayName("Отправка уведомления в очередь")
	public void publishNotification() {
		Mockito.when(tariffService.getTariffById(tariff.getId())).thenReturn(tariff);
		Mockito.when(membershipConfigRepository.getProperties())
			.thenReturn(calcProps);
		Mockito.doNothing().when(paymentServiceClient).createPayment(Mockito.any());
		Mockito.when(membershipRepository.save(Mockito.any())).thenReturn(membership);

		var response = membershipService.getMembership(request);

		assertNotNull(response);
		assertEquals(request.tariffId(), response.tariffId());


		var record = consumer.poll(Duration.ofSeconds(10))
			.iterator().next();
		NotificationRequest notificationRequest = record.value();

		assertNotNull(notificationRequest);
		assertEquals(membership.getFinalPrice(), notificationRequest.finalPrice());
	}

}
