package com.ciprian.cusmuliuc.aspect;

import com.ciprian.cusmuliuc.aspect.verify.VerifyAspect;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class VerifyArgumentsAspectTest {

  @Test(expected = TechnicalException.class)
  public void verifyArgsWhenArgumentIsNullObject_ShouldThrowError() {
    VerifyAspect.verifyArgs(new Object[] {null});
  }

  @Test
  public void verifyArgsWhenArgumentIsNotNullObject_ShouldNotThrowError() {
    VerifyAspect.verifyArgs(new Object[] {new Long(2)});
  }

  @Test(expected = TechnicalException.class)
  public void verifyArgsWhenArgumentIsEmptyString_ShouldThrowError() {
    VerifyAspect.verifyArgs(new Object[] {""});
  }

  @Test(expected = TechnicalException.class)
  public void verifyArgsWhenArgumentIsNotDigitString_ShouldThrowError() {
    VerifyAspect.verifyArgs(new Object[] {"adwdw"});
  }

  @Test
  public void verifyArgsWhenArgumentIsOkString_ShouldNotThrowError() {
    VerifyAspect.verifyArgs(new Object[] {"123"});
  }

  @Test
  public void verifyArgsWhenArgumentIsEmptu_ShouldNotThrowError() {
    VerifyAspect.verifyArgs(new Object[] {});
  }
}
