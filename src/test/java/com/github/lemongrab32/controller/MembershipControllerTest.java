package com.github.lemongrab32.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lemongrab32.controller.dto.*;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.type.MembershipConfig;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MembershipControllerTest {

	private final String urlPrefix = "/api/v1/memberships";

	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	@Mock
	private MembershipService membershipService;

	@InjectMocks
	private MembershipController controller;

	private final MembershipRequest membershipRequest =
		Instancio.create(MembershipRequest.class);

	private AutoCloseable mocks;

	@BeforeEach
	public void setUp() throws Exception {
		mocks = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@AfterEach
	public void tearDown() throws Exception {
		mocks.close();
	}

	@Test
	@DisplayName("Обработка запроса на расчёт стоимости абонемента")
	public void calculate() throws Exception {
		var response = new CalculationResponse(
			Status.SUCCESS, Messages.CALCULATION_SUCCESS_MESSAGE, 2400.0
		);
		String responseJson = mapper.writeValueAsString(response);

		String json = mapper.writeValueAsString(membershipRequest);

		Mockito.when(membershipService.calculateMembership(membershipRequest))
			.thenReturn(response);

		mockMvc.perform(post(urlPrefix + "/calculate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(responseJson));
	}

	@Test
	@DisplayName("Обработка запроса на оформление абонемента")
	public void create() throws Exception {
		var response = new MembershipResponse(
			Status.SUCCESS, Messages.MEMBERSHIP_SUCCESS_MESSAGE, 1
		);
		String responseJson = mapper.writeValueAsString(response);

		String json = mapper.writeValueAsString(membershipRequest);

		Mockito.when(membershipService.getMembership(membershipRequest))
			.thenReturn(response);

		mockMvc.perform(post(urlPrefix)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(responseJson));
	}

	@Test
	@DisplayName("Обработка запроса на получение текущих параметров для расчёта стоимости абонемента")
	public void getProperties() throws Exception {
		Map<String, Object> response = Collections.singletonMap(
			MembershipConfig.PRIVATE_MID_DISCOUNT, 0.06
		);
		String json = mapper.writeValueAsString(response);

		Mockito.when(membershipService.getProperties())
			.thenReturn(response);

		mockMvc.perform(get(urlPrefix + "/config"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(json));
	}

	@Test
	@DisplayName("Обработка запроса на установку нового значения одного из параметров")
	public void setProperty() throws Exception {
		PropertyRequest request = new PropertyRequest(
			MembershipConfig.PRIVATE_MID_DISCOUNT, 0.07);
		String valueStr = request.value().toString();

		PropertyResponse response = new PropertyResponse(
			Status.SUCCESS, Messages.PROPERTY_UPDATE_SUCCESS_MESSAGE, valueStr
		);
		String responseJson = mapper.writeValueAsString(response);

		String json = mapper.writeValueAsString(request);

		Mockito.when(membershipService.setProperty(request))
			.thenReturn(response);

		mockMvc.perform(put(urlPrefix + "/config")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(responseJson));
	}

}
