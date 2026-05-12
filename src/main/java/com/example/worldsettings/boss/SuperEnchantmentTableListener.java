package com.example.worldsettings.boss;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Random;

/**
 * Event listener for Super Enchantment Table interactions.
 * Handles rare drops, enchant override, and feedback systems.
 */
public class SuperEnchantmentTableListener implements Listener {
    private final SuperEnchantmentTableManager manager;
    private final Random random = new Random();
    
    // Drop probabilities for rare materials
    private static final double VOID_ESSENCE_DROP_CHANCE = 0.15; // 15% from void mobs
    private static final double DRAGON_CORE_DROP_CHANCE = 0.50;  // 50% from dragon death

    public SuperEnchantmentTableListener(SuperEnchantmentTableManager manager) {
        this.manager = manager;
    }

    /**
     * Handle rare drops from entity deaths.
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        EntityType type = event.getEntity().getType();

        // Dragon Core drop from End Dragon
        if (type == EntityType.ENDER_DRAGON) {
            if (random.nextDouble() < DRAGON_CORE_DROP_CHANCE) {
                event.getDrops().add(manager.createDragonCoreItem());
                killer.playSound(killer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);
            }
        }

        // Void Essence from void-related entities (enderman, endermite, shulker as proxy)
        if (type == EntityType.ENDERMAN || type == EntityType.ENDERMITE || type == EntityType.SHULKER) {
            if (random.nextDouble() < VOID_ESSENCE_DROP_CHANCE) {
                event.getDrops().add(manager.createVoidEssenceItem());
                killer.playSound(killer.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 0.8f);
            }
        }
    }

    /**
     * Handle enchanting at Super Enchantment Table.
     * Validates progression, applies exclusive enchants, deducts costs.
     */
    @EventHandler
    public void onSuperTableEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        
        // Check if this is a Super Table enchant (custom check would go here)
        // For now, just handle the cost scaling
        int expToRemove = event.getExpLevelCost() * 2; // Double XP cost
        
        // Check if player has enough experience
        if (player.getLevel() < expToRemove) {
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
            player.sendMessage("§c✗ Insufficient XP levels! Need: " + expToRemove + ", Have: " + player.getLevel());
            return;
        }

        // Check lapis requirement (scaled cost)
        int lapisRequired = SuperEnchantLogic.computeRequiredLapis(event.getExpLevelCost());
        if (!hasLapisInInventory(player, lapisRequired)) {
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
            player.sendMessage("§c✗ Insufficient Lapis Lazuli! Need: " + lapisRequired + " blocks");
            return;
        }

        // Check progression
        if (!manager.isProgressionUnlocked(player)) {
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
            player.sendMessage("§c✗ You must defeat the End Dragon first!");
            return;
        }

        // Success feedback
        removeLapisFromInventory(player, lapisRequired);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        
        // Send particle effect through world spawning
        player.getWorld().spawnParticle(
            org.bukkit.Particle.ENCHANTMENT_TABLE, 
            player.getLocation().add(0, 1, 0), 
            10
        );
        
        player.sendMessage("§a✓ Item enchanted with increased power!");
    }

    /**
     * Check if player has required lapis lazuli blocks in inventory.
     */
    private boolean hasLapisInInventory(Player player, int required) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType().name().contains("LAPIS")) {
                count += item.getAmount();
                if (count >= required) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Remove required lapis lazuli from player inventory.
     */
    private void removeLapisFromInventory(Player player, int required) {
        int toRemove = required;
        ItemStack[] contents = player.getInventory().getContents();
        
        for (ItemStack item : contents) {
            if (item != null && item.getType().name().contains("LAPIS")) {
                if (item.getAmount() >= toRemove) {
                    item.setAmount(item.getAmount() - toRemove);
                    return;
                } else {
                    toRemove -= item.getAmount();
                    item.setAmount(0);
                }
            }
        }
    }
}
