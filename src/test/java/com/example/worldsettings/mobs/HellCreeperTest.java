package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creeper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HellCreeper.
 */
public class HellCreeperTest {

    @Test
    public void testConvert() {
        WorldSettingsPlugin mockPlugin = mock(WorldSettingsPlugin.class);
        try (MockedStatic<WorldSettingsPlugin> mockedStatic = mockStatic(WorldSettingsPlugin.class)) {
            mockedStatic.when(WorldSettingsPlugin::getInstance).thenReturn(mockPlugin);

            Creeper mockCreeper = mock(Creeper.class);
            AttributeInstance mockHp = mock(AttributeInstance.class);

            when(mockCreeper.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHp);
            when(mockHp.getBaseValue()).thenReturn(20.0);

            HellCreeper.convert(mockCreeper);

            verify(mockCreeper).setMetadata(eq(HellCreeper.METADATA_KEY), any());
            verify(mockCreeper).setCustomName(HellCreeper.CUSTOM_NAME);
            verify(mockCreeper).setCustomNameVisible(true);
            verify(mockCreeper).setPowered(true);
            verify(mockCreeper).setAI(true);
            verify(mockCreeper).setRemoveWhenFarAway(false);
            verify(mockHp).getBaseValue();
            verify(mockCreeper).setHealth(anyDouble());
        }
    }

    @Test
    public void testIsHellCreeper() {
        Creeper mockCreeper = mock(Creeper.class);

        when(mockCreeper.hasMetadata(HellCreeper.METADATA_KEY)).thenReturn(true);
        assertTrue(HellCreeper.isHellCreeper(mockCreeper));

        when(mockCreeper.hasMetadata(HellCreeper.METADATA_KEY)).thenReturn(false);
        assertFalse(HellCreeper.isHellCreeper(mockCreeper));
    }
}