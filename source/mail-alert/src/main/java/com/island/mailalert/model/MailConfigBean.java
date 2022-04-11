package com.island.mailalert.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailConfigBean {
    private String senderEmail;
    private String senderPasswd;
    private String smtpHost;
    private String smtpPort;
    private String smtpAuth;
    private String smtpSocketFactoryPort;
    private String smtpSocketFactoryClass;
    private String mailContentTemplate;
    private String mailEventTemplate;
}
