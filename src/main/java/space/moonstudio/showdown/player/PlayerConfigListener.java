package space.moonstudio.showdown.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.ShowdownPlugin;

public class PlayerConfigListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        giveItems(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ShowdownPlugin.getInstance(), () ->
                giveItems(event.getPlayer()), 1);
    }

    private void giveItems(Player player)
    {
        PlayerConfig config = PlayerConfig.get(player.getName());
        config.restoreInventory();
        config.giveItems();
    }
}
