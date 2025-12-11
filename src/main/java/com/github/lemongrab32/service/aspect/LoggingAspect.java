package com.github.lemongrab32.service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Pointcut("execution(public * com.github.lemongrab32.service.*.*(..))")
	public void anyMembershipMethod() {}

	@Before("anyMembershipMethod()")
	public void beforeAnyMembershipServiceMethod(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		var args = joinPoint.getArgs();

		Object request = (args.length > 1) ? args[1] : args[0];

		log.info("{}.{}() is executing with next request: {}", className, methodName, request.toString());
	}

	@AfterThrowing(value = "anyMembershipMethod()", throwing = "ex")
	public void afterAnyMembershipServiceMethodThrows(JoinPoint joinPoint, Throwable ex) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();

		log.error("{}.{}() had thrown an exception. Message: {}", className, methodName, ex.getMessage());
	}

}
