package fambot.controller;

import fambot.input.DataPacket;
import fambot.output.ControlsOutput;

public abstract class Controller {
	public abstract void setState(DataPacket input);
	public abstract ControlsOutput getControl(DataPacket input);
	
	private int jumpTimer = 0;
	private int kickoffBoost = -1; //cooresponds to the boosts in kickoffBoosts in FamBotActions
	private int kickoffLocation = -1; //cooresponds to the locations in kickoffLocations in FamBotActions
		
	public int getJumpTimer() {
		return jumpTimer;
	}

	public void incrementJumpTimer() {
		jumpTimer++;
	}

	public void resetJumpTimer() {
		jumpTimer = 0;
	}

	public int getKickoffBoost() {
		return kickoffBoost;
	}

	public void setKickoffBoost(int kickoffBoost) {
		this.kickoffBoost = kickoffBoost;
	}
	
	public int getKickoffLocation() {
		return kickoffLocation;
	}

	public void setKickoffLocation(int kickoffLocation) {
		this.kickoffLocation = kickoffLocation;
	}
}



