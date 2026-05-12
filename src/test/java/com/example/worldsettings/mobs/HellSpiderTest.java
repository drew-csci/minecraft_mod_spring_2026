package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Spider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HellSpider.
 */
public class HellSpiderTest {

    @Test
    public void testConvert() {
        WorldSettingsPlugin mockPlugin = mock(WorldSettingsPlugin.class);
        try (MockedStatic<WorldSettingsPlugin> mockedStatic = mockStatic(WorldSettingsPlugin.class)) {
            mockedStatic.when(WorldSettingsPlugin::getInstance).thenReturn(mockPlugin);

            Spider mockSpider = mock(Spider.class);
            AttributeInstance mockHp = mock(AttributeInstance.class);
            AttributeInstance mockSpeed = mock(AttributeInstance.class);
            AttributeInstance mockDamage = mock(AttributeInstance.class);

            when(mockSpider.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHp);
            when(mockSpider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).thenReturn(mockSpeed);
            when(mockSpider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).thenReturn(mockDamage);

            HellSpider.convert(mockSpider);

            verify(mockSpider).setMetadata(eq(HellSpider.METADATA_KEY), any());
            verify(mockSpider).setCustomName(HellSpider.CUSTOM_NAME);
            verify(mockSpider).setCustomNameVisible(true);
            verify(mockSpider).setAI(true);
            verify(mockSpider).setRemoveWhenFarAway(false);
            verify(mockHp).setBaseValue(32.0);
            verify(mockSpider).setHealth(32.0);
            verify(mockSpeed).setBaseValue(0.6);
            verify(mockDamage).setBaseValue(6.0);
        }
    }

    @Test
    public void testIsHellSpider() {
        Spider mockSpider = mock(Spider.class);

        when(mockSpider.hasMetadata(HellSpider.METADATA_KEY)).thenReturn(true);
        assertTrue(HellSpider.isHellSpider(mockSpider));

        when(mockSpider.hasMetadata(HellSpider.METADATA_KEY)).thenReturn(false);
        assertFalse(HellSpider.isHellSpider(mockSpider));
    }
}