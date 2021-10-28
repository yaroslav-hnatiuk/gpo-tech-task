package com.github.yhnatiuk.gpotechtask;

import com.github.yhnatiuk.gpotechtask.repository.CommandRepository;
import com.github.yhnatiuk.gpotechtask.repository.ResponseRepository;
import com.github.yhnatiuk.gpotechtask.service.CommandService;
import com.github.yhnatiuk.gpotechtask.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractTest.DockerPostgreDataSourceInitializer.class)
@Testcontainers
public abstract class AbstractTest {

  @Autowired
  protected CommandRepository commandRepository;

  @Autowired
  protected ResponseRepository responseRepository;

  @Autowired
  protected CommandService commandService;

  @Autowired
  protected ResponseService responseService;

  protected String commandData = "f6f6f6f60405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122e2e2e2e2e2";

  protected String responseData = "f6f6f6f60405060708090a0b0c0d0e0fXXX112131415161718191a1b1c1d1e1f202XXXe2e2e2e2e2";

  public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>(
      "postgres:latest");

  static {
    postgreDBContainer.start();
  }

  public static class DockerPostgreDataSourceInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          applicationContext,
          "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
          "spring.datasource.username=" + postgreDBContainer.getUsername(),
          "spring.datasource.password=" + postgreDBContainer.getPassword()
      );
    }
  }
}
