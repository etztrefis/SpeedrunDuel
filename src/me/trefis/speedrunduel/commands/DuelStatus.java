package me.trefis.speedrunduel.commands;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getLogger;

public class DuelStatus implements CommandExecutor {
    private final PlayerData playerData;
    private final Plugin plugin;

    public DuelStatus(Plugin plugin, PlayerData playerData) {
        this.playerData = playerData;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        try {
            Player player = plugin.getServer().getPlayer(args[0]);
            if(playerData.getRole(player) == Roles.LAVA || playerData.getRole(player) == Roles.WATER ){
                sender.sendMessage(ChatColor.DARK_PURPLE + "Now player: " + player.getName() + " in " + playerData.getRole(player) + " team.");
            }else{
                sender.sendMessage(ChatColor.DARK_PURPLE + "Now player: " + player.getName() + " is not included in any teams.");
            }
            return true;
        } catch (Exception error) {
            getLogger().info("Error " + error.getStackTrace());
            sender.sendMessage(ChatColor.RED + "Error: Usage of command is - /duelStatus <player>");
        }
        return true;
    }
}
