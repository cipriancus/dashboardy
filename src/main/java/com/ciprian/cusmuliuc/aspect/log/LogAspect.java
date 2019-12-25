package com.ciprian.cusmuliuc.aspect.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
  Logger logger = LoggerFactory.getLogger(LogAspect.class);

  @Around("@annotation(com.ciprian.cusmuliuc.aspect.log.LogMethod)")
  public Object logMethodArg(ProceedingJoinPoint joinPoint) throws Throwable {
    logger.info("get info for method: {}", joinPoint.getSignature());
    return joinPoint.proceed();
  }
}
