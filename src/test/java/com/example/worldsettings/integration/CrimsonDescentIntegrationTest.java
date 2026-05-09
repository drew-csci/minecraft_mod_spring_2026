package com.example.worldsettings.integration;

import com.example.worldsettings.listeners.CrimsonDescentManager;
import com.example.worldsettings.settings.WorldSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration-style test that simulates nightly rolls for the Crimson Descent using the
 * exposed logic. This test verifies that within the configured max gap days, at least one
 * night will trigger the event when chance increases by 5% each failure.
 */
public class CrimsonDescentIntegrationTest {

    @Test
    public void testEventTriggersWithinMaxGap() {
        WorldSettings ws = new WorldSettings();
        int base = ws.getCrimsonBaseChancePercent();
        int maxGap = ws.getCrimsonMaxGapDays();

        boolean triggered = false;
        int chance = base;
        for (int day = 0; day <= maxGap; day++) {
            // simulate worst-case roll (always fail) until forced
            int roll = chance + 1; // force fail unless chance >= 100
            if (CrimsonDescentManager.shouldTrigger(chance, day, maxGap, roll)) {
                triggered = true;
                break;
            }
            chance = Math.min(100, chance + 5);
        }

        // The manager forces trigger at or before maxGap, so this must be true
        assertTrue(triggered, "Crimson Descent should trigger at least once within max gap days");
    }
}
