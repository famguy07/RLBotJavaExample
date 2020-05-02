package fambot.controller;

import fambot.FamBotMemory;
import fambot.input.DataPacket;
import fambot.output.ControlsOutput;

public class DefaultController {
	private static String state = "default";
	
	public static void setState(DataPacket input) {
		System.out.println("ID: " + Thread.currentThread().getId() + "\tBall Touched: " + input.ball.hasBeenTouched);
		if (!input.ball.hasBeenTouched) {
			state = "kickoff";
			return;
		}
		
		if (input.ball.hasBeenTouched && state.equals("kickoff")) {
			state = "default";
		}
		
		if((int) input.gameInfo.secondsElapsed() % 30 == 0 && input.car.boost < 20) {
			state = "largeBoost";
		}
		
		if (state.equals("largeBoost") && input.car.boost == 100) {
			state = "default";
		}
	}
	
	public static ControlsOutput getControl(DataPacket input, FamBotMemory memory) {
		System.out.println("ID: " + Thread.currentThread().getId() + "\tState: " + state);
		ControlsOutput output = null;
		switch (state) {
			case "largeBoost":
				output = FamBotActions.getNearestLargeBoost(input);
				break;
			case "kickoff":
				output = FamBotActions.kickoff(input, memory);
				break;
			default:
				output = FamBotActions.ballChase(input);
		}
		return output;
	}
}
