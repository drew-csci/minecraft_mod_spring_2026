package com.example.worldsettings.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface MobAbility {
    /**
     * Called when a mob is damaged by a player. Implement ability logic here.
     */
    void onMobDamaged(LivingEntity mob, Player attacker, MobBehaviorManager context);
}
