package space.moonstudio.showdown.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Plugins {

    public static Essentials getEssentials()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        if (plugin == null || !(plugin instanceof Essentials))
            return null;

        return (Essentials) plugin;
    }

    public static EssentialsSpawn getEssentialsSpawn()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("EssentialsSpawn");

        if (plugin == null || !(plugin instanceof EssentialsSpawn))
            return null;

        return (EssentialsSpawn) plugin;
    }
}
