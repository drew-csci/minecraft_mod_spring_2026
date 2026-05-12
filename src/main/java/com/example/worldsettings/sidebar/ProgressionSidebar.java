package com.example.worldsettings.sidebar;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import com.example.worldsettings.progression.ProgressionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ProgressionSidebar {

    private static final int EVENT_COUNTDOWN_DAYS = 7;

    public static void updateSidebar(Player player, ProgressionManager progressionManager) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) {
            return;
        }

        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("progression", Criteria.DUMMY, "§6§lProgression");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int completed = progressionManager.getCompletedAdvancementCount(player);
        int total = progressionManager.getTotalTrackedAdvancements();
        int percent = progressionManager.getProgressionPercent(player);
        String rank = progressionManager.getDifficultyLabel(percent);
        World world = player.getWorld();
        long currentDay = world.getFullTime() / 24000L;
        long daysRemaining = Math.max(0L, EVENT_COUNTDOWN_DAYS - currentDay);
        WorldSettings settings = WorldSettingsPlugin.getInstance().getWorldSettings();

        String eventLine = settings.isCrimsonDescentEnabled()
            ? "§cEvent: Active   "
            : "§7Event In: §f" + daysRemaining + "d   ";
        String dayLine = "§7World Day: §f" + currentDay + "   ";

        Score line1 = objective.getScore("§7Progress: §f" + percent + "%   ");
        line1.setScore(5);

        Score line2 = objective.getScore("§7Done: §f" + completed + "/" + total + "   ");
        line2.setScore(4);

        Score line3 = objective.getScore("§7Rank: " + rank + "   ");
        line3.setScore(3);

        Score line4 = objective.getScore(dayLine);
        line4.setScore(2);

        Score line5 = objective.getScore(eventLine);
        line5.setScore(1);

        Score line6 = objective.getScore(" ");
        line6.setScore(0);

        player.setScoreboard(board);
    }
}