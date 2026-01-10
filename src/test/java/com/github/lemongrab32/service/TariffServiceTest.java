package com.github.lemongrab32.service;

import com.github.lemongrab32.controller.dto.TariffDto;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.TariffRepository;
import com.github.lemongrab32.service.impl.DefaultTariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import com.github.lemongrab32.util.mapper.TariffMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TariffServiceTest {

	@Mock
	private TariffRepository tariffRepository;

	@Mock
	private TariffMapper tariffMapper;

	@InjectMocks
	private DefaultTariffService tariffService;

	private final Tariff tariff = Instancio.create(Tariff.class);

	private AutoCloseable closeable;

	@BeforeEach
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		Mockito.when(tariffRepository.findById(tariff.getId())).thenReturn(Optional.of(tariff));
	}

	@AfterEach
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	@DisplayName("Получение страницы с тарифами")
	public void findAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Tariff> page = new PageImpl<>(Collections.singletonList(tariff));

		Mockito.when(tariffRepository.findAll(pageable))
			.thenReturn(page);
		Mockito.when(tariffMapper.toDto(tariff)).thenReturn(
			new TariffDto(
				tariff.getName(), tariff.getBasePrice(),
				tariff.getClientCategory(), tariff.getClientType()
			)
		);

		var receivedPage = tariffService.getTariffs(pageable);
		var receivedTariff = receivedPage.getFirst();

		assertEquals(receivedPage.size(), page.getTotalElements());
		assertEquals(tariff.getName(), receivedTariff.name());
	}

	@Test
	@DisplayName("Добавление нового тарифа")
	public void save() {
		TariffDto request = new TariffDto(
			tariff.getName(), tariff.getBasePrice(),
			tariff.getClientCategory(), tariff.getClientType()
		);

		Mockito.when(tariffRepository.save(Mockito.any())).thenReturn(tariff);
		Mockito.when(tariffMapper.toTariff(request)).thenReturn(tariff);

		var response = tariffService.save(request);

		assertEquals(Status.SUCCESS, response.status());
		assertEquals(Messages.TARIFF_SAVE_SUCCESS_MESSAGE, response.message());
		assertEquals(tariff.getId(), response.tariffId());
	}

	@Test
	@DisplayName("Обновление данных тарифа")
	public void update() {
		TariffDto request = new TariffDto(
			"testName", 1.0,
			tariff.getClientCategory(), tariff.getClientType()
		);

		Mockito.when(tariffRepository.save(tariff)).thenReturn(tariff);
		Mockito.when(tariffMapper.toTariff(request)).thenReturn(
			Tariff.builder()
				.id(tariff.getId()).basePrice(request.basePrice())
				.clientCategory(request.category())
				.clientType(request.type())
				.build()
		);

		var response = tariffService.update(tariff.getId(), request);

		assertEquals(Status.SUCCESS, response.status());
		assertEquals(Messages.TARIFF_UPDATE_SUCCESS_MESSAGE, response.message());
	}

	@Test
	@DisplayName("Удаление тарифа")
	public void delete() {
		Mockito.doNothing().when(tariffRepository).deleteById(tariff.getId());

		var response = tariffService.delete(tariff.getId());

		assertEquals(Status.SUCCESS, response.status());
		assertEquals(Messages.TARIFF_DELETE_SUCCESS_MESSAGE, response.message());
	}

}
