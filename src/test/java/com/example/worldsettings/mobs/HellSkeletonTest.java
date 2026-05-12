package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Skeleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HellSkeleton.
 */
public class HellSkeletonTest {

    @Test
    public void testConvert() {
        WorldSettingsPlugin mockPlugin = mock(WorldSettingsPlugin.class);
        try (MockedStatic<WorldSettingsPlugin> mockedStatic = mockStatic(WorldSettingsPlugin.class)) {
            mockedStatic.when(WorldSettingsPlugin::getInstance).thenReturn(mockPlugin);

            Skeleton mockSkeleton = mock(Skeleton.class);
            AttributeInstance mockHp = mock(AttributeInstance.class);
            AttributeInstance mockSpeed = mock(AttributeInstance.class);
            AttributeInstance mockDamage = mock(AttributeInstance.class);

            when(mockSkeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHp);
            when(mockSkeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).thenReturn(mockSpeed);
            when(mockSkeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).thenReturn(mockDamage);

            HellSkeleton.convert(mockSkeleton);

            verify(mockSkeleton).setMetadata(eq(HellSkeleton.METADATA_KEY), any());
            verify(mockSkeleton).setCustomName(HellSkeleton.CUSTOM_NAME);
            verify(mockSkeleton).setCustomNameVisible(true);
            verify(mockSkeleton).setAI(true);
            verify(mockSkeleton).setRemoveWhenFarAway(false);
            verify(mockHp).setBaseValue(30.0);
            verify(mockSkeleton).setHealth(30.0);
            verify(mockSpeed).setBaseValue(0.35);
            verify(mockDamage).setBaseValue(5.0);
        }
    }

    @Test
    public void testIsHellSkeleton() {
        Skeleton mockSkeleton = mock(Skeleton.class);

        when(mockSkeleton.hasMetadata(HellSkeleton.METADATA_KEY)).thenReturn(true);
        assertTrue(HellSkeleton.isHellSkeleton(mockSkeleton));

        when(mockSkeleton.hasMetadata(HellSkeleton.METADATA_KEY)).thenReturn(false);
        assertFalse(HellSkeleton.isHellSkeleton(mockSkeleton));
    }
}