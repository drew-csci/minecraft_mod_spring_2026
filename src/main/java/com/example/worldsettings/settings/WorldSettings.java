package com.example.worldsettings.settings;

/**
 * Stores all configurable world settings.
 * Each field corresponds to a menu item in the GUI.
 */
public class WorldSettings {

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
}
