package space.moonstudio.showdown;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import space.moonstudio.showdown.player.PlayerConfig;

import java.util.Set;

@Getter
@AllArgsConstructor
public class ShowdownKit {

    private final String name;
    private final ItemStack icon;
    private final Set<ItemStack> items;

    public boolean isItemInKit(ItemStack item)
    {
        return items.stream().anyMatch(itemStack -> itemStack.isSimilar(item));
    }

    public void give(Player player)
    {
        PlayerConfig config = PlayerConfig.get(player.getName());
        config.saveInventory();
        items.forEach(item ->
        {
            PlayerInventory inventory = player.getInventory();
            String name = item.getType().name();
            if(name.endsWith("_HELMET") || item.getType() == Material.SKULL_ITEM)
                inventory.setHelmet(item);
            else if(name.endsWith("_CHESTPLATE"))
                inventory.setChestplate(item);
            else if(name.endsWith("_LEGGINGS"))
                inventory.setLeggings(item);
            else if(name.endsWith("_BOOTS"))
                inventory.setBoots(item);
            else inventory.addItem(item);
        });
    }
}
