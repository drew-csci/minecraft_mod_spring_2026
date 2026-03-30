package com.example.worldsettings.integration;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.MockPlugin;
import com.example.worldsettings.listeners.SuperEnchantListener;
import com.example.worldsettings.superenchant.SuperEnchantmentManager;
import com.example.worldsettings.superenchant.SuperEnchantmentTableGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SuperEnchantIntegrationTest {

    private ServerMock server;
    private PlayerMock player;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockPlugin plugin = MockBukkit.createMockPlugin("WorldSettingsPlugin", "1.0");
        server.getPluginManager().registerEvents(new SuperEnchantListener(), plugin);
        player = server.addPlayer();
        player.setLevel(40);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void enchantItemThroughTable_appliesSharpnessVII() {
        SuperEnchantmentTableGUI.open(player);

        player.getInventory().addItem(SuperEnchantmentManager.createSuperEnchantmentTable());
        player.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, 20));
        player.setLevel(40);

        Inventory tableInventory = player.getOpenInventory().getTopInventory();
        tableInventory.setItem(SuperEnchantmentTableGUI.INPUT_SLOT, new ItemStack(Material.DIAMOND_SWORD));

        player.simulateInventoryClick(player.getOpenInventory(), ClickType.LEFT, 12);

        ItemStack result = tableInventory.getItem(SuperEnchantmentTableGUI.INPUT_SLOT);
        assertNotNull(result, "The input slot item should remain after enchantment");
        assertEquals(7, result.getEnchantmentLevel(Enchantment.DAMAGE_ALL), "Sharpness VII should be applied to the sword");

        int totalLapis = player.getInventory().all(Material.LAPIS_LAZULI).values().stream().mapToInt(ItemStack::getAmount).sum();
        assertEquals(4, totalLapis, "16 lapis should be consumed from the player inventory");
        assertEquals(5, player.getLevel(), "35 XP levels should be consumed for the enchantment");
    }
}
