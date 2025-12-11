package com.github.lemongrab32.exception;

import lombok.Getter;

public class UnknownPropertyException extends RuntimeException {

	@Getter
	private final String propertyName;

	public UnknownPropertyException(String message, String propertyName) {
		super(message);
		this.propertyName = propertyName;
	}
}
