package io.github.teamfractal;

import static org.junit.Assert.*;

import java.util.ArrayList;

import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.util.AuctionBid;
import io.github.teamfractal.util.AuctionableItem;

import org.junit.Before;
import org.junit.Test;

public class AuctionTest {
	private Auction auction;
	private Player player1;
	private Player player2;
	private RoboticonQuest game;
	private AuctionableItem item1;
	private AuctionableItem item2;

	private AuctionBid bid1;
	private AuctionBid bid2;
	
	private final String PLAYER_1_NAME = "Player 1";
	private final String PLAYER_2_NAME = "Player 2";
	private final int AUCTION_ITEM_QUANTITY = 5;
	private final int BID_AMOUNT_PLAYER_1 = 10;
	private final int BID_AMOUNT_PLAYER_2 = 5;
	private final int EXPECTED_PLAYER_1_MONEY = 95;
	private final int EXPECTED_PLAYER_2_MONEY = 105;

	@Before
	public void Contractor() {
		auction = new Auction();
		game = new RoboticonQuest();
		player1 = new Player(game, PLAYER_1_NAME);
		player1.addResource(ResourceType.ENERGY, AUCTION_ITEM_QUANTITY);
		player2 = new Player(game, PLAYER_2_NAME);
		player2.addResource(ResourceType.ORE, AUCTION_ITEM_QUANTITY);
		
		item1 = new AuctionableItem(ResourceType.ENERGY, AUCTION_ITEM_QUANTITY, player1);
		item2 = new AuctionableItem(ResourceType.ORE   , AUCTION_ITEM_QUANTITY, player2);
		
		bid1 = new AuctionBid(BID_AMOUNT_PLAYER_1, player1);
		bid2 = new AuctionBid(BID_AMOUNT_PLAYER_2, player2);
	}
	
	@Test
	public void testAddItemToAuction() {
		auction.addItemToAuction(item1);
		assertEquals(new ArrayList<AuctionableItem>(), auction.getAuctionItems());
		auction.addItemToAuction(item2);
		assertEquals(new ArrayList<AuctionableItem>(), auction.getAuctionItems());
		
		auction.closeBidding();
		
		ArrayList<AuctionableItem> expectedItems = new ArrayList<AuctionableItem>();
		expectedItems.add(item1);
		expectedItems.add(item2);
		
		assertEquals(expectedItems, auction.getAuctionItems());
	}
	
	@Test
	public void testCloseBidding(){
		auction.addItemToAuction(item1);
		assertEquals(new ArrayList<AuctionableItem>(), auction.getAuctionItems());
		auction.addItemToAuction(item2);
		assertEquals(new ArrayList<AuctionableItem>(), auction.getAuctionItems());
		
		auction.closeBidding();
		
		auction.getAuctionItems(player1)[0].placeBid(bid1);
		auction.getAuctionItems(player2)[0].placeBid(bid2);
		
		auction.closeBidding();
		
		assertEquals(AUCTION_ITEM_QUANTITY, player1.getOre());
		assertEquals(AUCTION_ITEM_QUANTITY, player2.getEnergy());
		
		assertEquals(EXPECTED_PLAYER_1_MONEY, player1.getMoney());
		assertEquals(EXPECTED_PLAYER_2_MONEY, player2.getMoney());
	}
}
