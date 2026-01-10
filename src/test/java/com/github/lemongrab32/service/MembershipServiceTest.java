package com.github.lemongrab32.service;

import com.github.lemongrab32.client.PaymentServiceClient;
import com.github.lemongrab32.controller.dto.*;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.model.Membership;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.repository.MembershipRepository;
import com.github.lemongrab32.service.impl.DefaultMembershipService;
import com.github.lemongrab32.service.impl.DefaultPropertyService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import com.github.lemongrab32.util.mapper.MembershipMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipServiceTest {

	@Mock
	private MembershipRepository membershipRepository;

	@Mock
	private TariffService tariffService;

	@Mock
	private PaymentServiceClient paymentServiceClient;

	@Mock
	private PropertyService propertyService;

	@Mock
	private MembershipMapper mapper;

	@Mock
	private KafkaTemplate<String, NotificationRequest> kafkaTemplate;

	@InjectMocks
	private DefaultMembershipService membershipService;

	private final MembershipRequest request =
		new MembershipRequest(
			UUID.randomUUID(),
			ClientCategory.ADULT,
			ClientType.PRIVATE,
			1, 4, null,
			null
		);

	private final Tariff tariff =
		Tariff.builder()
			.id(1)
			.name("TestTariff")
			.basePrice(2400.0)
			.clientCategory(ClientCategory.ADULT)
			.clientType(ClientType.PRIVATE)
			.build();

	private AutoCloseable closeable;

	@BeforeEach
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		Mockito.when(tariffService.getTariffById(request.tariffId())).thenReturn(tariff);

		var propRepo = new MembershipConfigRepository();
		propRepo.init();
		Mockito.when(propertyService.getProperties()).thenReturn(propRepo.getProperties());
	}

	@AfterEach
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	@DisplayName("Расчёт стоимости абонемента")
	public void calculateMembership() {
		double basePrice = tariff.getBasePrice();
		double expectedPrice = request.months() * (basePrice - basePrice * 0.05);

		var result = membershipService.calculateMembership(request);

		assertNotNull(result);
		assertEquals(expectedPrice, result.finalPrice());
	}

	@Test
	@DisplayName("Оформление абонемента")
	public void getMembershipMembership() {
		final Membership membership = Membership.builder()
			.id(1L)
			.clientId(request.clientId())
			.tariffId(request.tariffId())
			.build();

		Mockito.doNothing().when(paymentServiceClient).createPayment(Mockito.any());
		Mockito.when(membershipRepository.save(Mockito.any())).thenReturn(membership);
		Mockito.when(mapper.toMembership(request)).thenReturn(membership);

		var response = membershipService.getMembership(request);

		assertNotNull(response);
		assertEquals(membership.getTariffId(), response.tariffId());
	}

}
