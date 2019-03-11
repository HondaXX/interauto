package com.honda.interauto.tools.MailTool;

import com.honda.interauto.tools.sysTool.OtherTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailInit {
    @Autowired
    private MailConfig mailConfig;
    @Bean(name = "javaMailSender")
    public JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailConfig.getSmtphost());
        javaMailSender.setPort(Integer.parseInt(mailConfig.getSmtpport()));
        javaMailSender.setUsername(mailConfig.getAccount());
        javaMailSender.setPassword(mailConfig.getSmtpAuthPw());

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", mailConfig.getIsauth());
        properties.put("mail.smtp.timeout", mailConfig.getTimeout());
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }

    public void sendSimpleMail(String receiver, String subject, String context) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailConfig.getAccount());

        String receivers[] = OtherTool.splitStr(receiver, ",");
        simpleMailMessage.setTo(receivers);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(context);
        getMailSender().send(simpleMailMessage);
    }
}
