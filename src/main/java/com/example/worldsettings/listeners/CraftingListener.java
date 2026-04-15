package com.example.worldsettings.listeners;

import com.example.worldsettings.ModdedItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handles crafting events for custom items.
 * Prevents crafting Reinforced Bow with enchanted bows.
 */
public class CraftingListener implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();

        // Check if the result is a Reinforced Bow
        if (result.getType() == Material.BOW && result.hasItemMeta() &&
            result.getItemMeta().hasCustomModelData() &&
            result.getItemMeta().getCustomModelData() == 16) {

            // Check if any ingredient is an enchanted bow
            for (ItemStack ingredient : event.getInventory().getMatrix()) {
                if (ingredient != null && ingredient.getType() == Material.BOW && ingredient.hasItemMeta() &&
                    ingredient.getItemMeta().hasEnchants()) {
                    event.setCancelled(true);
                    if (event.getWhoClicked() instanceof Player) {
                        Player player = (Player) event.getWhoClicked();
                        player.sendMessage(ChatColor.RED + "You cannot craft a Reinforced Bow with an enchanted bow. The enchantments will be lost!");
                    }
                    return;
                }
            }
        }

        // Check if the result is a Purifying Furnace (crafted from Furnace + Purifying Powder)
        if (result.getType() == Material.FURNACE && result.hasItemMeta() &&
            result.getItemMeta().hasCustomModelData() &&
            result.getItemMeta().getCustomModelData() == 24) {
            
            // Verify that Purifying Powder is in the recipe
            boolean hasPurifyingPowder = false;
            boolean hasFurnace = false;
            
            for (ItemStack ingredient : event.getInventory().getMatrix()) {
                if (ingredient != null) {
                    if (ingredient.getType() == Material.FURNACE) {
                        hasFurnace = true;
                    }
                    if (ingredient.getType() == Material.GLOWSTONE_DUST && ingredient.hasItemMeta() &&
                        ingredient.getItemMeta().hasCustomModelData() &&
                        ingredient.getItemMeta().getCustomModelData() == 9) {
                        hasPurifyingPowder = true;
                    }
                }
            }
            
            if (hasFurnace && hasPurifyingPowder) {
                // Valid recipe, allow crafting
                return;
            }
        }
    }
}