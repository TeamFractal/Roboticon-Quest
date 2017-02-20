package io.github.teamfractal;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.teamfractal.animation.AnimationPhaseTimeout;
import io.github.teamfractal.animation.AnimationShowPlayer;
import io.github.teamfractal.animation.IAnimationFinish;
import io.github.teamfractal.screens.*;
import io.github.teamfractal.entity.Market;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.util.PlotManager;

/**
 * This is the main game boot up class.
 * It will set up all the necessary classes.
 */
public class RoboticonQuest extends Game {
	static RoboticonQuest _instance;
	public static RoboticonQuest getInstance() {
		return _instance;
	}

	private PlotManager plotManager;
	SpriteBatch batch;
	public Skin skin;
	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	public ScoreScreen scoreScreen;
	public GameCreateScreen gameCreateScreen;
	private int phase;
	private int currentPlayer;
	public ArrayList<Player> playerList;
	public Market market;
	private int landBoughtThisTurn;
	
	public Auction auction;

	public int getPlayerIndex (Player player) {
		return playerList.indexOf(player);
	}

	public TiledMap tmx;
	
	public RoboticonQuest(){
		_instance = this;
		reset();
	}
	
	@Override
	public void create () {
		this.playerList = new ArrayList<Player>();
		playerList.add(new Player(this, "DefaultPlayer1"));
		playerList.add(new Player(this, "DefaultPlayer2"));

		batch = new SpriteBatch();
		setupSkin();	
		gameScreen = new GameScreen(this);
		gameCreateScreen = new GameCreateScreen(this);

		// Setup other screens.
		mainMenuScreen = new MainMenuScreen(this);
		auction = new Auction();
		
		setScreen(mainMenuScreen);
	}

	public Batch getBatch() {
		return batch;
	}

	/**
	 * Setup the default skin for GUI components.
	 */
	private void setupSkin() {
		skin = new Skin(
			Gdx.files.internal("skin/skin.json"),
			new TextureAtlas(Gdx.files.internal("skin/skin.atlas"))
		);
	}

	/**
	 * Clean up
	 */
	@Override
	public void dispose () {
		mainMenuScreen.dispose();
		gameScreen.dispose();
		skin.dispose();
		batch.dispose();
	}
	
	public int getPhase(){
		return this.phase;
	}

	public void reset() {
		this.currentPlayer = 0;
		this.phase = 0;
		this.currentPlayer = 0;
		this.market = new Market();
		plotManager = new PlotManager();
	}

	public void nextPhase () {
		phase++;

		switch (phase) {
			// Phase 2: Purchase Roboticon
			case 2:
				RoboticonMarketScreen roboticonMarket = new RoboticonMarketScreen(this);
				roboticonMarket.addAnimation(new AnimationPhaseTimeout(getPlayer(), this, phase, 30));
				setScreen(roboticonMarket);
				break;

			// Phase 3: Roboticon Customisation
			case 3:
				AnimationPhaseTimeout timeoutAnimation = new AnimationPhaseTimeout(getPlayer(), this, phase, 30);
				gameScreen.addAnimation(timeoutAnimation);
				timeoutAnimation.setAnimationFinish(new IAnimationFinish() {
					@Override
					public void OnAnimationFinish() {
						gameScreen.getActors().hideInstallRoboticon();
					}
				});
				gameScreen.getActors().updateRoboticonSelection();
				setScreen(gameScreen);
				break;

			// Phase 4: Purchase Resource
			case 4:
				generateResources();
				break;

			// Phase 5: Generate resource for player.
			case 5:
				setScreen(new MarketScreen(this));
				// Restore the next buton on the game screen now that it isn't the current screen.
				gameScreen.showNextButton();
				break;

			// End phase - Clean up and move to next player.
			case 6:
				this.nextPlayer();
				break;

			// Phase 1: Enable purchase of a LandPlot
			case 1:
				setScreen(gameScreen);
				gameScreen.addAnimation(new AnimationShowPlayer(getPlayer().getName()));
				break;
		}

		if (gameScreen != null){
			gameScreen.getActors().textUpdate();
		}
	}

	/**
	 * Phase 4: generate resources.
	 */
	private void generateResources() {
		// Switch back to purchase to game screen.
		setScreen(gameScreen);
		gameScreen.hideNextButton();

		// Generate resources.
		Player p = getPlayer();
		p.generateResources();
	}

	public String getPhaseString () {
		int phase = getPhase();

		switch(phase){
			case 1:
				return "Buy Land Plot";

			case 2:
				return "Purchase Roboticons";

			case 3:
				return "Install Roboticons";

			case 4:
				return "Resource Generation";

			case 5:
				return "Resource Auction";

			default:
				return "Unknown phase";
		}

	}

	public Player getPlayer(){
		return this.playerList.get(this.currentPlayer);
	}
	
	public int getPlayerInt(){
		return this.currentPlayer;
	}
	
	public void nextPlayer(){
		if (this.currentPlayer == playerList.size() - 1){
			if(isGameEnded()){
				scoreScreen = new ScoreScreen(this);
				setScreen(scoreScreen);
				phase=7;
			}
			else {
				//Close auction bids after every player has had the option to bid
				//or put items up for the next auction phase
				auction.closeBidding();
				this.currentPlayer = 0;
				phase=0;
				nextPhase();
				market.produceRoboticons();
			}
		}
		else{
			this.currentPlayer ++;
			phase=0;
			nextPhase();
		}
	}
	
	private boolean isGameEnded() {
		return plotManager.getNumUnownedTiles() == 0;
	}

	public Player getWinningPlayer() {
		int highestScore = Integer.MIN_VALUE;
		Player winningPlayer = null;
		
		for (Player player : playerList) {
			if(player.getScore() > highestScore){
				winningPlayer = player;
				highestScore = player.getScore();
			}
		}
		return winningPlayer;
	}

	public void SetupPlayers(ArrayList<String> playerNames){
		playerList.clear();
		for(int i = 0; i < playerNames.size(); i++) {
			playerList.add(new Player(this, playerNames.get(i)));
		}
	}

	public PlotManager getPlotManager() {
		return plotManager;
	}
}
