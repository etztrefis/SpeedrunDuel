package me.trefis.speedrunduel;

import me.trefis.speedrunduel.context.Roles;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Comparator;

public class Worker implements Runnable {
    private final Plugin plugin;
    private final PlayerData playerData;

    public Worker(Plugin plugin, PlayerData playerData) {
        this.plugin = plugin;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateCompass(player);
        }
    }

    private void updateCompass(Player player) {
        Player nearest = getNearest(player);
        PlayerInventory inv = player.getInventory();
        for (int j = 0; j < inv.getSize(); j++) {
            ItemStack stack = inv.getItem(j);
            if (stack == null) continue;
            if (stack.getType() != Material.COMPASS) continue;

            stack.addUnsafeEnchantment(Enchantment.LUCK, 1);
        }
        if (nearest == null || nearest.getWorld().getEnvironment() != player.getWorld().getEnvironment()) {
            float angle = (float) (Math.random() * Math.PI * 2);
            float dx = (float) (Math.cos(angle) * 5);
            float dz = (float) (Math.sin(angle) * 5);
            player.setCompassTarget(player.getLocation().add(new Vector(dx, 0, dz)));
        } else {
            if (player.getWorld().getEnvironment() == nearest.getWorld().getEnvironment()) {
                for (int j = 0; j < inv.getSize(); j++) {
                    ItemStack stack = inv.getItem(j);
                    if (stack == null) continue;
                    if (stack.getType() != Material.COMPASS) continue;

                    CompassMeta meta = (CompassMeta) stack.getItemMeta();
                    meta.setLodestone(nearest.getLocation());
                    meta.setLodestoneTracked(false);
                    stack.setItemMeta(meta);
                }
            }
        }
    }

    private Player getNearest(Player player) {
        Location playerLocation = player.getLocation();
        if (playerData.getRole(player) == Roles.LAVA) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> !p.equals(player))
                    .filter(p -> playerData.getRole(p) == Roles.WATER)
                    .filter(p -> p.getWorld().equals(player.getWorld()))
                    .min(Comparator.comparing(p -> p.getLocation().distance(playerLocation)))
                    .orElse(null);
        } else {
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> !p.equals(player))
                    .filter(p -> playerData.getRole(p) == Roles.LAVA)
                    .filter(p -> p.getWorld().equals(player.getWorld()))
                    .min(Comparator.comparing(p -> p.getLocation().distance(playerLocation)))
                    .orElse(null);
        }
    }
}
