package me.trefis.speedrunduel;

import me.trefis.speedrunduel.commands.DuelPlayerCommand;
import me.trefis.speedrunduel.commands.DuelStartCommand;
import me.trefis.speedrunduel.commands.DuelStatusCommand;
import me.trefis.speedrunduel.config.DataConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class SpeedrunDuelPlugin extends JavaPlugin {
    @Override
    public void onEnable(){
        getConfig().options().copyDefaults();
        saveConfig();

        DataConfig.setup();
        DataConfig.get().addDefault("timer", 3600);
        DataConfig.get().options().copyDefaults(true);
        DataConfig.save();

        TeamManager manager = new TeamManager(this);
        PlayerData playerData = new PlayerData();

        Optional.ofNullable(getCommand("duelPlayer"))
                .ifPresent(c -> c.setExecutor(new DuelPlayerCommand(this, manager, playerData)));
        Optional.ofNullable(getCommand("duelStatus"))
                .ifPresent(c -> c.setExecutor(new DuelStatusCommand(this, playerData)));
        Optional.ofNullable(getCommand("duelStart"))
                .ifPresent(c -> c.setExecutor(new DuelStartCommand(this, manager, playerData)));

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Worker(this, playerData), 1, 35L);

        getLogger().info("Speedrun duel plugin started.");
    }

    @Override
    public void onDisable(){
        getLogger().info("Speedrun duel plugin stopped.");
    }
}
