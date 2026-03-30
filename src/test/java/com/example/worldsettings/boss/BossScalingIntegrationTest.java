package com.example.worldsettings.boss;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Integration tests for Boss Scaling end-to-end flow.
 * Tests: spawn -> detect -> defeat -> increment tier -> respawn with scaling.
 * Uses Mockito to mock Bukkit entities.
 */
public class BossScalingIntegrationTest {

    private BossScalingManager manager;

    @Mock
    private LivingEntity mockBoss;

    @Mock
    private AttributeInstance mockHealthAttribute;

    @Mock
    private AttributeInstance mockDamageAttribute;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new BossScalingManager();
    }

    @Test
    public void testEndToEndBossScaling() {
        // Setup: Mock a Wither boss
        when(mockBoss.getType()).thenReturn(EntityType.WITHER);
        when(mockBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHealthAttribute);
        when(mockBoss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).thenReturn(mockDamageAttribute);

        String bossKey = manager.getBossKey(mockBoss);
        assertEquals("WITHER", bossKey, "Boss key should be WITHER");

        // Initial state: tier 0, no scaling
        int initialTier = manager.getTier(bossKey);
        assertEquals(0, initialTier, "Initial tier should be 0");

        double initialHealthMult = manager.getHealthMultiplier(initialTier);
        assertEquals(1.0, initialHealthMult, "Initial health multiplier should be 1.0x");

        // Simulate first defeat: increment tier and run scaling
        int newTier = manager.incrementDefeatCount(bossKey);
        assertEquals(1, newTier, "After defeat, tier should be 1");

        double scaledHealthMult = manager.getHealthMultiplier(newTier);
        assertTrue(scaledHealthMult > initialHealthMult, "Scaled health multiplier should be higher");

        double scaledDamageMult = manager.getDamageMultiplier(newTier);
        assertTrue(scaledDamageMult > 1.0, "Scaled damage multiplier should be > 1.0x");

        // Verify multipliers are applied correctly
        assertEquals(1.15, scaledHealthMult, 0.01, "Tier 1 health should be 1.15x");
        assertEquals(1.10, scaledDamageMult, 0.01, "Tier 1 damage should be 1.10x");
    }

    @Test
    public void testProgressiveTierAndScaling() {
        String bossKey = "WITHER";

        // Defeat 3 times
        for (int i = 1; i <= 3; i++) {
            manager.incrementDefeatCount(bossKey);
            assertEquals(i, manager.getTier(bossKey), "Tier should increment correctly");
        }

        // Verify each tier has higher multiplier
        double mult1 = manager.getHealthMultiplier(1);
        double mult2 = manager.getHealthMultiplier(2);
        double mult3 = manager.getHealthMultiplier(3);

        assertTrue(mult1 < mult2, "Tier 2 health > tier 1 health");
        assertTrue(mult2 < mult3, "Tier 3 health > tier 2 health");

        // Verify data export/import for persistence
        var exportedData = manager.exportData();
        assertEquals(3, exportedData.get(bossKey), "Exported data should have tier 3");

        // Simulate loading from persistence
        BossScalingManager newManager = new BossScalingManager();
        newManager.loadFromStorage(exportedData);
        assertEquals(3, newManager.getTier(bossKey), "Loaded tier should match exported");
    }

    @Test
    public void testMultipleBossTracking() {
        String wither = "WITHER";
        String enderDragon = "ENDER_DRAGON";

        manager.incrementDefeatCount(wither);
        manager.incrementDefeatCount(wither);
        manager.incrementDefeatCount(enderDragon);

        assertEquals(2, manager.getTier(wither), "Wither tier should be 2");
        assertEquals(1, manager.getTier(enderDragon), "Ender Dragon tier should be 1");

        double witherMult = manager.getHealthMultiplier(2);
        double dragonMult = manager.getHealthMultiplier(1);

        assertTrue(witherMult > dragonMult, "Wither (tier 2) should have higher mult than dragon (tier 1)");
    }
}

