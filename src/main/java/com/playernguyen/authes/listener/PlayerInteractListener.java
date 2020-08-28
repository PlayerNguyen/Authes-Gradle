package com.playernguyen.authes.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener extends AuthesListener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!getAccountManager().getAccountFromUUID(player.getUniqueId()).isRegistered()
        || !getSessionManager().hasSession(player.getUniqueId())) {

            event.setCancelled(true);
        }
    }
}
