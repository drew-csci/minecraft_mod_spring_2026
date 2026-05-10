package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Displays day tracker and Crimson Descent countdown in the actionbar.
 */
public class DayTrackerListener {

    public static void startDayTrackerTask(WorldSettingsPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                WorldSettings ws = plugin.getWorldSettings();
                
                for (World world : plugin.getServer().getWorlds()) {
                    if (world.getEnvironment() != World.Environment.NORMAL) continue;
                    
                    long dayCount = world.getFullTime() / 24000L;
                    
                    for (Player player : world.getPlayers()) {
                        // Calculate days until Crimson Descent
                        // For now, use a simplified estimate based on max gap
                        int maxGapDays = ws.getCrimsonMaxGapDays();
                        
                        String dayInfo = ChatColor.GOLD + "Day " + (dayCount + 1) + ChatColor.WHITE + " | " +
                                        ChatColor.DARK_RED + "Crimson every ~" + maxGapDays + " days";
                        
                        try {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(dayInfo));
                        } catch (Exception e) {
                            // Fallback: send as regular message if action bar fails
                            player.sendMessage(dayInfo);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }
}

