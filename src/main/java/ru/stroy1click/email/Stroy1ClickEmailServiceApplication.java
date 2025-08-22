package ru.stroy1click.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Stroy1ClickEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Stroy1ClickEmailServiceApplication.class, args);
    }

}
