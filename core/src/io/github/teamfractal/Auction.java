package io.github.teamfractal;

import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.Roboticon;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.util.AuctionBid;
import io.github.teamfractal.util.AuctionableItem;

import java.util.ArrayList;

public class Auction {
	private ArrayList<AuctionableItem> auctionItems = new ArrayList<AuctionableItem>();
	
	public void addItemToAuction(AuctionableItem item) {
		auctionItems.add(item);
	}
	
	public void removeItemFromAuction(AuctionableItem item){
		auctionItems.remove(item);
	}
	
	public ArrayList<AuctionableItem> getAuctionObjects() {
		return auctionItems;
	}
	
	public String[] getAuctionItemsDisplayStrings() {
		String[] strings = new String[auctionItems.size()];
		
		for (int i = 0; i < auctionItems.size(); i++) {
			strings[i] = auctionItems.get(i).toString(); 
		}
		
		return strings;
	}
	
	public void closeBidding() {
		for (AuctionableItem item : auctionItems) {
			AuctionBid currentItemWinningBid = item.getWinningBid();
			Player bidWinner = currentItemWinningBid.getBidOwner();
			Player itemOwner = item.getItemOwner();
			
			if(item.getItem() instanceof Roboticon){				
				Roboticon roboticon = (Roboticon)item.getItem();
				
				itemOwner.removeRoboticon(roboticon);
				bidWinner.addRoboticon(roboticon);
			}
			else if(item.getItem() instanceof ResourceType){				
				ResourceType resource = (ResourceType)item.getItem();
				
				itemOwner.addResource(resource, -item.getQuantity());				
				bidWinner.addResource(resource, item.getQuantity());
			}
			
			itemOwner.addMoney(currentItemWinningBid.getBidAmount());
			bidWinner.addMoney(-currentItemWinningBid.getBidAmount());
		}
		
		auctionItems = new ArrayList<AuctionableItem>();
	}
}
