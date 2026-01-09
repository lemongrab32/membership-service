package com.github.lemongrab32.service.impl;

import com.github.lemongrab32.exception.TariffNotFoundException;
import com.github.lemongrab32.controller.dto.TariffRequest;
import com.github.lemongrab32.controller.dto.TariffResponse;
import com.github.lemongrab32.model.Tariff;
import com.github.lemongrab32.repository.TariffRepository;
import com.github.lemongrab32.service.MembershipService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
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

	@Override
	public List<TariffRequest> getTariffs(Pageable pageable) {
		return repository.findAll(pageable).get().map(
			tariff -> new TariffRequest(
				tariff.getName(),
				tariff.getBasePrice(),
				tariff.getClientCategory(),
				tariff.getClientType())
		).toList();
	}

	@Override
	public Tariff getTariffById(Integer id) {
		return repository.findById(id)
			.orElseThrow(() -> new TariffNotFoundException(Messages.TARIFF_NOT_FOUND_MESSAGE, id));
	}

	@Override
	public TariffResponse save(TariffRequest request) {
		Tariff saved = repository.save(
			Tariff.builder()
				.name(request.name())
				.basePrice(request.basePrice())
				.clientCategory(request.category())
				.clientType(request.type())
				.build()
		);

		return new TariffResponse(Status.SUCCESS, Messages.TARIFF_SAVE_SUCCESS_MESSAGE, saved.getId());
	}

	@Override
	public TariffResponse update(Integer tariffId, TariffRequest request) {
		var tariff = repository.findById(tariffId)
			.orElseThrow(() -> new TariffNotFoundException(Messages.TARIFF_NOT_FOUND_MESSAGE, tariffId));

		tariff.setName(request.name());
		tariff.setBasePrice(request.basePrice());
		tariff.setClientCategory(request.category());
		tariff.setClientType(request.type());

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
