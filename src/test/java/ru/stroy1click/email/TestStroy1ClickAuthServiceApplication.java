package ru.stroy1click.email;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestStroy1ClickAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(Stroy1ClickEmailServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
