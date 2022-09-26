package space.moonstudio.showdown;

import lombok.Getter;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Location;

@Getter
public class ShowdownMapBorder extends ShowdownMapLocations {

    private Location firstPosition, secondPosition, center;

    public ShowdownMapBorder(ShowdownMap map) { super(map); }

    public boolean isInBorder(Location origin)
    {
        return new IntRange(firstPosition.getX(), secondPosition.getX()).containsDouble(origin.getX()) &&
            new IntRange(firstPosition.getY(), secondPosition.getY()).containsDouble(origin.getY()) &&
            new IntRange(firstPosition.getZ(), secondPosition.getZ()).containsDouble(origin.getZ());
    }

    public void setPosition(int index, Location location)
    {
        if(index == 1)
            firstPosition = location;
        else secondPosition = location;

        if(firstPosition != null && secondPosition != null)
            center = new Location(firstPosition.getWorld(),
                    (firstPosition.getX() + secondPosition.getX()) / 2,
                    (firstPosition.getY() + secondPosition.getY()) / 2,
                    (firstPosition.getZ() + secondPosition.getZ()) / 2);

        map.sync();
    }
}
