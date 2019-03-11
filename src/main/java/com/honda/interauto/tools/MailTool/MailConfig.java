package com.honda.interauto.tools.MailTool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail.stmp")
public class MailConfig {
    private String imaphost;
    private String imapport;
    private String imapAuthPw;
    private String imapProtocol;

    private String smtphost;
    private String smtpport;
    private String smtpAuthPw;

    private String account;
    private String subject;
    private String context;
    private String isauth;
    private String timeout;

    private String receivers;

    public String getImaphost() {
        return imaphost;
    }

    public void setImaphost(String imaphost) {
        this.imaphost = imaphost;
    }

    public String getImapport() {
        return imapport;
    }

    public void setImapport(String imapport) {
        this.imapport = imapport;
    }

    public String getImapAuthPw() {
        return imapAuthPw;
    }

    public void setImapAuthPw(String imapAuthPw) {
        this.imapAuthPw = imapAuthPw;
    }

    public String getImapProtocol() {
        return imapProtocol;
    }

    public void setImapProtocol(String imapProtocol) {
        this.imapProtocol = imapProtocol;
    }

    public String getSmtphost() {
        return smtphost;
    }

    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    public String getSmtpport() {
        return smtpport;
    }

    public void setSmtpport(String smtpport) {
        this.smtpport = smtpport;
    }

    public String getSmtpAuthPw() {
        return smtpAuthPw;
    }

    public void setSmtpAuthPw(String smtpAuthPw) {
        this.smtpAuthPw = smtpAuthPw;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getIsauth() {
        return isauth;
    }

    public void setIsauth(String isauth) {
        this.isauth = isauth;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
}
