package space.moonstudio.showdown.utils.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.utils.item.UtilItem;

import static space.moonstudio.showdown.ShowdownManager.*;

public enum ButtonGui {

    BACK(Material.BARRIER, ChatColor.YELLOW + "Назад"),
    TAKE_BIDS(ShowdownManager.COIN.getType(), ChatColor.GREEN + "Забрать ставки"),
    JOIN(Material.NETHER_STAR, ChatColor.GREEN + "Начать игру",
    ChatColor.GRAY + "Для начала игры требуется " + JOIN_PRICE + " " + COIN.getItemMeta().getDisplayName());

    private final ItemStack item;

    ButtonGui(Material material, String name, String... lore) { item = UtilItem.create(material, name, lore); }

    public ItemStack get() { return item; }
}
