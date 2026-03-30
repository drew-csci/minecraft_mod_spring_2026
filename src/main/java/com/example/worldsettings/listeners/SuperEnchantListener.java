package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.superenchant.SuperEnchantmentManager;
import com.example.worldsettings.superenchant.SuperEnchantmentTableGUI;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SuperEnchantListener implements Listener {

    private static final int REINFORCED_CORE_DROP_CHANCE = 30; // 0.30%
    private static final int VOID_CRYSTAL_DROP_CHANCE = 200;   // 2.00%
    private static final int ARCANE_DUST_DROP_CHANCE = 100;    // 1.00%
    private static final int BONE_SHARD_DROP_CHANCE = 400;     // 4.00%

    private final Random random = new Random();
    private final Map<UUID, List<ItemStack>> soulboundRestore = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (!SuperEnchantmentManager.isSuperEnchantmentTable(item)) {
            return;
        }

        if (!event.getAction().name().startsWith("RIGHT_CLICK")) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();
        SuperEnchantmentTableGUI.open(player);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.8f, 1.2f);
        player.spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5, 0.05);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null || !event.getView().getTitle().equals(SuperEnchantmentTableGUI.MENU_TITLE)) {
            return;
        }

        int rawSlot = event.getRawSlot();
        if (rawSlot != SuperEnchantmentTableGUI.INPUT_SLOT
            && !SuperEnchantmentTableGUI.isOptionSlot(rawSlot)
            && !SuperEnchantmentTableGUI.isCloseSlot(rawSlot)) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (SuperEnchantmentTableGUI.isCloseSlot(rawSlot)) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        if (!SuperEnchantmentTableGUI.isOptionSlot(rawSlot)) {
            return;
        }

        event.setCancelled(true);
        ItemStack inputItem = event.getInventory().getItem(SuperEnchantmentTableGUI.INPUT_SLOT);
        if (inputItem == null || inputItem.getType().name().equals("AIR")) {
            player.sendMessage(ChatColor.RED + "Place an item into the Super Enchantment Table first.");
            return;
        }

        if (!SuperEnchantmentManager.canEnchantItem(inputItem)) {
            player.sendMessage(ChatColor.RED + "That item cannot be enchanted here.");
            return;
        }

        int optionIndex = SuperEnchantmentTableGUI.toOptionIndex(rawSlot);
        if (optionIndex < 0) {
            return;
        }
        SuperEnchantmentManager.EnchantmentOption option = SuperEnchantmentManager.EnchantmentOption.values()[optionIndex];

        if (!SuperEnchantmentManager.playerHasRequiredLapis(player, option.getLapisCost())) {
            player.sendMessage(ChatColor.RED + "You need " + option.getLapisCost() + " lapis lazuli to perform that enchantment.");
            return;
        }

        if (!SuperEnchantmentManager.playerHasRequiredLevels(player, option.getXpCost())) {
            player.sendMessage(ChatColor.RED + "You need at least " + option.getXpCost() + " experience levels to enchant this item.");
            return;
        }

        if (!SuperEnchantmentManager.applyEnchantmentOption(player, inputItem, option)) {
            player.sendMessage(ChatColor.RED + "That enchantment is already present on this item.");
            return;
        }

        SuperEnchantmentManager.removeLapis(player, option.getLapisCost());
        player.setLevel(Math.max(0, player.getLevel() - option.getXpCost()));
        event.getInventory().setItem(SuperEnchantmentTableGUI.INPUT_SLOT, inputItem);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        player.spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
        player.sendMessage(ChatColor.GREEN + option.getDisplayName() + " has been applied.");
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        EntityType type = event.getEntityType();
        int roll = random.nextInt(10000);

        if (type == EntityType.WITHER_SKELETON && roll < REINFORCED_CORE_DROP_CHANCE) {
            event.getDrops().add(SuperEnchantmentManager.createReinforcedEnchantmentCore());
            killer.sendMessage(ChatColor.GOLD + "A rare Reinforced Enchantment Core has dropped!" );
            return;
        }

        if (roll < ARCANE_DUST_DROP_CHANCE) {
            event.getDrops().add(SuperEnchantmentManager.createAncientArcaneDust());
            return;
        }

        if (roll < ARCANE_DUST_DROP_CHANCE + VOID_CRYSTAL_DROP_CHANCE) {
            event.getDrops().add(SuperEnchantmentManager.createVoidCrystal());
            return;
        }

        if (roll < ARCANE_DUST_DROP_CHANCE + VOID_CRYSTAL_DROP_CHANCE + BONE_SHARD_DROP_CHANCE) {
            event.getDrops().add(SuperEnchantmentManager.createEnchantedBoneShard());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> keep = new ArrayList<>();
        Iterator<ItemStack> iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            ItemStack drop = iterator.next();
            if (SuperEnchantmentManager.hasSoulbound(drop)) {
                keep.add(drop.clone());
                iterator.remove();
            }
        }

        if (!keep.isEmpty()) {
            soulboundRestore.put(player.getUniqueId(), keep);
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your soulbound items will return to you after respawn." );
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!soulboundRestore.containsKey(playerId)) {
            return;
        }

        List<ItemStack> restored = soulboundRestore.remove(playerId);
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                for (ItemStack item : restored) {
                    player.getInventory().addItem(item);
                }
                player.sendMessage(ChatColor.GREEN + "Your soulbound items have returned to your inventory." );
            }
        }.runTaskLater(WorldSettingsPlugin.getInstance(), 1L);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    handleTemporalStrike((Player) ((Projectile) event.getDamager()).getShooter(), event);
                }
            }
            return;
        }

        Player attacker = (Player) event.getDamager();
        handleTemporalStrike(attacker, event);
    }

    private void handleTemporalStrike(Player attacker, EntityDamageByEntityEvent event) {
        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (SuperEnchantmentManager.hasTemporalStrike(weapon)) {
            if (event.getEntity().getType() != EntityType.PLAYER) {
                event.getEntity().getWorld().spawnParticle(Particle.SPELL_INSTANT, event.getEntity().getLocation().add(0, 1, 0), 10, 0.2, 0.2, 0.2, 0.05);
            }
            if (event.getEntity() instanceof org.bukkit.entity.LivingEntity livingEntity) {
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1, false, false, false));
            }
        }
    }
}
