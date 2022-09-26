package space.moonstudio.showdown.commands;

import org.bukkit.entity.Player;
import space.moonstudio.showdown.ShowdownMessage;

public class ShowdownSetSpawnpointCommand extends ShowdownSetCommand {

    @Override
    public boolean execute(Player sender, String[] args)
    {
        if(!super.execute(sender, args))
            return false;

        if(index < 1 || index > 10)
            return false;

        map.getSpawnPoints().setSpawnPoint(index, sender.getLocation());
        sender.sendMessage(ShowdownMessage.SUCCESS.toString());
        return true;
    }
}
