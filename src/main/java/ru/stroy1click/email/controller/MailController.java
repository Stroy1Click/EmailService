package ru.stroy1click.email.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.email.exception.ValidationException;
import ru.stroy1click.email.dto.SendEmailRequest;
import ru.stroy1click.email.service.EmailService;
import ru.stroy1click.email.util.ValidationErrorUtils;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/emails")
@RateLimiter(name = "mailController")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    private final MessageSource messageSource;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid SendEmailRequest sendEmailRequest,
                                            BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.emailService.sendEmail(sendEmailRequest.getUser(), sendEmailRequest.getCode());
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.email.sent",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
