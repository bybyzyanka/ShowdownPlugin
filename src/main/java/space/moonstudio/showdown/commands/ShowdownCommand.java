package space.moonstudio.showdown.commands;

import org.bukkit.entity.Player;

public interface ShowdownCommand {

    boolean execute(Player sender, String[] args);
}
