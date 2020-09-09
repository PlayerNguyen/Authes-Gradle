package com.playernguyen.authes.util;

import com.playernguyen.authes.AuthesInstance;
import com.playernguyen.authes.account.Session;
import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.config.LanguageFlag;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AnvilDialogUtil extends AuthesInstance {

    public static AnvilDialogUtil getCurrentInstance() {
        return new AnvilDialogUtil();
    }
    public void showDialog(Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(getInstance(), () -> {
            // Register popup
            if (!getAccountManager().getAccountFromUUID(p.getUniqueId()).isRegistered()) {
                new AnvilGUI.Builder()
                        .plugin(getInstance())
                        .text(getLanguage().get(LanguageFlag.COMPONENT_REGISTER_TEXT))
                        .onComplete((_p, _s) -> {
                            // Run task
                            Bukkit.getScheduler().runTaskLaterAsynchronously(getInstance(), () -> {
                                new AnvilGUI.Builder()
                                        .plugin(getInstance())
                                        .text(getLanguage().get(LanguageFlag.COMPONENT_RECONFIRM_REGISTER_TEXT))
                                        .onComplete((_p1, _cs) -> {
                                            // Invalid confirm
                                            if (!_s.equalsIgnoreCase(_cs)) {
                                                _p1.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.PASSWORD_NOT_MATCH));

                                            }
                                            // Register the account
                                            if (!getSQLAccountManager().register(_p, _p.getUniqueId(), _s)) {
                                                _p1.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.REGISTER_FAILED));
                                                return AnvilGUI.Response.text(getLanguage().get(LanguageFlag.REGISTER_FAILED));
                                            } else {
                                                // Send success
                                                _p1.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.REGISTER_SUCCESS));
                                                // Set register
                                                getAccountManager().getAccountFromUUID(_p1.getUniqueId()).setRegistered(true);
                                                // Login
                                                getSessionManager().add(new Session(_p1.getUniqueId()));
                                                _p1.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.LOGIN_SUCCESS));
                                            }
                                            return AnvilGUI.Response.close();
                                        })
                                        .preventClose()
                                        .open(_p);
                            }, 10);
                            return AnvilGUI.Response.close();
                        })
                        //.onClose(p1 -> p1.kickPlayer(getLanguage().get(LanguageFlag.KICK_REASON)))
                        .preventClose()
                        .open(p);
            } else {
                // Login pop up
                new AnvilGUI.Builder()
                        .plugin(getInstance())
                        .text(getLanguage().get(LanguageFlag.COMPONENT_LOGIN_TEXT))
                        .onComplete((_p, _s) -> {
                            // Login failed
                            if (!getSQLAccountManager().login(_p.getUniqueId(), _s)) {
                                _p.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.LOGIN_FAIL));
                                return AnvilGUI.Response.text(getLanguage().get(LanguageFlag.COMPONENT_WRONG_PASSWORD));
                            } else {
                                _p.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.LOGIN_SUCCESS));
                                getSessionManager().add(new Session(_p.getUniqueId()));
                            }
                            return AnvilGUI.Response.close();
                        })
                        .preventClose()
                        .open(p);
            }
        }, getConfiguration().getInt(ConfigurationFlag.TICK_POPUP_SHOW));
    }

}
