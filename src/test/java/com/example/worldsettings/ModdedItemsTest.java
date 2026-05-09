package com.example.worldsettings;

import com.example.worldsettings.settings.WorldSettings;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ModdedItems.
 * Contains two unit tests and one integration-style test in a separate file.
 */
class ModdedItemsTest {

    @Test
    void happyPathReturnsExpectedDropChanceForKnownItem() {
        assertEquals(5.0, ModdedItems.getDropChance("Zombie Arm"), 0.0001,
                "Happy path: known item should return its configured drop chance.");
    }

    @Test
    void edgeCaseReturnsZeroForEmptyItemName() {
        assertEquals(0.0, ModdedItems.getDropChance(""), 0.0001,
                "Edge case: empty item name should return 0.0 drop chance.");
    }

    @Test
    @Tag("integration")
    void integrationTestWorldSettingsAndDropChanceRegistryWorkTogether() {
        WorldSettings settings = new WorldSettings();

        assertEquals(WorldSettings.DifficultyLevel.EASY, settings.getDifficultyLevel(),
                "Initial difficulty level should be EASY.");
        settings.cycleDifficulty();
        assertEquals(WorldSettings.DifficultyLevel.MEDIUM, settings.getDifficultyLevel(),
                "Cycling difficulty once should move to MEDIUM.");
        settings.cycleDifficulty();
        assertEquals(WorldSettings.DifficultyLevel.HARD, settings.getDifficultyLevel(),
                "Cycling difficulty twice should move to HARD.");
        settings.cycleDifficulty();
        assertEquals(WorldSettings.DifficultyLevel.EASY, settings.getDifficultyLevel(),
                "Cycling difficulty three times should wrap back to EASY.");

        assertEquals("Easy", WorldSettings.DifficultyLevel.EASY.display());
        assertEquals("Medium", WorldSettings.DifficultyLevel.MEDIUM.display());
        assertEquals("Hard", WorldSettings.DifficultyLevel.HARD.display());

        assertEquals(5.0, ModdedItems.getDropChance("Zombie Arm"), 0.0001,
                "The drop chance registry should return the configured value for a known item.");
    }
}
