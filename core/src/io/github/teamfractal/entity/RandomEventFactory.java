package io.github.teamfractal.entity;

import com.badlogic.gdx.utils.Array;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.util.PlotManager;

import java.util.ArrayList;
import java.util.Random;
import static java.lang.Boolean.TRUE;

/**
 * Created by Richard on 16/02/2017.
 */

public class RandomEventFactory {
    public static final int EVENTCHANCE = 100;      // Percentage chance of event occurring each round 0-100
    private Random rand = new Random();

    public RandomEventFactory() {

    }

    public RandomEvent chooseEvent() {
        if (EventTakingPlace(EVENTCHANCE) == Boolean.TRUE) {
            switch (rand.nextInt(3)) {
                case 0:
                    return createTileEvent();
                case 1:
                    return createPlayerEvent();
                case 2:
                    return createRoboticonEvent();
            }
        }
        System.out.println("Creating a NoEvent");
        return new NoEvent();
    }

    public boolean EventTakingPlace(int percentage) {
        if (percentage >= rand.nextInt(101))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    private TileEvent createTileEvent() {
        int i = rand.nextInt(TileEvent.TEMPLATEVALS.length);        // Choose the random event template
        return new TileEvent(TileEvent.TEMPLATEVALS[i][0], TileEvent.TEMPLATEVALS[i][1], TileEvent.TEMPLATEVALS[i][2], TileEvent.TEMPLATESTRINGS[i][0], TileEvent.TEMPLATESTRINGS[i][1]);
    }

    private PlayerEvent createPlayerEvent() {
        int i = rand.nextInt(PlayerEvent.TEMPLATEVALS.length);        // Choose the random event template
        return new PlayerEvent(PlayerEvent.TEMPLATEVALS[i][0], PlayerEvent.TEMPLATEVALS[i][1], PlayerEvent.TEMPLATEVALS[i][2], PlayerEvent.TEMPLATEVALS[i][3], PlayerEvent.TEMPLATEVALS[i][4], PlayerEvent.TEMPLATESTRINGS[i][0], PlayerEvent.TEMPLATESTRINGS[i][1]);
    }
    private RoboticonEvent createRoboticonEvent() {
        int i = rand.nextInt(RoboticonEvent.TEMPLATEVALS.length);        // Choose the random event template
        return new RoboticonEvent(RoboticonEvent.TEMPLATEVALS[i], RoboticonEvent.TEMPLATESTRINGS[i][0], RoboticonEvent.TEMPLATESTRINGS[i][1]);      // Decide which resource should be customised
    }
/*
    private MarketEvent createMarketEvent() {
        int i = rand.nextInt(MarketEvent.TEMPLATEVALS.length);        // Choose the random event template
        return new MarketEvent(MarketEvent.TEMPLATEVALS[i][0], MarketEvent.TEMPLATEVALS[i][1], MarketEvent.TEMPLATEVALS[i][2], MarketEvent.TEMPLATESTRINGS[i][0], MarketEvent.TEMPLATESTRINGS[i][1]);      return new MarketEvent();           // TODO
    }
*/

}

class NoEvent implements RandomEvent {
    public void activate(Player player) {
        // Do nothing
    }
    public String getEventName() {
        return "No event";
    }
}

class TileEvent implements RandomEvent {
    // Base Class for random events changing values in specific Tile objects
    public String eventName;
    public String description;
    private int[] tileModifiers = new int[3];
    public static int[][] TEMPLATEVALS =
            {
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1},
                    {1, 1, 1},
                    {2, 2, 2},
                    {5, 5, 5}
            };
    public static String[][] TEMPLATESTRINGS =
            {
                    {"Meteor Shower", "Meteors have landed on one of your tiles. It now will produce more Ore when mined."},
                    {"Solar Flare", "A Solar Flare is affecting one of your tiles. It will now produce more Energy."},
                    {"Enriched Soil", "The soil in one of your tiles has been enriched; it will now produce more Food."},
                    {"Land of interest", "Our Scientists underestimated a tile in your colony; its production in all 3 resources has increased by 1."},
                    {"Valuable Land", "Our Scientists underestimated a tile in your colony; its production in all 3 resources has increased by 2."},
                    {"Ancient Civilisation", "The remains of an advanced civilisation has been discovered on one of your tiles. Its production values have dramatically increased from their knowledge."}
            };

    public TileEvent(int ore, int energy, int food, String eventName, String description) {
        this.tileModifiers[0] = ore;
        this.tileModifiers[1] = energy;
        this.tileModifiers[2] = food;
        this.eventName = eventName;
        this.description = description;
    }
    public void activate(Player player) {
        ArrayList<LandPlot> playerLand = player.getLand();
        if (playerLand.size() <= 0) {
            System.out.println("Error in activation: no landplots in list");
            return;     // Player has no tiles to modify
        }
        Random rand = new Random();
        LandPlot tile = playerLand.get(rand.nextInt(playerLand.size()));
        tile.productionModifiers[0] = tile.productionModifiers[0] + this.tileModifiers[0];      // Update the modifiers of the tile
        tile.productionModifiers[1] = tile.productionModifiers[1] + this.tileModifiers[1];
        tile.productionModifiers[2] = tile.productionModifiers[2] + this.tileModifiers[2];
    }

    public String getEventName() {
        return eventName + description;
    }

}


class RoboticonEvent implements RandomEvent {
    // Base Class for random events changing values in Roboticon objects
    public String eventName;
    public String description;
    private ResourceType customisation;
    public static ResourceType[] TEMPLATEVALS =
            {
                    ResourceType.ORE,
                    ResourceType.ENERGY,
                    ResourceType.FOOD
            };
    public static String[][] TEMPLATESTRINGS =
            {
                    {"Diamond Drill", "Diamond-plated drill technology has been salvaged from scrap, and one of your Roboticons have been upgraded. It now produces more Ore."},
                    {"Efficient Energy Core", "One of your Roboticons has been upgraded with an efficient energy core. It now produces more Energy."},
                    {"Advanced Harvesting", "One of your Roboticons has self-learnt more advanced harvesting techniques. It now produces more Food."},
            };

    public RoboticonEvent(ResourceType customisation, String eventName, String description) {
        this.customisation = customisation;
        this.eventName = eventName;
        this.description = description;
    }

    public void activate(Player player) {
            ArrayList<Roboticon> playerRoboticons = player.getRoboticons();
            if (playerRoboticons.size() <= 0) {
                System.out.println("Error in activation: no roboticons in list");
                return;     // No roboticons to customise
            }
            Random rand = new Random();
            int i = 0;
            Roboticon roboticon;
            do {
                roboticon = playerRoboticons.get(i);
                ++i;
            } while (roboticon.getCustomisation() == ResourceType.Unknown && i < playerRoboticons.size());
            if (i <= playerRoboticons.size()) {
                roboticon.setCustomisation(customisation);
            }
    }
    public String getEventName() {
        return eventName + description;
    }
}



class PlayerEvent implements RandomEvent {
    // Base Class for random events changing values in specific Player objects
    public String eventName;
    public String description;
    private int[] tileModifiers = new int[5];
    private int addRoboticon;
    private int addLand;
    private PlotManager plotManger;
    private LandPlot plot;
    private Random rand;
    public static int[][] TEMPLATEVALS =
            {
                    {20, 0, 0, 0, 0},
                    {0, 20, 0, 0, 0},
                    {0, 0, 20, 0, 0},
                    {0, 0, 0, 1, 0},
                    {100, 100, 100, 0, 0}
            };
    public static String[][] TEMPLATESTRINGS =
            {
                    {"Ore Deposits", "Our Roboticons have been mining large Ore deposits, and we have produced an extra 20 Ore as a result."},
                    {"Electrical Surge", "Good weather has meant our solar arrays have been functioning above average, and have produced an extra 20 Energy."},
                    {"Bountiful Harvest", "The recent harvest has been exceptional. We have produced a bonus 20 Food."},
                    {"Scrapheap Challenge", "A Roboticon has been assembled from salvaged scrap components, it has been added to your inventory."},
                    {"Alien Gift", "We found a small Alien colony on this planet. They have started worshipping us as deities, and gifted us all of their possessions."}
            };

    public PlayerEvent(int ore, int energy, int food, int addRoboticon, int addLand, String eventName, String description) {
        this.tileModifiers[0] = ore;
        this.tileModifiers[1] = energy;
        this.tileModifiers[2] = food;
        this.eventName = eventName;
        this.description = description;
        this.addRoboticon = addRoboticon;
        this.addLand = addLand;
        this.rand = new Random();
    }
    public void activate(Player player) {
            player.setOre(player.getOre() + tileModifiers[0]);        // Add/Subtract resources from inventory
            player.setEnergy(player.getEnergy() + tileModifiers[1]);
            player.setFood(player.getFood() + tileModifiers[2]);
            if (addRoboticon == 1) {
                player.roboticonList.add(new Roboticon(rand.nextInt(10000)));   // Add a new roboticon if event specifies
            }
    }
    public String getEventName() {
        return eventName + description;
    }
}
