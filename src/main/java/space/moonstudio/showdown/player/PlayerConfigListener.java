package space.moonstudio.showdown.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import space.moonstudio.showdown.ShowdownManager;

public class PlayerConfigListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        giveItems(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        giveItems(event.getPlayer());
    }

    private void giveItems(Player player)
    {
        PlayerConfig config = PlayerConfig.get(player.getName());
        config.restoreInventory();
        config.giveItems();
    }
}
