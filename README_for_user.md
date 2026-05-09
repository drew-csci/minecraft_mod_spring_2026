# Advanced Configuration Reference

This document is an advanced-user reference for the plugin settings exposed by the GUI. It is not a runtime configuration file.

## Usage
Open the settings GUI in Minecraft with:

```text
/worldsettings
```

Each option can be toggled or adjusted directly in the menu.

## How to use
1. Open the GUI with `/worldsettings`.
2. Click the item for a setting.
3. For toggles, the item flips between ON and OFF.
4. For numeric controls, left-click increases and right-click decreases the value.
5. Use this document to verify the valid values and performance impact before changing a setting.

## Configuration

1. **Post-End World**
   - Type: toggle
   - Default: `false`
   - Description: Enable or disable the post-End game phase.
   - Notes: When enabled, the plugin applies post-End behavior such as difficulty boost.

2. **Post-End Difficulty Boost**
   - Type: slider
   - Default: `1.0x`
   - Range: `1.0x` to `3.0x`
   - Step: `0.5x`
   - Description: Adjust the damage and power scaling for post-End encounters.

3. **New Boss**
   - Type: toggle
   - Default: `false`
   - Description: Enable or disable the custom boss encounter.

4. **Horde Events**
   - Type: toggle
   - Default: `false`
   - Description: Enable or disable random horde attack events.
   - Performance: Moderate impact when enabled, because additional mobs may spawn.

5. **Maximum Horde Size**
   - Type: slider
   - Default: `50`
   - Range: `10` to `100`
   - Step: `10`
   - Description: Maximum number of mobs allowed in a horde event.
   - Performance: High impact at larger values.

6. **Difficulty Level**
   - Type: cycle selector
   - Default: `Easy`
   - Options: `Easy`, `Medium`, `Hard`
   - Description: Global game difficulty modifier used by the plugin.

7. **Blood Moon Events**
   - Type: toggle
   - Default: `false`
   - Description: Enable or disable the Blood Moon mode.

8. **Blood Moon Spawn Multiplier**
   - Type: slider
   - Default: `100%`
   - Range: `25%` to `500%`
   - Step: `25%`
   - Description: Controls how many extra mobs spawn during a Blood Moon.
   - Performance: High impact at values above `100%`.

9. **1st Blood Moon**
   - Type: cycle selector
   - Default: `Day 3`
   - Options: `Day 1`, `Day 3`, `Day 5`
   - Description: Select which world day the first Blood Moon can occur.

10. **Blood Moon Chance**
    - Type: slider
    - Default: `10%`
    - Range: `0%` to `100%`
    - Step: `5%`
    - Description: Chance for a Blood Moon to occur each night.

## Notes

- This document is a reference only. Settings are changed through `/worldsettings` in-game in this release.
- Settings marked with `Performance:` are the most likely to affect server load.
- This file is the dedicated advanced-user settings reference, not the live runtime config.
