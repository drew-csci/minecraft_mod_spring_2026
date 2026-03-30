# Integration Test Notes

## What we attempted
- Added an integration test for `SettingsGUI.openMainMenu(...)` using MockBukkit.
- The test was intended to start a fake Bukkit server, load the plugin, add a fake player, and open the GUI.

## What failed
- The test dependency (`MockBukkit`) could not be resolved cleanly.
- Maven could not compile the test because the package `be.seeseemelk.mockbukkit` was not found.
- The test code also used Bukkit API methods that were not compatible with the available runtime/test setup.

## Why it failed
- We do not currently have a stable Bukkit mock/test framework configured in this project.
- The project is a normal Maven plugin project with `spigot-api` as provided scope, not a standalone app.
- Integration tests against Bukkit require a specialized mock library and correct repository setup.

## Result
- Removed the broken integration test file.
- Reverted the `pom.xml` changes that added MockBukkit and JitPack.
- Kept the project working with the current pure unit test only.

## Takeaway
- This session should keep focus on documentation and the simple session-scoped unit test.
- Adding Bukkit integration tests is possible later, but it requires separate setup and dependencies.
