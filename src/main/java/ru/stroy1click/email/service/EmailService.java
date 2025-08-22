package ru.stroy1click.email.service;

import ru.stroy1click.email.dto.UserDto;

public interface EmailService {

    void sendEmail(UserDto user, Integer code);
}
