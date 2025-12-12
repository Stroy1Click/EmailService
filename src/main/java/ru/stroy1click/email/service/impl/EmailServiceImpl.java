package ru.stroy1click.email.service.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.stroy1click.email.dto.UserDto;
import ru.stroy1click.email.exception.ServerErrorResponseException;
import ru.stroy1click.email.service.EmailService;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Configuration configuration;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(UserDto user, Integer code) {
        log.info("sendEmail to {}", user.getEmail());
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("Stroy1click");
            String emailContent = getConfirmationEmailContent(user, code);
            mimeMessageHelper.setText(emailContent, true);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e){
            log.error("sendEmail error ", e);
            throw new ServerErrorResponseException();
        }
    }

    private String getConfirmationEmailContent(UserDto user, Integer code) {
        try {
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> module = new HashMap<>();
            module.put("firstName", user.getFirstName());
            module.put("lastName", user.getLastName());
            module.put("code", code);
            this.configuration.getTemplate("confirmation.ftlh").process(module, stringWriter);
            return stringWriter.getBuffer().toString();
        } catch (IOException | TemplateException e){
            log.error("getConfirmationEmailContent error ", e);
            throw new ServerErrorResponseException();
        }
    }

}
