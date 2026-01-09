package com.github.lemongrab32.type.constraints;

import com.github.lemongrab32.util.EnumNamePatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация для валидации полей типа перечислений
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumNamePatternValidator.class)
public @interface EnumNamePattern {

	/**
	 * Регулярное выражение для поиска по содержимому перечислений
	 */
	String regexp();

	/**
	 * Сообщение, которое будет показано при ошибке валидации
	 */
	String message() default "must match \"{regexp}\"";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
