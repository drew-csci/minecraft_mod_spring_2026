package com.example.worldsettings.listeners;

import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MobBehaviorManagerDodgeTest {

    @Mock
    private LivingEntity mob;

    @Mock
    private Player attacker;

    // Use real Location instances instead of mocking (avoids ByteBuddy/mockito inline issues)

    @Test
    public void testRollForDodgeAndCanDodgeHelpers() {
        // rollForDodge: chance 50, roll 25 -> true
        assertTrue(MobBehaviorManager.rollForDodge(50, 25));
        assertFalse(MobBehaviorManager.rollForDodge(50, 51));

        // canDodge: last 0, now 1000, cooldown 500 -> true
        assertTrue(MobBehaviorManager.canDodge(0L, 1000L, 500L));
        // cooldown not expired
        assertFalse(MobBehaviorManager.canDodge(900L, 1000L, 200L));
    }

    @Test
    public void testTryHandleDodge_appliesVelocityAndRespectsCooldown() {
        // Prepare WorldSettings: enable dodge, 100% chance, long cooldown
        WorldSettings ws = new WorldSettings();
        ws.setEnhancedMobDodgeEnabled(true);
        ws.setDodgeChancePercent(100);
        ws.setDodgeCooldownTicks(2000); // long cooldown to ensure second dodge blocked

    // Coordinates: attacker at origin, mob at x=1
    Location attackerLoc = new Location(null, 0, 0, 0);
    Location mobLoc = new Location(null, 1, 0, 0);
    when(attacker.getLocation()).thenReturn(attackerLoc);
    when(mob.getLocation()).thenReturn(mobLoc);
        UUID id = UUID.randomUUID();
        when(mob.getUniqueId()).thenReturn(id);

        MobBehaviorManager manager = new MobBehaviorManager(null, ws, true);

        // First call should set velocity
        manager.tryHandleDodge(mob, attacker);
        // verify setVelocity called once with some vector
        verify(mob, times(1)).setVelocity(any(Vector.class));

        // Second call immediately should not set velocity again (cooldown)
        manager.tryHandleDodge(mob, attacker);
        verify(mob, times(1)).setVelocity(any(Vector.class));
    }

    @Test
    public void testTryHandleDodge_disabledInSettings() {
        WorldSettings ws = new WorldSettings();
        ws.setEnhancedMobDodgeEnabled(false);
        ws.setDodgeChancePercent(100);
        ws.setDodgeCooldownTicks(0);

    // No stubbing necessary: settings disabled so method should return immediately

        MobBehaviorManager manager = new MobBehaviorManager(null, ws, true);
        manager.tryHandleDodge(mob, attacker);
        verify(mob, never()).setVelocity(any(Vector.class));
    }
}
