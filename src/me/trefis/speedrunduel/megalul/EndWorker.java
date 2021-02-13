package me.trefis.speedrunduel.megalul;


import me.trefis.speedrunduel.PlayerData;
import me.trefis.speedrunduel.TeamManager;
import org.bukkit.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class EndWorker implements Runnable{
    private final Plugin plugin;
    private int countdown = 15;

    public EndWorker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run(){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(new Location(Bukkit.getServer().getWorld("world_the_end"), 0, 50, 0), PlayerTeleportEvent.TeleportCause.PLUGIN);
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.0f);
            player.sendMessage(" ");
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "WELCOME TO THE BOG > " + ChatColor.RESET + "" + ChatColor.WHITE + "Players can no longer respawn.");
            player.sendMessage(" ");
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "WELCOME TO THE BOG", ChatColor.WHITE + "Players can no longer respawn!", 15, 60, 15);
            player.sendMessage( ChatColor.RED + "If you die you don't respawn. You still have to kill the dragon.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
            player.sendMessage( ChatColor.RED + "15 seconds util PVP.");
        });
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                countdown--;
                if(countdown == 10){
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage( ChatColor.RED + "10 seconds util PVP."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                }
                if (countdown == 5) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "5 seconds util PVP."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                }
                if (countdown == 4) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "4 seconds util PVP."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                }
                if (countdown == 3) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "3 seconds util PVP."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                }
                if (countdown == 2) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "2 seconds util PVP."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                }
                if (countdown == 1) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "1 seconds util PVP."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                }
                if(countdown == 0){
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "PVP NOW."));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f));
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.0f));
                }
            }
        }, 0, 20L);
    }
}
