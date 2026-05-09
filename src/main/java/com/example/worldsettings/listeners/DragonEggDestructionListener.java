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

    static boolean shouldSpawnBoss(Material material, World.Environment environment, WorldSettings settings) {
        if (material != Material.DRAGON_EGG) {
            return false;
        }

        if (environment != World.Environment.NORMAL) {
            return false;
        }

        return settings.isNewBoss();
    }

    void triggerSpawn(World world, Player player, WorldSettings settings) {
        VoidDevourerSpawner.spawnVoidDevourer(world, player, settings);
    }

    void handleDragonEggBreak(Material material, World.Environment environment, World world, Player player, WorldSettings settings) {
        if (!shouldSpawnBoss(material, environment, settings)) {
            return;
        }

        triggerSpawn(world, player, settings);
    }

    @EventHandler
    public void onDragonEggDestruction(BlockBreakEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        WorldSettings settings = WorldSettingsPlugin.getInstance().getWorldSettings();

        handleDragonEggBreak(
                event.getBlock().getType(),
                world.getEnvironment(),
                world,
                player,
                settings
        );
    }
}
