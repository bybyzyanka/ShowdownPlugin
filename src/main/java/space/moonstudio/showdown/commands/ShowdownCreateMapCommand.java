package space.moonstudio.showdown.commands;

import org.bukkit.entity.Player;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.ShowdownMessage;

public class ShowdownCreateMapCommand implements ShowdownCommand {

    @Override
    public boolean execute(Player sender, String[] args)
    {
        sender.sendMessage(ShowdownMessage.CREATE_MAP.toString().replace("%id%",
                String.valueOf(ShowdownManager.createMap())));

        return true;
    }
}
