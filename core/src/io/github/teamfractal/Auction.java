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
	
	/**
	 * Get the items currently available to bid on, complement any items 
	 * owned by the given player and any items the player has already bid on
	 * @param player
	 * @return
	 */
	public AuctionableItem[] getAuctionItems(Player player) {
		int numItems = 0;
		
		for (AuctionableItem item : currentAuctionItems) {
			if(item.getItemOwner() != player
					&& !item.playerHasBid(player)){
				numItems++;
			}
		}
		
		AuctionableItem[] items = new AuctionableItem[numItems];
		int indexInItems = 0;
		
		for (int i = 0; i < currentAuctionItems.size(); i++) {
			AuctionableItem item = currentAuctionItems.get(i);
			if(item.getItemOwner() != player
					&& !item.playerHasBid(player)){
				items[indexInItems] = item; 
				indexInItems++;
			}
		}
		
		return items;
	}
	
	/**
	 * Gets the item at the given index, excluding items owned by the player
	 * and items which the player has already bid on.
	 * @param index
	 * @return
	 */
	public AuctionableItem getAuctionItemAtIndex(int index, Player player) {
		return getAuctionItems(player)[index];
	}
	
	public Object[] getPlayerAuctionableItems(Player player){
		Array<Roboticon> playerRoboticons = player.getRoboticons();
		int numItems = 3 + player.getNumUninstalledRoboticons();	//Player can always choose to auction food, energy, ore
																	//Plus an item for each (non-installed) roboticon they own.		
		Object[] auctionableObjects = new Object[numItems];
		
		auctionableObjects[0] = ResourceType.FOOD;
		auctionableObjects[1] = ResourceType.ENERGY;
		auctionableObjects[2] = ResourceType.ORE;
		
		int indexInAuctionableObjects = 3;
		
		for (int i = 0; i < playerRoboticons.size; i++) {
			Roboticon roboticon = playerRoboticons.get(i);

			if(!roboticon.isInstalled()){
				auctionableObjects[indexInAuctionableObjects] = roboticon;
				indexInAuctionableObjects++;
			}
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
				
				bidWinner.addRoboticon(roboticon);
			}
			else if(item.getItem() instanceof ResourceType){				
				ResourceType resource = (ResourceType)item.getItem();
							
				bidWinner.addResource(resource, item.getQuantity());
			}
			
			itemOwner.addMoney(currentItemWinningBid.getBidAmount());
			item.returnLosingBidMoney();
		}
		
		currentAuctionItems = nextAuctionItems;
		nextAuctionItems = new ArrayList<AuctionableItem>();
	}
}
