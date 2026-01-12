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

/**
 * In-memory репозиторий для хранения параметров для расчёта стоимости абонементов
 */
@Getter
@Repository
public class MembershipConfigRepository {

	private final Map<String, Object> properties = new ConcurrentHashMap<>();

	/**
	 * Инициализация стандартных значений параметров
	 */
	@PostConstruct
	public void init() {
		properties.put(MembershipConfig.PRIVATE_MID_DISCOUNT, 0.05);
		properties.put(MembershipConfig.PRIVATE_MAX_DISCOUNT, 0.1);
		properties.put(MembershipConfig.PRIVATE_HOUR_MULTIPLIER, 0.15);
		properties.put(MembershipConfig.ENTERPRISE_MID_DISCOUNT, 0.05);
		properties.put(MembershipConfig.ENTERPRISE_MAX_DISCOUNT, 0.1);
		properties.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_BOTTOM, 10000.0d);
		properties.put(MembershipConfig.ENTERPRISE_DONATION_BOUND_TOP, 50000.0d);
	}

	/**
	 * Изменение значения параметра
	 * @param request {@link PropertyRequest} тело запроса
	 * @return имя обновлённого параметра
	 */
	public String setProperty(PropertyRequest request) {
		if (!properties.containsKey(request.name())) {
			throw new UnknownPropertyException(Messages.UNKNOWN_PROPERTY_MESSAGE, request.name());
		}

		properties.put(request.name(), request.value());

		return request.name();
	}

}
