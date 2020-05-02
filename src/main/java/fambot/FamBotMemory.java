package fambot;

import fambot.boost.BoostPad;

public class FamBotMemory {
	private int kickoffJumpTimer = 0;
	private BoostPad kickoffBoost = null;

	public int getKickoffJumpTimer() {
		return kickoffJumpTimer;
	}

	public void incrementKickoffJumpTimer() {
		kickoffJumpTimer++;
	}

	public void resetKickoffJumpTimer() {
		kickoffJumpTimer = 0;
	}

	public BoostPad getKickoffBoost() {
		return kickoffBoost;
	}

	public void setKickoffBoost(BoostPad kickoffBoost) {
		this.kickoffBoost = kickoffBoost;
	}
	
	public void resetKickoff() {
		resetKickoffJumpTimer();
		setKickoffBoost(null);
	}
}
