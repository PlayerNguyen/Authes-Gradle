package com.playernguyen.authes.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AuthesListener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Check whether
        if (getSessionManager().hasSession(player.getUniqueId())) {
            getSessionManager().removeSession(player.getUniqueId());
        }
        // Clear account
        if (getAccountManager().getAccountFromUUID(player.getUniqueId()) != null) {
            getAccountManager()
                    .getContainer()
                    .remove(getAccountManager()
                            .getAccountFromUUID(player.getUniqueId()));
        }
        // Set logged
        getSQLAccountManager().setLogged(player, player.getUniqueId(), false);
    }

}
