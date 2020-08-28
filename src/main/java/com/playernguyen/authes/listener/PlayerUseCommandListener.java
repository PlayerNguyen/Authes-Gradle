package com.playernguyen.authes.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerUseCommandListener extends AuthesListener {

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent e) {
        System.out.println(e.getMessage());
        e.setCancelled(true);
    }

}
