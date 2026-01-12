package com.github.lemongrab32.util;

import com.github.lemongrab32.controller.dto.MembershipRequest;
import com.github.lemongrab32.model.ClientType;
import com.github.lemongrab32.repository.MembershipConfigRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrivateMembershipCalculatorTest {

	private static Map<String, Object> props;
	private static MembershipCalculator calculator;

	@BeforeAll
	public static void setUp() {
		var configRepo = new MembershipConfigRepository();
		configRepo.init();

		props = configRepo.getProperties();

		calculator = MembershipCalculator.getInstance(ClientType.valueOf("PRIVATE"), props);
	}

	@Test
	@DisplayName("Расчёт без скидки")
	public void testWithoutDiscount() {
		var request = new MembershipRequest(
			null, "ADULT", "PRIVATE",
			1, 2, null, null
		);

		double calculated = calculator.calculate(request, 2400.0);

		assertEquals(4800.0, calculated, 0.001);
	}

	@Test
	@DisplayName("Расчёт с минимальной скидкой")
	public void testMidDiscount() {
		var request = new MembershipRequest(
			null, "ADULT", "PRIVATE",
			null, 3, null, null
		);

		double calculated = calculator.calculate(request, 2400.0);

		assertEquals(6840.0, calculated, 0.001);
	}

	@Test
	@DisplayName("Расчёт с максимальной скидкой")
	public void testMaxDiscount() {
		var request = new MembershipRequest(
			null, "ADULT", "PRIVATE",
			null, 8, null, null
		);

		double calculated = calculator.calculate(request, 2400.0);

		assertEquals(17280.0, calculated, 0.001);
	}

}
