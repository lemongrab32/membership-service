package com.github.lemongrab32.repository;

import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.exception.UnknownPropertyException;
import com.github.lemongrab32.type.MembershipConfig;
import com.github.lemongrab32.type.Messages;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Repository
public class MembershipConfigRepository {

	private final Map<String, Object> properties = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		properties.put(MembershipConfig.PRIVATE_MID_DISCOUNT, 0.05);
		properties.put(MembershipConfig.PRIVATE_MAX_DISCOUNT, 0.1);
		properties.put(MembershipConfig.PRIVATE_HOUR_MULTIPLIER, 0.15);
		properties.put(MembershipConfig.ENTERPRISE_MID_DISCOUNT, 0.05);
		properties.put(MembershipConfig.ENTERPRISE_MAX_DISCOUNT, 0.1);
		properties.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_BOTTOM, 10000);
		properties.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_TOP, 50000);
	}

	public String setProperty(PropertyRequest request) {
		if (!properties.containsKey(request.name())) {
			throw new UnknownPropertyException(Messages.UNKNOWN_PROPERTY_MESSAGE, request.name());
		}

		properties.put(request.name(), request.value());

		return request.value().toString();
	}

}
