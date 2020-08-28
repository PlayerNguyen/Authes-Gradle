package com.playernguyen.authes;

import com.playernguyen.authes.account.AccountManager;
import com.playernguyen.authes.account.SQLAccountManager;
import com.playernguyen.authes.account.SessionManager;
import com.playernguyen.authes.config.AuthesConfiguration;
import com.playernguyen.authes.config.AuthesLanguage;
import com.playernguyen.authes.mail.MailSender;
import com.playernguyen.authes.sql.SQLEstablishment;

public abstract class AuthesInstance {

    protected Authes getInstance() {
        return Authes.getInstance();
    }

    protected AuthesConfiguration getConfiguration() {
        return getInstance().getConfiguration();
    }

    protected SQLEstablishment getEstablishment() {
        return getInstance().getEstablishment();
    }

    protected AccountManager getAccountManager() {
        return getInstance().getAccountManager();
    }

    protected SQLAccountManager getSQLAccountManager() {
        return getInstance().getSQLAccountManager();
    }

    protected SessionManager getSessionManager() {
        return getInstance().getSessionManager();
    }

    protected AuthesLanguage getLanguage() {
        return getInstance().getLanguage();
    }

    protected MailSender getMailSender() {
        return getInstance().getMailSender();
    }

}
