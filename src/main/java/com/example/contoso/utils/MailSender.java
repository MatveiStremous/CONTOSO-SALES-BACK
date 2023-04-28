package com.example.contoso.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MailSender {
    private final JavaMailSender mailSender;
    private final String username = "FastEmaaiil@yandex.ru";


    public void send(String emailTo, String subject, String message, MultipartFile multipartFile) throws MessagingException, IOException {
        MimeMessage messageTo = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messageTo, true);
        helper.setFrom(username);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(message);
        byte[] fileData = multipartFile.getBytes();
        ByteArrayResource resource = new ByteArrayResource(fileData);
        helper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), resource);
        mailSender.send(messageTo);
    }
}