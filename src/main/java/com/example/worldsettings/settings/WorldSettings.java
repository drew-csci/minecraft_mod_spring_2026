package com.example.worldsettings.settings;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Stores all configurable world settings.
 * Each field corresponds to a menu item in the GUI.
 */
public class WorldSettings {

    public static final double MIN_POST_END_DIFFICULTY = 1.0;
    public static final double MAX_POST_END_DIFFICULTY = 3.0;
    public static final int MIN_MAX_HORDE_SIZE = 10;
    public static final int MAX_MAX_HORDE_SIZE = 100;
    public static final int MIN_BLOOD_MOON_SPAWN = 25;
    public static final int MAX_BLOOD_MOON_SPAWN = 500;
    public static final int MIN_BLOOD_MOON_CHANCE = 0;
    public static final int MAX_BLOOD_MOON_CHANCE = 100;

    // ── Left Column Settings ────────────────────────────────────────────
    private boolean postEndWorld = false;
    private double postEndDifficultyBoost = 1.0;   // 1.0x to 3.0x
    private boolean newBoss = false;
    private boolean hordeEvents = false;
    private int maximumHordeSize = 50;              // 10 to 100
    private boolean enhancedMobs = false;

    // ── Right Column Settings ───────────────────────────────────────────
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private boolean bloodMoonEvents = false;
    private int bloodMoonSpawnMultiplier = 100;     // percentage
    private int firstBloodMoonDay = 3;              // 1, 3, or 5
    private int bloodMoonChancePercent = 10;        // % per night
    private boolean enhancedLootDrops = false;

    // ── Enums ───────────────────────────────────────────────────────────
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD;

        public DifficultyLevel next() {
            DifficultyLevel[] vals = values();
            return vals[(ordinal() + 1) % vals.length];
        }

        public String display() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    // ── Getters & Setters ───────────────────────────────────────────────

    public boolean isPostEndWorld()              { return postEndWorld; }
    public void togglePostEndWorld()             { postEndWorld = !postEndWorld; }

    public double getPostEndDifficultyBoost()    { return postEndDifficultyBoost; }
    public void increasePostEndDifficulty()      {
        postEndDifficultyBoost = Math.min(3.0, postEndDifficultyBoost + 0.5);
    }
    public void decreasePostEndDifficulty()      {
        postEndDifficultyBoost = Math.max(1.0, postEndDifficultyBoost - 0.5);
    }

    public boolean isNewBoss()                   { return newBoss; }
    public void toggleNewBoss()                  { newBoss = !newBoss; }

    public boolean isHordeEvents()               { return hordeEvents; }
    public void toggleHordeEvents()              { hordeEvents = !hordeEvents; }

    public int getMaximumHordeSize()             { return maximumHordeSize; }
    public void increaseHordeSize()              { maximumHordeSize = Math.min(100, maximumHordeSize + 10); }
    public void decreaseHordeSize()              { maximumHordeSize = Math.max(10, maximumHordeSize - 10); }

    public boolean isEnhancedMobs()              { return enhancedMobs; }
    public void toggleEnhancedMobs()             { enhancedMobs = !enhancedMobs; }

    public DifficultyLevel getDifficultyLevel()  { return difficultyLevel; }
    public void cycleDifficulty()                { difficultyLevel = difficultyLevel.next(); }

    public boolean isBloodMoonEvents()           { return bloodMoonEvents; }
    public void toggleBloodMoonEvents()          { bloodMoonEvents = !bloodMoonEvents; }

    public int getBloodMoonSpawnMultiplier()     { return bloodMoonSpawnMultiplier; }
    public void increaseBloodMoonSpawn()         { bloodMoonSpawnMultiplier = Math.min(500, bloodMoonSpawnMultiplier + 25); }
    public void decreaseBloodMoonSpawn()         { bloodMoonSpawnMultiplier = Math.max(25, bloodMoonSpawnMultiplier - 25); }

    public int getFirstBloodMoonDay()            { return firstBloodMoonDay; }
    public void cycleFirstBloodMoon()            {
        switch (firstBloodMoonDay) {
            case 1:  firstBloodMoonDay = 3; break;
            case 3:  firstBloodMoonDay = 5; break;
            default: firstBloodMoonDay = 1; break;
        }
    }

    public int getBloodMoonChancePercent()       { return bloodMoonChancePercent; }
    public void increaseBloodMoonChance()        { bloodMoonChancePercent = Math.min(100, bloodMoonChancePercent + 5); }
    public void decreaseBloodMoonChance()        { bloodMoonChancePercent = Math.max(0, bloodMoonChancePercent - 5); }

    public boolean isEnhancedLootDrops()         { return enhancedLootDrops; }
    public void toggleEnhancedLootDrops()        { enhancedLootDrops = !enhancedLootDrops; }

    // ── Config mapping and validation ───────────────────────────────────

    public void loadFromConfig(FileConfiguration config) {
        postEndWorld = config.getBoolean("post_end_world.enabled", false);
        postEndDifficultyBoost = clampDouble(
            config.getDouble("post_end_world.difficulty_boost", 1.0),
            MIN_POST_END_DIFFICULTY,
            MAX_POST_END_DIFFICULTY
        );
        newBoss = config.getBoolean("post_end_world.new_boss", false);
        hordeEvents = config.getBoolean("post_end_world.horde_events", false);
        maximumHordeSize = clampInt(
            config.getInt("post_end_world.maximum_horde_size", 50),
            MIN_MAX_HORDE_SIZE,
            MAX_MAX_HORDE_SIZE
        );
        enhancedMobs = config.getBoolean("post_end_world.enhanced_mobs", false);

        String diffName = config.getString("blood_moon.difficulty_level", DifficultyLevel.EASY.name());
        difficultyLevel = parseDifficulty(diffName, DifficultyLevel.EASY);
        bloodMoonEvents = config.getBoolean("blood_moon.enabled", false);
        bloodMoonSpawnMultiplier = clampInt(
            config.getInt("blood_moon.spawn_multiplier_percent", 100),
            MIN_BLOOD_MOON_SPAWN,
            MAX_BLOOD_MOON_SPAWN
        );

        int configuredFirstDay = config.getInt("blood_moon.first_blood_moon_day", 3);
        firstBloodMoonDay = (configuredFirstDay == 1 || configuredFirstDay == 3 || configuredFirstDay == 5)
            ? configuredFirstDay : 3;

        bloodMoonChancePercent = clampInt(
            config.getInt("blood_moon.chance_percent_per_night", 10),
            MIN_BLOOD_MOON_CHANCE,
            MAX_BLOOD_MOON_CHANCE
        );
        enhancedLootDrops = config.getBoolean("blood_moon.enhanced_loot_drops", false);
    }

    public void writeToConfig(FileConfiguration config) {
        config.set("post_end_world.enabled", postEndWorld);
        config.set("post_end_world.difficulty_boost", postEndDifficultyBoost);
        config.set("post_end_world.new_boss", newBoss);
        config.set("post_end_world.horde_events", hordeEvents);
        config.set("post_end_world.maximum_horde_size", maximumHordeSize);
        config.set("post_end_world.enhanced_mobs", enhancedMobs);

        config.set("blood_moon.difficulty_level", difficultyLevel.name());
        config.set("blood_moon.enabled", bloodMoonEvents);
        config.set("blood_moon.spawn_multiplier_percent", bloodMoonSpawnMultiplier);
        config.set("blood_moon.first_blood_moon_day", firstBloodMoonDay);
        config.set("blood_moon.chance_percent_per_night", bloodMoonChancePercent);
        config.set("blood_moon.enhanced_loot_drops", enhancedLootDrops);
    }

    public void sanitizeRanges() {
        postEndDifficultyBoost = clampDouble(postEndDifficultyBoost, MIN_POST_END_DIFFICULTY, MAX_POST_END_DIFFICULTY);
        maximumHordeSize = clampInt(maximumHordeSize, MIN_MAX_HORDE_SIZE, MAX_MAX_HORDE_SIZE);
        bloodMoonSpawnMultiplier = clampInt(bloodMoonSpawnMultiplier, MIN_BLOOD_MOON_SPAWN, MAX_BLOOD_MOON_SPAWN);
        bloodMoonChancePercent = clampInt(bloodMoonChancePercent, MIN_BLOOD_MOON_CHANCE, MAX_BLOOD_MOON_CHANCE);
        if (firstBloodMoonDay != 1 && firstBloodMoonDay != 3 && firstBloodMoonDay != 5) {
            firstBloodMoonDay = 3;
        }
    }

    private static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double clampDouble(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static DifficultyLevel parseDifficulty(String name, DifficultyLevel fallback) {
        if (name == null || name.isEmpty()) {
            return fallback;
        }
        try {
            return DifficultyLevel.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }
}
