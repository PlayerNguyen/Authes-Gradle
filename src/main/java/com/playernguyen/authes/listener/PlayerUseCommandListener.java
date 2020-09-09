package com.playernguyen.authes.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerUseCommandListener extends AuthesListener {

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent e) {
//        Player player = e.getPlayer();
//
//        if (!getSessionManager().hasSession(player.getUniqueId())) {
//            e.setCancelled(true);
//        }
    }

}
