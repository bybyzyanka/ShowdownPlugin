package space.moonstudio.showdown.commands;

import org.bukkit.entity.Player;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.ShowdownMap;
import space.moonstudio.showdown.ShowdownMessage;

public class ShowdownSetCommand implements ShowdownCommand {

    protected ShowdownMap map = null;
    protected int index = 0;

    @Override
    public boolean execute(Player sender, String[] args)
    {
        try
        {
            map = ShowdownManager.getMapById(Integer.parseInt(args[2]));
            if(map == null)
            {
                sender.sendMessage(ShowdownMessage.MAP_WITH_THIS_ID_NOT_FOUND.toString());
                return false;
            }

            index = Integer.parseInt(args[3]);
        }
        catch(Exception exception) { return false; }

        return true;
    }
}
