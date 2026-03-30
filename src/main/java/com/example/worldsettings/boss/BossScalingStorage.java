package com.example.worldsettings.boss;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Handles persistence of boss scaling data to YAML file.
 * Saves/loads defeat count per boss key.
 */
public class BossScalingStorage {

    private final File dataFile;
    private FileConfiguration config;

    public BossScalingStorage(Plugin plugin) {
        File pluginDir = plugin.getDataFolder();
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }
        this.dataFile = new File(pluginDir, "boss-scaling.yml");
    }

    /**
     * Load boss data from YAML file.
     * Returns a map of boss key -> defeat count.
     */
    public Map<String, Integer> loadData() {
        if (!dataFile.exists()) {
            return new HashMap<>();
        }

        try {
            config = YamlConfiguration.loadConfiguration(dataFile);
            Map<String, Integer> data = new HashMap<>();

            if (config.contains("bosses")) {
                for (String key : config.getConfigurationSection("bosses").getKeys(false)) {
                    int count = config.getInt("bosses." + key, 0);
                    data.put(key, count);
                }
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Save boss data to YAML file.
     */
    public void saveData(Map<String, Integer> data) {
        try {
            if (config == null) {
                config = new YamlConfiguration();
            }

            config.set("bosses", null); // Clear previous data
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                config.set("bosses." + entry.getKey(), entry.getValue());
            }

            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear all stored data.
     */
    public void clearData() {
        try {
            if (config == null) {
                config = new YamlConfiguration();
            }
            config.set("bosses", null);
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
