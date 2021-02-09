package me.trefis.speedrunduel.events;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class Events implements Listener {
    private final PlayerData playerData;
    private final TeamManager teamManager;
    private final Plugin plugin;
    private HashMap<UUID, Integer> deathsMap = new HashMap<UUID, Integer>();

    public Events(PlayerData playerData, TeamManager teamManager, Plugin plugin) {
        this.playerData = playerData;
        this.teamManager = teamManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.RED + player.getName() + " has logged out.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.GREEN + player.getName() + " has joined.");
        ItemStack compass = new ItemStack(Material.COMPASS);
        if (!player.getInventory().contains(compass)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().addItem(new ItemStack(Material.COMPASS));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (deathsMap.containsKey(event.getEntity().getUniqueId())) {
            deathsMap.put(event.getEntity().getUniqueId(), deathsMap.get(event.getEntity().getUniqueId()) + 1);
        } else {
            deathsMap.put(event.getEntity().getUniqueId(), 1);
        }
    }
}