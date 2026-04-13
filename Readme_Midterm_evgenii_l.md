# Midterm Work Summary

## What we changed

- `README_for_user.md`
  - Added an advanced-user settings reference.
  - Documented all 10 current GUI settings.
  - Included valid values, defaults, and performance notes.
  - Added a new “How to use” section.
  - Clarified that this file is documentation only, not a runtime config.

- `src/main/java/com/example/worldsettings/gui/SettingsGUI.java`
  - Added performance-impact lore for:
    - `Horde Events`
    - `Maximum Horde Size`
    - `Blood Moon Spawn Multiplier`
  - Added a helper overload for `createToggleItem(...)` so toggle items can include extra descriptive lore.

- `Readme_Plan_evgenii_l.md`
  - Recorded the planning session:
    - user story
    - tasks
    - acceptance criteria
    - scope
    - next steps

## What the code does

- `README_for_user.md`
  - Serves as the advanced-user configuration reference.
  - Describes how to use `/worldsettings` and how each setting behaves.
  - Explains which settings are performance-sensitive.

- `SettingsGUI.java`
  - Builds the in-game chest GUI menu.
  - Shows current values, valid ranges, and usage instructions.
  - Highlights heavier settings with performance warnings.

- `Readme_Plan_evgenii_l.md`
  - Stores the planning record, not the implementation.

## Verification

- There is no actual runtime `config.yml` in this project yet.
- The only existing config file is `plugin.yml` for Spigot plugin metadata.
- The project still builds successfully with:
  - `mvn clean package`

## Notes

- We kept the config-file feature separate for Kanban.
- This work is focused on documentation + GUI clarity only.