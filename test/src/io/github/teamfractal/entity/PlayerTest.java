package io.github.teamfractal.entity;

import static org.junit.Assert.*;

import org.junit.*;

public class PlayerTest {
	private Player player;
	
	@Before
	public void setUp(){
		player = new Player();
	}
	
	//Money Tests
	@Test
	public void testPlayerInitialMoney(){
		assertEquals(100, player.getMoney());
	}
	
	// Buy & Sell Resource Tests
	@Test
	public void testPlayerBuyResource() throws Exception{
		Market market = new Market();
		//Purchase 5 ore
		player.purchaseResourceFromMarket(5, market, ResourceType.ORE);
		assertEquals(100 - 5 * market.getResourcePrice(ResourceType.ORE), player.getMoney());
		assertEquals(5, player.getOre()); 
		//purchase 10 energy
		player.purchaseResourceFromMarket(10, market, ResourceType.ENERGY);
		assertEquals(95 - 10 * market.getResourcePrice(ResourceType.ENERGY), player.getMoney());
		assertEquals(10, player.getEnergy()); 
	}

	@Test
	public void testPlayerSellResource() throws Exception{
		Market market = new Market();
		player.purchaseResourceFromMarket(15, market, ResourceType.ORE);
		player.purchaseResourceFromMarket(15, market, ResourceType.ENERGY);
		//sell 5 ore
		player.sellResourceToMarket(5, market, ResourceType.ORE);
		assertEquals(70 + 5 * market.getResourcePrice(ResourceType.ORE), player.getMoney());
		assertEquals(10, player.getOre());
		//sell 5 energy
		player.sellResourceToMarket(5, market, ResourceType.ENERGY);
		assertEquals(75 + 5 * market.getResourcePrice(ResourceType.ENERGY), player.getMoney());
		assertEquals(10, player.getEnergy());
	} 
	
	@Test
	public void testPlayerCannotBuyMoreThanAllowed() throws Exception{
		Market market = new Market();
		// Attempt to purchase more ore than allowed
		try {
			player.purchaseResourceFromMarket(100, market, ResourceType.ORE);
		}
		catch(Exception exception1) {
			assertEquals(100, player.getMoney());
			assertEquals(0, player.getOre());
			// Attempt to purchase more energy than allowed
			try {
				player.purchaseResourceFromMarket(100, market, ResourceType.ENERGY);
			}
			catch(Exception exception2) {
				assertEquals(100, player.getMoney());
				assertEquals(0, player.getEnergy()); 
			}
		}
	}
	
	
	@Test
	public void testPlayerCannotSellMoreThanAllowed() throws Exception{
		Market market = new Market();
		player.purchaseResourceFromMarket(15, market, ResourceType.ORE);
		player.purchaseResourceFromMarket(15, market, ResourceType.ENERGY);
		// Attempt to sell more ore than allowed
		try {
		player.sellResourceToMarket(10, market, ResourceType.ORE);
		}
		catch(Exception exception1) { 
		assertEquals(100 + 10 * market.getResourcePrice(ResourceType.ORE), player.getMoney());
		assertEquals(5, player.getEnergy());
		// Attempt to sell more energy than allowed
		try {
			player.sellResourceToMarket(10, market, ResourceType.ENERGY);
		}
			catch(Exception exception2) { 
				assertEquals(100 + 10 * market.getResourcePrice(ResourceType.ENERGY), player.getMoney());
				assertEquals(5, player.getEnergy());
			}
		}
	} 
	
	
}