package ru.stroy1click.email.service;

import ru.stroy1click.common.command.SendEmailCommand;

public interface EmailService {

    void sendEmail(SendEmailCommand emailEvent);
}
