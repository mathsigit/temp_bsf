package com.island.mailalert.service;

import com.island.mailalert.config.MailSenderConfig;
import com.island.mailalert.model.DiCurrentAndHistoryResult;
import com.island.mailalert.model.DiCurrentResultAndTables;
import com.island.mailalert.model.MailConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service(value = "MailSenderService")
public class MailSenderService {
    Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    @Autowired
    @Qualifier("MailSenderConfig")
    private MailSenderConfig mailSenderConfig;

    public void send(List<DiCurrentResultAndTables> diCurrentResultAndTablesList,
                     List<DiCurrentAndHistoryResult> diCurrentAndHistoryResultList) throws IOException, MessagingException {
        logger.info("Start to send email!");

        MailConfigBean mailConfigBean = mailSenderConfig.getMailConfig();

        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailConfigBean.getSmtpHost());
        prop.put("mail.smtp.port", mailConfigBean.getSmtpPort());
        prop.put("mail.smtp.auth", mailConfigBean.getSmtpAuth());
        prop.put("mail.smtp.socketFactory.port", mailConfigBean.getSmtpSocketFactoryPort());
        prop.put("mail.smtp.socketFactory.class", mailConfigBean.getSmtpSocketFactoryClass());

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailConfigBean.getSenderEmail(), mailConfigBean.getSenderPasswd());
                    }
                });

        for(DiCurrentResultAndTables c : diCurrentResultAndTablesList) {
            if(c.getJobStreamStatus().equals("SUCC") && c.getInspection().equals("N")) {
                List<DiCurrentAndHistoryResult> singleTableResult = diCurrentAndHistoryResultList.stream()
                        .filter(tr -> tr.getTableName().equals(c.getTableName()))
                        .filter(tr -> tr.getTableOwner().equals(c.getTableOwner()))
                        .collect(Collectors.toList());
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailConfigBean.getSenderEmail()));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(c.getOwnerEmail())
                );
                message.setSubject("Data Inspection Alert - JS NAME :"+c.getJobStreamName());
                message.setText("\n\n" + getEmailLISTValueContent(c, singleTableResult));
                Transport.send(message);

                logger.info("Email sent to : " + c.getOwnerEmail() + " ,and JS NAME: " + c.getJobStreamName());
            }
        }
    }

    private String getEmailLISTValueContent(DiCurrentResultAndTables diCurrentResultAndTables,
                                            List<DiCurrentAndHistoryResult> diCurrentAndHistoryResult) throws IOException {
        String result = mailSenderConfig.getMailConfig().getMailContentTemplate()
                .replace("@JOB_STREAM_NAME", diCurrentResultAndTables.getJobStreamName())
                .replace("@TABLE_OWNER",diCurrentResultAndTables.getTableOwner())
                .replace("@TABLE_NAME",diCurrentResultAndTables.getTableName())
                .replace("@OWNER_EMAIL",diCurrentResultAndTables.getOwnerEmail())
                .replace("@ETL_METHOD",(diCurrentResultAndTables.getEtlMethod() == null) ? "":diCurrentResultAndTables.getEtlMethod())
                .replace("@RUN_CYCLE",(diCurrentResultAndTables.getRunCycle() == null) ? "":diCurrentResultAndTables.getRunCycle())
                .replace("@DI_DATETIME",diCurrentResultAndTables.getDiDatetime());
        StringBuilder eventTemplate= new StringBuilder();
        for(DiCurrentAndHistoryResult chr :diCurrentAndHistoryResult ) {
            String conditionTemplate = getEventCountContent(
                    mailSenderConfig.getMailConfig().getMailEventTemplate(),
                    chr
            );
            eventTemplate.append("\n\n").append(conditionTemplate).append("\n\n");
        }
        result = result.replace("@@EVENT-TEMPLATE", eventTemplate.toString());
        return result;
    }

    private String getEventCountContent(String templateContent,
                                             DiCurrentAndHistoryResult diCurrentAndHistoryResult) {
        return templateContent
                .replace("@CONDITION",diCurrentAndHistoryResult
                        .getCondition()+"("+diCurrentAndHistoryResult.getConditionColumn()+")")
                .replace("@ORACLE",diCurrentAndHistoryResult.getOracleResult())
                .replace("@HIVE",diCurrentAndHistoryResult.getHiveResult());
    }
}
