package com.example.worldsettings.listeners;

import com.example.worldsettings.ModdedItems;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Handles player movement for special abilities.
 * Specifically handles Spider Boots wall climbing.
 */
public class PlayerMovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if player is sneaking
        if (!player.isSneaking()) return;

        // Check if player is wearing Spider Boots
        ItemStack boots = player.getInventory().getBoots();
        if (boots == null || !boots.hasItemMeta() || !boots.getItemMeta().hasCustomModelData() ||
            boots.getItemMeta().getCustomModelData() != 17) return;

        // Check if there's a solid block in front of the player
        Block block = player.getLocation().getBlock().getRelative(player.getFacing());
        if (block.getType() == Material.AIR) return;

        // Also check the block above to ensure it's climbable
        Block above = block.getRelative(BlockFace.UP);
        if (above.getType() != Material.AIR) return;

        // Apply climbing velocity
        Vector velocity = player.getVelocity();
        velocity.setY(0.2); // Small upward velocity for climbing
        player.setVelocity(velocity);
    }
}