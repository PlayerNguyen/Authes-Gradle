package com.playernguyen.authes.schedule;

import com.playernguyen.authes.Authes;
import com.playernguyen.authes.account.AccountManager;
import com.playernguyen.authes.account.SessionManager;
import com.playernguyen.authes.config.AuthesConfiguration;
import com.playernguyen.authes.config.AuthesLanguage;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AuthesRunnable extends BukkitRunnable {

    public Authes getInstance() {
        return Authes.getInstance();
    }

    public AuthesConfiguration getConfiguration() {
        return this.getInstance().getConfiguration();
    }

    public AuthesLanguage getLanguage() {
        return this.getInstance().getLanguage();
    }

    public AccountManager getAccountManager() {
        return this.getInstance().getAccountManager();
    }

    public SessionManager getSessionManager() {
        return this.getInstance().getSessionManager();
    }

}
