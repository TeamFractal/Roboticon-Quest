package io.github.teamfractal.util;

import io.github.teamfractal.RoboticonQuest;

// Class created by Josh Neil - contains code needed to implement random events
/**
 * Contains most of the logic needed to implement the random events used 
 * throughout the game
 * @author jcn509, cb1423
 */
public class RandomEvents {
	/**
	 * Returns a boolean value that states whether or not a given roboticon is
	 * faulty
	 * @return A boolean value that states whether or not a given roboticon is
	 * faulty
	 */
	public static boolean roboticonIsFaulty(){
		return Math.random() >= 0.95; // Five percent chance of a roboticon being faulty
	}
	
	/**
	 * Returns a boolean value that states whether or not an acquired tile contains a treasure chest.
	 * @return A boolean value that states whether or not an acquired tile contains a treasure chest.
	 */
	public static boolean tileHasChest(){
		return Math.random() >= 0.8;
	}
	
	/**
	 * Returns an integer representing the amount of treasure found in a chest and 
	 * increase the player's balance by this amount.
	 * @return An integer representing the amount of treasure found in a chest.
	 */
	public static int treasureChest(RoboticonQuest game){
		int treasure = (int)(Math.random()*40)+10;
		int balance = game.getPlayer().getMoney();
		game.getPlayer().setMoney(balance+treasure);
		return treasure;
	}
}
