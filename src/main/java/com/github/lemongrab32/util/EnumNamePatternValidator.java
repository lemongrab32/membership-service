package com.github.lemongrab32.util;

import com.github.lemongrab32.type.constraints.EnumNamePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Реализация валидации полей перечислений
 */
public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {

	public static final String CATEGORY_PATTERN = "CHILD|STUDENT|ADULT";
	public static final String TYPE_PATTERN = "PRIVATE|ENTERPRISE";

	private Pattern pattern;

	@Override
	public void initialize(EnumNamePattern annotation) {
		try {
			pattern = Pattern.compile(annotation.regexp()); // инициализация шаблона для обработки регулярного выражения
		} catch (PatternSyntaxException e) {
			throw new IllegalArgumentException("Некорректное регулярное выражение", e);
		}
	}

	@Override
	public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		Matcher m = pattern.matcher(value.name());
		return m.matches();
	}

}
