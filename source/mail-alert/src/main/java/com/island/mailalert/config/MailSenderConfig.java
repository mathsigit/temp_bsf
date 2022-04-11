package com.island.mailalert.config;

import com.island.mailalert.model.MailConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration(value = "MailSenderConfig")
public class MailSenderConfig {

    @Value("${mail.sender.email}")
    private String senderEmail;
    @Value("${mail.sender.passwd}")
    private String senderPasswd;
    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private String smtpPort;
    @Value("${mail.smtp.auth}")
    private String smtpAuth;
    @Value("${mail.smtp.socketfactory.port}")
    private String smtpSocketFactoryPort;
    @Value("${mail.smtp.socketfactory.class}")
    private String smtpSocketFactoryClass;

    @Bean
    public MailConfigBean getMailConfig() throws IOException {
        String mailContentTemplate = getTempEmailContent("email.template");
        String mailEventTemplate = getTempEmailContent("email-event.template");

        return MailConfigBean.builder()
                .senderEmail(senderEmail)
                .senderPasswd(senderPasswd)
                .smtpAuth(smtpAuth)
                .smtpHost(smtpHost)
                .smtpPort(smtpPort)
                .smtpSocketFactoryClass(smtpSocketFactoryClass)
                .smtpSocketFactoryPort(smtpSocketFactoryPort)
                .mailContentTemplate(mailContentTemplate)
                .mailEventTemplate(mailEventTemplate)
                .build();
    }

    private String getTempEmailContent(String fileName) {
        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(fileName))
                        , StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
