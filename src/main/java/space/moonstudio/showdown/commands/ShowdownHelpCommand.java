package space.moonstudio.showdown.commands;

import org.bukkit.entity.Player;
import space.moonstudio.showdown.ShowdownMessage;

public class ShowdownHelpCommand implements ShowdownCommand {

    @Override
    public boolean execute(Player sender, String[] args)
    {
        sender.sendMessage(ShowdownMessage.HELP_1.toString());
        sender.sendMessage(ShowdownMessage.HELP_2.toString());
        sender.sendMessage(ShowdownMessage.HELP_3.toString());
        sender.sendMessage(ShowdownMessage.HELP_4.toString());
        sender.sendMessage(ShowdownMessage.HELP_5.toString());
        sender.sendMessage(ShowdownMessage.HELP_6.toString());
        sender.sendMessage(ShowdownMessage.HELP_7.toString());
        return true;
    }
}
