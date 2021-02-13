package me.trefis.speedrunduel.commands;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getLogger;

public class DuelPlayerCommand implements CommandExecutor {
    private final Plugin plugin;
    private final TeamManager teamManager;
    private final PlayerData playerData;
    private Roles role;

    public DuelPlayerCommand(Plugin plugin, TeamManager teamManager, PlayerData playerData) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerData = playerData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        try {
            Player player = plugin.getServer().getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Could not find player " + args[1]);
            }

            if (args[2].toUpperCase().equals("LAVA")) {
                role = Roles.LAVA;
            } else if (args[2].toUpperCase().equals("WATER")) {
                role = Roles.WATER;
            } else {
                sender.sendMessage(ChatColor.RED + "Error: teams only can be => lava or water");
            }

            if (args[0].equals("remove")) {
                removePlayer(player, role);
                if(role == Roles.LAVA){
                    sender.sendMessage(ChatColor.GOLD + "Removed player: " + player.getName() + " from " + role + " team.");
                }else {
                    sender.sendMessage(ChatColor.AQUA + "Removed player: " + player.getName() + " from " + role + " team.");
                }
            } else if (args[0].equals("add")) {
                addPlayer(player, role);
                if (role == Roles.LAVA) {
                    sender.sendMessage(ChatColor.GOLD + "Added player: " + player.getName() + " to " + role + " team.");
                } else {
                    sender.sendMessage(ChatColor.AQUA + "Added player: " + player.getName() + " to " + role + " team.");
                }
            }

            return true;
        } catch (Exception error) {
            getLogger().info("Error " + error.getStackTrace());
            sender.sendMessage(ChatColor.RED + "Error: Usage of command is - /duelPlayer [add|remove] <player> [lava|water]");
        }
        return true;
    }

    private void addPlayer(Player player, Roles role) {
        playerData.setRole(player, role);
        teamManager.addPlayer(role, player);
        player.getInventory().remove(Material.COMPASS);
        player.getInventory().addItem(new ItemStack(Material.COMPASS));
    }

    private void removePlayer(Player player, Roles role) {
        playerData.reset(player);
        teamManager.removePlayer(role, player);
        player.getInventory().remove(Material.COMPASS);
    }
}