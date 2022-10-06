package space.moonstudio.showdown;

import com.earth2me.essentials.spawn.IEssentialsSpawn;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import space.moonstudio.showdown.gui.ShowdownCreateKitGui;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ShowdownListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Bukkit.getScheduler().runTaskLater(ShowdownPlugin.getInstance(), () ->
        {
            ShowdownMap map = ShowdownManager.getMap(event.getEntity().getName());
            if(map != null && map.getStatus() == ShowdownStatus.STARTED)
            {
                event.getEntity().spigot().respawn();
                map.removePlayer(event.getEntity().getName(), false);
            }
        }, 1);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        ShowdownMap map = ShowdownManager.getMap(event.getPlayer().getName());
        if(map != null)
            map.removePlayer(event.getPlayer().getName(), false);
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event)
    {
        if(event.getNewGameMode() == GameMode.CREATIVE && ShowdownManager.getMap(event.getPlayer().getName()) != null)
            event.setCancelled(true);
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
        Player player = event.getPlayer();
        ShowdownMap map = ShowdownManager.getMap(player.getName());
        if(map != null && map.getStatus() == ShowdownStatus.STARTED)
        {
            if(!map.getBorder().isInBorder(event.getFrom()))
            {
                map.removePlayer(player.getName(), false);
                return;
            }

            if(!map.getBorder().isInBorder(event.getTo()))
            {
                event.setCancelled(true);
                player.sendMessage(ShowdownMessage.CANT_GO_OUT_OF_BORDER.toString());
            }

            return;
        }

        for(ShowdownMap showdownMap : ShowdownManager.getMaps())
        {
            if (showdownMap.getStatus() == ShowdownStatus.STARTED && showdownMap.getBorder().isInBorder(event.getTo())) {
                event.setCancelled(true);
                player.teleport(((IEssentialsSpawn) ShowdownManager.getEssentialsSpawn()).
                        getSpawn(ShowdownManager.getEssentials().getUser(player).getGroup()));
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        if(event.getMessage().startsWith("/showdown") || event.getMessage().startsWith("/shd"))
            return;

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