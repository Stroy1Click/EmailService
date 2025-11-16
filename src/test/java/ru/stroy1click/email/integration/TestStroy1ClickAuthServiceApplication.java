package ru.stroy1click.email.integration;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;
import ru.stroy1click.email.Stroy1ClickEmailServiceApplication;

public class TestStroy1ClickAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(Stroy1ClickEmailServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
