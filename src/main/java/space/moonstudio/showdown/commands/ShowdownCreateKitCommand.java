package space.moonstudio.showdown.commands;

import org.bukkit.entity.Player;
import space.moonstudio.showdown.ShowdownMessage;
import space.moonstudio.showdown.ShowdownManager;
import space.moonstudio.showdown.gui.ShowdownCreateKitGui;

import java.util.Arrays;

public class ShowdownCreateKitCommand implements ShowdownCommand {

    @Override
    public boolean execute(Player sender, String[] args)
    {
        String name = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        if(name.contains(".") || name.contains(":") || name.contains("/") || name.contains("\\"))
        {
            sender.sendMessage(ShowdownMessage.CONTAINS_IMPERMISSIBLE_SYMBOLS.toString());
            return true;
        }

        if(ShowdownManager.getKits().stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(name)))
        {
            sender.sendMessage(ShowdownMessage.KIT_ALREADY_EXISTS.toString());
            return true;
        }

        new ShowdownCreateKitGui(name).open(sender);
        return true;
    }
}
