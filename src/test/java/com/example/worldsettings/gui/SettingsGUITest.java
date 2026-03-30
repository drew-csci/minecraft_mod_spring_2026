package com.example.worldsettings.gui;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsGUITest {

    @Test
    void buildToggleLoreProducesCorrectStatusAndInstructions() {
        List<String> lore = SettingsGUI.buildToggleLore(true, null);

        assertNotNull(lore, "Lore should not be null");
        assertEquals(3, lore.size(), "Lore should include status, blank line, and instructions when extra lore is null");
        assertTrue(lore.get(0).contains("Status:"), "First line should include status text");
        assertTrue(lore.get(0).contains("ON"), "Status line should indicate ON");
        assertTrue(lore.get(2).contains("Click to toggle"), "Last line should include click instruction");
    }

    @Test
    void buildToggleLoreIncludesExtraLoreWhenProvided() {
        List<String> extraLore = Arrays.asList(
            "Performance: Moderate",
            "Spawns more mobs when enabled"
        );

        List<String> lore = SettingsGUI.buildToggleLore(false, extraLore);

        assertNotNull(lore, "Lore should not be null");
        assertEquals(6, lore.size(), "Lore should include status, blank line, extra lore, blank line, and instructions");
        assertTrue(lore.get(0).contains("Status:"), "First line should include status text");
        assertTrue(lore.get(0).contains("OFF"), "Status line should indicate OFF");
        assertTrue(lore.contains("Performance: Moderate"), "Extra lore should be present");
        assertTrue(lore.contains("Spawns more mobs when enabled"), "Extra lore should be present");
        assertTrue(lore.get(lore.size() - 1).contains("Click to toggle"), "Last line should include click instructions");
    }
}
