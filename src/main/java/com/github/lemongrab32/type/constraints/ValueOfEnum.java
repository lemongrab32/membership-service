package com.github.lemongrab32.type.constraints;

import com.github.lemongrab32.util.ValueOfEnumValidator;
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
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {

	/**
	 * Класс перечисления для валидации совпадения возможных значений
	 */
	Class<? extends Enum<?>> enumClass();

	/**
	 * Сообщение, которое будет показано при ошибке валидации
	 */
	String message() default "must match \"{regexp}\"";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
