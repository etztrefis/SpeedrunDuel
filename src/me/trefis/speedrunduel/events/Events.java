package me.trefis.speedrunduel.events;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class Events implements Listener {
    private final PlayerData playerData;
    private final TeamManager teamManager;
    private final Plugin plugin;
    private HashMap<UUID, Integer> deathsMap = new HashMap<UUID, Integer>();
    private HashMap<UUID, Roles> leaversMap = new HashMap<UUID, Roles>();

    public Events(PlayerData playerData, TeamManager teamManager, Plugin plugin) {
        this.playerData = playerData;
        this.teamManager = teamManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onDealDamage(EntityDamageByEntityEvent event) {
        try{
            Player damager = (Player) event.getDamager();
            Player entity = (Player) event.getEntity();
            if (playerData.getRole(damager) == playerData.getRole(entity)) {
                event.setCancelled(true);
            }
        }catch (Exception e){
            getLogger().info("Error while checking on entity damage event | " + e.getMessage());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(playerData.getRole(player) == Roles.LAVA || playerData.getRole(player) == Roles.WATER){
            leaversMap.put(player.getUniqueId(), playerData.getRole(player));
            removePlayer(player, playerData.getRole(player));
        }
        event.setQuitMessage(ChatColor.RED + player.getName() + " has logged out.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(leaversMap.containsKey(player.getUniqueId())){
            addPlayer(player, leaversMap.get(player.getUniqueId()));
            leaversMap.remove(player.getUniqueId());
        }
        event.setJoinMessage(ChatColor.GREEN + player.getName() + " has joined.");
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

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().addItem(new ItemStack(Material.COMPASS));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(i -> i.getType() == Material.COMPASS);

        if (deathsMap.containsKey(event.getEntity().getUniqueId())) {
            deathsMap.put(event.getEntity().getUniqueId(), deathsMap.get(event.getEntity().getUniqueId()) + 1);
        } else {
            deathsMap.put(event.getEntity().getUniqueId(), 1);
        }
        if (deathsMap.get(event.getEntity().getUniqueId()) == 3) {
            event.getEntity().setGameMode(GameMode.SPECTATOR);
        }
    }
}