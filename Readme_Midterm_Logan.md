# Midterm Feature Summary: Super Enchantment Table

## Newly Written Code Files

### 1. `src/main/java/com/example/worldsettings/superenchant/SuperEnchantmentManager.java`
- Added the core Super Enchantment Table support and custom items.
- Defines new item creation methods for:
  - `Super Enchantment Table`
  - `Ancient Arcane Dust`
  - `Void Crystal`
  - `Enchanted Bone Shard`
  - `Reinforced Enchantment Core`
- Defines the late-game enchantment options available through the table.
- Handles enchantment application, lapis cost checking, XP cost checking, and custom persistent effects like Soulbound and Temporal Strike.

### 2. `src/main/java/com/example/worldsettings/superenchant/SuperEnchantmentTableGUI.java`
- Builds a custom 27-slot GUI for the Super Enchantment Table.
- Displays an input slot, enchantment option buttons, close button, and information book.
- Shows enchantment details, lapis cost, and XP cost for each option.
- Provides a visual interface for selecting and applying enchantments.

### 3. `src/main/java/com/example/worldsettings/listeners/SuperEnchantListener.java`
- Listens for player interactions with the Super Enchantment Table item.
- Opens the Super Enchantment Table GUI when the item is right-clicked.
- Handles GUI clicks to apply enchantments, validate lapis and XP, and play sound/particle feedback.
- Adds rare loot drops from mobs for new progression materials.
- Adds Soulbound restore logic after player death and respawn.
- Adds Temporal Strike behavior to slow enemies when using an enchanted weapon.
- Handles inventory closing to return items placed into the Super Enchantment Table GUI.

### 4. `src/main/java/com/example/worldsettings/WorldSettingsPlugin.java`
- Integrated the Super Enchantment Table feature into plugin startup.
- Registers the new `SuperEnchantListener` event listener.
- Initializes Super Enchantment keys and data structures.
- Registers crafting recipes for the `Reinforced Enchantment Core` and `Super Enchantment Table`.
- Added commands for testing and using the new table:
  - `/superenchant`
  - `/supertable`

### 5. `src/main/resources/plugin.yml`
- Added new commands and permissions for the Super Enchantment Table feature.
- Exposes `superenchant` and `supertable` commands in plugin metadata.

## What the New Code Does

- Enables a late-game Super Enchantment Table that players can craft once they obtain rare mob drop materials.
- Provides stronger and exclusive enchantments not available through the vanilla enchantment table.
- Uses rare materials and higher lapis/XP costs to ensure this upgrade remains a late-game reward.
- Creates a custom GUI for selecting enchantments with clear cost information and item placement.
- Adds visual and audio feedback for table use, enchantment success, and special enchantment behavior.
- Introduces Soulbound item persistence and a Temporal Strike effect for enhanced late-game combat.
- Ensures items placed in the Super Enchantment Table GUI are safely returned when the inventory is closed.

## Notes

- The implementation is compiled and packaged successfully using Maven.
- The feature is designed to work within the existing plugin by extending the current plugin entry point and event system.
