package io.github.teamfractal.entity;

import com.badlogic.gdx.utils.Array;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.util.PlotManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Richard on 16/02/2017.
 */

public interface RandomEvent {
    public void activate();
}

class TileValModify implements RandomEvent {
    // Base Class for random events changing values in specific Tile objects
    public String eventName;
    public String description;
    private int[] tileModifiers = new int[5];

    public void TileValModify(int ore, int energy, int food, String eventName, String description) {
        this.tileModifiers[0] = ore;
        this.tileModifiers[1] = energy;
        this.tileModifiers[2] = food;
        this.eventName = eventName;
        this.description = description;
    }
    public void activate(ArrayList<Player> playerList) {
        for (int i = 0; i < playerList.size(); ++i) {
            Random rand = new Random();
            ArrayList<LandPlot> playerLand = playerList.get(i).getLand()
            LandPlot tile = playerLand.get(rand.nextInt(playerLand.size()));
            tile.productionModifiers[0] = tile.productionModifiers[0] + this.tileModifiers[0];      // Update the modifiers of the tile
            tile.productionModifiers[1] = tile.productionModifiers[1] + this.tileModifiers[1];
            tile.productionModifiers[2] = tile.productionModifiers[2] + this.tileModifiers[2];
            }
        }
    }
}

class RoboticonModify implements RandomEvent {
    // Base Class for random events changing values in specific Tile objects
    public String eventName;
    public String description;
    private int[] tileModifiers = new int[5];

    public void RoboticonModify(int customisation, String eventName, String description) {
        this.tileModifiers[4] = customisation;
        this.eventName = eventName;
        this.description = description;
    }
    public void activate(ArrayList<Player> playerList) {
        for (int i = 0; i < playerList.size(); ++i) {
            Random rand = new Random();
            int counter = 0;
            Roboticon roboticon;
            Array<Roboticon> playerRoboticons;
            do {
                playerRoboticons = playerList.get(i).getRoboticons();
                roboticon = playerRoboticons.get(rand.nextInt(playerRoboticons.size));
                ++counter;
            } while (roboticon.getCustomisation() == ResourceType.Unknown &&
                    counter < playerRoboticons.size);
            if (counter != playerRoboticons.size) {
                int resource = rand.nextInt(3);
                switch (resource) {
                    case 0:
                        roboticon.setCustomisation(ResourceType.ORE);
                        break;
                    case 1:
                        roboticon.setCustomisation(ResourceType.ENERGY);
                        break;
                    case 2:
                        roboticon.setCustomisation(ResourceType.FOOD);
                        break;
                };
            }
        }
    }
}
}


class PlayerEvent implements RandomEvent {
    // Base Class for random events changing values in specific Player objects
    public void activate(RoboticonQuest game) {

    }
}

class MarketEvent implements RandomEvent {
    // Base Class for random events changing values in the Market objects
    public void activate(RoboticonQuest game){

    }
}

class WorldEvent implements RandomEvent {
    // Base Class for random events changing values in the Game manager object
    public void activate(RoboticonQuest game){

    }
}

public class RandomEventFactory {

    public static final int EVENTCHANCE = 50;      // Percentage chance of event occurring each round 0-100

    public static RandomEvent chooseEvent() {
        if (EventTakingPlace(EVENTCHANCE) == True) {
            Random rand = new Random();
            switch (rand.nextInt(5)) {
                case 0:
                    return new TileEvent();
                    break;
                case 1:
                    return new PlayerEvent();
                    break;
                case 2:
                    return new MarketEvent();
                    break;
                case 3:
                    return new WorldEvent();
                    break;
            }
        }
    }

    public static boolean EventTakingPlace(int percentage) {
        Random rand = new Random();
        if (percentage >= rand.nextInt(101))
            return True;
        else
            return False;
    }


}

