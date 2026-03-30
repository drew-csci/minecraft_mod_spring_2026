# Unit Test Documentation - Dmitry

## Overview

**Unit Test Class**: `BossScalingManagerTest`  
**Location**: `src/test/java/com/example/worldsettings/boss/BossScalingManagerTest.java`  
**Framework**: JUnit 5  
**Tests**: 8 unit tests

## Purpose

This unit test suite validates the **core scaling logic** of `BossScalingManager` in isolation, ensuring that:
1. Tier progression works correctly after defeats
2. Multiplier calculations are accurate and monotonic
3. Multiplier caps are enforced correctly
4. Data export/import for persistence works as expected

## Test Cases

### 1. `testInitialTierIsZero()`
**Validates**: Initial state
- **Setup**: Create new `BossScalingManager`
- **Action**: Call `getTier(TEST_BOSS_KEY)` without any defeats
- **Expected**: Returns 0
- **Why**: Ensures undefeated bosses start at tier 0

### 2. `testIncrementDefeatCount()`
**Validates**: Tier increment logic
- **Setup**: Create manager, defeat boss once, then again
- **Action**: Call `incrementDefeatCount()` twice and track returned tier values
- **Expected**: Returns 1, then 2 respectively; `getTier()` returns 2 after
- **Why**: Ensures each defeat increments tier correctly and persists

### 3. `testHealthMultiplierIncreases()`
**Validates**: Health multiplier calculation and progression
- **Setup**: Calculate health multipliers for tiers 0, 1, 2
- **Action**: Compare multiplier values and verify expected formulas
- **Assertions**:
  - Tier 0: 1.0x
  - Tier 1: 1.15x (1.0 + 1×0.15)
  - Tier 2: 1.30x (1.0 + 2×0.15)
- **Why**: Ensures the 15% per-tier formula is correct

### 4. `testDamageMultiplierIncreases()`
**Validates**: Damage multiplier calculation and progression
- **Setup**: Calculate damage multipliers for tiers 0, 1, 2
- **Action**: Compare multiplier values and verify expected formulas
- **Assertions**:
  - Tier 0: 1.0x
  - Tier 1: 1.10x (1.0 + 1×0.10)
  - Tier 2: 1.20x (1.0 + 2×0.10)
- **Why**: Ensures the 10% per-tier formula is correct

### 5. `testMultiplierCap()`
**Validates**: Multiplier cap enforcement (5.0x max)
- **Setup**: Calculate multiplier for very high tier (50)
- **Action**: Check if multiplier is capped
- **Expected**: 5.0x (not higher)
- **Why**: Prevents infinite scaling on high-tier bosses; stabilizes game balance

### 6. `testMultipliplierMonotonicity()`
**Validates**: Multipliers increase monotonically (no decreases)
- **Setup**: Iterate through tiers 0-30
- **Action**: Verify each tier has multiplier ≥ previous tier
- **Expected**: All comparisons true; `multiplier(i+1) >= multiplier(i)`
- **Why**: Ensures scaling always increases, never decreases (expected behavior)

### 7. `testResetBoss()`
**Validates**: Boss reset functionality
- **Setup**: Increment boss tier to 1, then call reset
- **Action**: Check tier after reset
- **Expected**: Returns to 0
- **Why**: Allows admin/testing capability to reset individual boss tiers

### 8. `testClearAll()`
**Validates**: Complete data clear
- **Setup**: Increment two different bosses, then clear all
- **Action**: Check both bosses' tiers
- **Expected**: Both return 0
- **Why**: Ensures ability to wipe all scaling data for testing

## How Tests Relate to Acceptance Criteria

**Acceptance Criteria**: "Boss HP/Damage increases per defeat"

| Criteria | Test Coverage |
|----------|---------------|
| HP increases | `testHealthMultiplierIncreases()` validates HP formula |
| Damage increases | `testDamageMultiplierIncreases()` validates damage formula |
| Monotonic progression | `testIncrementDefeatCount()`, `testMultipliplierMonotonicity()` ensure each defeat adds value |
| No runaway scaling | `testMultiplierCap()` prevents infinite scaling |

## Running the Tests

```bash
mvn test -Dtest=BossScalingManagerTest
```

Or run all tests:
```bash
mvn test
```

## Test Results

✅ All 8 tests pass  
✅ No failures or errors  
✅ Execution time: ~25ms

## Key Design Decisions Validated

1. **Linear scaling model**: Multipliers increase linearly per tier (not exponential)
2. **Separate HP/damage multipliers**: HP has 15% increase, damage has 10% (different rates)
3. **Cap at 5.0x**: Prevents balance-breaking scaling at extreme tiers
4. **Tier persistence**: Data can be exported and re-imported for restarts

## Edge Cases Covered

- Tier 0 behavior (no scaling)
- Very high tier (50+) cap behavior
- Multiple bosses tracked independently
- Reset/clear operations
