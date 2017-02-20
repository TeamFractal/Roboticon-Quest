package io.github.teamfractal.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidResourceTypeException;
import io.github.teamfractal.exception.NotCommonResourceException;
import io.github.teamfractal.util.PlotManager;

public class LandPlot {
	private String name;
	private TiledMapTileLayer.Cell mapTile;
	private TiledMapTileLayer.Cell playerTile;
	private TiledMapTileLayer.Cell roboticonTile;
	private Player owner;
	
	private final int ROBOTICON_BASE_YIELD_INCREASE_PERCENTAGE = 50;
	private final int ROBOTICON_SPECIALISED_YIELD_INCREASE_PERCENTAGE = 100;
	
	int x, y;


	//<editor-fold desc="Class getters">
	public TiledMapTileLayer.Cell getMapTile() {
		return mapTile;
	}

	public TiledMapTileLayer.Cell getPlayerTile() {
		return playerTile;
	}

	public TiledMapTileLayer.Cell getRoboticonTile() {
		return roboticonTile;
	}

	public Player getOwner() {
		return owner;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


	public boolean setOwner(Player player) {
		if (hasOwner()) {
			return false;
		}

		owner = player;
		player.addLandPlot(this);
		return true;
	}

	public boolean hasOwner() {
		return getOwner() != null;
	}

	public void removeOwner() {
		if (!hasOwner())
			return ;

		owner.removeLandPlot(this);
	}
	
	//</editor-fold>

	private final int IndexOre = 0;
	private final int IndexEnergy = 1;
	private final int IndexFood = 2;

	/**
	 * Saved modifiers for LandPlot.
	 * [ Ore, Energy, Food ]
	 */
	int[] productionModifiers = {0, 0, 0};

	/**
	 * The base production amounts.
	 * [ Ore, Energy, Food ]
	 */
	private int[] productionAmounts;
	private boolean owned;
	private Roboticon installedRoboticon;
	private boolean hasRoboticon;

	/**
	 * Initialise LandPlot with specific base amount of resources.
	 *
	 * @param ore     Amount of ore
	 * @param energy  Amount of energy
	 * @param food    Amount of food
	 */
	public LandPlot(int ore, int energy, int food) {
		this.productionAmounts = new int[]{ore, energy, food};
		this.owned = false;
	}

	public void setupTile (PlotManager plotManager, int x, int y, String tileName) {
		this.x = x;
		this.y = y;
		this.mapTile = plotManager.getMapLayer().getCell(x, y);
		this.playerTile = plotManager.getPlayerOverlay().getCell(x, y);
		this.roboticonTile = plotManager.getRoboticonOverlay().getCell(x, y);
		this.name = tileName;
	}

	/**
	 * Get the type index from the {@link ResourceType}
	 * @param resource   The {@link ResourceType}
	 * @return           The index.
	 * @throws InvalidResourceTypeException Exception is thrown if the resource index is invalid.
	 */
	private int resourceTypeToIndex(ResourceType resource) {
		switch (resource) {
			case ORE:    return IndexOre;
			case FOOD:   return IndexFood;
			case ENERGY: return IndexEnergy;
		}

		throw new NotCommonResourceException(resource);
	}

	/**
	 * Install a roboticon to this LandPlot.
	 *
	 * @param roboticon    The roboticon to be installed.
	 */
	public synchronized boolean installRoboticon(Roboticon roboticon) {
		// Check if supplied roboticon is already installed.
		if (roboticon.isInstalled()) {
			return false;
		}
		
		productionModifiers[0] += ROBOTICON_BASE_YIELD_INCREASE_PERCENTAGE;
		productionModifiers[1] += ROBOTICON_BASE_YIELD_INCREASE_PERCENTAGE;
		productionModifiers[2] += ROBOTICON_BASE_YIELD_INCREASE_PERCENTAGE;

		if (roboticon.getCustomisation() != ResourceType.Unknown){
			int index = resourceTypeToIndex(roboticon.getCustomisation());
			if (roboticon.setInstalledLandplot(this)) {
				productionModifiers[index] += ROBOTICON_SPECIALISED_YIELD_INCREASE_PERCENTAGE;
				this.installedRoboticon = roboticon;
				return true;
			}
		}
		else{
			if (roboticon.setInstalledLandplot(this)) {
				this.installedRoboticon = roboticon;
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculate the amount of resources to be produced for specific resource.
	 * @param resource  The resource type to be calculated.
	 * @return          Calculated amount of resource to be generated.
	 */
	public int produceResource(ResourceType resource) {
			int resIndex = resourceTypeToIndex(resource);
			int productionAmount = productionAmounts[resIndex];

			return productionAmount + (int)((float)productionAmount * ((float)productionModifiers[resIndex] / 100));
	}

	public int getResource(ResourceType resource) {
		int resIndex = resourceTypeToIndex(resource);
		return productionAmounts[resIndex];
	}
	
	public boolean hasRoboticon(){
		return this.hasRoboticon;
	}
	
	public void setHasRoboticon(boolean roboticonInstalled){
		this.hasRoboticon = roboticonInstalled;
	}

	@Override
	public String toString(){
		return name;
	}
}