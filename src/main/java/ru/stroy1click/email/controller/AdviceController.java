package ru.stroy1click.email.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stroy1click.email.exception.ValidationException;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final MessageSource messageSource;

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleException(ValidationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.validation",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ProblemDetail handleException(RequestNotPermitted exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.TOO_MANY_REQUESTS, exception.getMessage()
        );
        problemDetail.setTitle(this.messageSource.getMessage(
                "error.title.too_many_requests",
                null,
                Locale.getDefault()
        ));
        problemDetail.setDetail(
                this.messageSource.getMessage(
                        "error.details.too_many_requests",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }
}
