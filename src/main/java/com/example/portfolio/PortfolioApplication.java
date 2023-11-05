package com.example.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class PortfolioApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortfolioApplication.class, args);
  }

  @Bean
  public Executor taskExecutor() {
    var coreCount = Runtime.getRuntime().availableProcessors();

    var executor = new TaskExecutorBuilder()
      .corePoolSize(1)
      .maxPoolSize(coreCount)
      .threadNamePrefix("AsyncExecutor-")
      .queueCapacity(500)
      .build();
    executor.initialize();
    return executor;
  }
}
