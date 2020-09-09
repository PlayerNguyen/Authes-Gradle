package com.playernguyen.authes.listener;

import com.playernguyen.authes.account.Account;
import com.playernguyen.authes.account.Session;
import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.config.LanguageFlag;
import com.playernguyen.authes.schedule.AuthesForceLogin;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener extends AuthesListener {

    @EventHandler
    public void onPlayerJoinListen(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // Put into AccountManager
        Account account = getSQLAccountManager().getAccount(p.getUniqueId());
        getAccountManager().add(account);

        getInstance().getLogger().info(String.format(
                "Player %s (%s), registered: %s",
                p.getName(), p.getUniqueId(), (account.isRegistered()) ? "Yes":"No"
        ));

        // Anvil dialog
        if (getConfiguration().getBoolean(ConfigurationFlag.USING_ANVIL_DIALOG)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(getInstance(), () -> {
                // Register popup
                if (!getAccountManager().getAccountFromUUID(p.getUniqueId()).isRegistered()) {
                    new AnvilGUI.Builder()
                            .plugin(getInstance())
                            .text(getLanguage().get(LanguageFlag.COMPONENT_REGISTER_TEXT))
                            .onComplete((_p, _s) -> {
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
                                                _p1.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.REGISTER_SUCCESS));
                                            }
                                            return AnvilGUI.Response.close();
                                        })
                                        .preventClose()
                                        .open(_p);
                                return AnvilGUI.Response.close();
                            })
                            .onClose(p1 -> p1.kickPlayer(getLanguage().get(LanguageFlag.KICK_REASON)))
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

        // Create new task to force player :)
        BukkitRunnable runnable = new AuthesForceLogin(p);
        runnable.runTaskTimer(getInstance(), 20, 20);

    }

}
