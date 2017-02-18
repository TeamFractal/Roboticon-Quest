package io.github.teamfractal.util;

import java.util.ArrayList;

import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.Roboticon;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidAuctionableItemException;
import io.github.teamfractal.exception.NotEnoughResourceException;

public class AuctionableItem {
	private Object item;
	private int quantity;
	private ArrayList<AuctionBid> bids = new ArrayList<AuctionBid>();
	private Player itemOwner;
	
	public AuctionableItem(Object item, int quantity, Player itemOwner){
		if(!(item instanceof Roboticon)
				&& !(item instanceof ResourceType)){
			throw new InvalidAuctionableItemException();
		}
		
		if(item instanceof ResourceType){
			ResourceType resourceType = (ResourceType)item;
			if(!itemOwner.hasEnoughResource(resourceType, quantity)
					|| quantity <= 0){
				throw new NotEnoughResourceException();
			}
			
			itemOwner.addResource(resourceType, -quantity);
		}
		else if(item instanceof Roboticon){
			Roboticon roboticon = (Roboticon) item;
			itemOwner.removeRoboticon(roboticon);
		}
				
		this.item = item;
		this.quantity = quantity;
		this.itemOwner = itemOwner;
	}
	
	public void placeBid(AuctionBid bid) {
		bids.add(bid);
	}
	
	public AuctionBid getWinningBid() {
		int highestBid = Integer.MIN_VALUE;
		AuctionBid winningBid = null;
		
		for (AuctionBid bid : bids) {
			if(highestBid < bid.getBidAmount()){
				highestBid = bid.getBidAmount();
				winningBid = bid;
			}
		}
		
		return winningBid;
	}
	
	/**
	 * Call when the bid has been closed. Returns money to players
	 * for bids which did not win.
	 */
	public void returnLosingBidMoney() {
		AuctionBid winningBid = getWinningBid();
		
		for (AuctionBid bid : bids) {
			if(bid != winningBid){
				bid.returnBidMoneyToPlayer();
			}
		}
	}
	
	/**
	 * Call when the item has had no bids placed on it.
	 * i.e. getWinningBid returns null.
	 */
	public void noBidsPlaced() {
		if(item instanceof Roboticon){
			itemOwner.addRoboticon((Roboticon)item);
		}
		else if(item instanceof ResourceType){
			itemOwner.addResource((ResourceType)item, quantity);
		}
	}
	
	public Player getItemOwner() {
		return itemOwner;
	}
	
	public Object getItem() {
		return item;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	@Override
	public String toString(){
		String string = item.toString();
		
		if(item instanceof ResourceType){
			string = quantity + " " + string;
		}
		
		return string;
	}

	/**
	 * Returns true if the given player has already placed a bid on
	 * this item.
	 */
	public boolean playerHasBid(Player player) {
	for (AuctionBid bid : bids) {
		if(bid.getBidOwner() == player){
			return true;
		}
	}
		
		return false;
	}	
}
