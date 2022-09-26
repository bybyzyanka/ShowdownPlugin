package space.moonstudio.showdown;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import space.moonstudio.showdown.gui.ShowdownCreateKitGui;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ShowdownListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        ShowdownMap map = ShowdownManager.getMap(event.getEntity().getName());
        if(map != null && map.getStatus() == ShowdownStatus.STARTED)
            map.removePlayer(event.getEntity().getName(), false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        ShowdownMap map = ShowdownManager.getMap(event.getPlayer().getName());
        if(map != null)
            map.removePlayer(event.getPlayer().getName(), false);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event)
    {
        if(event.getItemDrop() == null)
            return;

        ShowdownMap map = ShowdownManager.getMap(event.getPlayer().getName());
        if(map != null)
            if(map.getPlayer(event.getPlayer().getName()).getKit().isItemInKit(event.getItemDrop().getItemStack()))
                event.setCancelled(true);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        ShowdownMap map = ShowdownManager.getMap(event.getPlayer().getName());
        if(map != null && map.getStatus() == ShowdownStatus.STARTED)
        {
            if(!map.getBorder().isInBorder(event.getTo()))
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ShowdownMessage.CANT_GO_OUT_OF_BORDER.toString());
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        ShowdownMap map = ShowdownManager.getMap(event.getPlayer().getName());
        if(map != null && map.getStatus() == ShowdownStatus.STARTED)
        {
            event.getPlayer().sendMessage(ShowdownMessage.CANT_USE_COMMANDS.toString());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().getHolder() instanceof ShowdownCreateKitGui)
        {
            ShowdownCreateKitGui gui = (ShowdownCreateKitGui) event.getInventory().getHolder();
            ShowdownManager.createKit(gui.getKitName(), gui.getIcon(), Arrays.stream(event.getInventory().getContents())
                    .filter(itemStack -> itemStack != null && itemStack.getType() != Material.AIR)
                    .collect(Collectors.toSet()));

            event.getPlayer().sendMessage(ShowdownMessage.SUCCESS.toString());
        }
    }
}