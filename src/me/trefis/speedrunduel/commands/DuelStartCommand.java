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
        if (countdown != 300) { //change this value
            sender.sendMessage(ChatColor.RED + "Duel already started. If duel ended, make sure you reload the server.");
        } else {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                public void run() {
                    countdown--;

                    ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                    Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                    Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "SPEEDRUN DUEL");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    Score s = objective.getScore("    ");
                    Score s1 = objective.getScore("Time to " + ChatColor.RED + "" + ChatColor.BOLD + "BOG: " + ChatColor.RESET + secToMin(countdown) + ChatColor.MAGIC + " lul");
                    Score s2 = objective.getScore("   ");
                    Score s3 = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "LAVA");
                    Score s4, s5;

                    if (playerData.getPlayersByRole(Roles.LAVA).size() == 2) {
                        s4 = objective.getScore(ChatColor.UNDERLINE + playerData.getPlayersByRole(Roles.LAVA).get(0).getName());
                        s5 = objective.getScore(ChatColor.UNDERLINE + playerData.getPlayersByRole(Roles.LAVA).get(1).getName());
                    } else if (playerData.getPlayersByRole(Roles.LAVA).size() == 1) {
                        s4 = objective.getScore(ChatColor.UNDERLINE + playerData.getPlayersByRole(Roles.LAVA).get(0).getName());
                        s5 = objective.getScore("Player: null     ");
                    } else {
                        s4 = objective.getScore("Player: null    ");
                        s5 = objective.getScore("Player: null   ");
                    }

                    Score s6 = objective.getScore("  ");
                    Score s7 = objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "WATER");
                    Score s8, s9;

                    if (playerData.getPlayersByRole(Roles.WATER).size() == 2) {
                        s8 = objective.getScore(ChatColor.UNDERLINE + playerData.getPlayersByRole(Roles.WATER).get(0).getName());
                        s9 = objective.getScore(ChatColor.UNDERLINE + playerData.getPlayersByRole(Roles.WATER).get(1).getName());
                    } else if (playerData.getPlayersByRole(Roles.WATER).size() == 1) {
                        s8 = objective.getScore(ChatColor.UNDERLINE + playerData.getPlayersByRole(Roles.WATER).get(0).getName());
                        s9 = objective.getScore("Player: null  ");
                    } else {
                        s8 = objective.getScore("Player: null ");
                        s9 = objective.getScore("Player: null");
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
