package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles player interactions with special items.
 * Specifically handles Blood Orb usage, Enchanted Pearl teleportation, and Copper Staff throwing.
 */
public class PlayerInteractListener implements Listener {

    // Set to track players who used Enchanted Pearl
    private static final Set<Player> enchantedPearlUsers = new HashSet<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        // Handle Enriched Bone Meal usage
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && item != null &&
            item.getType() == Material.BONE_MEAL && item.hasItemMeta() &&
            item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 6) {

            if (block != null && block.getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getBlockData();
                int currentAge = ageable.getAge();
                int maxAge = ageable.getMaximumAge();

                if (currentAge < maxAge) {
                    // Grow the crop by 3 stages or to max, whichever is less
                    int newAge = Math.min(currentAge + 3, maxAge);
                    ageable.setAge(newAge);
                    block.setBlockData(ageable);

                    // Consume the bone meal
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().setItemInMainHand(null);
                    }

                    event.setCancelled(true); // Prevent default bone meal behavior
                    return;
                }
            }
        }

        // Handle Copper Staff throw
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
            item != null && item.getType() == Material.DIAMOND_AXE && item.hasItemMeta() &&
            item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 22) {

            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setVelocity(player.getEyeLocation().getDirection().multiply(1.5));
            snowball.setMetadata("CopperStaff", new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
            event.setCancelled(true);
            return;
        }

        // Handle Blood Orb usage
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
            item != null && item.getType() == Material.ENDER_PEARL && item.hasItemMeta() &&
            item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 18) {

            // Check if it's night time
            long time = player.getWorld().getTime();
            if (time >= 13000 && time <= 23000) { // Night time in Minecraft
                // TODO: Implement Blood Moon event
                player.sendMessage(ChatColor.DARK_RED + "Blood Moon event started! (Placeholder - event implementation pending)");
                // Consume the orb
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            } else {
                player.sendMessage(ChatColor.RED + "The Blood Orb can only be used at night!");
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof EnderPearl) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                ItemStack item = player.getInventory().getItemInMainHand();

                // Check if the thrown item is Enchanted Pearl
                if (item != null && item.getType() == Material.ENDER_PEARL && item.hasItemMeta() &&
                    item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 5) {
                    enchantedPearlUsers.add(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = event.getPlayer();
            if (enchantedPearlUsers.contains(player)) {
                // Prevent fall damage by resetting fall distance
                player.setFallDistance(0);
                enchantedPearlUsers.remove(player);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball && event.getEntity().hasMetadata("CopperStaff")) {
            Location loc = event.getEntity().getLocation();
            event.getEntity().getWorld().strikeLightningEffect(loc);

            for (org.bukkit.entity.Entity entity : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)) {
                if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    double distance = entity.getLocation().distance(loc);
                    double damage = Math.max(0.5, 8.0 - distance * 2.5);
                    ((LivingEntity) entity).damage(damage);
                }
            }
        }
    }
}