package com.example.worldsettings.boss;

import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoidDevourerSpawnerTest {

    @Test
    void getDifficultyMultiplier_returnsExpectedValues() {
        assertEquals(0.5, VoidDevourerSpawner.getDifficultyMultiplier(WorldSettings.DifficultyLevel.EASY));
        assertEquals(1.0, VoidDevourerSpawner.getDifficultyMultiplier(WorldSettings.DifficultyLevel.MEDIUM));
        assertEquals(1.5, VoidDevourerSpawner.getDifficultyMultiplier(WorldSettings.DifficultyLevel.HARD));
    }

    @Test
    void generateRandomSpawnNearby_returnsLocationWithinRadius() {
        Location center = new Location(null, 100, 64, 100);
        int radius = 50;

        for (int i = 0; i < 100; i++) {
            Location result = VoidDevourerSpawner.generateRandomSpawnNearby(center, radius);

            assertNotNull(result);

            double dx = result.getX() - center.getX();
            double dz = result.getZ() - center.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            assertTrue(distance <= radius, "Spawn location should be within radius");
            assertEquals(center.getY(), result.getY(), 0.0001);
        }
    }
}
