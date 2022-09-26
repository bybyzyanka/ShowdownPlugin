package space.moonstudio.showdown.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import space.moonstudio.showdown.ShowdownGuiTitle;
import space.moonstudio.showdown.ShowdownMessage;
import space.moonstudio.showdown.utils.gui.ButtonGui;
import space.moonstudio.showdown.utils.gui.Gui;

@Getter
public class ShowdownCreateKitGui extends Gui {

    private final String kitName;
    private ItemStack icon;

    public ShowdownCreateKitGui(String kitName)
    {
        super(ShowdownGuiTitle.KIT_CREATE.toString(), 27);
        this.kitName = kitName;
    }

    @Override
    public void onGuiClick(InventoryClickEvent event)
    {
        if(event.getCurrentItem() != null)
                event.setCancelled(true);
    }

    @Override
    public boolean create(Player player)
    {
        if(player.getInventory().getItemInMainHand() == null)
        {
            player.sendMessage(ShowdownMessage.TAKE_ITEM_IN_HAND.toString());
            return false;
        }

        icon = player.getInventory().getItemInMainHand();
        inventory.setItem(template.getSlot(ButtonGui.BACK, inventory.getSize()), null);
        return true;
    }
}
