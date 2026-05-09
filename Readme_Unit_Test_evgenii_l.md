The unit test lives in SettingsGUITest.java

It tests the helper method SettingsGUI.buildToggleLore(...)

This method builds the lore text used by toggle menu items

There are two checks:
1. buildToggleLoreProducesCorrectStatusAndInstructions
    verifies lore is not null
    verifies it contains Status: and ON
    verifies it ends with Click to toggle
2. buildToggleLoreIncludesExtraLoreWhenProvided
    verifies extra lines like Performance: Moderate are included
    verifies the status line says OFF
    verifies the click instruction still appears

This test is simple and explicit because it only tests the new session-specific logic, not the full Bukkit GUI

Run it with:
mvn test