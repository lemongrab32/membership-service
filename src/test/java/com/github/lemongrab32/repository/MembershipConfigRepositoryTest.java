package com.github.lemongrab32.repository;

import com.github.lemongrab32.controller.dto.PropertyRequest;
import com.github.lemongrab32.exception.UnknownPropertyException;
import com.github.lemongrab32.type.MembershipConfig;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class MembershipConfigRepositoryTest {

	private MembershipConfigRepository repository;

	@BeforeEach
	public void setUp() throws Exception {
		repository = new MembershipConfigRepository();
		repository.init();
	}

	@Test
	@DisplayName("Получение параметра по корректному имени")
	public void testGetProperty() {
		Double expected = 0.1;

		Optional<Object> property =
			repository.getProperty(MembershipConfig.ENTERPRISE_MAX_DISCOUNT);

		Assertions.assertTrue(property.isPresent());
		Assertions.assertEquals(expected, property.get());
	}

	@Test
	@DisplayName("Получение параметра по некорректному имени")
	public void testGetPropertyProvidingIncorrectName() {
		Optional<Object> property =
			repository.getProperty("unknown");

		Assertions.assertFalse(property.isPresent());
	}

	@Test
	@DisplayName("Обновление значения параметра по корректному имени")
	public void testSetProperty() {
		Double expected = 0.28;
		PropertyRequest request = new PropertyRequest(MembershipConfig.ENTERPRISE_MAX_DISCOUNT, expected);

		String updated = repository.setProperty(request);

		var props = repository.getProperties();

		Assertions.assertNotNull(props);
		Assertions.assertEquals(expected, props.get(updated));
	}

	@Test
	@DisplayName("Обновление значения параметра по некорректному имени")
	public void testSetPropertyProvidingIncorrectName() {
		PropertyRequest request = new PropertyRequest("unknown", 0.28);

		Assertions.assertThrows(UnknownPropertyException.class, () -> repository.setProperty(request));
	}

}
