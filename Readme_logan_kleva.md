# WorldSettingsPlugin - Project Documentation

**Author:** Logan Kleva  
**Date:** March 22, 2026  
**Language:** Java 17  
**Target Platform:** Minecraft Spigot/Paper 1.20+

---

## Project Description

**WorldSettingsPlugin** is a Minecraft server plugin that provides an in-game graphical user interface (GUI) for customizing world difficulty and event settings. Players with the appropriate permission (`worldsettings.use`, restricted to operators by default) can execute the `/worldsettings` command to open a chest-based inventory menu containing 10 configurable options arranged in a two-column layout.

The plugin enables server administrators and authorized players to dynamically adjust gameplay modifiers without restarting the server, including:
- Post-End World difficulty scaling
- Boss encounters and enemy horde events
- Blood Moon event mechanics
- Enhanced mob and loot drop settings

---

## Project Structure

```
minecraft_mod_spring_2026/
├── pom.xml                              # Maven build configuration
├── README.md                            # General setup and usage guide
├── Readme_logan_kleva.md               # This file - detailed documentation
├── .gitignore                          # Git ignore rules
├── src/
│   ├── main/
│   │   ├── java/com/example/worldsettings/
│   │   │   ├── WorldSettingsPlugin.java        # Main plugin entry point
│   │   │   ├── gui/
│   │   │   │   └── SettingsGUI.java           # GUI builder and item creation
│   │   │   ├── listeners/
│   │   │   │   └── GUIClickListener.java      # Event handler for inventory clicks
│   │   │   └── settings/
│   │   │       └── WorldSettings.java         # Data model for all settings
│   │   └── resources/
│   │       └── plugin.yml               # Minecraft plugin manifest
│   └── test/                            # (Empty - no unit tests)
└── target/                              # Maven build output (compiled .class files)
```

---

## File Descriptions

### 1. Configuration & Build Files

#### **pom.xml**
- **Purpose:** Maven project configuration file
- **Key Details:**
  - Project name: `world-settings-plugin` (version 1.0.0)
  - Compiler target: Java 17+ with UTF-8 encoding
  - Depends on Spigot API 1.20.4 (Minecraft server framework)
  - Builds a JAR file for deployment on Minecraft servers
- **Dependencies:** Spigot API (marked as `provided` — server supplies it)

#### **plugin.yml**
- **Purpose:** Minecraft plugin descriptor file (required by all Spigot plugins)
- **Key Details:**
  - Plugin name: `WorldSettingsPlugin`
  - Version: `1.0.0`
  - Main entry class: `com.example.worldsettings.WorldSettingsPlugin`
  - API version: `1.20`
  - Registers command `/worldsettings` with description and usage
  - Defines permission `worldsettings.use` with default: `op` (operators only)

---

### 2. Java Source Files

#### **WorldSettingsPlugin.java** (Main Entry Point)
**Location:** `src/main/java/com/example/worldsettings/`

**Purpose:**  
Acts as the main plugin class and coordinates all other components. This is the entry point that Minecraft/Spigot calls when the plugin loads.

**Key Methods:**
- **`onEnable()`** — Executed when the server starts or plugin is loaded
  - Instantiates `WorldSettings` singleton
  - Registers `GUIClickListener` event handler
  - Logs startup confirmation

- **`onDisable()`** — Executed when server stops or plugin is unloaded
  - Logs shutdown message

- **`onCommand()`** — Handles the `/worldsettings` command
  - Verifies sender is a player (not console)
  - Checks permission (`worldsettings.use`)
  - Calls `SettingsGUI.openMainMenu()` to display the GUI

- **`getInstance()`** — Static method for singleton access
  - Allows other classes to retrieve the plugin instance

- **`getWorldSettings()`** — Getter for the settings data model
  - Returns the persistent `WorldSettings` instance

**Dependencies:** Imports `WorldSettings`, `SettingsGUI`, `GUIClickListener`

---

#### **WorldSettings.java** (Data Model)
**Location:** `src/main/java/com/example/worldsettings/settings/`

**Purpose:**  
Stores and manages the 10 configurable world settings. This is the "Model" in the MVC pattern, containing all data and business logic for setting values.

**Left Column Settings (5 settings):**
1. **postEndWorld** (boolean)
   - Toggle: ON/OFF
   - Effect: Enables/disables post-End world mechanics

2. **postEndDifficultyBoost** (double: 1.0 to 3.0)
   - Slider: Increments of ±0.5
   - Effect: Multiplies difficulty after defeating Ender Dragon

3. **newBoss** (boolean)
   - Toggle: ON/OFF
   - Effect: Enables/disables custom boss encounters

4. **hordeEvents** (boolean)
   - Toggle: ON/OFF
   - Effect: Enables/disables enemy horde attacks

5. **maximumHordeSize** (int: 10 to 100)
   - Slider: Increments of ±10
   - Effect: Sets the maximum number of mobs in a horde event

**Right Column Settings (5 settings):**
1. **difficultyLevel** (enum: EASY, MEDIUM, HARD)
   - Cycle: Rotates through difficulty levels
   - Effect: Base game difficulty scaling

2. **bloodMoonEvents** (boolean)
   - Toggle: ON/OFF
   - Effect: Enables/disables blood moon events

3. **bloodMoonSpawnMultiplier** (int: 25% to 500%)
   - Slider: Increments of ±25%
   - Effect: Multiplies mob spawn rate during blood moons

4. **firstBloodMoonDay** (int: 1, 3, or 5)
   - Cycle: 1 → 3 → 5 → 1
   - Effect: Sets which day the first blood moon occurs

5. **bloodMoonChancePercent** (int: 0% to 100%)
   - Slider: Increments of ±5%
   - Effect: Probability of blood moon occurring each night

**Key Methods:**
- Getters for each field (e.g., `isPostEndWorld()`, `getPostEndDifficultyBoost()`)
- Toggle methods for boolean values (e.g., `togglePostEndWorld()`)
- Increment/decrement methods for numeric sliders (e.g., `increaseHordeSize()`, `decreaseHordeSize()`)
- Cycle methods for enumerated values (e.g., `cycleDifficulty()`)
- All methods include bounds checking to prevent invalid values

**Dependencies:** None (pure data class)

---

#### **SettingsGUI.java** (View/GUI Builder)
**Location:** `src/main/java/com/example/worldsettings/gui/`

**Purpose:**  
Constructs and renders the visual chest-based GUI menu. This is the "View" in the MVC pattern, responsible for displaying the data to the player.

**GUI Layout (54-slot double chest = 6 rows × 9 columns):**
```
Row 0 [title bar]:        9 gray glass panes + 1 gold "World Settings" item
Row 1-5 [settings]:       [Left Item 1] [Black Divider] [Right Item 1]
                          [Left Item 2] [Black Divider] [Right Item 2]
                          [Left Item 3] [Black Divider] [Right Item 3]
                          [Left Item 4] [Black Divider] [Right Item 4]
                          [Left Item 5] [Black Divider] [Right Item 5]
```

**Slot Positions (Constants):**
| Setting | Slot |
|---------|------|
| Post-End World | 10 |
| Post-End Difficulty | 19 |
| New Boss | 28 |
| Horde Events | 37 |
| Max Horde Size | 46 |
| Difficulty Level | 14 |
| Blood Moon Events | 23 |
| Blood Moon Spawn | 32 |
| First Blood Moon | 41 |
| Blood Moon Chance | 50 |

**Key Methods:**
- **`openMainMenu(Player)`** — Main method that builds and displays the GUI
  - Creates a 54-slot inventory
  - Fills background with gray glass panes
  - Adds title item (NETHER_STAR)
  - Adds black dividers between columns
  - Creates 10 item stacks representing each setting:
    - **Toggle items:** Display ON/OFF state with different materials
      - ON: e.g., END_STONE (white appearance)
      - OFF: e.g., STONE (dark appearance)
    - **Slider items:** Show current value and range with adjustment instructions
      - Displays: "Current: X.Xx / Range: 1.0x - 3.0x"
      - Instructions: "Left-click: +0.5 | Right-click: -0.5"
    - **Cycle items:** Display current selection with options
      - Example: "Current: MEDIUM | Click to cycle"

**Helper Methods:**
- `createToggleItem()` — Creates ON/OFF display items with appropriate materials
- `createItem()` — Generic method to create items with name and lore descriptions
- `addLore()` — Adds multi-line tooltips to items

**Dependencies:** Imports `WorldSettingsPlugin`, `WorldSettings`

---

#### **GUIClickListener.java** (Controller/Event Handler)
**Location:** `src/main/java/com/example/worldsettings/listeners/`

**Purpose:**  
Handles player interactions with the GUI menu. This is the "Controller" in the MVC pattern, detecting clicks and updating the model accordingly.

**Key Method:**
- **`onInventoryClick(InventoryClickEvent)`** — Responds to inventory clicks
  - Validates the clicked inventory is the settings GUI
  - Cancels the click event (prevents item removal)
  - Determines click type: LEFT (increase) or RIGHT (decrease)
  - Routes to appropriate setting based on slot number using switch statement:
    - **Toggle settings:** Calls `toggle*()` method
    - **Slider settings:** Calls `increase*()` or `decrease*()` based on click type
    - **Cycle settings:** Calls `cycle*()` method
  - Sends player feedback via chat message
  - **Calls `SettingsGUI.openMainMenu()` to refresh the GUI display**

**Feedback Methods:**
- **`notify(Player, String, boolean)`** — Displays toggle status
  - Example: "✓ Post-End World is now ON" (green) or "✗ New Boss is now OFF" (red)
  
- **`notifyValue(Player, String, String)`** — Displays numeric/cycle values
  - Example: "⛏ Post-End Difficulty: 2.0x" (yellow)

**Sound Effects:**
- Plays `Sound.BLOCK_NOTE_BLOCK_PLING` on successful click

**Dependencies:** Imports `WorldSettingsPlugin`, `SettingsGUI`, `WorldSettings`

---

## File Interactions & Data Flow

### Startup Sequence
```
1. Minecraft server loads plugins
2. WorldSettingsPlugin.onEnable() called
   ├─ Creates WorldSettings instance (empty data)
   └─ Registers GUIClickListener with event system
3. Plugin is ready
```

### Command Execution Flow
```
Player types: /worldsettings
        ↓
Minecraft calls WorldSettingsPlugin.onCommand()
        ↓
WorldSettingsPlugin verifies permission & sender type
        ↓
WorldSettingsPlugin calls SettingsGUI.openMainMenu(player)
        ↓
SettingsGUI reads current values from WorldSettings
        ↓
SettingsGUI creates chest inventory with 10 items
        ↓
Inventory displayed to player in-game
```

### Click Interaction Loop
```
Player left-clicks "Post-End Difficulty" in inventory
        ↓
Minecraft fires InventoryClickEvent
        ↓
GUIClickListener.onInventoryClick() intercepted
        ↓
Listeners retrieves WorldSettings instance from plugin
        ↓
Identifies slot = SLOT_POST_END_DIFFICULTY (19)
        ↓
Determines click type = LEFT
        ↓
Calls worldSettings.increasePostEndDifficulty() [+0.5]
        ↓
Sends player chat message: "⛏ Post-End Difficulty: 2.0x"
        ↓
Plays PLING sound effect
        ↓
Calls SettingsGUI.openMainMenu(player) [refreshes display]
        ↓
Updated inventory shown (new value displayed on item)
```

### Class Dependency Diagram
```
WorldSettingsPlugin (singleton hub)
    ├── Contains: WorldSettings (data model)
    ├── Registers: GUIClickListener (listens for clicks)
    └── Uses: SettingsGUI (renders GUI)

GUIClickListener (event handler)
    ├── Gets WorldSettings from: WorldSettingsPlugin.getInstance()
    └── Calls to update display: SettingsGUI.openMainMenu()

SettingsGUI (view builder)
    ├── Reads data from: WorldSettingsPlugin.getInstance().getWorldSettings()
    └── Displays items based on: WorldSettings field values

WorldSettings (data model)
    ├── No external dependencies
    └── Pure data storage + accessors
```

---

## Design Pattern: Model-View-Controller (MVC)

| Component | Role | Class(es) |
|-----------|------|-----------|
| **Model** | Stores and manages data | `WorldSettings` |
| **View** | Renders data to user | `SettingsGUI` |
| **Controller** | Handles user input | `GUIClickListener` |
| **Coordinator** | Wires components together | `WorldSettingsPlugin` |

**Benefits of this design:**
- **Separation of concerns:** Each class has a single responsibility
- **Reusability:** SettingsGUI can render any WorldSettings instance
- **Testability:** WorldSettings can be tested independently
- **Maintainability:** Changes to display don't affect data logic

---

## Settings Summary Table

| # | Setting Name | Type | Left Column | Range/Values | Default |
|---|--------------|------|-------------|--------------|---------|
| 1 | Post-End World | Toggle | ✓ | ON/OFF | OFF |
| 2 | Post-End Difficulty Boost | Slider | ✓ | 1.0x - 3.0x | 1.0x |
| 3 | New Boss | Toggle | ✓ | ON/OFF | OFF |
| 4 | Horde Events | Toggle | ✓ | ON/OFF | OFF |
| 5 | Max Horde Size | Slider | ✓ | 10 - 100 | 50 |
| 6 | Difficulty Level | Cycle | ✗ | EASY/MEDIUM/HARD | EASY |
| 7 | Blood Moon Events | Toggle | ✗ | ON/OFF | OFF |
| 8 | Blood Moon Spawn | Slider | ✗ | 25% - 500% | 100% |
| 9 | First Blood Moon | Cycle | ✗ | Day 1/3/5 | Day 3 |
| 10 | Blood Moon Chance | Slider | ✗ | 0% - 100% | 10% |

---

## How to Build & Deploy

### Build the Plugin
```bash
mvn clean package
```
This creates `world-settings-plugin-1.0.0.jar` in the `target/` directory.

### Deploy to Server
1. Copy `world-settings-plugin-1.0.0.jar` to your Minecraft server's `plugins/` folder
2. Restart the server
3. Plugin loads automatically via Spigot

### Test the Plugin
```
In-game command: /worldsettings
Required permission: worldsettings.use (default: OP only)
```

---

## Summary

The **WorldSettingsPlugin** is a well-structured Minecraft plugin that demonstrates clean code principles through MVC architecture. Players interact with a visual chest-based GUI to customize 10 world settings dynamically. The plugin maintains persistent state through the `WorldSettings` class, renders the interface through `SettingsGUI`, and processes user interactions through `GUIClickListener`, all coordinated by the singleton `WorldSettingsPlugin` class.

---

*Documentation created March 22, 2026*
