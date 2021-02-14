package me.trefis.speedrunduel.events;

import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import me.trefis.speedrunduel.context.Roles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
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
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
            try {
                Player damager = (Player) event.getDamager();
                Player entity = (Player) event.getEntity();
                if (playerData.getRole(damager) == playerData.getRole(entity)) {
                    event.setCancelled(true);
                }
            } catch (Exception e) {
                getLogger().info("Error while checking on entity damage event | " + e.getMessage());
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (playerData.getRole(player) == Roles.LAVA || playerData.getRole(player) == Roles.WATER) {
            leaversMap.put(player.getUniqueId(), playerData.getRole(player));
            removePlayer(player, playerData.getRole(player));
        }
        event.setQuitMessage(ChatColor.RED + player.getName() + " has logged out.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (leaversMap.containsKey(player.getUniqueId())) {
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
        if (deathsMap.get(event.getEntity().getUniqueId()) == 100) {
            event.getEntity().setGameMode(GameMode.SPECTATOR);
        }

        if (playerData.getPlayersByRole(playerData.getRole(event.getEntity())).size() == 2) {
            if (playerData.getPlayersByRole(playerData.getRole(event.getEntity())).get(0).getGameMode() != GameMode.SURVIVAL &&
                    playerData.getPlayersByRole(playerData.getRole(event.getEntity())).get(1).getGameMode() != GameMode.SURVIVAL) {
                String team = String.valueOf(playerData.getRole(event.getEntity())).toUpperCase();
                if (team.equals("LAVA")) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "WATER WINS", ChatColor.GOLD + "Team lava" + ChatColor.RED + " has been eliminated!", 15, 60, 15));

                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM ELIMINATED > " +
                            ChatColor.RESET + "" + ChatColor.GOLD + "Team lava" + ChatColor.RED + " has been eliminated!"));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                } else {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "LAVA WINS", ChatColor.AQUA + "Team water" + ChatColor.RED + " has been eliminated!", 15, 60, 15));

                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM ELIMINATED > " +
                            ChatColor.RESET + "" + ChatColor.AQUA + "Team water" + ChatColor.RED + " has been eliminated!"));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                }
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f));
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0f, 1.0f));
                plugin.getServer().getScheduler().cancelTasks(plugin);
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        } else if (playerData.getPlayersByRole(playerData.getRole(event.getEntity())).size() == 1) {
            if (playerData.getPlayersByRole(playerData.getRole(event.getEntity())).get(0).getGameMode() != GameMode.SURVIVAL) {
                String team = String.valueOf(playerData.getRole(event.getEntity())).toUpperCase();
                if (team.equals("LAVA")) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "WATER WINS", ChatColor.GOLD + "Team lava" + ChatColor.RED + " has been eliminated!", 15, 60, 15));

                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM ELIMINATED > " +
                            ChatColor.RESET + "" + ChatColor.GOLD + "Team lava" + ChatColor.RED + " has been eliminated!"));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                } else {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "LAVA WINS", ChatColor.AQUA + "Team water" + ChatColor.RED + " has been eliminated!", 15, 60, 15));

                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "TEAM ELIMINATED > " +
                            ChatColor.RESET + "" + ChatColor.AQUA + "Team water" + ChatColor.RED + " has been eliminated!"));
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(" "));
                }
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f));
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0f, 1.0f));
                plugin.getServer().getScheduler().cancelTasks(plugin);
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            LivingEntity dragon = event.getEntity();
            Player killer = dragon.getKiller();
            String team = String.valueOf(playerData.getRole(killer)).toUpperCase();
            if (team.equals("LAVA")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + team + " WINS", ChatColor.WHITE + killer.getName() + " killed the dragon!", 15, 60, 15));
            } else {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + team + " WINS", ChatColor.WHITE + killer.getName() + " killed the dragon!", 15, 60, 15));
            }
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0f, 1.0f));
            plugin.getServer().getScheduler().cancelTasks(plugin);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().toString().toLowerCase().contains("bed")) {
                Block block = event.getClickedBlock();
                if (block.getLocation().getWorld() == Bukkit.getWorld("world_the_end")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.5f, 5.0f));
    }
}