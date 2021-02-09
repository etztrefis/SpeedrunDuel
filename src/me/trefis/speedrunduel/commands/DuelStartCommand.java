package me.trefis.speedrunduel.commands;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;

public class DuelStartCommand implements CommandExecutor {
    private final PlayerData playerData;
    private final TeamManager teamManager;
    private final Plugin plugin;
    public int countdown = 300; // 3600 in 1 hour

    public DuelStartCommand(Plugin plugin, TeamManager manager, PlayerData playerData) {
        this.playerData = playerData;
        this.plugin = plugin;
        this.teamManager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "SPEEDRUN DUEL");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score s = objective.getScore("    ");
                s.setScore(11);
                Score s1 = objective.getScore("Time to BOG " + secToMin(countdown) + ChatColor.RED + "" + ChatColor.BOLD + "MEGALUL");
                s1.setScore(10);
                Score s2 = objective.getScore("   ");
                s2.setScore(9);
                Score s3 = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "LAVA");
                s3.setScore(8);

                Score s4;
                Score s5;
                Score s8;
                Score s9;

                try {
                    s4 = objective.getScore(playerData.getPlayersByRole(Roles.LAVA).get(0).getName());
                } catch (Exception e) {
                    s4 = objective.getScore("null");
                }

                try {
                    s5 = objective.getScore(playerData.getPlayersByRole(Roles.LAVA).get(1).getName());
                } catch (Exception e) {
                    s5 = objective.getScore("null");
                }

                s4.setScore(7);
                s5.setScore(6);
                Score s6 = objective.getScore("  ");
                s6.setScore(5);
                Score s7 = objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "WATER");
                s7.setScore(4);

                try {
                    s8 = objective.getScore(playerData.getPlayersByRole(Roles.WATER).get(0).getName());
                } catch (Exception e) {
                    s8 = objective.getScore("null");
                }

                try {
                    s9 = objective.getScore(playerData.getPlayersByRole(Roles.WATER).get(1).getName());
                } catch (Exception e) {
                    s9 = objective.getScore("null");
                }

                s8.setScore(3);
                s9.setScore(2);
                Score s10 = objective.getScore(" ");
                s10.setScore(1);

                Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard));
                countdown--;
                if (countdown == 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.RED + "Я ебал твою папу", ChatColor.GREEN + "Привет!", 15, 15, 15));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0f, 1.0f));
                    plugin.getServer().getScheduler().cancelTasks(plugin);
                }
            }
        }, 0L, 20L);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Game started!");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0f, 1.0f);
        }
        return true;
    }

    public static String secToMin(int i) {
        int ms = i / 60;
        int ss = i % 60;
        String m = (ms < 10 ? "0" : "") + ms;
        String s = (ss < 10 ? "0" : "") + ss;
        String f = m + ":" + s;
        return f;
    }
}
