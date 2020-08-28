package com.playernguyen.authes.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener extends AuthesListener {

    @EventHandler
    public void onMoveListen(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!getAccountManager().getAccountFromUUID(player.getUniqueId()).isRegistered()
            || !getSessionManager().hasSession(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
