package com.example.worldsettings.boss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BossScalingManager tier progression and multiplier calculations.
 */
public class BossScalingManagerTest {

    private BossScalingManager manager;
    private static final String TEST_BOSS_KEY = "WITHER";

    @BeforeEach
    public void setUp() {
        manager = new BossScalingManager();
    }

    @Test
    public void testInitialTierIsZero() {
        assertEquals(0, manager.getTier(TEST_BOSS_KEY), "Initial tier should be 0");
    }

    @Test
    public void testIncrementDefeatCount() {
        int tier1 = manager.incrementDefeatCount(TEST_BOSS_KEY);
        assertEquals(1, tier1, "First defeat should result in tier 1");

        int tier2 = manager.incrementDefeatCount(TEST_BOSS_KEY);
        assertEquals(2, tier2, "Second defeat should result in tier 2");

        assertEquals(2, manager.getTier(TEST_BOSS_KEY), "Tier should persist");
    }

    @Test
    public void testHealthMultiplierIncreases() {
        double mult0 = manager.getHealthMultiplier(0);
        double mult1 = manager.getHealthMultiplier(1);
        double mult2 = manager.getHealthMultiplier(2);

        assertEquals(1.0, mult0, 0.01, "Tier 0 should have 1.0x multiplier");
        assertTrue(mult1 > mult0, "Tier 1 should have higher multiplier than tier 0");
        assertTrue(mult2 > mult1, "Tier 2 should have higher multiplier than tier 1");

        // Expected: 1.0 + (1 * 0.15) = 1.15
        assertEquals(1.15, mult1, 0.01, "Tier 1 should have 1.15x multiplier");

        // Expected: 1.0 + (2 * 0.15) = 1.30
        assertEquals(1.30, mult2, 0.01, "Tier 2 should have 1.30x multiplier");
    }

    @Test
    public void testDamageMultiplierIncreases() {
        double mult0 = manager.getDamageMultiplier(0);
        double mult1 = manager.getDamageMultiplier(1);
        double mult2 = manager.getDamageMultiplier(2);

        assertEquals(1.0, mult0, 0.01, "Tier 0 should have 1.0x damage multiplier");
        assertTrue(mult1 > mult0, "Tier 1 should have higher damage multiplier");
        assertTrue(mult2 > mult1, "Tier 2 should have higher damage multiplier");

        // Expected: 1.0 + (1 * 0.10) = 1.10
        assertEquals(1.10, mult1, 0.01, "Tier 1 should have 1.10x damage multiplier");

        // Expected: 1.0 + (2 * 0.10) = 1.20
        assertEquals(1.20, mult2, 0.01, "Tier 2 should have 1.20x damage multiplier");
    }

    @Test
    public void testMultiplierCap() {
        // At tier 33+, multiplier should be capped at 5.0x
        double highTierMult = manager.getHealthMultiplier(50);
        assertEquals(5.0, highTierMult, 0.01, "Multiplier should be capped at 5.0x");
    }

    @Test
    public void testMultipliplierMonotonicity() {
        // Verify multipliers increase monotonically
        for (int i = 0; i < 30; i++) {
            double current = manager.getHealthMultiplier(i);
            double next = manager.getHealthMultiplier(i + 1);
            assertTrue(next >= current, "Multiplier should not decrease with higher tier");
        }
    }

    @Test
    public void testResetBoss() {
        manager.incrementDefeatCount(TEST_BOSS_KEY);
        assertEquals(1, manager.getTier(TEST_BOSS_KEY));

        manager.resetBoss(TEST_BOSS_KEY);
        assertEquals(0, manager.getTier(TEST_BOSS_KEY), "After reset, tier should be 0");
    }

    @Test
    public void testClearAll() {
        manager.incrementDefeatCount("BOSS1");
        manager.incrementDefeatCount("BOSS2");

        manager.clearAll();
        assertEquals(0, manager.getTier("BOSS1"));
        assertEquals(0, manager.getTier("BOSS2"));
    }
}
