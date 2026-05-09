package com.example.worldsettings.settings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldSettingsTest {

    @Test
    public void testPostEndToggleAndSetter() {
        WorldSettings ws = new WorldSettings();
        assertFalse(ws.isPostEndWorld(), "default post-end should be false");

        ws.togglePostEndWorld();
        assertTrue(ws.isPostEndWorld(), "toggle should set post-end true");

        ws.setPostEndWorld(false);
        assertFalse(ws.isPostEndWorld(), "setter should set post-end false");
    }

    @Test
    public void testPostEndDifficultyBounds() {
        WorldSettings ws = new WorldSettings();
        // default is 1.0
        assertEquals(1.0, ws.getPostEndDifficultyBoost());

        ws.increasePostEndDifficulty(); // 1.5
        ws.increasePostEndDifficulty(); // 2.0
        ws.increasePostEndDifficulty(); // 2.5
        ws.increasePostEndDifficulty(); // 3.0
        ws.increasePostEndDifficulty(); // remains 3.0
        assertEquals(3.0, ws.getPostEndDifficultyBoost(), 0.0001);

        ws.decreasePostEndDifficulty(); // 2.5
        ws.decreasePostEndDifficulty(); // 2.0
        ws.decreasePostEndDifficulty(); // 1.5
        ws.decreasePostEndDifficulty(); // 1.0
        ws.decreasePostEndDifficulty(); // remains 1.0
        assertEquals(1.0, ws.getPostEndDifficultyBoost(), 0.0001);
    }
}
