package com.example.worldsettings.listeners;

import com.example.worldsettings.ModdedItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles furnace-related events for the Purifying Furnace.
 * Tracks placed Purifying Furnaces and converts Rotten Flesh to Cooked Flesh when smelted.
 */
public class FurnaceListener implements Listener {

    // Set to track locations of Purifying Furnaces
    private static final Set<Location> purifyingFurnaces = new HashSet<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Check if a furnace is being placed
        if (event.getBlock().getType() != Material.FURNACE) {
            return;
        }

        // Check if the item placed is a Purifying Furnace (custom model data 24)
        ItemStack itemInHand = event.getItemInHand();
        if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasCustomModelData() &&
            itemInHand.getItemMeta().getCustomModelData() == 24) {
            
            // Track this furnace as a Purifying Furnace
            purifyingFurnaces.add(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Remove the furnace from tracking when it's broken
        if (event.getBlock().getType() == Material.FURNACE) {
            purifyingFurnaces.remove(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Block block = event.getBlock();

        // Check if this is a Purifying Furnace
        if (!purifyingFurnaces.contains(block.getLocation())) {
            return;  // Not a Purifying Furnace, use default behavior
        }

        // Check if the input is Rotten Flesh
        if (event.getSource().getType() != Material.ROTTEN_FLESH) {
            return;  // Not rotten flesh, use default behavior
        }

        // Convert Rotten Flesh to Cooked Flesh
        ItemStack cookedFlesh = ModdedItems.createCookedFlesh();
        cookedFlesh.setAmount(event.getResult().getAmount());
        event.setResult(cookedFlesh);
    }
}
