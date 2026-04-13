## Project: WorldSettings Minecraft Plugin

This is a small Spigot/Paper Minecraft plugin that provides a chest-based GUI for viewing and changing in-memory world settings. It's a single-player-friendly administrative GUI: a player with permission can run `/worldsettings` to open a 54-slot chest UI and toggle or change several world-level options stored in memory.

This file documents the project, the role of each file, and how the pieces interact.

## Quick facts

- Language: Java
- Build: Maven (`pom.xml`) — the project was originally targeted to Java 17 in the provided `pom.xml`.
- Plugin descriptor: `src/main/resources/plugin.yml`
- How to build:

```bash
# from repository root (zsh)
mvn clean package
```

The resulting plugin JAR will be in `target/` and can be dropped into a Spigot/Paper server's `plugins/` folder for testing.

## Files and purpose

- `src/main/java/com/example/worldsettings/WorldSettingsPlugin.java`
  - Main plugin class extending `JavaPlugin`.
  - Responsibilities:
    - Holds the singleton plugin instance accessible with `WorldSettingsPlugin.getInstance()`.
    - Constructs the in-memory `WorldSettings` object on enable.
    - Registers the GUI click listener with the server plugin manager.
    - Handles the `/worldsettings` command in `onCommand` by opening the GUI for the player.

- `src/main/java/com/example/worldsettings/settings/WorldSettings.java`
  - Data model for the plugin settings (the various world options).
  - Provides getters and mutator methods such as toggles, increments, or cycles used by the GUI or the click handler.
  - All state is stored in memory (no persistence) in the current codebase.

- `src/main/java/com/example/worldsettings/gui/SettingsGUI.java`
  - Builds and displays a chest inventory UI (54 slots) representing the available settings.
  - Reads current values from `WorldSettings` via the plugin singleton (`WorldSettingsPlugin.getInstance().getWorldSettings()`).
  - Populates item icons, names, and lore to show the current value for each setting.
  - Exposes a public helper like `openMainMenu(Player)` used by the command handler and listeners to open/refresh the GUI.

- `src/main/java/com/example/worldsettings/listeners/GUIClickListener.java`
  - Listens for inventory click events (e.g., `InventoryClickEvent`).
  - Maps clicked slot indices to actions on `WorldSettings` (toggle/delta/cycle operations).
  - Sends feedback (chat messages, sounds) to the player and requests a GUI refresh by calling `SettingsGUI.openMainMenu` so the displayed values update.

- `src/main/resources/plugin.yml`
  - Plugin descriptor used by Bukkit/Spigot when loading the plugin JAR.
  - Declares the plugin `main` class, `api-version`, the `/worldsettings` command, and permissions.

- `pom.xml`
  - Maven build configuration. Controls Java compilation target, dependencies (Spigot API in provided scope), and packaging.
  - The provided `pom.xml` in this workspace targeted Java 17 when inspected; if you want to upgrade the project to Java 21, update the source/target and toolchain configuration here.

- `README.md`
  - Project-level documentation (existing). The file added here (`Readme_yifeng_peng.md`) is an extra per-request copy with a focused description for Yifeng / Peng.

- `.gitignore`
  - Ignores build artifacts, IDE files, and other generated content.

- `target/`
  - Build output created by Maven. Contains compiled classes (e.g., `target/classes/com/example/worldsettings/WorldSettingsPlugin.class`), packaged resources, and the final JAR.

## Runtime flow / how the parts interact

1. Server loads the plugin JAR and initializes the `WorldSettingsPlugin` main class declared in `plugin.yml`.
2. `WorldSettingsPlugin.onEnable()` runs:
   - Sets the static singleton instance.
   - Creates a `WorldSettings` object and stores it on the plugin instance.
   - Registers `GUIClickListener` so GUI events are handled.
3. A player runs `/worldsettings` (the command is declared in `plugin.yml`). `WorldSettingsPlugin.onCommand(...)` verifies the sender is a player and calls `SettingsGUI.openMainMenu(player)`.
4. `SettingsGUI.openMainMenu(player)` builds a 54-slot chest inventory and fills slot items based on values read from `WorldSettingsPlugin.getInstance().getWorldSettings()`.
5. The player interacts with the inventory; when they click, `GUIClickListener` receives the event. The listener:
   - Determines which slot was clicked and which setting that corresponds to.
   - Calls the appropriate mutator on `WorldSettings` (toggle/increment/cycle).
   - Provides feedback (message or sound) to the player.
   - Re-opens or refreshes the GUI by calling `SettingsGUI.openMainMenu(player)` so the UI reflects the updated values.

### Call graph (simplified)

- Player command → `WorldSettingsPlugin.onCommand` → `SettingsGUI.openMainMenu`
- GUI build → `SettingsGUI` reads values from `WorldSettingsPlugin.getWorldSettings()` → `WorldSettings` getters
- Player click → `GUIClickListener.onInventoryClick` → `WorldSettings` mutators → `SettingsGUI.openMainMenu` (refresh)

## Notes and suggested next steps

- Persistence: settings are currently ephemeral. To persist between restarts, add save/load methods in `WorldSettings` and call them from `WorldSettingsPlugin.onDisable()` / `onEnable()` (store in `config.yml` or a custom file). 
- Java upgrade: `pom.xml` currently targets Java 17. To upgrade to Java 21, update `maven-compiler-plugin` configuration (source/target/toolchain), and test against your server build. Also ensure your runtime (server JVM) is Java 21.
- Tests: add small unit tests around the `WorldSettings` logic (toggle and cycle behavior) — those are easy to validate in isolation.

If you'd like, I can:
- Add persistent save/load for `WorldSettings` (simple YAML file),
- Upgrade `pom.xml` to target Java 21 and produce a short upgrade plan, or
- Generate a slot-to-action mapping table extracted directly from `GUIClickListener`.

-- Readme created for: Yifeng / Peng
