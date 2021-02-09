package me.trefis.speedrunduel;

import me.trefis.speedrunduel.context.Roles;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class TeamManager {
    private final Scoreboard board;

    public TeamManager(Plugin plugin){
        ScoreboardManager manager = plugin.getServer().getScoreboardManager();
        if(manager == null){
            throw new RuntimeException("No worlds loaded for scoreboard manager.");
        }

        this.board = manager.getMainScoreboard();
        if (board.getTeam(Roles.LAVA.toString()) == null)
            this.board.registerNewTeam(Roles.LAVA.toString());

        if (board.getTeam(Roles.WATER.toString()) == null) {
            board.registerNewTeam(Roles.WATER.toString());
        }
    }

    public void addPlayer(Roles teamName, Player player){
        Team team = this.board.getTeam(teamName.toString());
        if(team == null){
            throw new RuntimeException("There is no team: " + teamName);
        }
        team.addEntry(player.getName());
    }

    public void removePlayer(Roles teamName, Player player){
        Team team = this.board.getTeam(teamName.toString());
        if(team == null){
            throw new RuntimeException("There is no team: " + teamName);
        }
        team.removeEntry(player.getName());
    }
}
