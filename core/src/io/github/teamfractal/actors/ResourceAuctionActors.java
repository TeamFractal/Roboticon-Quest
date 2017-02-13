package io.github.teamfractal.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.screens.ResourceMarketScreen;

public class ResourceAuctionActors extends Table {
	private RoboticonQuest game;
	private Label auctionTitle;
	private Label bidTitle;
	private Label putUpItemTitle;
	private Label bidAmountPounds;
	private TextButton placeBid;
	private SelectBox<String> itemsUpForBidding;
	private TextField bidAmount;
	
	private SelectBox<String> auctionableItems;
	private TextField auctionItemAmount;
	private TextButton auctionItemButton;
	
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
		itemsUpForBidding = new SelectBox<String>(skin);
		bidAmount = new TextField("0", skin);
		bidAmountPounds = new Label("£", skin);
		auctionableItems = new SelectBox<String>(skin);
		auctionItemAmount = new TextField("0", skin);
		auctionItemButton = new TextButton("Auction Item", skin);
		
		String[] testItems = {"Test", "Test2", "365"};
		itemsUpForBidding.setItems(game.auction.getAuctionItemsDisplayStrings());
	
		auctionableItems.setItems(testItems);
		

		// Adjust properties.
		auctionTitle.setAlignment(Align.left);
		bidTitle.setAlignment(Align.left);
		putUpItemTitle.setAlignment(Align.left);
		
		float auctionSectionWidth = Gdx.graphics.getWidth() / 4;
		
		// Add UI components to screen.
		add(auctionTitle);
		rowWithHeight(20);

		// Setup UI Layout.
		add(bidTitle);
		rowWithHeight(10);
		add(itemsUpForBidding);
		add().spaceRight(20);
		add(bidAmountPounds);
		add(bidAmount).width(40);
		add().spaceRight(10);
		add(placeBid);
		
		rowWithHeight(20);
		
		add(putUpItemTitle);
		rowWithHeight(10);
		add(auctionableItems);
		add().spaceRight(20);
		add();
		add(auctionItemAmount).width(40);
		add().spaceRight(20);
		add(auctionItemButton);

		debugAll();
		pad(20);
		
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