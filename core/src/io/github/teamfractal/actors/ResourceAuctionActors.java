package io.github.teamfractal.actors;

import com.badlogic.gdx.Gdx;
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
	private Label auctionTitle;
	private Label bidTitle;
	private Label putUpItemTitle;
	private TextButton placeBid;
	
	public ResourceAuctionActors(final RoboticonQuest game, ResourceMarketScreen screen) {
		center();

		Skin skin = game.skin;
		this.game = game;
		Stage stage = screen.getStage();
		
		// Create UI Components
		auctionTitle = new Label("Auction: ", skin);
		bidTitle = new Label("Place a bid: ", skin);
		putUpItemTitle = new Label("Put an item up for Auction:", skin);
		placeBid = new TextButton("Place Bid", skin);

		// Adjust properties.
		auctionTitle.setAlignment(Align.left);
		bidTitle.setAlignment(Align.left);
		putUpItemTitle.setAlignment(Align.left);
		
		// Add UI components to screen.
		add(auctionTitle).width(Gdx.graphics.getWidth() / 2);
		rowWithHeight(20);

		// Setup UI Layout.
		add(bidTitle).width(Gdx.graphics.getWidth() / 2);
		add().spaceRight(20);
		add(placeBid);
		rowWithHeight(200);
		
		add(putUpItemTitle).width(Gdx.graphics.getWidth() / 2);

		debugAll();
		pad(50);
		
		bindEvents();
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
}