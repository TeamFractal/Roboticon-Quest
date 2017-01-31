package io.github.teamfractal.util;


// Class created by Josh Neil - contains code needed to implement random events
/**
 * Contains most of the logic needed to implement the random events used 
 * throughout the game
 * @author jcn509
 */
public class RandomEvents {
	/**
	 * Returns a boolean value that states whether or not a given roboticon is
	 * faulty
	 * @return A boolean value that states whether or not a given roboticon is
	 * faulty
	 */
	public static boolean RoboticonIsFaulty(){
		return Math.random() >= 0.95; // Five percent chance of a roboticon being faulty
	}
}
