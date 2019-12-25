package com.ciprian.cusmuliuc;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CusmuliucApplicationTests {

  @Autowired private WebApplicationContext wac;

  @Test
  public void contextLoads() {
    Assert.assertNotNull(wac);
  }
}
