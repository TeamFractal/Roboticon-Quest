package io.github.teamfractal.actors;

import java.util.ArrayList;

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

import io.github.teamfractal.Auction;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.screens.ResourceMarketScreen;
import io.github.teamfractal.util.AuctionBid;
import io.github.teamfractal.util.AuctionableItem;

public class ResourceAuctionActors extends Table {
	private Auction auction;
	private RoboticonQuest game;
	private Label auctionTitle;
	private Label bidTitle;
	private Label putUpItemTitle;
	private Label bidAmountPounds;
	private TextButton placeBid;
	private SelectBox<String> itemsUpForBiddingSelectBox;
	private TextField bidAmount;
	
	private SelectBox<String> auctionableItemsSelectBox;
	private TextField auctionItemAmount;
	private TextButton auctionItemButton;

	public ResourceAuctionActors(final RoboticonQuest game, ResourceMarketScreen screen) {
		center();

		Skin skin = game.skin;
		this.game = game;
		Stage stage = screen.getStage();
		auction = game.auction;
		
		// Create UI Components
		auctionTitle = new Label("Auction: ", skin);
		bidTitle = new Label("Place a bid: ", skin);
		putUpItemTitle = new Label("Put an item up for Auction:", skin);
		placeBid = new TextButton("Place Bid", skin);
		itemsUpForBiddingSelectBox = new SelectBox<String>(skin);
		bidAmount = new TextField("0", skin);
		bidAmountPounds = new Label("£", skin);
		auctionableItemsSelectBox = new SelectBox<String>(skin);
		auctionItemAmount = new TextField("0", skin);
		auctionItemButton = new TextButton("Auction Item", skin);
		
		itemsUpForBiddingSelectBox.setItems(auction.getAuctionItemsDisplayStrings());
		auctionableItemsSelectBox.setItems(getCurrentPlayerAuctionableItemStrings());

		// Adjust properties.
		auctionTitle.setAlignment(Align.left);
		bidTitle.setAlignment(Align.left);
		putUpItemTitle.setAlignment(Align.left); 
		
		// Add UI components to screen.
		add(auctionTitle);
		rowWithHeight(40);

		// Setup UI Layout.
		add(bidTitle);
		rowWithHeight(10);
		add(itemsUpForBiddingSelectBox);
		add().spaceRight(20);
		add(bidAmountPounds);
		add(bidAmount).width(40);
		add().spaceRight(10);
		add(placeBid);
		
		rowWithHeight(30);
		
		add(putUpItemTitle);
		rowWithHeight(10);
		add(auctionableItemsSelectBox);
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
		auctionItemButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Object objectToAuction = auction.getPlayerAuctionableItems(
						game.getPlayer())[auctionableItemsSelectBox.getSelectedIndex()];
				AuctionableItem itemToAuction = new AuctionableItem(objectToAuction,
						Integer.parseInt(auctionItemAmount.getText()), game.getPlayer());
				auction.addItemToAuction(itemToAuction);
			}
		});
		
		placeBid.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AuctionBid bid = new AuctionBid(Integer.parseInt(bidAmount.getText()), game.getPlayer());
				auction.getAuctionItemAtIndex(itemsUpForBiddingSelectBox.getSelectedIndex()).placeBid(bid);
			}
		});
	}

	private String[] getCurrentPlayerAuctionableItemStrings() {
		Object[] auctionableObjects = auction.getPlayerAuctionableItems(game.getPlayer());
		String[] strings = new String[auctionableObjects.length];
		
		for (int i = 0; i < strings.length; i++) {
			strings[i] = auctionableObjects[i].toString();
		}
		
		return strings;
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