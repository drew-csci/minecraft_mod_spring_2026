package com.example.worldsettings.mobs;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HellZombieSpawner {

    private final JavaPlugin plugin;

    public HellZombieSpawner(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawnHellZombie(Location loc) {
        Zombie zombie = loc.getWorld().spawn(loc, Zombie.class);
        zombie.setCustomName("§cHell Zombie");
        zombie.setCustomNameVisible(true);

        // Fireproof
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        zombie.setFireTicks(0);

        // Health / Strength
        zombie.setMaxHealth(25);
        zombie.setHealth(25);

        // Glowing + potion effects
        zombie.setGlowing(true);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));

        // Equip blood-red armor
        equipFieryArmor(zombie);

        // Scary particle trail
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!zombie.isDead()) {
                    loc.getWorld().spawnParticle(Particle.FLAME, zombie.getLocation().add(0, 1, 0), 5, 0.2, 0.5, 0.2, 0.05);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, zombie.getLocation().add(0, 1.5, 0), 2, 0.2, 0.5, 0.2, 0.02);
                    loc.getWorld().playSound(zombie.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1f);
                } else cancel();
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    private void equipFieryArmor(Zombie zombie) {
        ItemStack helmet = new ItemStack(org.bukkit.Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(Color.RED);
        helmet.setItemMeta(helmetMeta);

        ItemStack chest = new ItemStack(org.bukkit.Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        chestMeta.setColor(Color.RED);
        chest.setItemMeta(chestMeta);

        ItemStack legs = new ItemStack(org.bukkit.Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) legs.getItemMeta();
        legsMeta.setColor(Color.RED);
        legs.setItemMeta(legsMeta);

        ItemStack boots = new ItemStack(org.bukkit.Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.RED);
        boots.setItemMeta(bootsMeta);

        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chest);
        zombie.getEquipment().setLeggings(legs);
        zombie.getEquipment().setBoots(boots);
    }
}