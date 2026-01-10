package com.github.lemongrab32.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lemongrab32.controller.dto.*;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;
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
import java.util.UUID;

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
		new MembershipRequest(
			UUID.randomUUID(), ClientCategory.ADULT, ClientType.PRIVATE,
			1, 3, null, null
		);

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

}
