# WorldSettingsPlugin Overview

## 1. Project Description
WorldSettingsPlugin is a Minecraft Spigot/Paper plugin that adds an in-game graphical menu for configuring world-related gameplay settings.

Main idea:
- A player with permission runs `/worldsettings`
- The plugin opens a chest-style GUI
- Clicking menu items changes settings (toggles, sliders, and cycles)
- The menu is refreshed immediately so changes are visible right away

Tech stack:
- Java 17
- Maven
- Spigot API (provided by the server at runtime)

---

## 2. Description of Each Main File

### `pom.xml`
Maven build configuration for the project.
- Defines artifact coordinates (`groupId`, `artifactId`, `version`)
- Sets Java compiler level to 17
- Declares Spigot API dependency
- Builds a deployable plugin JAR

### `src/main/resources/plugin.yml`
Plugin metadata and Bukkit registration file.
- Declares plugin name, version, and main class
- Registers the `/worldsettings` command
- Defines the `worldsettings.use` permission
- Tells the server how to load the plugin

### `src/main/java/com/example/worldsettings/WorldSettingsPlugin.java`
Main entry point (extends `JavaPlugin`).
- Called by the server on enable/disable
- Initializes the shared `WorldSettings` object
- Registers the GUI click listener
- Handles `/worldsettings` command and opens the menu for players
- Exposes plugin instance and settings instance

### `src/main/java/com/example/worldsettings/settings/WorldSettings.java`
Core settings model.
- Stores all configurable values (booleans, percentages, numeric ranges, difficulty)
- Contains business logic for changing values (toggle/increase/decrease/cycle)
- Enforces limits (for example min/max boundaries)
- Includes `DifficultyLevel` enum and cycle behavior

### `src/main/java/com/example/worldsettings/gui/SettingsGUI.java`
GUI construction logic.
- Builds a 54-slot chest inventory
- Places decorative fillers and divider items
- Places each setting item into a specific slot
- Reads current values from `WorldSettings` and renders them in item names/lore

### `src/main/java/com/example/worldsettings/listeners/GUIClickListener.java`
Inventory click event handler.
- Listens for clicks inside the plugin GUI
- Cancels item movement to keep GUI read-only
- Detects clicked slot and click type (left/right)
- Applies the corresponding change to `WorldSettings`
- Sends feedback message + button click sound
- Reopens GUI to refresh displayed state

### `README.md`
Human-oriented setup and usage guide.
- Build and installation steps
- Server setup instructions
- In-game usage notes
- Troubleshooting guidance

### `target/` (generated output)
Build artifacts created by Maven.
- Compiled classes
- Build metadata
- Final plugin JAR (`world-settings-plugin-1.0.0.jar`)

Note: `target/` is generated and typically not edited manually.

---

## 3. How the Files Interact

### Runtime Flow
1. The Minecraft server reads `plugin.yml` and loads `WorldSettingsPlugin`.
2. `WorldSettingsPlugin#onEnable()` creates `WorldSettings` and registers `GUIClickListener`.
3. A player executes `/worldsettings`.
4. `WorldSettingsPlugin#onCommand()` calls `SettingsGUI.openMainMenu(player)`.
5. `SettingsGUI` builds and opens the inventory UI using values from `WorldSettings`.
6. Player clicks an item in the GUI.
7. `GUIClickListener` handles the click, updates `WorldSettings`, and sends feedback.
8. `GUIClickListener` reopens the GUI so the player immediately sees updated values.

### Responsibility Split
- `WorldSettingsPlugin`: lifecycle + command entry
- `WorldSettings`: data and setting update rules
- `SettingsGUI`: visual representation of settings
- `GUIClickListener`: user interaction and state mutation
- `plugin.yml`: server-level registration and metadata
- `pom.xml`: build/dependency configuration

---

## Important Note
Current settings are stored in memory only. After server restart/reload, values return to defaults unless persistence (for example YAML/JSON config saving) is added.
