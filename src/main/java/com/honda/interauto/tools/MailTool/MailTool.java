package com.honda.interauto.tools.MailTool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Component
public class MailTool {
    protected static Logger logger = LogManager.getLogger(MailTool.class);

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailConfig mailConfig;

    public Object sendEmail(String fromEmail, String toEmail, String subject, String context) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
//            String[] toEmailList = OtherTool.splitStr(toEmail, ",");
            InternetAddress[] internetAddressTo = InternetAddress.parse(toEmail);
            mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressTo);
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            //抄送
//            message.setCc(fromEmail);
            //收件人，单个时方法，用的list方法
//            message.setTo(toEmail);
            message.setFrom(fromEmail);
            message.setSubject(subject);
            message.setText(context);
            mailSender.send(mimeMessage);
            saveEmailToSentMailFolder(mimeMessage);
            logger.info(fromEmail + " 发送至 " + toEmail + " 邮件发送成功");
            return "success";
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return "failed";
        }
    }

    //保存邮件
    private void saveEmailToSentMailFolder(Message message) {
        Store store = null;
        Folder sentFolder = null;
        try {
            sentFolder = getSentMailFolder(message, store, mailConfig);
            // 设置已读标志
            message.setFlag(Flags.Flag.SEEN, true);
            if(mailConfig.getImaphost().equals("imap.qq.com")){
                sentFolder.appendMessages(new Message[]{message});
            }
            logger.info("========>已保存到【发件箱】与【其他文件夹/系统邮件】下...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 判断发件文件夹是否打开如果打开则将其关闭
            if (sentFolder != null && sentFolder.isOpen()) {
                try {
                    sentFolder.close(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            // 判断邮箱存储是否打开如果打开则将其关闭
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //获取用户的发件箱文件夹
    private Folder getSentMailFolder(Message message, Store store, MailConfig mailConfig) throws IOException, MessagingException {
        //准备连接服务器的会话信息
        String serverProtocol = mailConfig.getImapProtocol();
        String serverHost = mailConfig.getImaphost();
        String serverPort = mailConfig.getImapport();
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", serverProtocol);
        props.setProperty("mail.imap.host", serverHost);
        props.setProperty("mail.imap.port", serverPort);

        //创建Session实例对象
        Session session = Session.getInstance(props);
        URLName urln = new URLName(serverProtocol, serverHost, Integer.parseInt(serverPort), null, mailConfig.getAccount(), mailConfig.getImapAuthPw());
        //创建IMAP协议的Store对象
        store = session.getStore(urln);
        store.connect();
        // 获得发件箱
        Folder folder = store.getFolder("其他文件夹/系统邮件");
        // 以读写模式打开发件箱
        folder.open(Folder.READ_WRITE);

        return folder;
    }
}
