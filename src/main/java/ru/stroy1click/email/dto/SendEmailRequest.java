package ru.stroy1click.email.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {

    @Min(value = 1_000_000, message = "{validation.send_email_request.code.size}")
    @Max(value = 9_999_999, message = "{validation.send_email_request.code.size}")
    private Integer code;

    @Valid
    @NotNull(message = "{validation.send_email_request.userdto}")
    private UserDto user;
}
