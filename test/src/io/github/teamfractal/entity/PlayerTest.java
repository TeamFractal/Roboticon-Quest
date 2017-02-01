package io.github.teamfractal.entity;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.enums.PurchaseStatus;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.NotEnoughResourceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class PlayerTest {
	
	// Josh Neil moved all of the previous tests into this class
	// as well as any tests that are non-parametrised and should only be ran once
	public static class MarketSingleTests{
		@Rule
		public final ExpectedException exception = ExpectedException.none();
	
		private Player player;
	
		@Before
		public void setUp() {
			RoboticonQuest game = new RoboticonQuest();
			player = new Player(game);
		}
	
		//Money Tests
		@Test
		public void testPlayerInitialMoney() {
			assertEquals(100, player.getMoney());
		}
	
		/**
		 * Test to purchase and sell resource from the market.
		 */
		@Test
		public void testPlayerBuyResource() {
			Market market = new Market();
			market.setOre(16);
			player.setMoney(1000);
	
	
			int playerMoney = player.getMoney();
			int orePrice = market.getSellPrice(ResourceType.ORE);
			//Purchase 5 ore
			player.purchaseResourceFromMarket(5, market, ResourceType.ORE);
			// Player should now have 5 more ores, and the market have 5 less ores.
			assertEquals(playerMoney - 5 * orePrice, player.getMoney());
			assertEquals(5, player.getOre());
			assertEquals(11, market.getOre());
	
	
			playerMoney = player.getMoney();
			int energyPrice = market.getSellPrice(ResourceType.ENERGY);
			//purchase 10 energy
			player.purchaseResourceFromMarket(10, market, ResourceType.ENERGY);
			assertEquals(playerMoney - 10 * energyPrice, player.getMoney());
			assertEquals(10, player.getEnergy());
			assertEquals(6, market.getEnergy());
		}
	
		@Test
		public void testPlayerSellResource() throws Exception {
			Market market = new Market();
	
			player.setMoney(1000);
			player.setResource(ResourceType.ORE, 15);
			player.setResource(ResourceType.ENERGY, 15);
	
	
			int orePrice = market.getBuyPrice(ResourceType.ORE);
			//sell 5 ore
			player.sellResourceToMarket(5, market, ResourceType.ORE);
			assertEquals(1000 + 5 * orePrice, player.getMoney());
			assertEquals(10, player.getOre());
			assertEquals(5, market.getOre());
	
			int energyPrice = market.getBuyPrice(ResourceType.ENERGY);
			player.setMoney(1000);
			//sell 5 energy
			player.sellResourceToMarket(5, market, ResourceType.ENERGY);
			assertEquals(1000 + 5 * energyPrice, player.getMoney());
			assertEquals(10, player.getEnergy());
			assertEquals(21, market.getEnergy());
		}
	
		@Test
		public void testPlayerCannotBuyMoreThanAllowed() throws Exception {
			Market market = new Market();
			// Attempt to purchase more ore than allowed
			try {
				player.purchaseResourceFromMarket(100, market, ResourceType.ORE);
			} catch (Exception exception1) {
				assertEquals(100, player.getMoney());
				assertEquals(0, player.getOre());
				// Attempt to purchase more energy than allowed
				try {
					player.purchaseResourceFromMarket(100, market, ResourceType.ENERGY);
				} catch (Exception exception2) {
					assertEquals(100, player.getMoney());
					assertEquals(0, player.getEnergy());
				}
			}
		}
	
		@Test
		public void testPlayerCannotSellMoreEnergyThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setEnergy(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.ENERGY);
		}
	
		@Test
		public void testPlayerCannotSellMoreOreThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setOre(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.ORE);
		}
	
		@Test
		public void testPlayerCanCustomiseRoboticon() {
			// Setup
			Roboticon roboticon = new Roboticon(1);
			player.customiseRoboticon(roboticon, ResourceType.ORE);
			assertEquals(ResourceType.ORE, roboticon.getCustomisation());
	
			Roboticon roboticon2 = new Roboticon(2);
			player.customiseRoboticon(roboticon2, ResourceType.ENERGY);
			assertEquals(ResourceType.ENERGY, roboticon2.getCustomisation());
		}
	
		@Test
		public void testPlayerCanCustomiseOwnedRoboticons() {
			Roboticon roboticon3 = new Roboticon(3); 
			Roboticon roboticon4 = new Roboticon(4);
			player.roboticonList = new Array<Roboticon>();
			player.roboticonList.add(roboticon3);
			player.roboticonList.add(roboticon4);
			player.customiseRoboticon(player.roboticonList.get(0), ResourceType.ORE);
			player.customiseRoboticon(player.roboticonList.get(1), ResourceType.ENERGY);
			assertEquals(ResourceType.ORE, player.roboticonList.get(0).getCustomisation());
			assertEquals(ResourceType.ENERGY, player.roboticonList.get(1).getCustomisation());
		}
	}
	
	
	/// Tests added by Josh Neil
	@RunWith(Parameterized.class)
	public static class MarketPurchaseRoboticonParamaterisedTests{
		private Player player;
		private Market market;
		private PurchaseStatus purchaseStatus;
		private int initialMoney;
		private int numberOfRoboticonsToPurchase;
		private int moneyRemoved;
		private int playerRoboticonChange;
		private int marketRoboticonChange;
		private int marketInitialRoboticons;
		
		public MarketPurchaseRoboticonParamaterisedTests(int marketInitialRoboticons,PurchaseStatus purchaseStatus,int initialMoney, int moneyRemoved, int playerRoboticonChange, int marketRoboticonChange, int numberOfRoboticonsToPurchase){
			this.marketInitialRoboticons = marketInitialRoboticons;
			this.purchaseStatus = purchaseStatus;
			this.initialMoney = initialMoney;
			this.moneyRemoved = moneyRemoved;
			this.playerRoboticonChange = playerRoboticonChange;
			this.marketRoboticonChange = marketRoboticonChange;
			this.numberOfRoboticonsToPurchase = numberOfRoboticonsToPurchase;
		}
		
		// Added by Josh Neil
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market)} with many different values and ensures
		 * that the correct results are produced
		 * @return
		 */
		@Parameterized.Parameters
		public static Collection roboticonPurchaseValues(){
			 int roboticonPrice = (new Market()).getSellPrice(ResourceType.ROBOTICON);
			 return Arrays.asList(new Object[][] {
		         {1,PurchaseStatus.Success,roboticonPrice,roboticonPrice,1,1,1},
		         {1,PurchaseStatus.Success,roboticonPrice+1,roboticonPrice,1,1,1},
		         {1,PurchaseStatus.Success,roboticonPrice+1,0,0,0,0},
		         {1,PurchaseStatus.FailPlayerNotEnoughMoney,0,0,0,0,1},
		         {1,PurchaseStatus.FailPlayerNotEnoughMoney,roboticonPrice-1,0,0,0,1},
		         {5,PurchaseStatus.FailPlayerNotEnoughMoney,(2*roboticonPrice)-1,0,0,0,2},
		         {0,PurchaseStatus.FailMarketNotEnoughResource,roboticonPrice,0,0,0,1},
		         {1,PurchaseStatus.FailMarketNotEnoughResource,roboticonPrice,0,0,0,2},
		      });
		}
		
		@Before
		public void setup(){
			player = new Player(null);
			market = new Market();
			market.setRoboticon(marketInitialRoboticons);
			player.setMoney(initialMoney);
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market) ensures that the correct result is
		 * returned
		 */
		@Test
		public void testPurchaseRoboticon(){
			assertEquals(purchaseStatus,player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market));
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market) ensures that when the player 
		 * purchases roboticons the correct number of roboticons are added to their inventory
		 */
		@Test
		public void testPurchaseRoboticonRoboticonAdded(){
			int roboticonsBefore = player.getRoboticons().size;
			player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market);
			assertEquals(roboticonsBefore+playerRoboticonChange,player.getRoboticons().size);
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market) ensures that when the player 
		 * purchases roboticons the correct amount of money is removed from their inventory
		 */
		@Test
		public void testPurchaseRoboticonMoneyRemoved(){
			int moneyBefore = player.getMoney();
			player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market);
			assertEquals(moneyBefore-moneyRemoved,player.getMoney());
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market) ensures that when the player 
		 * purchases roboticons the correct number of roboticons are removed from the markets inventory
		 */
		@Test
		public void testPurchaseRoboticonRoboticonRemoved(){
			int roboticonsBefore = market.getResource(ResourceType.ROBOTICON);
			player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market);
			assertEquals(roboticonsBefore-marketRoboticonChange,market.getResource(ResourceType.ROBOTICON));
		}
	}

}