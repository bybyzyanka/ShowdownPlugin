package space.moonstudio.showdown;

import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.moonstudio.showdown.utils.Plugins;
import space.moonstudio.showdown.utils.item.UtilItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowdownManager {

    private static final FileConfiguration CONFIG = ShowdownPlugin.getInstance().getConfig();

    public static final ItemStack COIN = UtilItem.create(
        UtilItem.getMaterial(CONFIG.getString("coin.id")),
        CONFIG.getString("coin.name"));

    public static final int JOIN_PRICE = CONFIG.getInt("join-price");
    public static final int MAX_PLAYERS = 10;
    private static List<ShowdownKit> kits = new ArrayList<>();
    private static List<ShowdownMap> maps = new ArrayList<>();

    /*static
    {
        initializeMaps();
        initializeKits();
        new ShowdownChecker().start();
    } */

    public static void init() {
        initializeMaps();
        initializeKits();
        new ShowdownChecker().start();
    }

    private static void initializeKits()
    {
        try
        {
            CONFIG.getConfigurationSection("kits").getKeys(false).forEach(name ->
            {
                String path = "kits." + name + ".";
                Set<ItemStack> items = CONFIG.getConfigurationSection(path + "items")
                        .getKeys(false).stream().map(key -> CONFIG.getItemStack(path + "items." + key))
                        .collect(Collectors.toSet());

                kits.add(new ShowdownKit(name, CONFIG.getItemStack(path + "icon"), items));
            });
        }
        catch(Exception exception) {}
    }

    private static void initializeMaps()
    {
        try
        {
            for (int id = 0; id < getFreeMapId(); id++)
            {
                ConfigurationSection section = CONFIG.getConfigurationSection("maps." + id);
                ShowdownMap map = new ShowdownMap(id);
                for (int index = 1; index <= 2; index++)
                {
                    try
                    {
                        Location location = new Location(Bukkit.getWorld(section.getString("border." + index + ".world")),
                                section.getDouble("border." + index + ".x"),
                                section.getDouble("border." + index + ".y"),
                                section.getDouble("border." + index + ".z"));

                        map.getBorder().setPosition(index, location);
                    }
                    catch (Exception exception) {}
                }

                for (int index = 1; index <= 10; index++)
                {
                    try
                    {
                        Location location = new Location(Bukkit.getWorld(section.getString("spawn-points." + index + ".world")),
                                section.getDouble("spawn-points." + index + ".x"),
                                section.getDouble("spawn-points." + index + ".y"),
                                section.getDouble("spawn-points." + index + ".z"));

                        map.getSpawnPoints().setSpawnPoint(index, location);
                    }
                    catch (Exception exception) {}
                }

                maps.add(map);
            }
        }
        catch(Exception exception) {}
    }

    public static void createKit(String name, ItemStack icon, Set<ItemStack> items)
    {
        //name = ChatColor.translateAlternateColorCodes('&', name);
        CONFIG.set("kits." + name + ".icon", icon);
        int index = 0;
        for(ItemStack item : items)
        {
            CONFIG.set("kits." + name + ".items." + index, item);
            index++;
        }

        ShowdownPlugin.getInstance().saveConfig();
        kits.add(new ShowdownKit(name, icon, items));
    }

    public static int createMap()
    {
        int id = getFreeMapId();
        ShowdownMap map = new ShowdownMap(id);
        map.sync();
        maps.add(map);

        return id;
    }

    private static int getFreeMapId()
    {
        int id = 0;
        while(CONFIG.contains("maps." + id))
            id++;

        return id;
    }

    public static ShowdownMap getMap(String nick)
    {
        return maps.stream().filter(map -> map.getPlayer(nick) != null).findFirst().orElse(null);
    }

    public static ShowdownMap getMapById(int id)
    {
        return maps.stream().filter(map -> map.getId() == id).findFirst().orElse(null);
    }

    public static ShowdownMap getFreeMap()
    {
        return maps.stream().filter(map -> isFreeMap(map)).findFirst().orElse(null);
    }

    public static boolean isFreeMap(ShowdownMap map)
    {
        return map.getPlayers().size() < MAX_PLAYERS && map.getStatus() != ShowdownStatus.STARTED &&
            map.getBorder().getFirstPosition() != null && map.getBorder().getSecondPosition() != null &&
                map.getSpawnPoints().getSpawnPoints().size() == 10;
    }

    public static void prepareForBattle(Player player)
    {
        if(player.getGameMode() != GameMode.SURVIVAL)
            player.setGameMode(GameMode.SURVIVAL);

        if(player.isFlying())
            player.setFlying(false);

        if(player.getAllowFlight())
            player.setAllowFlight(false);

        User user = Plugins.getEssentials().getUser(player);
        if(user.isGodModeEnabled())
            user.setGodModeEnabled(false);

        if(player.getWalkSpeed() > 0.2F)
            //player.setWalkSpeed(0.2F);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "speed " + player.getName() + " 1");
    }

    public static List<ShowdownMap> getMaps() { return maps; }

    public static List<ShowdownKit> getKits() { return kits; }
}
