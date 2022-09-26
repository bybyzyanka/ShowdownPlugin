package space.moonstudio.showdown;

import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShowdownMapSpawnPoints extends ShowdownMapLocations {

    private List<Location> spawnPoints = new ArrayList<>();

    public ShowdownMapSpawnPoints(ShowdownMap map) { super(map); }

    public void setSpawnpoint(int index, Location location)
    {
        index -= 1;
        if(index >= spawnPoints.size())
            spawnPoints.add(location);
        else spawnPoints.set(index, location);

        map.sync();
    }
}
