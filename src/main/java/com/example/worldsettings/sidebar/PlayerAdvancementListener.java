package com.example.worldsettings.sidebar;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancementListener implements Listener {

    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        ProgressionSidebar.updateSidebar(
                event.getPlayer(),
                WorldSettingsPlugin.getInstance().getProgressionManager()
        );
    }
}