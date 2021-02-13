package me.trefis.speedrunduel.megalul;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import me.trefis.speedrunduel.events.BogEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

public class EndScoreboard implements Runnable {
    private final PlayerData playerData;
    private final Plugin plugin;

    public EndScoreboard(Plugin plugin, PlayerData playerData) {
        this.playerData = playerData;
        this.plugin = plugin;
    }
    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "SPEEDRUN DUEL");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score s = objective.getScore("    ");
                Score s1 = objective.getScore("Time to " + ChatColor.RED + "" + ChatColor.BOLD + "BOG: NOW" + ChatColor.RESET + " ☠");
                Score s2 = objective.getScore("   ");
                Score s3 = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "LAVA");
                Score s4, s5;

                if (playerData.getPlayersByRole(Roles.LAVA).size() == 2) {
                    if (playerData.getPlayersByRole(Roles.LAVA).get(0).getGameMode() == GameMode.SURVIVAL) {
                        s4 = objective.getScore(playerData.getPlayersByRole(Roles.LAVA).get(0).getName() + ChatColor.RESET + "" + ChatColor.GREEN + " ✔");
                    } else {
                        s4 = objective.getScore(ChatColor.GRAY + playerData.getPlayersByRole(Roles.LAVA).get(0).getName() + ChatColor.RESET + "" + ChatColor.RED + " ❌");
                    }
                    if (playerData.getPlayersByRole(Roles.LAVA).get(1).getGameMode() == GameMode.SURVIVAL) {
                        s5 = objective.getScore(playerData.getPlayersByRole(Roles.LAVA).get(1).getName() + ChatColor.RESET + "" + ChatColor.GREEN + " ✔");
                    } else {
                        s5 = objective.getScore(ChatColor.GRAY + playerData.getPlayersByRole(Roles.LAVA).get(1).getName() + ChatColor.RESET + "" + ChatColor.RED + " ❌");
                    }
                } else if (playerData.getPlayersByRole(Roles.LAVA).size() == 1) {
                    if (playerData.getPlayersByRole(Roles.LAVA).get(0).getGameMode() == GameMode.SURVIVAL) {
                        s4 = objective.getScore(playerData.getPlayersByRole(Roles.LAVA).get(0).getName() + ChatColor.RESET + "" + ChatColor.GREEN + " ✔");
                    } else {
                        s4 = objective.getScore(ChatColor.GRAY + playerData.getPlayersByRole(Roles.LAVA).get(0).getName() + ChatColor.RESET + "" + ChatColor.RED + " ❌");
                    }
                    s5 = objective.getScore(ChatColor.GRAY + "Player: null" + ChatColor.RED + " ❌" + "    ");
                } else {
                    s4 = objective.getScore(ChatColor.GRAY + "Player: null" + ChatColor.RED + " ❌" + "    ");
                    s5 = objective.getScore(ChatColor.GRAY + "Player: null" + ChatColor.RED + " ❌" + "   ");
                }

                Score s6 = objective.getScore("  ");
                Score s7 = objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "WATER");
                Score s8, s9;

                if (playerData.getPlayersByRole(Roles.WATER).size() == 2) {
                    if (playerData.getPlayersByRole(Roles.WATER).get(0).getGameMode() == GameMode.SURVIVAL) {
                        s8 = objective.getScore(playerData.getPlayersByRole(Roles.WATER).get(0).getName() + ChatColor.RESET + "" + ChatColor.GREEN + " ✔");
                    } else {
                        s8 = objective.getScore(ChatColor.GRAY + playerData.getPlayersByRole(Roles.WATER).get(0).getName() + ChatColor.RESET + "" + ChatColor.RED + " ❌");
                    }
                    if (playerData.getPlayersByRole(Roles.WATER).get(1).getGameMode() == GameMode.SURVIVAL) {
                        s9 = objective.getScore(playerData.getPlayersByRole(Roles.WATER).get(1).getName() + ChatColor.RESET + "" + ChatColor.GREEN + " ✔");
                    } else {
                        s9 = objective.getScore(ChatColor.GRAY + playerData.getPlayersByRole(Roles.WATER).get(1).getName() + ChatColor.RESET + "" + ChatColor.RED + " ❌");
                    }
                } else if (playerData.getPlayersByRole(Roles.WATER).size() == 1) {
                    if (playerData.getPlayersByRole(Roles.WATER).get(0).getGameMode() == GameMode.SURVIVAL) {
                        s8 = objective.getScore(playerData.getPlayersByRole(Roles.WATER).get(0).getName() + ChatColor.RESET + "" + ChatColor.GREEN + " ✔");
                    } else {
                        s8 = objective.getScore(ChatColor.GRAY + playerData.getPlayersByRole(Roles.WATER).get(0).getName() + ChatColor.RESET + "" + ChatColor.RED + " ❌");
                    }
                    s9 = objective.getScore(ChatColor.GRAY + "Player: null" + ChatColor.RED + " ❌" + "  ");
                } else {
                    s8 = objective.getScore(ChatColor.GRAY + "Player: null" + ChatColor.RED + " ❌" + " ");
                    s9 = objective.getScore(ChatColor.GRAY + "Player: null" + ChatColor.RED + " ❌");
                }

                Score s10 = objective.getScore(" ");

                s.setScore(11);
                s1.setScore(10);
                s2.setScore(9);
                s3.setScore(8);
                s4.setScore(7);
                s5.setScore(6);
                s6.setScore(5);
                s7.setScore(4);
                s8.setScore(3);
                s9.setScore(2);
                s10.setScore(1);

                Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard));
            }
        }, 1, 20L);

    }
}


