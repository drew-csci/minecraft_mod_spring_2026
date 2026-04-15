package com.example.worldsettings.sidebar;

import com.example.worldsettings.progression.ProgressionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ProgressionSidebar {

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

        Score line1 = objective.getScore("§7Progress: §f" + percent + "%   ");
        line1.setScore(3);

        Score line2 = objective.getScore("§7Done: §f" + completed + "/" + total + "   ");
        line2.setScore(2);

        Score line3 = objective.getScore("§7Rank: " + rank + "   ");
        line3.setScore(1);

        Score line4 = objective.getScore(" ");
        line4.setScore(0);

        player.setScoreboard(board);
    }
}