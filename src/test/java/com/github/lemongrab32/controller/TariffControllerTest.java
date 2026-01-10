package com.github.lemongrab32.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lemongrab32.controller.dto.TariffDto;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.service.TariffService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TariffControllerTest {

	private final String urlPrefix = "/api/v1/tariffs";

	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	@Mock
	private TariffService tariffService;

	@InjectMocks
	private TariffController tariffController;

	private final TariffDto tariffDto = Instancio.create(TariffDto.class);

	private AutoCloseable mocks;

	@BeforeEach
	public void setUp() throws Exception {
		mocks = MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(tariffController).build();
	}

	@AfterEach
	public void tearDown() throws Exception {
		mocks.close();
	}

	@Test
	@DisplayName("Запрос на получение страницы с тарифами")
	public void getTariffs() throws Exception {
		Mockito.when(tariffService.getTariffs(Mockito.any()))
			.thenReturn(Collections.emptyList());

		String emptyList = mapper.writeValueAsString(Collections.emptyList());

		mockMvc.perform(get(urlPrefix + "?offset=0&limit=10"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(emptyList));
	}

	@Test
	@DisplayName("Запрос на добавление нового тарифа")
	public void save() throws Exception {
		TariffResponse tariffResponse = new TariffResponse(Status.SUCCESS, Messages.TARIFF_SAVE_SUCCESS_MESSAGE, 1);
		String response = mapper.writeValueAsString(tariffResponse);

		String request = mapper.writeValueAsString(tariffDto);

		Mockito.when(tariffService.save(tariffDto)).thenReturn(tariffResponse);

		mockMvc.perform(
				post(urlPrefix)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request)
			)
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(response));
	}

	@Test
	@DisplayName("Запрос на обновление данных тарифа")
	public void update() throws Exception {
		TariffResponse tariffResponse = new TariffResponse(Status.SUCCESS, Messages.TARIFF_UPDATE_SUCCESS_MESSAGE, 1);
		String response = mapper.writeValueAsString(tariffResponse);

		String request = mapper.writeValueAsString(tariffDto);

		Mockito.when(tariffService.update(1, tariffDto)).thenReturn(tariffResponse);

		mockMvc.perform(
				put(urlPrefix + "/1")
					.contentType(MediaType.APPLICATION_JSON)
					.content(request)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(response));
	}

	@Test
	@DisplayName("Запрос на удаление тарифа")
	public void deleteTest() throws Exception {
		TariffResponse tariffResponse = new TariffResponse(Status.SUCCESS, Messages.TARIFF_DELETE_SUCCESS_MESSAGE, 1);
		String response = mapper.writeValueAsString(tariffResponse);

		Mockito.when(tariffService.delete(1)).thenReturn(tariffResponse);

		mockMvc.perform(
				delete(urlPrefix + "/1")
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(response));
	}

}
