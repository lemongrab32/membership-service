package com.github.lemongrab32.service.impl;


import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.controller.dto.PropertyResponse;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import com.github.lemongrab32.service.PropertyService;
import com.github.lemongrab32.service.TariffService;
import com.github.lemongrab32.type.Messages;
import com.github.lemongrab32.type.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Стандартная реализация сервиса {@link PropertyService}
 */
@Service
@RequiredArgsConstructor
public class DefaultPropertyService implements PropertyService {

	private final MembershipConfigRepository membershipConfigRepository;

	@Override
	public Map<String, Object> getProperties() {
		return membershipConfigRepository.getProperties();
	}

	@Override
	public PropertyResponse setProperty(PropertyRequest request) {
		return new PropertyResponse(
			Status.SUCCESS,
			Messages.PROPERTY_UPDATE_SUCCESS_MESSAGE,
			membershipConfigRepository.setProperty(request)
		);
	}

}
