package io.github.teamfractal.util;

import io.github.teamfractal.entity.Player;
import io.github.teamfractal.exception.NotEnoughMoneyException;

public class AuctionBid {
	private int bidAmount;
	private Player bidOwner;
	
	public AuctionBid(int bidAmount, Player bidOwner){
		if(bidOwner.getMoney() < bidAmount){
			throw new NotEnoughMoneyException();
		}
		
		bidOwner.addMoney(-bidAmount);
		this.bidAmount = bidAmount;
		this.bidOwner = bidOwner;
	}
	
	public int getBidAmount() {
		return bidAmount;
	}
	
	public Player getBidOwner() {
		return bidOwner;
	}
	
	/**
	 * Gives the player who placed this bid their money back.
	 */
	public void returnBidMoneyToPlayer() {
		bidOwner.addMoney(bidAmount);
	}
}
