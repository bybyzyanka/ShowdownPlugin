package space.moonstudio.showdown.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.moonstudio.showdown.gui.ShowdownMenuGui;

public class ShowdownMenuCommand implements ShowdownCommand {

    @Override
    public boolean execute(Player sender, String[] args)
    {
        new ShowdownMenuGui().open(sender);
        return true;
    }
}
