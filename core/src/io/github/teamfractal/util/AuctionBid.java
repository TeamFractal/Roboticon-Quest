package io.github.teamfractal.util;

import io.github.teamfractal.entity.Player;

public class AuctionBid {
	private int bidAmount;
	private Player bidOwner;
	
	public AuctionBid(int bidAmount, Player bidOwner){
		this.bidAmount = bidAmount;
		this.bidOwner = bidOwner;
	}
	
	public int getBidAmount() {
		return bidAmount;
	}
	
	public Player getBidOwner() {
		return bidOwner;
	}
}
