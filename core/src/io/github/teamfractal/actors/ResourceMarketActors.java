package io.github.teamfractal.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidResourceTypeException;
import io.github.teamfractal.screens.ResourceMarketScreen;
import io.github.teamfractal.util.MessagePopUp;

public class ResourceMarketActors extends Table {

	private RoboticonQuest game;
	private Label phaseInfo;
	private ResourceMarketScreen screen;
	private TextButton nextButton;
	private Label marketStats;
	private final Stage stage;
	private SelectBox<String> resourceDropDown;
	private SelectBox<String> buyOrSellDropDown;
	private SelectBox<String> playerSellToDropDown;
	private SelectBox<String> playerBuyFromDropDown;
	private AdjustableActor priceDropDown;
	private AdjustableActor quantityDropDown;
	private Label[] playerStatsLabels;
	private Integer sellEnergyAmount;
	private final Stage stage;
	/**
	 * Get price in string format
	 *
	 * @param resource   The resource type.
	 * @param bIsSell    <code>true</code> if is for sell,
	 *                   or <code>false</code> if is for buy in.
	 * @return           The formatted string for the resource.
	 */
	private String getPriceString(ResourceType resource, boolean bIsSell) {
		// getBuyPrice: market buy-in price (user sell price)
		// getSellPrice: market sell price (user buy price)
		return resource.toString() + ": "
				+ (bIsSell
					? game.market.getBuyPrice(resource)
					: game.market.getSellPrice(resource))
				+ " Gold";
	}

	/**
	 * Sync. information with the adjustable.
	 * @param adjustableActor     The adjustable to manipulate with.
	 * @param resource            The resource type.
	 * @param bIsSell             <code>true</code> if the adjustable is for sell,
	 *                            <code>false</code> if is for buy.
	 */
	private void updateAdjustable(AdjustableActor adjustableActor, ResourceType resource,
	                              boolean bIsSell) {
		if (bIsSell) {
			adjustableActor.setMax(game.getPlayer().getResource(resource));
		} else {
			adjustableActor.setMax(game.market.getResource(resource));
		}

		adjustableActor.setTitle(getPriceString(resource, bIsSell));
	}


	
	private void createResourceSelectBox(){
		resourceDropDown = new SelectBox<String>(game.skin);
		String[] resources = {"Food","Energy","Ore"};
		resourceDropDown.setItems(resources);
	}
	
	private ResourceType stringToResource(String resourceString){
		if(resourceString.toLowerCase() == "food"){
			return ResourceType.FOOD;
		}
		else if(resourceString.toLowerCase() == "energy"){
			return ResourceType.ENERGY;
		}
		else if(resourceString.toLowerCase() == "ore"){
			return ResourceType.ORE;
		}
		else{ // Shouldn't use this method for other kinds of resource
			throw new InvalidResourceTypeException();
		}
	}
	
	private void completeMarketTransaction(){
		int sellingPlayerIndex = playerBuyFromDropDown.getSelectedIndex();
		int buyingPlayerIndex = playerSellToDropDown.getSelectedIndex();
		int quantity = quantityDropDown.getValue();
		Player sellingPlayer = game.playerList.get(sellingPlayerIndex);
		ResourceType resource = stringToResource(resourceDropDown.getSelected());
		if(sellingPlayerIndex == -1 || resource == null){
			return; // Not enough information has been supplied, cannot complete transaction
		}
		if(buyOrSellDropDown.getSelectedIndex() == 0){ // Buying is the first option
			sellingPlayer.purchaseResourceFromMarket(quantity, game.market, resource);
		}
		else{ // Selling
			sellingPlayer.purchaseResourceFromMarket(quantity, game.market, resource);
		}
		widgetUpdate();
	}
	
	private void createPriceDropDown(){
		priceDropDown = new AdjustableActor(game.skin , 1,1,50,"price", "complete transaction",false);
		priceDropDown.setActionEvent(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				completeMarketTransaction();
				ResourceMarketActors.this.widgetUpdate();
			}
		});
		
	}
	
	private void createPlayerSelectBoxes(){
		playerSellToDropDown = new SelectBox<String>(game.skin);
		playerBuyFromDropDown = new SelectBox<String>(game.skin);
		String[] players = new String[game.playerList.size()];
		for(int player=0;player<game.playerList.size();player++){
			players[player] = "Player "+Integer.toString(player+1);
		}
		playerSellToDropDown.setItems(players);
		playerBuyFromDropDown.setItems(players);
	}
	
	private void createBuyOrSellDropDown(){
		buyOrSellDropDown = new SelectBox<String>(game.skin);
		String[] options = {"buy","sell"};
		buyOrSellDropDown.setItems(options);
	}

	private void createQuantityDropDown(){
		quantityDropDown = new AdjustableActor(game.skin,1,1,100, "quantity", "",false);
	}
	
	private void updatePlayerStatsLabels(){
		for(int playerIndex =0;playerIndex<game.playerList.size();playerIndex++){
			Player player = game.playerList.get(playerIndex);
			String labelText = "Player "+Integer.toString(playerIndex+1)+
					"    Ore: "+player.getResource(ResourceType.ORE)+
					"   Energy: "+player.getResource(ResourceType.ENERGY)+
					"   Food: "+player.getResource(ResourceType.FOOD)+
					"   Money: "+player.getMoney();
			playerStatsLabels[playerIndex].setText(labelText);
		}
	}
	
	private void createPlayerStatsLabels(){
		int numberOfPlayers = game.playerList.size();
		playerStatsLabels = new Label[numberOfPlayers];
		for(int player =0;player<numberOfPlayers;player++){
			playerStatsLabels[player] = new Label("",game.skin);
		}
	}
	
	private void addPlayerStatsLabels(){
		for(int player=0; player<playerStatsLabels.length;player++){
			add(playerStatsLabels[player]).left();
			row();
		}
	}
	
	private void completePlayerToPlayerTransaction(){
		// TODO: fill this out
	}
	
	private void setPlayerToPlayerTransactionButtonBehaviour(TextButton button){
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				completePlayerToPlayerTransaction();
			}
		});
	}
	
	private void addMarketBuySellWidgets(){
		// All placed in a table so treated like 1 single widget for a clean layout
		Table container = new Table();
		container.add(playerBuyFromDropDown).padRight(10);
		container.add(new Label("sell",game.skin)).padRight(10);
		container.add(quantityDropDown).padRight(10);
		container.add(resourceDropDown).padRight(10);
		container.add(new Label("to",game.skin)).padRight(10);
		container.add(playerSellToDropDown).padRight(10);
		container.add(new Label("for",game.skin)).padRight(10);
		container.add(priceDropDown).padRight(10);
		container.add(new Label("money per unit",game.skin)).padRight(10);
		TextButton playerToPlayerTransactionButton = new TextButton(" SELL ", game.skin);
		setPlayerToPlayerTransactionButtonBehaviour(playerToPlayerTransactionButton);
		container.add(playerToPlayerTransactionButton);
		add(container).left();
	}
	
	/**
	 * Initialise market actors.
	 * @param game       The game object.
	 * @param screen     The screen object.
	 */
	public ResourceMarketActors(final RoboticonQuest game, ResourceMarketScreen screen) {
		center();

		Skin skin = game.skin;
		this.game = game;
		this.screen = screen;
		this.stage = screen.getStage();
		
		// Modified by Josh Neil
		createResourceSelectBox();
		createPlayerSelectBoxes();
		createPriceDropDown();
		createQuantityDropDown();
		createBuyOrSellDropDown();
		createPlayerStatsLabels();

		// Create UI Components
		phaseInfo = new Label("", game.skin);
		nextButton = new TextButton("Next ->", game.skin);

		marketStats = new Label("", game.skin);


		// Adjust properties.
		phaseInfo.setAlignment(Align.right);

		// Add UI components to screen.
		stage.addActor(phaseInfo);
		stage.addActor(nextButton);


		// Setup UI Layout.
		// Row: Player and Market Stats.
		addPlayerStatsLabels();
		row();
		add(marketStats).left().padBottom(75);
		row();
		addMarketBuySellWidgets();
		
		bindEvents();
		widgetUpdate();
	}

	/**
	 * Bind button events.
	 */
	private void bindEvents() {
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.nextPhase();
			}
		});
	}

	/**
	 * Add an empty row to current table.
	 * @param height  The height for that empty row.
	 */
	private void rowWithHeight(int height) {
		row();
		add().spaceTop(height);
		row();
	}

	/**
	 * Updates all widgets on screen
	 */
	public void widgetUpdate() {
		// update player stats, phase text, and the market stats.
		String phaseText =
				"Player " + (game.getPlayerInt() + 1) + "; " +
				"Phase " + game.getPhase() + " - " + game.getPhaseString();

		String marketStatText = "Market      "+
				"Ore: " +    game.market.getResource(ResourceType.ORE   ) + "   " +
				"Energy: " + game.market.getResource(ResourceType.ENERGY) + "   " +
				"Food: " +   game.market.getResource(ResourceType.FOOD  );

		phaseInfo.setText(phaseText);
		updatePlayerStatsLabels();
		marketStats.setText(marketStatText);

	}

	/**
	 * Respond to the screen resize event, updates widgets position
	 * accordingly.
	 * @param width    The new width.
	 * @param height   The new Height.
	 */
	public void screenResize(float width, float height) {
		// Bottom Left
		phaseInfo.setPosition(0, height - 20);
		phaseInfo.setWidth(width - 10);

		// Bottom Right
		nextButton.setPosition(width - nextButton.getWidth() - 10, 10);

		setWidth(width);
	}
}