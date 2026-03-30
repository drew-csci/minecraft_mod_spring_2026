package com.example.worldsettings.superenchant;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SuperEnchantmentManagerTest {

    @Test
    void canEnchantItem_acceptsWeaponsAndArmorAndRejectsNonEnchantables() {
        assertTrue(SuperEnchantmentManager.canEnchantItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_SWORD)));
        assertTrue(SuperEnchantmentManager.canEnchantItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_HELMET)));
        assertTrue(SuperEnchantmentManager.canEnchantItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_PICKAXE)));
        assertFalse(SuperEnchantmentManager.canEnchantItem(new org.bukkit.inventory.ItemStack(Material.DIRT)));
        assertFalse(SuperEnchantmentManager.canEnchantItem(new org.bukkit.inventory.ItemStack(Material.TORCH)));
    }
}
