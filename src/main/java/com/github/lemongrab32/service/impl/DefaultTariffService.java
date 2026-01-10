package com.github.lemongrab32.service.impl;

import com.github.lemongrab32.exception.TariffNotFoundException;
import com.github.lemongrab32.controller.dto.TariffDto;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.TariffRepository;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import com.github.lemongrab32.util.mapper.TariffMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Стандартная реализация сервиса {@link TariffService}
 */
@Service
@RequiredArgsConstructor
public class DefaultTariffService implements TariffService {

	private final TariffRepository repository;
	private final TariffMapper mapper;

	@Override
	public List<TariffDto> getTariffs(Pageable pageable) {
		return repository.findAll(pageable).get().map(
			mapper::toDto
		).toList();
	}

	@Override
	public Tariff getTariffById(Integer id) {
		return repository.findById(id)
			.orElseThrow(() -> new TariffNotFoundException(Messages.TARIFF_NOT_FOUND_MESSAGE, id));
	}

	@Override
	public TariffResponse save(TariffDto request) {
		Tariff saved = repository.save(
			mapper.toTariff(request)
		);

		return new TariffResponse(Status.SUCCESS, Messages.TARIFF_SAVE_SUCCESS_MESSAGE, saved.getId());
	}

	@Override
	public TariffResponse update(Integer tariffId, TariffDto request) {
		var tariff = mapper.toTariff(request);

		tariff.setId(tariffId);

		repository.save(tariff);

		return new TariffResponse(Status.SUCCESS, Messages.TARIFF_UPDATE_SUCCESS_MESSAGE, tariffId);
	}

	@Override
	public TariffResponse delete(Integer id) {
		repository.findById(id)
			.orElseThrow(() -> new TariffNotFoundException(Messages.TARIFF_NOT_FOUND_MESSAGE, id));

		repository.deleteById(id);

		return new TariffResponse(Status.SUCCESS, Messages.TARIFF_DELETE_SUCCESS_MESSAGE, id);
	}

}
