package com.github.lemongrab32.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.controller.dto.PropertyResponse;
import com.github.lemongrab32.service.PropertyService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PropertyControllerTest {

	private final String urlPrefix = "/api/v1/config";

	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	@Mock
	private PropertyService propertyService;

	@InjectMocks
	private PropertyController controller;

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
	@DisplayName("Обработка запроса на получение текущих параметров для расчёта стоимости абонемента")
	public void getProperties() throws Exception {
		Map<String, Object> response = Collections.singletonMap(
			MembershipConfig.PRIVATE_MID_DISCOUNT, 0.06
		);
		String json = mapper.writeValueAsString(response);

		Mockito.when(propertyService.getProperties())
			.thenReturn(response);

		mockMvc.perform(get(urlPrefix))
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

		Mockito.when(propertyService.setProperty(request))
			.thenReturn(response);

		mockMvc.perform(put(urlPrefix)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(responseJson));
	}

}
