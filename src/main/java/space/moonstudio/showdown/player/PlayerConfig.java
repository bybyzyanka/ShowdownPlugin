package space.moonstudio.showdown.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.ShowdownPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerConfig {

    private static final String BIDS = "available-bids", INVENTORY = "saved-inventory", ITEMS = "items";

    private static List<PlayerConfig> configs = new ArrayList<>();

    @Getter
    private final String nick;
    private final File file;
    private final FileConfiguration config;

    public static PlayerConfig get(String nick)
    {
        return configs.stream().filter(config -> config.getNick().equals(nick))
            .findFirst().orElse(new PlayerConfig(nick));
    }

    public PlayerConfig(String nick)
    {
        this.nick = nick;
        file = new File(ShowdownPlugin.getInstance().getDataFolder() +
                File.separator + "players" + File.separator + nick + ".yml");

        if(!file.exists())
        {
            try { file.createNewFile(); }
            catch(Exception exception) {}
        }

        config = YamlConfiguration.loadConfiguration(file);
        configs.add(this);
    }

    public void takeBids()
    {
        Player player = Bukkit.getPlayer(nick);
        if(player == null)
            return;

        addMoney(getAvailableBids());
        config.set(BIDS, 0);
        saveConfig();
    }

    public void addAvailableBid(int bid)
    {
        config.set(BIDS, getAvailableBids() + bid);
        saveConfig();
    }

    public int getAvailableBids() { return config.getInt(BIDS); }

    public void saveInventory()
    {
        Player player = Bukkit.getPlayer(getNick());
        if(player == null)
            return;

        resetSavedInventory();
        ItemStack[] contents = player.getInventory().getContents();
        for(int index = 0; index < contents.length; index++)
            config.set(INVENTORY + "." + index, contents[index]);

        saveConfig();
        player.getInventory().setContents(new ItemStack[contents.length]);
    }

    public void restoreInventory()
    {
        Player player = Bukkit.getPlayer(getNick());
        if(player == null)
            return;

        ConfigurationSection section = config.getConfigurationSection(INVENTORY);
        if(section == null)
            return;

        ItemStack[] items = new ItemStack[41];
        for(String index : section.getKeys(false))
            items[Integer.parseInt(index)] = section.getItemStack(index);

        player.getInventory().setContents(items);
    }

    private void resetSavedInventory()
    {
        config.set(INVENTORY, null);
        saveConfig();
    }

    public void giveItems()
    {
        Player player = Bukkit.getPlayer(getNick());
        if(player == null)
            return;

        ConfigurationSection section = config.getConfigurationSection(ITEMS);
        if(section == null)
            return;

        for(String index : section.getKeys(false))
            player.getInventory().addItem(section.getItemStack(index));

        config.set(ITEMS, null);
        saveConfig();
    }

    public void addItem(ItemStack item)
    {
        Player player = Bukkit.getPlayer(getNick());
        if(player != null)
        {
            player.getInventory().addItem(item);
            return;
        }

        ConfigurationSection section = config.getConfigurationSection(ITEMS);
        if(section == null)
            section = config.createSection(ITEMS);

        int index = section.getKeys(false).size();
        section.set(String.valueOf(index), item);
        saveConfig();
    }

    public boolean addMoney(int money)
    {
        if(money > 0)
        {
            while(money > 0)
            {
                int amount = money > 64 ? 64 : money;
                money -= amount;
                ItemStack item = new ItemStack(ShowdownManager.COIN);
                item.setAmount(money);
                addItem(item);
            }

            return true;
        }

        Player player = Bukkit.getPlayer(getNick());
        if(player == null)
            return false;

        money = Math.abs(money);
        if(getMoney() < money)
            return false;

        ItemStack coin = ShowdownManager.COIN;
        ItemStack[] contents = player.getInventory().getContents();
        for(int slot = 0; slot < contents.length; slot++)
        {
            try
            {
                ItemStack item = contents[slot];
                if(coin.getType() != item.getType() || !coin.getItemMeta().getDisplayName().
                    equalsIgnoreCase(item.getItemMeta().getDisplayName()))
                        continue;

                if (item.getAmount() > money)
                {
                    item.setAmount(item.getAmount() - money);
                    break;
                }

                money -= item.getAmount();
                contents[slot] = null;
                if(money == 0)
                    break;
            }
            catch (Exception exception) {}
        }

        player.getInventory().setContents(contents);
        return true;
    }

    public int getMoney()
    {
        int money = 0;
        Player player = Bukkit.getPlayer(getNick());
        if(player == null)
            return money;

        ItemStack coin = ShowdownManager.COIN;
        for(ItemStack item : player.getInventory().getContents())
        {
            try
            {
                if(item.getType() == coin.getType() && item.getItemMeta().getDisplayName().
                    equals(coin.getItemMeta().getDisplayName()))
                        money += item.getAmount();
            }
            catch(Exception exception) {}
        }

        return money;
    }

    private void saveConfig()
    {
        try { config.save(file); }
        catch(Exception exception) {}
    }
}
