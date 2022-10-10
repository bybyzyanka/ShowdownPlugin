package space.moonstudio.showdown;

import org.bukkit.Bukkit;

public class ShowdownChecker {

    public void start()
    {
        Bukkit.getScheduler().runTaskTimer(ShowdownPlugin.getInstance(), () ->
        {
            ShowdownManager.getMaps().stream().filter(map -> map.getStatus() == ShowdownStatus.STARTED).forEach(map ->
                map.getPlayers().stream().forEach(value -> ShowdownManager
                        .prepareForBattle(Bukkit.getPlayer(value.getNick()))));
        }, 0, 20L);
    }
}
