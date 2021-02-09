package me.trefis.speedrunduel;

import me.trefis.speedrunduel.commands.DuelPlayerCommand;
import me.trefis.speedrunduel.events.Events;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class SpeedrunDuelPlugin extends JavaPlugin {
    @Override
    public void onEnable(){
        TeamManager manager = new TeamManager(this);
        PlayerData playerData = new PlayerData();

        getServer().getPluginManager().registerEvents(new Events(playerData, manager, this), this);

        Optional.ofNullable(getCommand("duelPlayer"))
                .ifPresent(c -> c.setExecutor(new DuelPlayerCommand(this, manager, playerData)));

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Worker(this, playerData), 1, 1);

        getLogger().info("Speedrunners vs viewers plugin started.");
    }

    @Override
    public void onDisable(){
        getLogger().info("Speedrunners vs viewers plugin stopped.");
    }
}
