package space.moonstudio.showdown.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.moonstudio.showdown.ShowdownMessage;

public class ShowdownSetBorderCommand extends ShowdownSetCommand {

    @Override
    public boolean execute(Player sender, String[] args)
    {
        if(!super.execute(sender, args))
            return false;

        if(index < 1 || index > 2)
            return false;

        map.getBorder().setPosition(index, sender.getLocation());
        sender.sendMessage(ShowdownMessage.SUCCESS.toString());
        return true;
    }
}
