package com.ciprian.cusmuliuc.aspect.verify;

import com.ciprian.cusmuliuc.exception.TechnicalException;
import com.ciprian.cusmuliuc.util.Constants;
import com.google.common.base.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class VerifyAspect {

  @Around("@annotation(com.ciprian.cusmuliuc.aspect.verify.VerifyArguments)")
  public Object verifyArgs(ProceedingJoinPoint joinPoint) throws Throwable {
    verifyArgs(joinPoint.getArgs());
    return joinPoint.proceed();
  }

  public static void verifyArgs(Object[] objects) {
    for (int iterator = 0; iterator < objects.length; iterator++) {
      if (objects[iterator] != null && objects[iterator].getClass().equals(String.class)) {
        verifyParamExistence((String) objects[iterator]);
      } else {
        verifyObject(objects[iterator]);
      }
    }
  }

  public static void verifyParamExistence(String id) {
    if (Strings.isNullOrEmpty(id) || !id.chars().allMatch(Character::isDigit)) {
      throw new TechnicalException(Constants.METRIC_TYPE_ID_DOES_NOT_EXIST);
    }
  }

  public static void verifyObject(Object object) {
    if (object == null) {
      throw new TechnicalException(Constants.METRIC_TYPE_DOES_NOT_EXIST);
    }
  }
}
