package com.playernguyen.authes.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerAsyncChatListener extends AuthesListener {

    @EventHandler
    public void asyncChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!getSessionManager().hasSession(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
