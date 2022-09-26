package space.moonstudio.showdown.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import space.moonstudio.showdown.ShowdownGuiTitle;
import space.moonstudio.showdown.ShowdownKit;
import space.moonstudio.showdown.ShowdownMap;
import space.moonstudio.showdown.ShowdownManager;
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

        if(map.getPlayers().size() < 10)
            map.addPlayer(event.getWhoClicked().getName(), kits.get(event.getSlot()));

        event.getWhoClicked().closeInventory();
    }

    @Override
    public boolean create(Player player)
    {
        for(int slot = 1; slot < kits.size(); slot++)
        {
            ShowdownKit kit = kits.get(slot - 1);
            inventory.setItem(slot, kit.getIcon());
        }

        return true;
    }
}
