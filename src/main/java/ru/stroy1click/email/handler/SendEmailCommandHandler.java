package ru.stroy1click.email.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.stroy1click.common.command.SendEmailCommand;
import ru.stroy1click.email.service.EmailService;

@Slf4j
@Component
@KafkaListener(topics = {"send-email-commands"})
@RequiredArgsConstructor
public class SendEmailCommandHandler {

    private final EmailService emailService;

    @KafkaHandler
    public void handle(SendEmailCommand command){
        log.info("handle {}", command);

        this.emailService.sendEmail(command);
    }
}
