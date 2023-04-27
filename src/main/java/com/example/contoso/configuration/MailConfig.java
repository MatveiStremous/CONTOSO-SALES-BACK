package com.example.contoso.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/12/2023 3:39 PM
 */
@Configuration
public class MailConfig {

    private final String host = "smtp.yandex.ru";
    private final int port = 465;
    private final String username = "FastEmaaiil@yandex.ru";
    private final String password = "EmaaiilFast";
    private final String protocol = "smtps";
    private final String debug = "true";

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);
        return mailSender;
    }

}
