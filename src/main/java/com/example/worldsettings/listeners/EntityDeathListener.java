package com.example.worldsettings.listeners;

import com.example.worldsettings.ModdedItems;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Handles custom item drops from entity deaths.
 * Specifically handles Zombie Arm drops from zombie variants, Spider Legs from spiders, and Blood Shards from undead.
 */
public class EntityDeathListener implements Listener {

    // List of zombie entity types that can drop the Zombie Arm
    private static final List<EntityType> ZOMBIE_TYPES = Arrays.asList(
        EntityType.ZOMBIE,
        EntityType.ZOMBIE_VILLAGER,
        EntityType.HUSK,
        EntityType.DROWNED,
        EntityType.ZOMBIFIED_PIGLIN
    );

    // List of spider entity types that can drop Spider Legs
    private static final List<EntityType> SPIDER_TYPES = Arrays.asList(
        EntityType.SPIDER,
        EntityType.CAVE_SPIDER
    );

    // List of undead entity types that can drop Blood Shards
    private static final List<EntityType> UNDEAD_TYPES = Arrays.asList(
        EntityType.ZOMBIE,
        EntityType.SKELETON,
        EntityType.WITHER_SKELETON,
        EntityType.ZOMBIE_VILLAGER,
        EntityType.HUSK,
        EntityType.DROWNED,
        EntityType.ZOMBIFIED_PIGLIN,
        EntityType.WITHER,
        EntityType.PHANTOM,
        EntityType.SKELETON_HORSE,
        EntityType.ZOMBIE_HORSE,
        EntityType.STRAY
    );

    // List of endermen that can drop Enchanted Pearls
    private static final List<EntityType> ENDERMAN_TYPES = Arrays.asList(
        EntityType.ENDERMAN
    );

    // List of skeleton entity types that can drop Enriched Bone Meal
    private static final List<EntityType> SKELETON_TYPES = Arrays.asList(
        EntityType.SKELETON,
        EntityType.STRAY,
        EntityType.WITHER_SKELETON
    );

    // List of witch entity types that can drop Purifying Powder
    private static final List<EntityType> WITCH_TYPES = Arrays.asList(
        EntityType.WITCH
    );


    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityType entityType = event.getEntityType();

        // Check if the dead entity is a zombie variant
        if (ZOMBIE_TYPES.contains(entityType)) {
            double dropChance = ModdedItems.getDropChance("Zombie Arm") / 100.0; // Convert percentage to decimal

            // Generate random number and check if item should drop
            if (Math.random() < dropChance) {
                ItemStack zombieArm = ModdedItems.createZombieArm();
                event.getDrops().add(zombieArm);
            }
        }

        // Check if the dead entity is a spider variant
        if (SPIDER_TYPES.contains(entityType)) {
            double dropChance = ModdedItems.getDropChance("Spider Legs") / 100.0; // Convert percentage to decimal

            // Generate random number and check if item should drop
            if (Math.random() < dropChance) {
                ItemStack spiderLegs = ModdedItems.createSpiderLegs();
                event.getDrops().add(spiderLegs);
            }
        }

        // Check if the dead entity is undead
        if (UNDEAD_TYPES.contains(entityType)) {
            double dropChance = ModdedItems.getDropChance("Blood Shard") / 100.0; // Convert percentage to decimal

            // Generate random number and check if item should drop
            if (Math.random() < dropChance) {
                ItemStack bloodShard = ModdedItems.createBloodShard();
                event.getDrops().add(bloodShard);
            }
        }

        // Check if the dead entity is an enderman
        if (ENDERMAN_TYPES.contains(entityType)) {
            double dropChance = ModdedItems.getDropChance("Enchanted Pearl") / 100.0; // Convert percentage to decimal

            // Generate random number and check if item should drop
            if (Math.random() < dropChance) {
                ItemStack enchantedPearl = ModdedItems.createEnchantedPearl();
                event.getDrops().add(enchantedPearl);
            }
        }

        // Check if the dead entity is a skeleton variant
        if (SKELETON_TYPES.contains(entityType)) {
            double dropChance = ModdedItems.getDropChance("Enriched Bone Meal") / 100.0; // Convert percentage to decimal

            // Generate random number and check if item should drop
            if (Math.random() < dropChance) {
                ItemStack enrichedBoneMeal = ModdedItems.createEnrichedBoneMeal();
                event.getDrops().add(enrichedBoneMeal);
            }
        }

        // Check if the dead entity is a witch
        if (WITCH_TYPES.contains(entityType)) {
            double dropChance = ModdedItems.getDropChance("Purifying Powder") / 100.0; // Convert percentage to decimal

            // Generate random number and check if item should drop
            if (Math.random() < dropChance) {
                ItemStack purifyingPowder = ModdedItems.createPurifyingPowder();
                event.getDrops().add(purifyingPowder);
            }
        }

        // Check if the dead entity is a creeper (charged for Explosive Charge)
        if (entityType == EntityType.CREEPER && event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();

            // Thermal Gunpowder from normal creeper, optional
            if (!creeper.isPowered()) {
                double dropChance = ModdedItems.getDropChance("Thermal Gunpowder") / 100.0;
                if (Math.random() < dropChance) {
                    int amount = (int) (Math.random() * 3) + 1;
                    ItemStack thermalGunpowder = ModdedItems.createThermalGunpowder();
                    thermalGunpowder.setAmount(amount);
                    event.getDrops().add(thermalGunpowder);
                }
            }

            // Explosive Charge from charged creeper (100% one)
            if (creeper.isPowered()) {
                ItemStack explosiveCharge = ModdedItems.createExplosiveCharge();
                explosiveCharge.setAmount(1);
                event.getDrops().add(explosiveCharge);
            }
        }
    }
}