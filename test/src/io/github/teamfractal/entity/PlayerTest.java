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
	
	/** 
	 * Josh Neil moved any tests that are non-parametrised and should only be ran once into this class
	 */
	public static class MarketSingleTests{
		@Rule
		public final ExpectedException exception = ExpectedException.none();
	
		Player player;
	
		/**
		 * Runs before every test and creates the Player object that is udner test as well as the RoboticonQuest object
		 * that is requried by some tests
		 */
		@Before
		public void setUp() {
			RoboticonQuest game = new RoboticonQuest();
			player = new Player(game);
		}
	
		/**
		 * Ensures that the quantity of money initially in the player's inventory is correct
		 */
		@Test
		public void testPlayerInitialMoney() {
			assertEquals(100, player.getMoney());
		}
	
		/**
		 * Tests {@link Player#purchaseResourceFromMarket(int, Market, ResourceType)} and ensures that
		 * the correct amount of money is removed from the players inventory, the correct amount of resources are
		 * added to the player's inventory and the correct amount of resources are removed from the market's inventory
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
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} and ensures that
		 * the correct amount of money is added to the players inventory, the correct amount of resources are
		 * removed from the player's inventory and the correct amount of resources are added to the market's inventory
		 */
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
	
		/**
		 * Tests {@link Player#purchaseResourceFromMarket(int, Market, ResourceType)} ensures
		 * that players cannot buy more of a given resource than the market possesses 
		 * <p>
		 * Also ensures that the players inventory does not change when they try to do this
		 * </p>
		 * @throws Exception An exception should be thrown when the player tries to buy more
		 * of a given resource than is allowed
		 */
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
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} ensures
		 * that the player cannot sell more energy than they have in their possession
		 * @throws Exception An exception should be thrown when the player tries to do this
		 */
		@Test
		public void testPlayerCannotSellMoreEnergyThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setEnergy(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.ENERGY);
		}
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} ensures
		 * that the player cannot sell more ore than they have in their possession
		 * @throws Exception An exception should be thrown when the player tries to do this
		 */
		@Test
		public void testPlayerCannotSellMoreOreThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setOre(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.ORE);
		}
	
		/**
		 * Tests {@link Player#customiseRoboticon(Roboticon, ResourceType)} and ensures that players
		 * can customise roboticons
		 */
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
		
		/**
		 * Tests {@link Player#customiseRoboticon(Roboticon, ResourceType)} and ensures that players
		 * can customise roboticons that they own
		 */
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
		
		// Tests below this comment were added by Josh Neil
		/**
		 * Helper method that causes the player to acquire three roboticons with given customisations
		 * and add them to three plots that the player also acquires
		 */
		private void acquireThreePlotsWithRoboticons(){
			Roboticon r1 = new Roboticon(0);
			Roboticon r2 = new Roboticon(1);
			Roboticon r3 = new Roboticon(2);
			
			r1.setCustomisation(ResourceType.ORE);
			r2.setCustomisation(ResourceType.ENERGY);
			r3.setCustomisation(ResourceType.FOOD);
			
			LandPlot l1 = new LandPlot(1, 100, 100);
			LandPlot l2 = new LandPlot(2, 200, 200);
			LandPlot l3 = new LandPlot(3, 300, 5);
			
			l1.installRoboticon(r1);
			l1.setHasRoboticon(true);
			l1.setOwner(player);
			
			l2.installRoboticon(r2);
			l2.setHasRoboticon(true);
			l2.setOwner(player);
			
			l3.installRoboticon(r3);
			l3.setHasRoboticon(true);
			l3.setOwner(player);
			
			player.addLandPlot(l1);
			player.addLandPlot(l2);
			player.addLandPlot(l3);		
		}
		
		/**
		 * Tests {@link Player#produceResources()} ensures that the correct amount of energy is produced
		 */
		@Test
		public void testProduceResourcesEnergy(){
			acquireThreePlotsWithRoboticons();
			int energyBefore = player.getEnergy();
			player.produceResources();
			assertEquals(energyBefore+200,player.getEnergy());
		}
		
		/**
		 * Tests {@link Player#produceResources()} ensures that the correct amount of ore is produced
		 */
		@Test
		public void testProduceResourcesOre(){
			acquireThreePlotsWithRoboticons();
			int oreBefore = player.getOre();
			player.produceResources();
			assertEquals(oreBefore+1,player.getOre());
		}
		
		/**
		 * Tests {@link Player#produceResources()} ensures that the correct amount of food is produced
		 */
		@Test
		public void testProduceResourcesFood(){
			acquireThreePlotsWithRoboticons();
			int foodBefore = player.getFood();
			player.produceResources();
			assertEquals(foodBefore+5,player.getFood());
		}
	}
	
	
	/// Tests added by Josh Neil
	@RunWith(Parameterized.class)
	public static class MarketPurchaseRoboticonParamaterisedTests{
		 Player player;
		 Market market;
		 PurchaseStatus purchaseStatus;
		 int initialMoney;
		 int numberOfRoboticonsToPurchase;
		 int moneyRemoved;
		 int playerRoboticonChange;
		 int marketRoboticonChange;
		 int marketInitialRoboticons;
		
		/**
		 * Runs before each test and sets up the values of the variables needed during that test
		 * @param marketInitialRoboticons The number of roboticons in the market before the test runs
		 * @param purchaseStatus The result that should be returned
		 * @param initialMoney The amount of money that the player has before the test
		 * @param moneyRemoved The amount of money that should be removed from the player's inventory
		 * @param playerRoboticonChange The number of roboticons that should be added to the player's inventory
		 * @param marketRoboticonChange The number of roboticons that should be removed from the market's inventory
		 * @param numberOfRoboticonsToPurchase The number of roboticons that the player should attempt to purchase
		 */
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
		 * Defines the values to be used in each test
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
		
		/**
		 * Runs before every test and creates the necessary objects
		 */
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
	
	/// Tests added by Josh Neil
		/**
		 * Runs parameterised tests on {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} using
		 * many different values and ensures that the correct changes are made to various attributes of the player
		 * and roboticon and the correct value is returned by the method.
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class MarketPurchaseCustomisationParamaterisedTests{
			 Player player;
			 Market market;
			 PurchaseStatus purchaseStatus;
			 int initialMoney;
			 ResourceType customisation;
			 int expectedMoneyRemoved;
			 private Roboticon roboticon;
			 private ResourceType expectedRoboticonCustomisation;
			
			 /**
			  * Runs before each test and sets up the values of the variables needed by the test
			  * @param purchaseStatus The result returned by the method call
			  * @param initialMoney The amount of money that the player has in their inventory before the test is run
			  * @param customisation The customisation that the player should purchase
			  * @param expectedMoneyRemoved The amount of money that should be removed from the player's inventory
			  * @param expectedRoboticonCustomisation The customisation type that the roboticon should have after the test
			  */
			public MarketPurchaseCustomisationParamaterisedTests(PurchaseStatus purchaseStatus, int initialMoney, ResourceType customisation,  int expectedMoneyRemoved, ResourceType expectedRoboticonCustomisation){
				this.purchaseStatus = purchaseStatus;
				this.initialMoney = initialMoney;
				this.customisation = customisation;
				this.expectedMoneyRemoved = expectedMoneyRemoved;
				this.expectedRoboticonCustomisation = expectedRoboticonCustomisation;
			}
			
			// Added by Josh Neil
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection customisationPurchaseValues(){
				 int orePrice = (new Market()).getSellPrice(ResourceType.CUSTOMISATION);
				 int energyPrice = (new Market()).getSellPrice(ResourceType.CUSTOMISATION);
				 int foodPrice = (new Market()).getSellPrice(ResourceType.CUSTOMISATION);
				 return Arrays.asList(new Object[][] {
			         {PurchaseStatus.Success,orePrice,ResourceType.ORE,orePrice,ResourceType.ORE},
			         {PurchaseStatus.Success,2*orePrice,ResourceType.ORE,orePrice,ResourceType.ORE},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,orePrice-1,ResourceType.ORE,0,ResourceType.Unknown},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,0,ResourceType.ORE,0,ResourceType.Unknown},
			         
			         {PurchaseStatus.Success,energyPrice,ResourceType.ENERGY,energyPrice,ResourceType.ENERGY},
			         {PurchaseStatus.Success,2*energyPrice,ResourceType.ENERGY,energyPrice,ResourceType.ENERGY},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,energyPrice-1,ResourceType.ENERGY,0,ResourceType.Unknown},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,0,ResourceType.ENERGY,0,ResourceType.Unknown},
			         
			         {PurchaseStatus.Success,foodPrice,ResourceType.FOOD,foodPrice,ResourceType.FOOD},
			         {PurchaseStatus.Success,2*foodPrice,ResourceType.FOOD,foodPrice,ResourceType.FOOD},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,foodPrice-1,ResourceType.FOOD,0,ResourceType.Unknown},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,0,ResourceType.FOOD,0,ResourceType.Unknown},
			         
			      });
			}
			/**
			 * Runs before every test and creates the necessary objects
			 */
			@Before
			public void setup(){
				player = new Player(null);
				market = new Market();
				player.setMoney(initialMoney);
				roboticon = new Roboticon(0);
			}
			
			/**
			 * Tests {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} ensures 
			 * that the correct value is returned when a player tries to purchase a customisation
			 */
			@Test
			public void testPurchaseCustomisationReturnedValue(){
				assertEquals(player.purchaseCustomisationFromMarket(customisation, roboticon, market),purchaseStatus);
			}
			
			/**
			 * Tests {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} ensures
			 * that the correct amount of money is removed from a player's inventory when they attempt
			 * to purchase a customisation
			 */
			@Test
			public void testCorrectMoneyRemoved(){
				player.purchaseCustomisationFromMarket(customisation, roboticon, market);
				assertEquals(initialMoney-expectedMoneyRemoved,player.getMoney());
			}			
			
			/**
			 * Tests {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} ensures 
			 * that when a player attempts to purchase a customisation from the market the roboticon
			 * used ends up with the correct customisation
			 */
			@Test
			public void roboticonCustomisationCorrect(){
				player.purchaseCustomisationFromMarket(customisation, roboticon, market);
				assertEquals(roboticon.getCustomisation(),expectedRoboticonCustomisation);
			}
			
		}

}