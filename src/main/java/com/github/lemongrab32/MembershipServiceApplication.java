package com.github.lemongrab32;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Главный класс, содержащий точку входа в приложение
 */
@SpringBootApplication
@EnableCaching
@EnableFeignClients
@EnableAspectJAutoProxy
public class MembershipServiceApplication {

	/**
	 * Точка входа в приложение
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(MembershipServiceApplication.class, args);
	}

}
