package space.moonstudio.showdown.gui;

import com.earth2me.essentials.spawn.IEssentialsSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.ShowdownGuiTitle;
import space.moonstudio.showdown.ShowdownMap;
import space.moonstudio.showdown.ShowdownMessage;
import space.moonstudio.showdown.player.PlayerConfig;
import space.moonstudio.showdown.utils.gui.ButtonGui;
import space.moonstudio.showdown.utils.gui.Gui;

public class ShowdownMenuGui extends Gui {

    public ShowdownMenuGui() { super(ShowdownGuiTitle.MENU.toString(), 9); }

    @Override
    public void onGuiClick(InventoryClickEvent event)
    {
        super.onGuiClick(event);
        if(event.isCancelled())
            return;

        event.setCancelled(true);
        PlayerConfig config = PlayerConfig.get(event.getWhoClicked().getName());
        if(event.getCurrentItem().isSimilar(ButtonGui.JOIN.get()))
        {
            if(ShowdownManager.getMap(event.getWhoClicked().getName()) != null)
            {
                event.getWhoClicked().sendMessage(ShowdownMessage.ALREADY_IN_GAME.toString());
                return;
            }

            ShowdownMap map = ShowdownManager.getFreeMap();
            if(map == null)
            {
                event.getWhoClicked().sendMessage(ShowdownMessage.NO_WORLDS_AVAILABLE.toString());
                return;
            }

            if(config.getMoney() < ShowdownManager.JOIN_PRICE)
            {
                event.getWhoClicked().sendMessage(ShowdownMessage.NOT_ENOUGH_MONEY.toString());
                return;
            }

            new ShowdownKitPickGui(map).open((Player) event.getWhoClicked());
        }
        else if(event.getCurrentItem().isSimilar(ButtonGui.TAKE_BIDS.get()))
        {
            ShowdownMap map = ShowdownManager.getMap(event.getWhoClicked().getName());
            if(map == null)
                config.takeBids();
        }
        else if(event.getCurrentItem().isSimilar(ButtonGui.LEAVE.get()))
        {
            ShowdownMap map = ShowdownManager.getMap(event.getWhoClicked().getName());
            if(map != null)
                map.removePlayer(event.getWhoClicked().getName(), false, false);
        }
    }

    @Override
    public boolean create(Player player)
    {
        inventory.setItem(template.getSlot(ButtonGui.TAKE_BIDS, inventory.getSize()), ButtonGui.TAKE_BIDS.get());
        inventory.setItem(template.getSlot(ButtonGui.JOIN, inventory.getSize()), ButtonGui.JOIN.get());
        inventory.setItem(template.getSlot(ButtonGui.BACK, inventory.getSize()), ShowdownManager.
                getMap(player.getName()) != null ? ButtonGui.LEAVE.get() : null);

        return true;
    }
}
