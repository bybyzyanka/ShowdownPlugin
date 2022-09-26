package space.moonstudio.showdown;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import space.moonstudio.showdown.commands.*;
import space.moonstudio.showdown.player.PlayerConfigListener;
import space.moonstudio.showdown.utils.gui.GuiListener;

public final class ShowdownPlugin extends JavaPlugin {

    private static ShowdownPlugin instance;

    public static ShowdownPlugin getInstance() { return instance; }

    @Override
    public void onEnable()
    {
        instance = this;
        initializeConfig();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable()
    {
        ShowdownManager.getMaps().forEach(ShowdownMap::finish);
    }

    private void initializeConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands()
    {
        getCommand("showdown").setExecutor((sender, command, label, args) ->
        {
            if(!(sender instanceof Player))
                return false;

            Player player = (Player) sender;
            if(args.length > 0)
            {
                if(sender.isOp())
                {
                    if(args[0].equalsIgnoreCase("set") && args.length > 3)
                    {
                        if(args[1].equalsIgnoreCase("spawnpoint"))
                            return new ShowdownSetSpawnpointCommand().execute(player, args);
                        else if(args[1].equalsIgnoreCase("border"))
                            return new ShowdownSetBorderCommand().execute(player, args);
                    }
                    else if(args[0].equalsIgnoreCase("create") && args.length > 1)
                    {
                        if(args[1].equalsIgnoreCase("map"))
                            return new ShowdownCreateMapCommand().execute(player, args);
                        else if(args[1].equalsIgnoreCase("kit") && args.length > 2)
                            return new ShowdownCreateKitCommand().execute(player, args);
                    }
                    else if(args[0].equalsIgnoreCase("help"))
                        return new ShowdownHelpCommand().execute(player, args);
                }

                return false;
            }

            new ShowdownMenuCommand().execute(player, args);
            return true;
        });
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new ShowdownListener(), getInstance());
        getServer().getPluginManager().registerEvents(new GuiListener(), getInstance());
        getServer().getPluginManager().registerEvents(new PlayerConfigListener(), getInstance());
    }
}
