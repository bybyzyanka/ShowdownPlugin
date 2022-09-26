package space.moonstudio.showdown;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        items.forEach(item -> player.getInventory().addItem(item));
    }
}
