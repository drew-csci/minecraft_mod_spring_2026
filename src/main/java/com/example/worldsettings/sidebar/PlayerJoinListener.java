package com.example.worldsettings.sidebar;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ProgressionSidebar.updateSidebar(
                event.getPlayer(),
                WorldSettingsPlugin.getInstance().getProgressionManager()
        );
    }
}