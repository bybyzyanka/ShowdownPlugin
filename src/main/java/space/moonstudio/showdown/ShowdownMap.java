package space.moonstudio.showdown;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import space.moonstudio.showdown.player.PlayerConfig;

import java.util.ArrayList;
import java.util.List;

public class ShowdownMap {

    @Getter
    private final int id;
    @Getter
    private final ShowdownMapBorder border;
    @Getter
    private final ShowdownMapSpawnPoints spawnPoints;

    @Getter
    private List<ShowdownPlayer> players = new ArrayList<>();
    private int taskId;
    private int playersOnStart;

    public ShowdownMap(int id)
    {
        this.id = id;
        this.border = new ShowdownMapBorder(this);
        this.spawnPoints = new ShowdownMapSpawnPoints(this);
        reset();
    }

    public void addPlayer(String nick, ShowdownKit kit)
    {
        players.add(new ShowdownPlayer(nick, kit));
        if(getStatus() == ShowdownStatus.NOT_STARTED && players.size() == 5)
            startCount();
    }

    public void removePlayer(String nick, boolean finish)
    {
        players.remove(getPlayer(nick));
        if(getStatus() != ShowdownStatus.NOT_STARTED)
        {
            if(getStatus() == ShowdownStatus.STARTED)
            {
                giveReward(Bukkit.getPlayer(nick), players.size() + 1);
                Player player = Bukkit.getPlayer(nick);
                if(player != null)
                {
                    player.teleport(player.getLocation().getWorld().getSpawnLocation());
                    PlayerConfig.get(nick).restoreInventory();
                }
            }

            if(!finish)
                if(players.size() <= 5)
                    finish();
        }

        if(getStatus() != ShowdownStatus.STARTED)
            PlayerConfig.get(nick).addAvailableBid(ShowdownManager.JOIN_PRICE);
    }

    private void giveReward(Player player, int place)
    {
        if(place > 3)
            return;

        String message = "";
        int money = playersOnStart * ShowdownManager.JOIN_PRICE * 100;
        if(place == 2)
        {
            message = ShowdownMessage.SECOND_PLACE.toString();
            money /= 30;
        }
        else if(place == 3)
        {
            message = ShowdownMessage.THIRD_PLACE.toString();
            money /= 20;
        }
        else
        {
            message = ShowdownMessage.FIRST_PLACE.toString();
            money /= 50;
        }

        message = message.replace("%nick%", player.getName())
            .replace("%money%", String.valueOf(money));

        if(place == 1)
            player.sendMessage(message);
        else notification(message);

        PlayerConfig.get(player.getName()).addAvailableBid(money);
    }

    private void notification(String message)
    {
        players.forEach(showdownPlayer -> Bukkit.getPlayer(showdownPlayer.getNick()).sendMessage(message));
    }

    public void startCount()
    {
        taskId = Bukkit.getScheduler().runTaskAsynchronously(ShowdownPlugin.getInstance(), () ->
        {
            int timeLeft = 300;
            while(timeLeft > 0)
            {
                if(timeLeft > 15 && getPlayers().size() == 10)
                    timeLeft = 15;

                if(timeLeft == 300 || timeLeft == 30 || timeLeft == 15 || timeLeft < 6)
                {
                    if (timeLeft % 60 == 0)
                        notification(ShowdownMessage.GAME_START_MINUTES.toString().replace("%time%", String.valueOf(timeLeft / 60)));
                    else if (timeLeft <= 10)
                        notification(ShowdownMessage.GAME_START_SECONDS.toString().replace("%time%", String.valueOf(timeLeft)));
                }

                try { Thread.sleep(1000); }
                catch(InterruptedException e) {}

                timeLeft--;
            }

            startGame();
        }).getTaskId();
    }

    private void startGame()
    {
        playersOnStart = players.size();
        for(int index = 0; index < playersOnStart; index++)
        {
            ShowdownPlayer showdownPlayer = players.get(index);
            Player player = Bukkit.getPlayer(showdownPlayer.getNick());
            player.teleport(spawnPoints.getSpawnPoints().get(index));
            showdownPlayer.getKit().give(player);
            player.sendTitle(ShowdownMessage.GAME_START_TITLE.toString(), ShowdownMessage.GAME_START_SUBTITLE.toString(),
                    10, 40, 10);
        }

        try { Thread.sleep(300000); }
        catch(Exception exception) {}

        finish();
    }

    public void finish()
    {
        if(getStatus() == ShowdownStatus.STARTED)
        {
            List<ShowdownPlayer> players = new ArrayList<>(this.players);
            players.forEach(player -> removePlayer(player.getNick(), true));
        }
        else if(getStatus() == ShowdownStatus.WAITING)
            notification(ShowdownMessage.NOT_ENOUGH_PLAYERS.toString());

        reset();
    }

    public ShowdownStatus getStatus()
    {
        if(taskId != -1)
        {
            if(playersOnStart != 0)
                return ShowdownStatus.STARTED;
            else return ShowdownStatus.WAITING;
        }

        return ShowdownStatus.NOT_STARTED;
    }

    public ShowdownPlayer getPlayer(String nick)
    {
        return players.stream().filter(player -> player.getNick().equals(nick)).findFirst().orElse(null);
    }

    private void reset()
    {
        if(getStatus() == ShowdownStatus.STARTED)
            players = new ArrayList<>();

        playersOnStart = 0;
        cancelTask();
    }

    private void cancelTask()
    {
        if(taskId == -1)
            return;

        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
    }

    void sync()
    {
        ConfigurationSection section = ShowdownPlugin.getInstance().getConfig().getConfigurationSection("maps." + id);
        if(section == null)
        {
            ShowdownPlugin.getInstance().getConfig().createSection("maps." + id);
            section = ShowdownPlugin.getInstance().getConfig().getConfigurationSection("maps." + id);
        }

        for(int index = 1; index <= 2; index++)
        {
            Location location = index == 1 ? border.getFirstPosition() : border.getSecondPosition();
            if(location == null)
                continue;

            section.set("border." + index + ".x", location.getBlockX());
            section.set("border." + index + ".y", location.getBlockY());
            section.set("border." + index + ".z", location.getBlockZ());
            section.set("border." + index + ".world", location.getWorld().getName());
        }

        for(int index = 1; index <= spawnPoints.getSpawnPoints().size(); index++)
        {
            Location location = spawnPoints.getSpawnPoints().get(index - 1);
            if(location == null)
                continue;

            section.set("spawn-points." + index + ".x", location.getBlockX());
            section.set("spawn-points." + index + ".y", location.getBlockY());
            section.set("spawn-points." + index + ".z", location.getBlockZ());
            section.set("spawn-points." + index + ".world", location.getWorld().getName());
        }

        ShowdownPlugin.getInstance().saveConfig();
    }
}
