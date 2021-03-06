package me.trefis.speedrunduel;

import me.trefis.speedrunduel.context.Roles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class PlayerData {
    private final Map<Player, PlayerDetails> players = new HashMap<>();

    public List<Player> getPlayersByRole(Roles role){
        return players.entrySet().stream()
                .filter(e -> e.getValue().role == role)
                .map(Map.Entry :: getKey)
                .collect(Collectors.toList());
    }

    public Roles getRole(Player player){
        return Optional.ofNullable(players.get(player))
                .map(PlayerDetails:: getRole)
                .orElse(null);
    }

    public void reset(Player player){
        players.remove(player);
    }

    public void setRole(Player player, Roles role){
        PlayerDetails details = players.getOrDefault(player, new PlayerDetails());
        details.setRole(role);
        players.putIfAbsent(player, details);
        if(role == Roles.LAVA) {
            player.sendMessage(ChatColor.GOLD + "You were assigned to the " + role + " team.");
        }else{
            player.sendMessage(ChatColor.AQUA + "You were assigned to the " + role + " team.");
        }
    }

    private static class PlayerDetails{
        private Roles role;
        public Roles getRole(){
            return role;
        }
        public void setRole(Roles role){
            this.role = role;
        }
    }
}
