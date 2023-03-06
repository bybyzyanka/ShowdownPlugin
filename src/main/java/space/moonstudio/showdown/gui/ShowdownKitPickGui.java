package space.moonstudio.showdown.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import space.moonstudio.showdown.*;
import space.moonstudio.showdown.player.PlayerConfig;
import space.moonstudio.showdown.utils.gui.ButtonGui;
import space.moonstudio.showdown.utils.gui.Gui;

import java.util.List;

public class ShowdownKitPickGui extends Gui {

    private final ShowdownMap map;
    private final List<ShowdownKit> kits;

    public ShowdownKitPickGui(ShowdownMap map)
    {
        super(ShowdownGuiTitle.KIT_PICK.toString(), 54);
        this.map = map;
        this.kits = ShowdownManager.getKits();
    }

    @Override
    public void onGuiClick(InventoryClickEvent event)
    {
        super.onGuiClick(event);
        if(event.isCancelled())
            return;

        event.setCancelled(true);
        if(event.getCurrentItem().isSimilar(ButtonGui.BACK.get()))
        {
            new ShowdownMenuGui().open((Player) event.getWhoClicked());
            return;
        }

        if(event.getSlot() >= kits.size())
            return;

        String nick = event.getWhoClicked().getName();
        if(!PlayerConfig.get(nick).addMoney(-ShowdownManager.JOIN_PRICE, true))
        {
            event.getWhoClicked().sendMessage(ShowdownMessage.NOT_ENOUGH_MONEY.toString());
            return;
        }

        ShowdownKit kit = kits.get(event.getSlot());
        if(map.getPlayers().size() < 10)
        {
            if(!map.addPlayer(nick, kit))
                return;

            event.getWhoClicked().sendMessage(ShowdownMessage.KIT.toString().replace("%kit%", kit.getName()));
        }

        event.getWhoClicked().closeInventory();
    }

    @Override
    public boolean create(Player player)
    {
        for(int slot = 0; slot < kits.size(); slot++)
        {
            ShowdownKit kit = kits.get(slot);
            inventory.setItem(slot, kit.getIcon());
        }

        return true;
    }
}
