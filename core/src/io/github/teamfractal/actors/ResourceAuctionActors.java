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
import io.github.teamfractal.screens.ResourceMarketScreen;

public class ResourceAuctionActors extends Table {
	private RoboticonQuest game;
	private Label playerStats;
	private Label auctionTitle;
	private Label bidTitle;
	private Label putUpItemTitle;
	
	public ResourceAuctionActors(final RoboticonQuest game, ResourceMarketScreen screen) {
		center();

		Skin skin = game.skin;
		this.game = game;

		// Create UI Components
		auctionTitle = new Label("Auction: ", skin);
		bidTitle = new Label("Place a bid: ", skin);
		putUpItemTitle = new Label("Put an item up for Auction:", skin);
		playerStats = new Label("", skin);

		// Adjust properties.
		auctionTitle.setAlignment(Align.left);
		bidTitle.setAlignment(Align.left);
		putUpItemTitle.setAlignment(Align.left);
		playerStats.setAlignment(Align.left);


		// Add UI components to screen.
		add(auctionTitle);
		rowWithHeight(40);

		// Setup UI Layout.
		add(playerStats);
		rowWithHeight(40);
		add(bidTitle);
		rowWithHeight(200);
		
		add(putUpItemTitle);

		pad(100);
		debugAll();
		bindEvents();
		widgetUpdate();
	}
	
	/**
	 * Bind button events.
	 */
	private void bindEvents() {
		
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

		String statText = "Your resources:\n\n" + 
				"Ore: "    + game.getPlayer().getOre()    + "\n" +
				"Energy: " + game.getPlayer().getEnergy() + "\n" +
				"Food: "   + game.getPlayer().getFood()   + "\n" +
				"Money: "  + game.getPlayer().getMoney()  + "\n" ;

		playerStats.setText(statText);
	}
}