package com.github.lemongrab32.exception;

import lombok.Getter;

public class TariffNotFoundException extends RuntimeException {

	@Getter
	private final Integer tariffId;

	public TariffNotFoundException(String message, Integer tariffId) {
		super(message);
		this.tariffId = tariffId;
	}
}
