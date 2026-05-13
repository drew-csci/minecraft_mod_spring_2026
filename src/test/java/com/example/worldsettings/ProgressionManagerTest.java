package com.example.worldsettings;

import com.example.worldsettings.progression.ProgressionManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressionManagerTest {

    @Test
    void progressionScaleStartsAtHalf() {
        assertEquals(0.50, ProgressionManager.getProgressionScale(0), 0.0001);
    }

    @Test
    void progressionScaleReachesThreeQuartersAtHalfProgression() {
        assertEquals(0.75, ProgressionManager.getProgressionScale(50), 0.0001);
    }

    @Test
    void progressionScaleEndsAtFullStrength() {
        assertEquals(1.00, ProgressionManager.getProgressionScale(100), 0.0001);
    }

    @Test
    void progressionScaleClampsBadValues() {
        assertEquals(0.50, ProgressionManager.getProgressionScale(-25), 0.0001);
        assertEquals(1.00, ProgressionManager.getProgressionScale(150), 0.0001);
    }

    @Test
    void spawnCountScalesFromHalfToFull() {
        assertEquals(35, ProgressionManager.scaleCountByProgression(70, 0));
        assertEquals(53, ProgressionManager.scaleCountByProgression(70, 50));
        assertEquals(70, ProgressionManager.scaleCountByProgression(70, 100));
    }
}