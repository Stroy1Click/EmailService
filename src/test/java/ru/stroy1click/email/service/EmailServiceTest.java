package ru.stroy1click.email.service;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import ru.stroy1click.common.command.SendEmailCommand;
import ru.stroy1click.common.exception.ServiceErrorResponseException;
import ru.stroy1click.email.service.impl.EmailServiceImpl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private Configuration configuration;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private Template freemarkerTemplate;

    @InjectMocks
    private EmailServiceImpl emailService;

    private SendEmailCommand sendEmailCommand;

    @BeforeEach
    void setUp() {
        sendEmailCommand = SendEmailCommand.builder()
                .email("test@mail.com")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void sendEmail_WithValidInput_SendsEmail() throws Exception {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));

        when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(this.configuration.getTemplate("confirmation.ftlh")).thenReturn(this.freemarkerTemplate);

        doAnswer(inv -> {
            StringWriter writer = inv.getArgument(1);
            writer.write("Rendered template");
            return null;
        }).when(this.freemarkerTemplate).process(any(), any(StringWriter.class));

        this.emailService.sendEmail(sendEmailCommand);

        verify(this.mailSender, times(1)).createMimeMessage();
        verify(this.mailSender, times(1)).send(mimeMessage);
        verify(this.configuration, times(1)).getTemplate("confirmation.ftlh");
        verify(this.freemarkerTemplate, times(1)).process(any(), any(StringWriter.class));
    }

    @Test
    void sendEmail_WithTemplateNotFound_ThrowsServerErrorResponseException() throws Exception {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(this.configuration.getTemplate("confirmation.ftlh"))
                .thenThrow(new IOException("not found"));

        ServiceErrorResponseException exception = assertThrows(
                ServiceErrorResponseException.class,
                () -> this.emailService.sendEmail(sendEmailCommand)
        );

        assertNotNull(exception);
    }

    @Test
    void sendEmail_WithTemplateProcessingFailure_ThrowsServerErrorResponseException() throws Exception {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(this.configuration.getTemplate("confirmation.ftlh"))
                .thenReturn(this.freemarkerTemplate);

        doThrow(new TemplateException("fail", (Environment) null))
                .when(this.freemarkerTemplate).process(any(), any());

        ServiceErrorResponseException exception = assertThrows(
                ServiceErrorResponseException.class,
                () -> this.emailService.sendEmail(sendEmailCommand)
        );

        assertNotNull(exception);
    }

    @Test
    void sendEmail_WithMailSenderFailure_ThrowsMailException() throws Exception {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));

        when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(this.configuration.getTemplate("confirmation.ftlh")).thenReturn(this.freemarkerTemplate);

        doAnswer(inv -> {
            StringWriter writer = inv.getArgument(1);
            writer.write("Template processed");
            return null;
        }).when(this.freemarkerTemplate).process(any(), any(StringWriter.class));

        doThrow(new MailException("SMTP error") {
        }).when(this.mailSender).send(any(MimeMessage.class));

        assertThrows(MailException.class, () -> this.emailService.sendEmail(sendEmailCommand));
    }

}
