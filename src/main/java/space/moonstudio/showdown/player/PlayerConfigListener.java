package space.moonstudio.showdown.player;

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
        PlayerConfig.get(event.getPlayer().getName()).giveItems();
    }

    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        PlayerConfig.get(event.getPlayer().getName()).restoreInventory();
        PlayerConfig.get(event.getPlayer().getName()).giveItems();
    } */
}
