package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Phantom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HellPhantom.
 */
public class HellPhantomTest {

    @Test
    public void testConvert() {
        WorldSettingsPlugin mockPlugin = mock(WorldSettingsPlugin.class);
        try (MockedStatic<WorldSettingsPlugin> mockedStatic = mockStatic(WorldSettingsPlugin.class)) {
            mockedStatic.when(WorldSettingsPlugin::getInstance).thenReturn(mockPlugin);

            Phantom mockPhantom = mock(Phantom.class);
            AttributeInstance mockHp = mock(AttributeInstance.class);
            AttributeInstance mockSpeed = mock(AttributeInstance.class);
            AttributeInstance mockReach = mock(AttributeInstance.class);

            when(mockPhantom.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHp);
            when(mockPhantom.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).thenReturn(mockSpeed);
            when(mockPhantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).thenReturn(mockReach);

            HellPhantom.convert(mockPhantom);

            verify(mockPhantom).setMetadata(eq(HellPhantom.METADATA_KEY), any());
            verify(mockPhantom).setCustomName(HellPhantom.CUSTOM_NAME);
            verify(mockPhantom).setCustomNameVisible(true);
            verify(mockPhantom).setAI(true);
            verify(mockPhantom).setRemoveWhenFarAway(false);
            verify(mockHp).setBaseValue(30.0);
            verify(mockPhantom).setHealth(30.0);
            verify(mockSpeed).setBaseValue(0.55);
            verify(mockReach).setBaseValue(50.0);
        }
    }

    @Test
    public void testIsHellPhantom() {
        Phantom mockPhantom = mock(Phantom.class);

        when(mockPhantom.hasMetadata(HellPhantom.METADATA_KEY)).thenReturn(true);
        assertTrue(HellPhantom.isHellPhantom(mockPhantom));

        when(mockPhantom.hasMetadata(HellPhantom.METADATA_KEY)).thenReturn(false);
        assertFalse(HellPhantom.isHellPhantom(mockPhantom));
    }
}