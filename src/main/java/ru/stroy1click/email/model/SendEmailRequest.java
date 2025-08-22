package ru.stroy1click.email.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.stroy1click.email.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {

    private Integer code;

    private UserDto user;
}
