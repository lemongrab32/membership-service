package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.controller.dto.PropertyResponse;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.service.impl.DefaultPropertyService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyServiceTest {

	@Mock
	private MembershipConfigRepository membershipConfigRepository;

	@InjectMocks
	private DefaultPropertyService propertyService;

	private AutoCloseable closeable;

	@BeforeEach
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	@DisplayName("Получение списка параметров")
	public void getMembershipProperties() {
		Mockito.when(membershipConfigRepository.getProperties())
			.thenReturn(Collections.emptyMap());

		var response = propertyService.getProperties();

		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	@DisplayName("Задание значения параметру")
	public void setProperty() {
		var propertyRequest = new PropertyRequest("testProperty", "testValue");

		var response = new PropertyResponse(
			Status.SUCCESS, Messages.PROPERTY_UPDATE_SUCCESS_MESSAGE, "str"
		);

		Mockito.when(membershipConfigRepository.setProperty(Mockito.any()))
			.thenReturn(response.name());

		var received = propertyService.setProperty(propertyRequest);

		assertNotNull(received);
		assertEquals(received, response);
	}

}
