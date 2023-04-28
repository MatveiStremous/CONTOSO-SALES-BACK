package com.example.contoso.utils;

import com.example.contoso.dto.response.MailResponse;
import com.example.contoso.entity.enums.PaymentMethod;
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
import java.util.List;
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

    public void send(String emailTo, String subject, String message, List<MailResponse> mailResponseList, Double price, PaymentMethod paymentMethod) {
        MimeMessage messageTo = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(messageTo, true, "UTF-8");

            String html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <title>Order Summary</title>\n" +
                    "    <style>\n" +
                    getCSS() +
                    "    </style>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div class=\"container\">\n" +
                    "      <h1>" + message + "</h1><br>\n" +
                    "      <table>\n" +
                    "        <tr>\n" +
                    "          <th>Продукт</th>\n" +
                    "          <th>Количество</th>\n" +
                    "          <th>Стоимость</th>\n" +
                    "        </tr>\n";

            for (MailResponse product : mailResponseList) {
                html += "        <tr>\n" +
                        "          <td>" + product.productName() + "</td>\n" +
                        "          <td>" + product.productAmount() + " шт." + "</td>\n" +
                        "          <td>" + product.price() + " руб. </td>\n" +
                        "        </tr>\n";
            }

            html += "      </table>\n" +
                    "      <div class=\"total\">\n" +
                    "        Итоговая стоимость: " + price + "\n" +
                    "      </div>\n" +
                    "      <div class=\"payment-method\">\n" +
                    "        <label>Способ оплаты:\n" +
                    "        " + paymentMethod.getUrl() + "</label>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>";


            helper.setFrom(username);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(messageTo);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    private String getCSS() {
        return " body {\n" +
                "    font-family: Arial, sans-serif;\n" +
                "    font-size: 14px;\n" +
                "    line-height: 1.5;\n" +
                "    background-color: #f5f5f5;\n" +
                "    margin: 0;\n" +
                "    padding: 0;\n" +
                "  }\n" +
                "\n" +
                "  h1, h2, h3, h4, h5, h6 {\n" +
                "    font-weight: bold;\n" +
                "    margin: 0;\n" +
                "  }\n" +
                "\n" +
                "  table {\n" +
                "    width: 100%;\n" +
                "    border-collapse: collapse;\n" +
                "  }\n" +
                "\n" +
                "  table, th, td {\n" +
                "    border: 1px solid #ccc;\n" +
                "  }\n" +
                "\n" +
                "  th, td {\n" +
                "    padding: 8px;\n" +
                "    text-align: left;\n" +
                "  }\n" +
                "\n" +
                "  th {\n" +
                "    background-color: #f2f2f2;\n" +
                "  }\n" +
                "\n" +
                "  /* Define styles for specific elements */\n" +
                "  .container {\n" +
                "    max-width: 800px;\n" +
                "    margin: 0 auto;\n" +
                "    background-color: #fff;\n" +
                "    padding: 20px;\n" +
                "    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "  }\n" +
                "\n" +
                "  .total {\n" +
                "    font-size: 18px;\n" +
                "    font-weight: bold;\n" +
                "    margin-top: 20px;\n" +
                "  }\n" +
                "\n" +
                "  .payment-method {\n" +
                "    margin-top: 20px;\n" +
                "  }\n" +
                "\n" +
                "  .payment-method label {\n" +
                "    font-weight: bold;\n" +
                "    display: block;\n" +
                "    margin-bottom: 5px;\n" +
                "  }";
    }

}