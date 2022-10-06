package space.moonstudio.showdown;


import com.earth2me.essentials.spawn.IEssentialsSpawn;
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

    public boolean addPlayer(String nick, ShowdownKit kit)
    {
        if(getPlayer(nick) != null)
            return false;

        players.add(new ShowdownPlayer(nick, kit));
        notification(ShowdownMessage.JOIN.toString().replace("%nick%", nick).
            replace("%players%", String.valueOf(players.size())));

        if(getStatus() == ShowdownStatus.NOT_STARTED && players.size() == 3) // 5
            startCount();

        return true;
    }

    public void removePlayer(String nick, boolean finish)
    {
        if(getPlayer(nick) == null)
            return;

        players.remove(getPlayer(nick));
        if(getStatus() != ShowdownStatus.STARTED)
            PlayerConfig.get(nick).addAvailableBid(ShowdownManager.JOIN_PRICE);

        if(getStatus() != ShowdownStatus.NOT_STARTED)
        {
            Player player = Bukkit.getPlayer(nick);
            if(getStatus() == ShowdownStatus.STARTED)
            {
                giveReward(player, players.size() + 1);
                if(player != null)
                {
                    PlayerConfig.get(nick).restoreInventory();
                    player.teleport(((IEssentialsSpawn) ShowdownManager.getEssentialsSpawn()).
                            getSpawn(ShowdownManager.getEssentials().getUser(player).getGroup()));
                }
            }
            else
            {
                String message = ShowdownMessage.LEAVE.toString().replace("%nick%", nick).
                        replace("%players%", String.valueOf(players.size()));

                notification(message);
                player.sendMessage(message);
            }

            if(!finish)
                if((getStatus() == ShowdownStatus.WAITING && players.size() < 5) || players.size() == 1)
                    finish();
        }
    }

    private void giveReward(Player player, int place)
    {
        String message = ShowdownMessage.OTHER_PLACE.toString();
        double money = place <= 3 ? playersOnStart * ShowdownManager.JOIN_PRICE : 0;
        if(place == 2)
        {
            message = ShowdownMessage.SECOND_PLACE.toString();
            money *= 0.3;
        }
        else if(place == 3)
        {
            message = ShowdownMessage.THIRD_PLACE.toString();
            money *= 0.2;
        }
        else if(place == 1)
        {
            message = ShowdownMessage.FIRST_PLACE.toString();
            money *= 0.5;
        }

        money = Math.round(money);
        message = message.replace("%nick%", player.getName())
            .replace("%money%", String.valueOf(money)).replace("%place%", String.valueOf(place));

        if(place != 1)
            notification(message);

        if(player != null)
            player.sendMessage(message);

        if(money > 0.0)
            PlayerConfig.get(player.getName()).addMoney((int) money);
    }

    private void notification(String message)
    {
        players.forEach(showdownPlayer -> Bukkit.getPlayer(showdownPlayer.getNick()).sendMessage(message));
    }

    public void startCount()
    {
        taskId = Bukkit.getScheduler().runTaskAsynchronously(ShowdownPlugin.getInstance(), () ->
        {
            int timeLeft = 15; //300
            while(timeLeft > 0)
            {
                if(timeLeft > 15 && getPlayers().size() == 10)
                    timeLeft = 15;

                if(timeLeft == 300 || timeLeft == 30 || timeLeft == 15 || timeLeft < 6)
                {
                    if (timeLeft % 60 == 0)
                        notification(ShowdownMessage.GAME_START_MINUTES.toString().replace("%time%", String.valueOf(timeLeft / 60)));
                    else if (timeLeft <= 30)
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
        Bukkit.getScheduler().runTask(ShowdownPlugin.getInstance(), () ->
        {
            for (int index = 0; index < playersOnStart; index++)
            {
                ShowdownPlayer showdownPlayer = players.get(index);
                Player player = Bukkit.getPlayer(showdownPlayer.getNick());
                player.teleport(spawnPoints.getSpawnPoints().get(index));
                ShowdownManager.prepareForBattle(player);
                showdownPlayer.getKit().give(player);
                player.sendTitle(ShowdownMessage.GAME_START_TITLE.toString(), ShowdownMessage.GAME_START_SUBTITLE.toString(),
                        10, 40, 10);
            }
        });

        try { Thread.sleep(300000); }
        catch(Exception exception) {}

        finish();
    }

    public void finish()
    {
        Bukkit.getScheduler().runTask(ShowdownPlugin.getInstance(), () ->
        {
            if(getStatus() == ShowdownStatus.NOT_STARTED)
                return;

            if(getStatus() == ShowdownStatus.STARTED)
            {
                List<ShowdownPlayer> players = new ArrayList<>(this.players);
                players.forEach(player -> removePlayer(player.getNick(), true));
            }
            else if(getStatus() == ShowdownStatus.WAITING)
                notification(ShowdownMessage.NOT_ENOUGH_PLAYERS.toString());

            reset();
        });
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
