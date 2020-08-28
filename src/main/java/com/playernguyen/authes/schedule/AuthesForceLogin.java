package com.playernguyen.authes.schedule;

import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.config.LanguageFlag;
import org.bukkit.entity.Player;

public class AuthesForceLogin extends AuthesRunnable {

    private int ticker;
    private final Player player;

    public AuthesForceLogin(Player player) {
        this.player = player;
        this.ticker = getConfiguration().getInt(ConfigurationFlag.KICK_AFTER_LOGIN);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void run() {
        // Update tick
        this.ticker --;

        // If out of time (idle), kick player
        if (ticker <= 0) {
            this.getPlayer().kickPlayer(this.getLanguage().get(LanguageFlag.KICK_REASON));
            // Cancel the task too
            this.cancel();
            return;
        }

        // Player not found
        if (player == null) {
            this.cancel();
            return;
        }

        // Not found account
        if (getAccountManager().getAccountFromUUID(getPlayer().getUniqueId()) == null) {
            this.cancel();
            return;
        }

        // Is not register
        if (!getAccountManager().getAccountFromUUID(getPlayer().getUniqueId()).isRegistered()) {
            // Anti spam
            if (ticker % 2 == 0) {
                this.getPlayer().sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.REQUIRE_REGISTER));
            }
            return;
        }

        // Not has session
        if (!getSessionManager().hasSession(player.getUniqueId())) {
            if (ticker % 2 == 0) {
                this.getPlayer().sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.REQUIRE_LOGIN));

            }
            return;
        }

        this.cancel();
    }
}
