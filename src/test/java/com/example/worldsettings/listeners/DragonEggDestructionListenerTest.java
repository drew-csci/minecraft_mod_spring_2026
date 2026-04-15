package com.example.worldsettings.listeners;

import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DragonEggDestructionListenerTest {

    @Test
    void shouldSpawnBoss_returnsFalseForWrongBlock() {
        WorldSettings settings = new WorldSettings();
        settings.toggleNewBoss();

        boolean result = DragonEggDestructionListener.shouldSpawnBoss(
                Material.STONE,
                World.Environment.NORMAL,
                settings
        );

        assertFalse(result);
    }

    @Test
    void shouldSpawnBoss_returnsTrueForDragonEggInOverworldWhenEnabled() {
        WorldSettings settings = new WorldSettings();
        settings.toggleNewBoss();

        boolean result = DragonEggDestructionListener.shouldSpawnBoss(
                Material.DRAGON_EGG,
                World.Environment.NORMAL,
                settings
        );

        assertTrue(result);
    }

    @Test
    void handleDragonEggBreak_callsTriggerSpawnWhenConditionsAreMet() {
        class TestListener extends DragonEggDestructionListener {
            boolean triggered = false;

            @Override
            void triggerSpawn(World world, org.bukkit.entity.Player player, WorldSettings settings) {
                triggered = true;
            }
        }

        TestListener listener = new TestListener();

        World world = null;
        org.bukkit.entity.Player player = null;

        WorldSettings settings = new WorldSettings();
        settings.toggleNewBoss();

        listener.handleDragonEggBreak(
                Material.DRAGON_EGG,
                World.Environment.NORMAL,
                world,
                player,
                settings
        );

        assertTrue(listener.triggered);
    }
}
