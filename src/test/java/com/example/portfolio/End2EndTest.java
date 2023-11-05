package com.example.portfolio;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Tag("end2end")
@Testcontainers
@Import(TestPortfolioApplication.class)
// To consider: https://docs.spring.io/spring-boot/docs/3.1.0/reference/html/features.html#features.docker-compose
public @interface End2EndTest {
}
