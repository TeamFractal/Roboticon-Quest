package io.github.teamfractal.util;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.LandPlot;

public class PlotManager {
	private LandPlot[][] plots;
	private TiledMapTileSets tiles;
	private TiledMapTileLayer mapLayer;
	private TiledMapTileLayer playerOverlay;
	private TiledMapTileLayer roboticonOverlay;
	private int width;
	private int height;

	private final int PLAINS_TILE_INDEX  = 2;	//Defined in assets/tiles/city.tmx
	private final int HILLS_TILE_1_INDEX = 4;
	private final int HILLS_TILE_2_INDEX = 5;
	private final int HILLS_TILE_3_INDEX = 6;
	private final int HILLS_TILE_4_INDEX = 7;
	private final int WATER_TILE_INDEX   = 10;
	private final int CITY_TILE_INDEX    = 61;
	private final int CS_TILE_INDEX      = 64;
	private final int LMB_TILE_INDEX     = 65;
	private final int RON_COOKE_INDEX    = 66;
	
	/**
	 * Set up the plot manager.
	 * @param tiles    Tiles.
	 * @param layers   Layers.
	 */
	public void setup(TiledMapTileSets tiles, MapLayers layers) {
		this.tiles = tiles;
		this.mapLayer = (TiledMapTileLayer)layers.get("MapData");
		this.playerOverlay = (TiledMapTileLayer)layers.get("PlayerOverlay");
		this.roboticonOverlay = (TiledMapTileLayer)layers.get("RoboticonOverlay");

		width = mapLayer.getWidth();
		height = mapLayer.getHeight();
		plots = new LandPlot[width][height];

		// This fills the plots array with empty plots so that when we check for unowned plots there is something to
		// check against.
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				plots[x][y] = createLandPlot(x, y);
			}
		}
	}

	/**
	 * Get {@link LandPlot} at specific position.
	 * @param x   The x index.
	 * @param y   The y index.
	 * @return    The corresponding {@link LandPlot} object.
	 */
	public LandPlot getPlot(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;

		return plots[x][y];
	}

	public int getNumUnownedTiles() {
		int numUnownedTiles = 0;

		for (LandPlot[] plotColumns : plots) {
			for(LandPlot plot : plotColumns){
				if(plot != null)
				{
					if(!plot.hasOwner()){
						numUnownedTiles ++;
					}
				}
			}
		}

		return numUnownedTiles;
	}
	
	/**
	 * Creates a landplot from a tile tile on the tiled map
	 * @param x - x coordinate on tiled map
	 * @param y - y coordinate on tiled map
	 * @return new Landplot with statistics determined by tile on tiled map
	 */
	private LandPlot createLandPlot(int x, int y) {
		int ore, energy, food;
		TiledMapTile tile = mapLayer.getCell(x, y).getTile();
		String name;

		if (tile == tiles.getTile(CITY_TILE_INDEX)){
			ore = 1;
			energy = 2;
			food = 3;
			name = "City";
		}
		else if (tile == tiles.getTile(WATER_TILE_INDEX)){
			ore = 3;
			energy = 1;
			food = 2;
			name = "River";
		}
		else if (tile == tiles.getTile(HILLS_TILE_1_INDEX) 
				|| tile == tiles.getTile(HILLS_TILE_2_INDEX)
				|| tile == tiles.getTile(HILLS_TILE_3_INDEX)
				|| tile == tiles.getTile(HILLS_TILE_4_INDEX)){
			ore = 3;
			energy = 2;
			food = 1;
			name = "Hills";
		}
		else if (tile == tiles.getTile(CS_TILE_INDEX)){
			ore = 5;
			energy = 4;
			food = 0;
			name = "CS Department";
		}
		else if (tile == tiles.getTile(LMB_TILE_INDEX)){
			ore = 3;
			energy = 3;
			food = 3;
			name = "Law and Management";
		}
		else if (tile == tiles.getTile(RON_COOKE_INDEX)){
			ore = 0;
			energy = 2;
			food = 7;
			name = "Ron Cooke Hub";
		}
		else if (tile == tiles.getTile(PLAINS_TILE_INDEX)){
			ore = 2;
			energy = 2;
			food = 2;
			name = "Grassland";
		}
		else{
			ore = 1;
			energy = 1;
			food = 0;
			name = "Road";
		}

		LandPlot p = new LandPlot(ore, energy, food);
		p.setupTile(this, x, y, name);
		plots[x][y] = p;
		return p;
	}

	public TiledMapTileLayer getMapLayer() {
		return mapLayer;
	}

	public TiledMapTileLayer getPlayerOverlay() {
		return playerOverlay;
	}

	public TiledMapTileLayer getRoboticonOverlay() {
		return roboticonOverlay;
	}
}