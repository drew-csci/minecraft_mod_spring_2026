package com.example.worldsettings.listeners;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrimsonDescentLogicTest {

    @Test
    public void testShouldTriggerByRoll() {
        // chance 10, days since last 0, roll 5 -> trigger
        assertTrue(CrimsonDescentManager.shouldTrigger(10, 0, 7, 5));
        // roll outside chance -> no trigger
        assertFalse(CrimsonDescentManager.shouldTrigger(10, 0, 7, 50));
    }

    @Test
    public void testShouldTriggerByMaxGap() {
        // when daysSinceLast >= maxGapDays it must trigger regardless of roll
        assertTrue(CrimsonDescentManager.shouldTrigger(10, 7, 7, 99));
        assertTrue(CrimsonDescentManager.shouldTrigger(10, 8, 7, 99));
    }
}
