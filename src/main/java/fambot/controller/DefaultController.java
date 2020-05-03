package fambot.controller;

import fambot.actions.FamBotActions;
import fambot.actions.FamBotKickoffs;
import fambot.input.DataPacket;
import fambot.output.ControlsOutput;

public class DefaultController extends Controller{
	private String state = "default";

	public void setState(DataPacket input) {
		if (!input.ball.hasBeenTouched) {
			state = "kickoff";
			return;
		}
		
		if (input.ball.hasBeenTouched && state.equals("kickoff")) {
			setKickoffBoost(-1);
			state = "default";
		}
		
		if((int) input.gameInfo.secondsElapsed() % 30 == 0 && input.car.boost < 20) {
			state = "largeBoost";
		}
		
		if (state.equals("largeBoost") && input.car.boost == 100) {
			state = "default";
		}
	}
	
	public ControlsOutput getControl(DataPacket input) {
		System.out.println("ID: " + Thread.currentThread().getId() + "\tState: " + state);
		ControlsOutput output = null;
		switch (state) {
			case "largeBoost":
				output = FamBotActions.getNearestLargeBoost(input);
				break;
			case "kickoff":
				output = FamBotKickoffs.kickoffKill(input, this);
				break;
			default:
				output = FamBotActions.ballChaseWithFlip(input, this);
		}
		return output;
	}
}
