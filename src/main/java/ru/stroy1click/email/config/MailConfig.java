package ru.stroy1click.email.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.stroy1click.email.props.MailProperties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.mailProperties.getHost());
        javaMailSender.setPort(this.mailProperties.getPort());
        javaMailSender.setUsername(this.mailProperties.getUsername());
        javaMailSender.setPassword(this.mailProperties.getPassword());
        javaMailSender.setJavaMailProperties(this.mailProperties.getProperties());
        javaMailSender.getJavaMailProperties();
        return javaMailSender;
    }

}
