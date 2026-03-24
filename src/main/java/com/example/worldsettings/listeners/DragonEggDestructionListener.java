package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.boss.VoidDevourerSpawner;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * Listens for the destruction of the Dragon Egg and spawns the Void Devourer boss.
 */
public class DragonEggDestructionListener implements Listener {

    @EventHandler
    public void onDragonEggDestruction(BlockBreakEvent event) {
        // Check if the destroyed block is a dragon egg
        if (event.getBlock().getType() != Material.DRAGON_EGG) {
            return;
        }

        // Get the player who destroyed it
        Player player = event.getPlayer();
        World world = player.getWorld();

        // Check if this is the Overworld (End dimension events may need custom handling)
        if (world.getEnvironment() != World.Environment.NORMAL) {
            return;
        }

        // Check if the New Boss feature is enabled
        WorldSettings settings = WorldSettingsPlugin.getInstance().getWorldSettings();
        if (!settings.isNewBoss()) {
            return;
        }

        // Trigger the Void Devourer spawn sequence
        VoidDevourerSpawner.spawnVoidDevourer(world, player, settings);
    }
}
