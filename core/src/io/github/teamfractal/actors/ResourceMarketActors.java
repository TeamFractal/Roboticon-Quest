package io.github.teamfractal.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.screens.MarketScreen;
import io.github.teamfractal.screens.ResourceMarketScreen;

public class ResourceMarketActors extends Table {
	private final AdjustableActor oreBuy;
	private final AdjustableActor oreSell;
	private final AdjustableActor energyBuy;
	private final AdjustableActor energySell;
	private final AdjustableActor foodBuy;
	private final AdjustableActor foodSell;
	private final TextButton returnButton;
	private RoboticonQuest game;
	private Integer buyOreAmount;
	private Integer sellOreAmount;
	private Integer buyEnergyAmount;
	private Label phaseInfo;
	private Label playerStats;
	private ResourceMarketScreen screen;
	private TextButton nextButton;
	private Label marketStats;
	private Integer sellEnergyAmount;

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
				+ " Credits";
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

	/**
	 * Generate an adjustable actor for sell/buy.
	 *
	 * @param resource   The resource type.
	 * @param bIsSell    <code>true</code> if is for sell,
	 *                   or <code>false</code> if is for buy in.
	 * @return           The adjustable actor generated.
	 */
	private AdjustableActor createAdjustable(final ResourceType resource, final boolean bIsSell) {
		final Player player = game.getPlayer();
		final AdjustableActor adjustableActor = new AdjustableActor(game.skin, getPriceString(resource, bIsSell),
				(bIsSell ? "Sell" : "Buy") + " " + resource.toString());
		updateAdjustable(adjustableActor, resource, bIsSell);
		adjustableActor.setActionEvent(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (bIsSell) {
					// Sell from player to market.
					player.sellResourceToMarket(adjustableActor.getValue(), game.market, resource);
				} else {
					// Player buy from market.
					player.purchaseResourceFromMarket(adjustableActor.getValue(), game.market, resource);
				}

				ResourceMarketActors.this.widgetUpdate();
			}
		});
		return adjustableActor;
	}

	/**
	 * Initialise market actors.
	 * @param game       The game object.
	 * @param screen     The screen object.
	 */
	public ResourceMarketActors(final RoboticonQuest game, ResourceMarketScreen screen, final MarketScreen marketScreen) {
		center();
		Skin skin = game.skin;
		this.game = game;
		this.screen = screen;
		Stage stage = screen.getStage();

		// Create UI Components
		phaseInfo = new Label("", game.skin);
		nextButton = new TextButton("Next ->", game.skin);

		playerStats = new Label("Your Resources\n\n\n\n\n", game.skin); //Pad out the initial string with new lines as the label width property does not correctly update
		marketStats = new Label("", game.skin);
		Label buyLabel  = new Label("Buy",  skin);
		Label sellLabel = new Label("Sell", skin);

		oreBuy = createAdjustable(ResourceType.ORE, false);
		oreSell = createAdjustable(ResourceType.ORE, true);
		energyBuy = createAdjustable(ResourceType.ENERGY, false);
		energySell = createAdjustable(ResourceType.ENERGY, true);
		foodBuy = createAdjustable(ResourceType.FOOD, false);
		foodSell = createAdjustable(ResourceType.FOOD, true);

		returnButton = new TextButton("Back to the Market Menu", skin);
		returnButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(marketScreen);
			}
		});


		// Adjust properties.
		phaseInfo.setAlignment(Align.right);
		marketStats.setAlignment(Align.right);

		buyLabel.setAlignment(Align.center);
		sellLabel.setAlignment(Align.center);
		
		playerStats.setAlignment(Align.center);
		playerStats.setHeight(90);

		// Add UI components to screen.
		stage.addActor(phaseInfo);
		stage.addActor(nextButton);
		//stage.addActor(playerStats);
		add(playerStats);
		row();

		// Setup UI Layout.
		// Row: Player and Market Stats.

		add(marketStats);
		rowWithHeight(20);

		Table innerTable = new Table();

		{
			innerTable.add(oreBuy).padRight(5);
			innerTable.add(energyBuy);
			innerTable.add(foodBuy).padLeft(5);
			innerTable.row();
			innerTable.add().height(30).colspan(3);
			innerTable.row();
			innerTable.add(oreSell).padRight(5);
			innerTable.add(energySell);
			innerTable.add(foodSell).padLeft(5);
			innerTable.row();
		}

		add(innerTable);
		row();
		add().height(10);
		row();
		add(returnButton);

		pad(20);
		
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
				game.getPlayer().getName() + "; " +
				"Phase " + game.getPhase() + " - " + game.getPhaseString();

		String statText = "Your resources:\n\n" + 
				" Ore: "    + game.getPlayer().getOre()    + "\n" +
				" Energy: " + game.getPlayer().getEnergy() + "\n" +
				" Food: "   + game.getPlayer().getFood()   + "\n" +
				" Money: "  + game.getPlayer().getMoney()  + "\n" ;

		String marketStatText = "Market Resources -    " +
				"Ore: " +    game.market.getResource(ResourceType.ORE   ) + "  " +
				"Energy: " + game.market.getResource(ResourceType.ENERGY) + "  " +
				"Food: " +   game.market.getResource(ResourceType.FOOD  );

		phaseInfo.setText(phaseText);
		playerStats.setText(statText);
		marketStats.setText(marketStatText);

		updateAdjustable(oreBuy, ResourceType.ORE, false);
		updateAdjustable(oreSell, ResourceType.ORE, true);

		updateAdjustable(energyBuy, ResourceType.ENERGY, false);
		updateAdjustable(energySell, ResourceType.ENERGY, true);
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
		
		// Center top
		playerStats.setPosition(width / 2 - width / 8, height - playerStats.getHeight() - 20);
		playerStats.setWidth(width / 4);

		// Bottom Right
		nextButton.setPosition(width - nextButton.getWidth() - 10, 10);

		setWidth(width);
		center();
	}
}
