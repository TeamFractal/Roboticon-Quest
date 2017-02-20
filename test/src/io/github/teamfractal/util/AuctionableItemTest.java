package io.github.teamfractal.util;

import static org.junit.Assert.*;
import io.github.teamfractal.Auction;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.Roboticon;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidAuctionableItemException;
import io.github.teamfractal.exception.NotEnoughResourceException;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.utils.Array;

public class AuctionableItemTest {
	private Player player1;
	private Player player2;

	private RoboticonQuest game;
	private Auction auction;
	
	private AuctionableItem item1;
	private AuctionableItem item2;
	
	private Roboticon item1Roboticon;
	
	private final int ROBOTICON_ID = 55;
	private final String PLAYER_1_NAME = "Player 1";
	private final String PLAYER_2_NAME = "Player 2";
	private final int ORE_QUANTITY = 50;
	
	private final String ITEM_1_EXPECTED_STRING = "Roboticon 55: Uncustomised";
	private final String ITEM_2_EXPECTED_STRING = "50 Ore";
	private final int PLAYER_DEFAULT_MONEY = 100;
	
	@Before
	public void Contractor() {
		game = new RoboticonQuest();
		auction = new Auction();
		
		item1Roboticon = new Roboticon(ROBOTICON_ID);
		player1 = new Player(game, PLAYER_1_NAME);
		player1.addRoboticon(item1Roboticon);
		item1 = new AuctionableItem(item1Roboticon, 0, player1);
		
		player2 = new Player(game, PLAYER_2_NAME);
		player2.addResource(ResourceType.ORE, ORE_QUANTITY);
		item2 = new AuctionableItem(ResourceType.ORE, ORE_QUANTITY, player2);
	}
	
	@Test
	public void testToString() {
		assertEquals(ITEM_1_EXPECTED_STRING, item1.toString());
		assertEquals(ITEM_2_EXPECTED_STRING, item2.toString());
	}
	
	@Test
	public void testInit(){
		assertEquals(new Array<Roboticon>(), player1.getRoboticons());
		assertEquals(0, player2.getOre());
		
		try {
			AuctionableItem item = new AuctionableItem(ResourceType.ORE, 50, player1);
			assert false;
		} catch (NotEnoughResourceException e) {
			
		}
		
		try {
			AuctionableItem item = new AuctionableItem("Break", 0, player1);
			assert false;
		} catch (InvalidAuctionableItemException e) {
			
		}
	}
	
	@Test 
	public void testNoBidsPlaced(){
		auction.addItemToAuction(item1);
		auction.addItemToAuction(item2);
		
		auction.closeBidding();
		auction.closeBidding();
		
		assertEquals(ORE_QUANTITY, player2.getOre());
		assertEquals(1, player1.getRoboticons().size);
		assertEquals(PLAYER_DEFAULT_MONEY, player1.getMoney());
		assertEquals(PLAYER_DEFAULT_MONEY, player2.getMoney());
	}

}
