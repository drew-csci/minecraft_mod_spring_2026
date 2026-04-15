package com.example.worldsettings.settings;

/**
 * Stores all configurable world settings.
 * Each field corresponds to a menu item in the GUI.
 */
public class WorldSettings {

git merge origin/main    // ── Mob Ability Settings (Dodge) ─────────────────────────────
    private boolean enhancedMobDodgeEnabled = true;
    private int dodgeChancePercent = 20; // default 20%
    private int dodgeCooldownTicks = 40; // default 2 seconds (40 ticks)


    public boolean isEnhancedMobDodgeEnabled() { return enhancedMobDodgeEnabled; }
    public void setEnhancedMobDodgeEnabled(boolean enabled) { this.enhancedMobDodgeEnabled = enabled; }
    public int getDodgeChancePercent() { return dodgeChancePercent; }
    public void setDodgeChancePercent(int percent) { this.dodgeChancePercent = Math.max(0, Math.min(100, percent)); }
    public int getDodgeCooldownTicks() { return dodgeCooldownTicks; }
    public void setDodgeCooldownTicks(int ticks) { this.dodgeCooldownTicks = Math.max(0, ticks); }

    // ── Left Column Settings ────────────────────────────────────────────
    /**
     * When true, the "post-end" phase is considered active.
     *
     * Control:
     * - Toggle programmatically with `togglePostEndWorld()` or `setPostEndWorld(boolean)`.
     * - This flag is checked by listeners (e.g. `PostEndListener`) to apply post-end behavior.
     *
     * Note: This value is in-memory only. To persist across restarts, add save/load logic in
     * `WorldSettings` and call it from the plugin's `onEnable()` / `onDisable()`.
     */
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
    
    // ── Crimson Descent Event Settings ──────────────────────────────────
    /**
     * Master toggle for The Crimson Descent event. When false, the manager will skip all checks
     * and the event will not occur.
     *
     * You can change this at runtime via `setCrimsonDescentEnabled(false)` for testing or
     * configuration purposes. To expose in the GUI, add an item in `SettingsGUI` that calls
     * `WorldSettings.setCrimsonDescentEnabled(...)`.
     */
    private boolean crimsonDescentEnabled = true;

    /**
     * Starting nightly chance (percentage) that the Crimson Descent will occur when night starts.
     * If the event does not trigger, the manager increases the chance by +5% each day (configurable
     * in the manager logic) until it triggers or reaches the forced maximum gap.
     */
    private int crimsonBaseChancePercent = 10;   // starting chance (percent)

    /**
     * The event will not trigger before this Minecraft day (world full days). For example, a value
     * of 5 prevents Crimson Descent from occurring during the first 5 days.
     */
    private int crimsonMinStartDay = 5;          // event won't start before this day

    /**
     * The manager forces the event to happen if it hasn't occurred within this many days to guarantee
     * at least one occurrence within the configured gap.
     */
    private int crimsonMaxGapDays = 7;           // ensure at least once in this many days

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
    /**
     * Explicitly set whether the post-end phase is active.
     */
    public void setPostEndWorld(boolean active)  { postEndWorld = active; }

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

    // Crimson Descent accessors
    public boolean isCrimsonDescentEnabled()     { return crimsonDescentEnabled; }
    public void setCrimsonDescentEnabled(boolean v) { crimsonDescentEnabled = v; }

    public int getCrimsonBaseChancePercent()    { return crimsonBaseChancePercent; }
    public void setCrimsonBaseChancePercent(int p) { crimsonBaseChancePercent = Math.max(0, Math.min(100, p)); }

    public int getCrimsonMinStartDay()           { return crimsonMinStartDay; }
    public void setCrimsonMinStartDay(int d)     { crimsonMinStartDay = Math.max(0, d); }

    public int getCrimsonMaxGapDays()            { return crimsonMaxGapDays; }
    public void setCrimsonMaxGapDays(int d)      { crimsonMaxGapDays = Math.max(1, d); }
}
