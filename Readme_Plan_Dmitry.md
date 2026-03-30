# Readme_Plan_Dmitry

## Title
Boss Scaling

## User Story
As a player, I want the bosses to scale after I beat it, so that when I rematch it the difficulty increases.

## Tasks
- After boss defeat, increment boss tier.
- Scale stats per tier.

## Acceptance Criteria
- Boss HP increases per defeat.
- Boss damage increases per defeat.

## Story Points
2

## Step 5 Plan For This Project

This repository currently has GUI setting toggles and no boss combat listeners yet. The Boss Scaling feature will be implemented as a new module and wired into the existing plugin lifecycle.

### Current Project Context
- Main plugin entry point: `src/main/java/com/example/worldsettings/WorldSettingsPlugin.java`
- Existing settings model: `src/main/java/com/example/worldsettings/settings/WorldSettings.java`
- Existing GUI/listener layer: `src/main/java/com/example/worldsettings/gui/SettingsGUI.java` and `src/main/java/com/example/worldsettings/listeners/GUIClickListener.java`
- Plugin config: `src/main/resources/plugin.yml`

### Planned New Files
- `src/main/java/com/example/worldsettings/boss/BossScalingManager.java`
- `src/main/java/com/example/worldsettings/boss/BossScalingStorage.java`
- `src/main/java/com/example/worldsettings/boss/BossDefeatListener.java`
- `src/main/java/com/example/worldsettings/boss/BossSpawnListener.java`

### Planned Updated Files
- `src/main/java/com/example/worldsettings/WorldSettingsPlugin.java` (initialize manager and register listeners)
- `pom.xml` (test dependencies and plugins for JUnit/MockBukkit)

## Implementation Plan

### 1. Build Boss Scaling Core Logic
Create a dedicated manager class to:
- identify a boss key (vanilla type or normalized custom boss name),
- track defeat count/tier per boss key,
- compute HP and damage multipliers from tier,
- apply safe caps to avoid extreme scaling values.

Planned class:
- `BossScalingManager`

Planned scaling baseline:
- Tier starts at 0 for unseen bosses.
- After each defeat, tier increments by 1.
- HP multiplier and damage multiplier increase per tier using linear growth with a max cap.

### 2. Add Persistence Layer
Store boss scaling state in a YAML file so data survives restart.

Planned file path:
- `plugins/WorldSettingsPlugin/boss-scaling.yml`

Planned class:
- `BossScalingStorage`

Saved data includes:
- boss key,
- defeat count,
- current tier.

Project-specific behavior:
- Load data in `onEnable`.
- Save after each tier increment and during plugin shutdown.

### 3. Add Boss Event Listeners
Create listeners for boss lifecycle events.

Planned listeners:
- `BossDefeatListener`: on boss death, increment tier and save.
- `BossSpawnListener`: on boss spawn/rematch, apply scaled HP/damage from current tier.

Boss scope for this assignment:
- Vanilla bosses (Wither, Ender Dragon)
- Custom named bosses (normalized custom name + type key)

### 4. Wire Into Existing Plugin Lifecycle
Update plugin bootstrap to:
- initialize scaling manager/storage on enable,
- register new listeners,
- flush/save state on disable.

Primary integration file:
- `src/main/java/com/example/worldsettings/WorldSettingsPlugin.java`

### 5. Verify Acceptance Criteria
Validation flow:
1. Spawn boss and record base stats.
2. Defeat boss once.
3. Respawn/rematch same boss key.
4. Confirm HP and damage are higher than previous spawn.
5. Restart server and verify tier persistence.

Expected result:
- Boss HP/Damage increases per defeat.

### 6. Add Test Coverage
- Add one unit test for tier and multiplier progression.
- Add one integration test (MockBukkit) for death -> tier increment -> spawn scaling flow.

Planned test files:
- `src/test/java/com/example/worldsettings/boss/BossScalingManagerTest.java`
- `src/test/java/com/example/worldsettings/boss/BossScalingIntegrationTest.java`

## Deliverables from This Plan
- Boss scaling implementation classes
- Persistence file support
- Unit test
- Integration test
- Midterm and test readme files

## Risks and Mitigation
- Boss key collisions for custom names: normalize names and prefix with entity type.
- Over-scaling at high tiers: enforce multiplier caps.
- Duplicate scaling application on same spawn event: make scaling application idempotent.

## Definition of Done
- Code compiles and tests pass.
- Boss HP/damage scale after each defeat.
- Scaling survives restart.
- Required README artifacts exist with Dmitry naming.

## Summary
This Step 5 plan is now aligned to this project and ready to be used in Copilot Agent mode for implementation.
