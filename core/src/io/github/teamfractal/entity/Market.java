package io.github.teamfractal.entity;

import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidResourceTypeException;
import io.github.teamfractal.exception.NotCommonResourceException;

public class Market {
	private static final int ORE_DEFAULT_PRICE = 10;
	private static final int ENERGY_DEFAULT_PRICE = 20;
	private static final int FOOD_DEFAULT_PRICE = 30;
	private static final int ROBOTICON_DEFAULT_PRICE = 40;
	private static final int CUSTOMISATION_DEFAULT_PRICE = 10;
	private static final int ACCELERATOR_DEFAULT = 1;
	private static final int ORE_DEFAULT_AMOUNT = 0;
	private static final int ENERGY_DEFAULT_AMOUNT = 16;
	private static final int FOOD_DEFAULT_AMOUNT = 16;
	private static final int ROBOTICON_DEFAULT_AMOUNT = 12;
	private static final int CUSTOMISATION_BUY_PRICE = 1000;
	private static final int CUSTOMISATION_SELL_PRICE = 10;
  
	private final int ROBOTICON_ORE_COST = 2;
	private final int MAX_NO_OF_ROBOTICONS_PER_TURN = 2;

	/**
	 * Initialise the market
	 */
	public Market() {
		setFood(FOOD_DEFAULT_AMOUNT);
		setEnergy(ENERGY_DEFAULT_AMOUNT);
		setOre(ORE_DEFAULT_AMOUNT);
		setRoboticon(ROBOTICON_DEFAULT_AMOUNT);
		setAccelerator(ACCELERATOR_DEFAULT);
	}

	//<editor-fold desc="Resource getters and setters">
	private int food;
	private int energy;
	private int ore;
	private int roboticon;
	private int accelerator;

	/**
	 * Get the amount of food in the market
	 * @return The amount of food in the market.
	 */
	int getFood() {
		return food;
	}

	/**
	 * Get the amount of energy in the market
	 * @return The amount of energy in the market.
	 */
	int getEnergy() {
		return energy;
	}

	/**
	 * Get the amount of ore in the market
	 * @return The amount of ore in the market.
	 */
	int getOre() {
		return ore;
	}

	/**
	 * Get the amount of roboticon in the market
	 * @return The amount of roboticon in the market.
	 */
	int getRoboticon() {
		return roboticon;
	}

	/**
	 * Get the total amount of all available resources added together.
	 * @return   The total amount.
	 */

	int getAccelerator() {
		return accelerator; 
	}

	private synchronized int getTotalResourceCount() {
		return food + energy + ore + roboticon;
	}

	/**
	 * Set the amount of ore in the market
	 * @param amount                     The amount of new ore count.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	synchronized void setOre(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Ore can't be negative.");
		}

		this.ore = amount;
	}

	/**
	 * Set the amount of energy in the market
	 * @param amount                     The amount of new energy count.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	synchronized void setEnergy(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Energy can't be negative.");
		}

		this.energy = amount;
	}

	/**
	 * Set the amount of food in the market
	 * @param amount                     The amount of new food amount.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	synchronized void setFood(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Food can't be negative.");
		}

		this.food = amount;
	}

	/**
	 * Set the amount of roboticon in the market
	 * @param amount                     The amount of new roboticon count.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	void setRoboticon(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Roboticon can't be negative.");
		}

		roboticon = amount;
	}

	void setAccelerator(int accel) {
		accelerator = accel;
	}

	//</editor-fold>

	/**
	 * Get the amount of specific resource.
	 * @param type   The {@link ResourceType}.
	 * @return       The amount.
	 */
	public int getResource(ResourceType type) {
		switch (type) {
			case ORE:
				return getOre();

			case ENERGY:
				return getEnergy();

			case ROBOTICON:
				return getRoboticon();

			case FOOD:
				return getFood();
				
			case CUSTOMISATION:
				return CUSTOMISATION_SELL_PRICE;

			default:
				throw new NotCommonResourceException(type);
		}
	}



	/**
	 * Set the amount of specific resource.
	 * @param type   The {@link ResourceType}.
	 * @param amount The new resource amount.
	 * @throws IllegalArgumentException      Will be thrown if the new amount is negative.
	 * @throws InvalidResourceTypeException  Will be thrown if the resource specified is invalid.
	 */
	void setResource(ResourceType type, int amount)
			throws IllegalArgumentException, InvalidResourceTypeException {

		switch (type) {
			case ORE:
				setOre(amount);
				break;

			case ENERGY:
				setEnergy(amount);
				break;

			case ROBOTICON:
				setRoboticon(amount);
				break;

			case FOOD:
				setFood(amount);
				break;
				
			case CUSTOMISATION:
				break;

			default:
				throw new NotCommonResourceException(type);
		}

	}

	/**
	 * Method to ensure the market have enough resources for user to purchase.
	 * @param type    The {@link ResourceType}.
	 * @param amount  the amount of resource to check.
	 * @return        If there are enough resources.
	 * @throws InvalidResourceTypeException  Will be thrown if the resource specified is invalid.
	 */
	boolean hasEnoughResources(ResourceType type, int amount)
			throws InvalidResourceTypeException {
		int resource = getResource(type);
		return amount <= resource;
	}

	/**
	 * Get the single price for a resource type.
	 * @param resource   The {@link ResourceType}.
	 * @return           The buy in price.
	 */
	public int getBuyPrice(ResourceType resource) {
		int price;
		switch (resource) {
			case ORE:
				price = getCurvePoint(resource, ORE_DEFAULT_AMOUNT, ORE_DEFAULT_PRICE, accelerator, 1);
				return price;

			case ENERGY:
				price = getCurvePoint(resource, ENERGY_DEFAULT_AMOUNT, ENERGY_DEFAULT_PRICE, accelerator, 1);
				return price;

			case FOOD:
				price = getCurvePoint(resource, FOOD_DEFAULT_AMOUNT, FOOD_DEFAULT_PRICE, accelerator,1 );
				return price;

			case ROBOTICON:
				price = getCurvePoint(resource, ROBOTICON_DEFAULT_AMOUNT, ROBOTICON_DEFAULT_PRICE, accelerator,1 );
				return price;

			case CUSTOMISATION:
				price = CUSTOMISATION_BUY_PRICE;
				return price;

			default:
				throw new IllegalArgumentException("Error: Resource type is incorrect.");
		}
	}

	/**
	 * Get the single price for a resource type.
	 * Selling prices will be dictated via exponential decay in getCurvePoint
	 * @param resource   The {@link ResourceType}.
	 * @return           The sell price.
	 */
	public int getSellPrice(ResourceType resource) {
		int price;
		switch (resource) {
			case ORE:
				price = getCurvePoint(resource, ORE_DEFAULT_AMOUNT, ORE_DEFAULT_PRICE, accelerator, -1);
				return price;

			case ENERGY:
				price = getCurvePoint(resource, ENERGY_DEFAULT_AMOUNT, ENERGY_DEFAULT_PRICE, accelerator, -1);
				return price;

			case FOOD:
				price = getCurvePoint(resource, FOOD_DEFAULT_AMOUNT, FOOD_DEFAULT_PRICE, accelerator, -1);
				return price;

			case ROBOTICON:
				price = getCurvePoint(resource, ROBOTICON_DEFAULT_AMOUNT, ROBOTICON_DEFAULT_PRICE, accelerator, -1);
				return price;

			case CUSTOMISATION:
				price = CUSTOMISATION_BUY_PRICE;
				return price;

			default:
				throw new IllegalArgumentException("Error: Resource type is incorrect.");
		}
	}

	public void produceRoboticons(){
		for(int i = 0; i < MAX_NO_OF_ROBOTICONS_PER_TURN; i++) {
			if(this.ore >= ROBOTICON_ORE_COST) {
				this.ore = this.ore - ROBOTICON_ORE_COST;
				this.roboticon = this.roboticon + 1;
			}
		}
	}

	/**
	 * Take the curve y=|x-defaultAmount| and adds it to the default value of the resource or takes it
	 * away depending on if buying or selling. This value is multiplied by the accelerator which has the same purpose
	 * as changing the graph gradient, a higher accelaeator meaning a steeper curve
	 *
	 * @param resource The ResourceType.
	 * @param defaultAmount The initialisation amount of the resource
	 * @param defaultPrice The initialisation price of the resource
	 * @param accelerator Changes the gradient of the points
	 * @param buying Integer representation of wether buying or selling, 1 for buying, -1 for selling
	 * @return The point on the curve with the current amount of the resource parameter as its x value
	 */
	private int getCurvePoint(ResourceType resource, int defaultAmount,int defaultPrice, int accelerator, int buying){
		double pointCurve;

		if (getResource(resource) >= defaultAmount){
			pointCurve = Math.max(0,(defaultPrice+((buying)*(accelerator*Math.abs(getResource(resource)-defaultAmount)))));
		}
		else{
			pointCurve = Math.max(0,defaultPrice-(buying)*(accelerator*Math.abs(getResource(resource)-defaultAmount)));
		}


		return (int) (long) pointCurve;
	}


	/**
	 * Buy Resource from the market, caller <i>must</i> be doing all the checks.
	 * For example, take money away from the player.
	 *
	 * This method will only increase the amount of specified resource.
	 *
	 * @param resource    The {@link ResourceType}
	 * @param amount      The amount of resource to buy in.
	 */
	public synchronized void buyResource(ResourceType resource, int amount){
		setResource(resource, getResource(resource) + amount);
	}

	/**
	 * Sell Resource from the market, caller <i>must</i> be doing all the checks.
	 * For example, add money in to the player.
	 *
	 * This method will only decrease the amount of specified resource.
	 *
	 * @param resource    The {@link ResourceType}
	 * @param amount      The amount of resource to sell out.
	 */
	public synchronized void sellResource(ResourceType resource, int amount) {
		setResource(resource, getResource(resource) - amount);
	}
}








