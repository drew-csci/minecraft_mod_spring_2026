# Midterm README - Dmitry

## Title
Boss Scaling

## User Story
As a player, I want the bosses to scale after I beat it, so that when I rematch it the difficulty increases.

## What This Feature Adds

This midterm task implements a complete boss scaling system where bosses become progressively stronger after each defeat.

### Core Behavior
- **After a boss defeat**, that boss's tier increments.
- **On next spawn/rematch**, the same boss spawns with scaled stats.
- **HP and damage increase per defeat** using a linear multiplier formula.
- **Data persists** across server restarts via YAML file storage.

## Acceptance Criteria Mapping
✅ **Boss HP increases per defeat**: Implemented via `BossSpawnListener` which applies health multiplier (15% per tier).
✅ **Boss damage increases per defeat**: Implemented via `BossSpawnListener` which applies damage multiplier (10% per tier).

## Newly Written Code

### Core Implementation Classes

#### 1. **BossScalingManager** 
(`src/main/java/com/example/worldsettings/boss/BossScalingManager.java`)
- Tracks defeat count per boss key (tier progression).
- Computes health and damage multipliers based on tier.
- Supports vanilla bosses (Wither, Ender Dragon) and custom named bosses.
- **Key methods**:
  - `getBossKey(LivingEntity)` - Identifies boss type or normalized custom name
  - `getTier(String bossKey)` - Returns current tier (0 = no defeats)
  - `incrementDefeatCount(String bossKey)` - Increments tier after defeat
  - `getHealthMultiplier(int tier)` - Returns HP multiplier (1.0 base + 0.15 per tier, max 5x)
  - `getDamageMultiplier(int tier)` - Returns damage multiplier (1.0 base + 0.10 per tier, max 5x)

#### 2. **BossScalingStorage**
(`src/main/java/com/example/worldsettings/boss/BossScalingStorage.java`)
- Persists tier data to `plugins/WorldSettingsPlugin/boss-scaling.yml`.
- **Key methods**:
  - `loadData()` - Reads YAML on plugin startup
  - `saveData(Map<String, Integer>)` - Writes tier data to disk after each defeat
  - `clearData()` - Resets all stored data

#### 3. **BossDefeatListener**
(`src/main/java/com/example/worldsettings/boss/BossDefeatListener.java`)
- Listens for `EntityDeathEvent`.
- On boss death: identifies boss key, increments tier, and persists immediately.
- Logs each defeat to console.

#### 4. **BossSpawnListener**
(`src/main/java/com/example/worldsettings/boss/BossSpawnListener.java`)
- Listens for `EntitySpawnEvent`.
- On boss spawn: applies scaled HP (via `Attribute.GENERIC_MAX_HEALTH`) and damage (via `Attribute.GENERIC_ATTACK_DAMAGE`).
- Skips scaling for tier 0 (first encounter).

#### 5. **WorldSettingsPlugin (Updated)**
- Initializes `BossScalingManager` and `BossScalingStorage` on enable.
- Registers `BossDefeatListener` and `BossSpawnListener`.
- Loads tier data from storage on startup.
- Saves tier data on plugin shutdown.

### Data and Persistence

**Storage file**: `plugins/WorldSettingsPlugin/boss-scaling.yml`

**Storage format**:
```yaml
bosses:
  WITHER: 3
  ENDER_DRAGON: 1
  ZOMBIE_BOSS: 2
```

Each entry is `boss_key: defeat_count`.

## How It Works (Scaling Formula)

1. **Boss Identification**:
   - Vanilla bosses: fixed type key (WITHER, ENDER_DRAGON)
   - Custom bosses: normalized name + entity type (e.g., ZOMBIE_My_Custom_Boss)

2. **Tier Progression**:
   - First defeat → tier 1
   - Each subsequent defeat → tier +1
   - No cap on tier (but multiplier is capped)

3. **Multiplier Calculation**:
   - **Health multiplier** = 1.0 + (tier × 0.15), capped at 5.0x
   - **Damage multiplier** = 1.0 + (tier × 0.10), capped at 5.0x
   - Example: tier 3 boss = 1.45x HP, 1.30x damage

4. **Event Flow**:
   - Boss spawns → `BossSpawnListener` reads tier and applies scaled attributes
   - Boss defeated → `BossDefeatListener` increments tier and saves to file
   - Server shuts down → `WorldSettingsPlugin.onDisable()` flushes final data

## Story Points
2

## Testing

✅ **Unit Tests** (8 tests):
- Tier initialization and increment logic
- Multiplier monotonicity and cap enforcement
- Data export/import for persistence

✅ **Integration Tests** (3 tests):
- End-to-end spawn → defeat → respawn flow with realistic tier progression
- Multiple boss tracking independence
- Data persistence across manager instances

All tests pass with 100% success rate.

## Notes
- Scaling formula is configurable in `BossScalingManager` (constants: `MULTIPLIER_PER_TIER`, `DAMAGE_MULTIPLIER_PER_TIER`, `MULTIPLIER_CAP`)
- Listeners register only if boss feature is active (can be extended to respect world settings toggle)
- Custom boss normalization handles color codes and spaces for robust key generation

