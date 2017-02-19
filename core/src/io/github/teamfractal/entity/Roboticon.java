package io.github.teamfractal.entity;

import io.github.teamfractal.entity.enums.ResourceType;

public class Roboticon {
	private int ID;
	private ResourceType customisation;
	private LandPlot installedLandPlot;
	
	public Roboticon(int ID) {
		this.ID = ID;
		customisation = ResourceType.Unknown;
	}
	
	public int getID () {
		return this.ID;
	}

	public ResourceType getCustomisation() {
		return this.customisation;
	}
	
	void setCustomisation(ResourceType type) {
		this.customisation = type;
	}
	
	public synchronized boolean isInstalled() {
		return installedLandPlot != null;
	}
	/**
	 * 
	 * @param sets land plot which roboticon is installed to
	 * @return true if roboticon can be installed, false if not
	 */
	public synchronized boolean setInstalledLandplot(LandPlot landplot) {
		if (!isInstalled()) {
			installedLandPlot = landplot;
			return true;
		}

		return false;
	}
	
	@Override
	public String toString(){
		String returnString = "Roboticon " + ID;
		
		switch (customisation) {
		case ORE:
			returnString += ": Ore";
			break;
			
		case ENERGY:
			returnString += ": Energy";
			break;

		case FOOD:
			returnString += ": Food";
			break;
			
		default:
			returnString += ": Uncustomised";
			break;
		}
		
		return returnString;
	}
}
