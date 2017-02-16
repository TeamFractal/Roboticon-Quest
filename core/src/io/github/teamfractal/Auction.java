package io.github.teamfractal;

import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.Roboticon;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.util.AuctionBid;
import io.github.teamfractal.util.AuctionableItem;

import java.util.ArrayList;
import java.util.Objects;

import com.badlogic.gdx.utils.Array;

public class Auction {
	private ArrayList<AuctionableItem> currentAuctionItems = new ArrayList<AuctionableItem>();
	private ArrayList<AuctionableItem> nextAuctionItems    = new ArrayList<AuctionableItem>();
	
	public void addItemToAuction(AuctionableItem item) {
		nextAuctionItems.add(item);
	}
	
	public ArrayList<AuctionableItem> getAuctionItems() {
		return currentAuctionItems;
	}
	
	public String[] getAuctionItemsDisplayStrings() {
		String[] strings = new String[currentAuctionItems.size()];
		
		for (int i = 0; i < currentAuctionItems.size(); i++) {
			strings[i] = currentAuctionItems.get(i).toString(); 
		}
		
		return strings;
	}
	
	public AuctionableItem getAuctionItemAtIndex(int index) {
		return currentAuctionItems.get(index);
	}
	
	public Object[] getPlayerAuctionableItems(Player player){
		Array<Roboticon> playerRoboticons = player.getRoboticons();
		int numItems = 3 + playerRoboticons.size;	//Player can always choose to auction food, energy, ore
													//Plus an item for each roboticon they own.		
		Object[] auctionableObjects = new Object[numItems];
		
		auctionableObjects[0] = ResourceType.FOOD;
		auctionableObjects[1] = ResourceType.ENERGY;
		auctionableObjects[2] = ResourceType.ORE;
		
		for (int i = 0; i < playerRoboticons.size; i++) {
			auctionableObjects[i + 3] = playerRoboticons.get(i);
		}
		
		return auctionableObjects;
	}
	
	public void closeBidding() {
		for (AuctionableItem item : currentAuctionItems) {
			AuctionBid currentItemWinningBid = item.getWinningBid();
			
			if(currentItemWinningBid == null){
				item.noBidsPlaced();
				continue;
			}
			
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
		
		currentAuctionItems = nextAuctionItems;
		nextAuctionItems = new ArrayList<AuctionableItem>();
	}
}
