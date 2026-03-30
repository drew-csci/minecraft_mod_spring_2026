# Unit Test Explanation: Super Enchantment Table

## Test file
`src/test/java/com/example/worldsettings/superenchant/SuperEnchantmentManagerTest.java`

## Purpose
This unit test verifies the pure logic in `SuperEnchantmentManager` that determines whether an item is valid for super enchantments.

## What it checks
- The test creates several `ItemStack` instances for common item types.
- It ensures that weapons and armor are considered enchantable by the super enchantment feature.
- It verifies that basic non-enchantable items (like dirt and torches) are rejected.

## How it works
1. The test uses JUnit 5 with the `@Test` annotation.
2. It calls `SuperEnchantmentManager.canEnchantItem(...)` with:
   - `DIAMOND_SWORD`
   - `DIAMOND_HELMET`
   - `DIAMOND_PICKAXE`
   - `DIRT`
   - `TORCH`
3. It asserts:
   - `true` for valid enchantable items
   - `false` for invalid items

## Why this test is useful
- It validates the core item-filtering logic for the Super Enchantment Table without requiring a full Bukkit server.
- It protects the plugin from incorrectly allowing or blocking items in the late-game enchantment flow.

## Running the test
Execute:

```bash
mvn clean test
```

The project is configured with JUnit 5 so the test runs as part of Maven's standard test lifecycle.
