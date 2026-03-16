# WorldSettingsPlugin

A Minecraft Spigot/Paper plugin that provides an in-game GUI for customizing world settings. Players with permission can open a chest-based menu with 10 configurable options arranged in a two-column layout, including toggles, sliders, and cycle selectors.

![Menu Layout](https://img.shields.io/badge/Menu_Items-10_Settings-green) ![Minecraft](https://img.shields.io/badge/Minecraft-1.20+-blue) ![Java](https://img.shields.io/badge/Java-17+-orange)

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Generation](#project-generation)
3. [Building the Plugin](#building-the-plugin)
4. [Server Setup](#server-setup)
5. [Installation](#installation)
6. [Testing the Plugin](#testing-the-plugin)
7. [In-Game Usage](#in-game-usage)
8. [Menu Reference](#menu-reference)
9. [Project Structure](#project-structure)
10. [Troubleshooting](#troubleshooting)

---

## Prerequisites

Install the following before you begin:

### 1. Java Development Kit (JDK) 17 or higher

The plugin targets Java 17. You need a JDK (not just a JRE) to compile.

**Windows:**
- Download from [Adoptium (Eclipse Temurin)](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- Run the installer and check "Set JAVA_HOME variable" during setup
- Verify: open a terminal and run `java -version`

**macOS:**
```bash
brew install openjdk@17
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Verify installation:**
```bash
java -version
javac -version
```
Both should show version 17 or higher.

### 2. Apache Maven 3.8+

Maven handles dependency resolution and builds the plugin JAR.

**Windows:**
- Download the binary zip from [maven.apache.org](https://maven.apache.org/download.cgi)
- Extract to a folder (e.g., `C:\Program Files\Maven`)
- Add the `bin` directory to your system PATH
- Verify: `mvn -version`

**macOS:**
```bash
brew install maven
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt install maven
```

**Verify installation:**
```bash
mvn -version
```
You should see Maven version 3.8+ and Java 17+.

### 3. Python 3.7+

Required only to run the project generator script.

```bash
python3 --version
```

### 4. A Spigot or Paper Minecraft Server

You need a Spigot-compatible server to run the plugin. Paper is recommended for better performance.

**Download Paper (recommended):**
1. Go to [papermc.io/downloads](https://papermc.io/downloads/paper)
2. Select Minecraft version 1.20.4 (or your preferred 1.20+ version)
3. Download the JAR file

---

## Project Generation

The Python script creates the entire Java project structure in one step.

```bash
# 1. Place the script somewhere convenient
cd ~/projects

# 2. Run the generator
python3 generate_minecraft_plugin.py

# 3. The project is now at ./WorldSettingsPlugin/
```

The script creates all directories, Java source files, the Maven `pom.xml`, and the `plugin.yml` — no manual file creation needed.

---

## Building the Plugin

```bash
# Navigate into the generated project
cd WorldSettingsPlugin

# Build the JAR (first build will download dependencies — may take a minute)
mvn clean package
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] -------------------------------------------------------
```

The compiled plugin JAR will be at:
```
WorldSettingsPlugin/target/world-settings-plugin-1.0.0.jar
```

If you see `BUILD FAILURE`, check the [Troubleshooting](#troubleshooting) section.

---

## Server Setup

If you don't already have a Minecraft server running, follow these steps to set one up locally for testing.

### Quick Local Server Setup

```bash
# 1. Create a server directory
mkdir ~/minecraft-server
cd ~/minecraft-server

# 2. Copy or move the Paper JAR here (rename for convenience)
mv ~/Downloads/paper-1.20.4-*.jar server.jar

# 3. First run — generates config files and EULA
java -Xmx2G -Xms1G -jar server.jar nogui

# 4. Accept the EULA (required)
#    Open eula.txt and change eula=false to eula=true
#    Or on Linux/macOS:
sed -i 's/eula=false/eula=true/' eula.txt

# 5. (Optional) Configure server.properties for local testing
#    Recommended settings for a test server:
#      online-mode=false      (if testing without a paid account)
#      gamemode=creative       (easier for testing GUIs)
#      difficulty=peaceful     (no mob distractions)

# 6. Start the server
java -Xmx2G -Xms1G -jar server.jar nogui
```

The server is ready when you see:
```
[Server] Done (X.XXXs)! For help, type "help"
```

### Connecting to Your Test Server

1. Open Minecraft Java Edition (matching your server version, e.g., 1.20.4)
2. Click **Multiplayer** → **Add Server**
3. Server Address: `localhost`
4. Click **Done**, then join the server

---

## Installation

```bash
# Copy the built JAR into your server's plugins folder
cp WorldSettingsPlugin/target/world-settings-plugin-1.0.0.jar ~/minecraft-server/plugins/

# Restart the server (or use the reload command if already running)
```

**From the server console:**
```
stop
```
Then start the server again. On startup, you should see:
```
[WorldSettingsPlugin] ========================================
[WorldSettingsPlugin]  WorldSettingsPlugin v1.0.0 enabled!
[WorldSettingsPlugin]  Use /worldsettings to open the GUI.
[WorldSettingsPlugin] ========================================
```

**Hot reload (without full restart):**

If you have a plugin manager like PlugMan, you can reload without restarting. Otherwise, a full server restart is the safest approach.

---

## Testing the Plugin

### Step 1: Verify the Plugin Loaded

From the server console or in-game chat:
```
/plugins
```
You should see `WorldSettingsPlugin` listed in green (green = enabled).

### Step 2: Grant Permissions

The command requires the `worldsettings.use` permission, which defaults to **op** (server operators).

From the server console:
```
op YourPlayerName
```

### Step 3: Open the Menu

In-game, type:
```
/worldsettings
```

A double-chest GUI should open with the title **"Customize World Settings"** showing all 10 setting items in two columns.

### Step 4: Test Interactions

| Action | What to Test | Expected Result |
|--------|-------------|-----------------|
| Left-click a toggle (e.g., Post-End World) | Toggle ON/OFF | Item changes appearance, chat message confirms new state, click sound plays |
| Left-click a slider (e.g., Horde Size) | Increase value | Value goes up by the increment shown in the lore |
| Right-click a slider | Decrease value | Value goes down; stops at minimum |
| Click a cycle option (e.g., Difficulty) | Cycle through values | Rotates Easy → Medium → Hard → Easy |
| Click the background glass panes | Should do nothing | No change, no sound, no chat message |
| Close the inventory (Escape) | Menu closes | No errors in console |

### Step 5: Check the Server Console

Watch the console for any errors during interaction. A clean test produces no errors or warnings.

---

## In-Game Usage

### Command

| Command | Description | Permission |
|---------|-------------|------------|
| `/worldsettings` | Opens the settings GUI | `worldsettings.use` (default: op) |

### Controls

- **Left-click** — Toggle ON, increase value, or cycle forward
- **Right-click** — Decrease value (for slider-type settings)
- **Escape** — Close the menu

---

## Menu Reference

The GUI displays 10 settings in a two-column layout inside a double chest (54 slots):

### Left Column

| Setting | Type | Range |
|---------|------|-------|
| Post-End World | Toggle | ON / OFF |
| Post-End Difficulty Boost | Slider | 1.0x – 3.0x (step: 0.5) |
| New Boss | Toggle | ON / OFF |
| Horde Events | Toggle | ON / OFF |
| Maximum Horde Size | Slider | 10 – 100 (step: 10) |

### Right Column

| Setting | Type | Range |
|---------|------|-------|
| Difficulty Level | Cycle | Easy → Medium → Hard |
| Blood Moon Events | Toggle | ON / OFF |
| Blood Moon Spawn Multiplier | Slider | 25% – 500% (step: 25%) |
| 1st Blood Moon | Cycle | Day 1 → Day 3 → Day 5 |
| Blood Moon Chance | Slider | 0% – 100% per night (step: 5%) |

---

## Project Structure

```
WorldSettingsPlugin/
├── pom.xml                                         # Maven build configuration
└── src/main/
    ├── resources/
    │   └── plugin.yml                              # Spigot plugin descriptor
    └── java/com/example/worldsettings/
        ├── WorldSettingsPlugin.java                # Main class, command handler
        ├── settings/
        │   └── WorldSettings.java                  # Data model for all 10 settings
        ├── gui/
        │   └── SettingsGUI.java                    # Chest inventory GUI builder
        └── listeners/
            └── GUIClickListener.java               # Click event handler
```

**Key classes:**

- **WorldSettingsPlugin.java** — Entry point. Registers the listener and handles the `/worldsettings` command.
- **WorldSettings.java** — Plain Java object storing all setting values with getters, toggles, and increment/decrement methods.
- **SettingsGUI.java** — Builds the 54-slot chest inventory with styled items, lore text, and a two-column layout separated by glass pane dividers.
- **GUIClickListener.java** — Listens for `InventoryClickEvent`, maps slot positions to setting actions, refreshes the GUI after each change, and plays feedback sounds.

---

## Troubleshooting

### Build fails with "javac: source release 17 requires target release 17"
Your default Java version is older than 17. Make sure `JAVA_HOME` points to JDK 17+:
```bash
export JAVA_HOME=/path/to/jdk-17
mvn clean package
```

### Build fails with dependency resolution errors
Maven needs internet access to download the Spigot API. If you're behind a firewall or proxy, configure Maven's `settings.xml` with your proxy details, or try:
```bash
mvn clean package -U
```

### Plugin shows red in /plugins list
The plugin failed to enable. Check `logs/latest.log` in your server directory for the full error. Common causes:
- Server version mismatch (plugin targets 1.20+; older servers won't work)
- Java version mismatch on the server

### Menu doesn't open when typing /worldsettings
- Confirm you are opped: run `op YourPlayerName` from the server console
- Confirm the plugin is loaded: `/plugins` should list it in green
- Check for conflicting plugins that might register the same command

### Settings don't persist after server restart
This starter plugin stores settings **in memory only**. Settings reset when the server restarts. To add persistence, you would extend `WorldSettings.java` to save/load from the plugin's `config.yml` using Spigot's `FileConfiguration` API.

---

## Next Steps

This is a starter template. Here are ideas for extending it:

- **Persistence** — Save settings to `config.yml` so they survive restarts
- **Per-world settings** — Store different configurations for each world
- **Permissions per setting** — Fine-grained control over who can change what
- **Actually apply the settings** — Hook into game events to make Blood Moon, Hordes, etc. functional
- **Additional GUI pages** — Add pagination for more settings categories (Advanced, Combat, Loot, etc.)
- **Tab completion** — Add tab-complete support for the command

---

## License

This project is provided as a starter template. Use and modify freely.
