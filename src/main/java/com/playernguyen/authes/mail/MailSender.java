package com.playernguyen.authes.mail;

import com.playernguyen.authes.AuthesInstance;
import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.util.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MailSender extends AuthesInstance {

    private final String hostname;
    private final String username;
    private final String password;
    private final String sendFrom;
    private final String sendName;
    private final String subject;
    private final String serverName;
    private final String message;

    private final int port;

    private final boolean ssl;
    private final boolean tls;

    public MailSender () {
        this.hostname = getConfiguration().getString(ConfigurationFlag.EMAIL_SMTP_HOST);
        this.port = getConfiguration().getInt(ConfigurationFlag.EMAIL_SMTP_PORT);
        this.username = getConfiguration().getString(ConfigurationFlag.EMAIL_SMTP_USERNAME);
        this.password = getConfiguration().getString(ConfigurationFlag.EMAIL_SMTP_PASSWORD);
        this.sendFrom = getConfiguration().getString(ConfigurationFlag.EMAIL_SEND_FROM_EMAIL);
        this.sendName = getConfiguration().getString(ConfigurationFlag.EMAIL_SEND_FROM_NAME);
        this.serverName = getConfiguration().getString(ConfigurationFlag.SERVER_NAME);
        this.ssl = getConfiguration().getBoolean(ConfigurationFlag.EMAIL_SMTP_SSL_ENABLE);
        this.subject = getConfiguration().getString(ConfigurationFlag.EMAIL_SEND_SUBJECT);
        this.message = getConfiguration().getString(ConfigurationFlag.EMAIL_SEND_TEXT_NON_HTML);
        this.tls = getConfiguration().getBoolean(ConfigurationFlag.EMAIL_SMTP_TLS);
    }


    public void sendRecoveryMail(UUID uuid) throws EmailException, IOException {

        String recoveryKey = getSQLAccountManager().getRecoveryKey(uuid);
        String userMail = getSQLAccountManager().getEmail(uuid);

        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostname);
        email.setSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(this.ssl);
        email.setStartTLSEnabled(tls);

        email.setFrom(sendFrom, sendName);
        email.setSubject(subject);
        // Read file
        String body = FileUtils.fileToString(new File(getInstance().getDataFolder(), "email.html"));
        body = body.replace("%recovery_code%", recoveryKey).replace("%server_name%", serverName);
        email.setHtmlMsg(body);
        email.setMsg(body);
        email.addTo(userMail);
        email.send();
    }

}
