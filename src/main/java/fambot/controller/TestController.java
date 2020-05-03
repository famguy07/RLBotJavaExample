package fambot.controller;

import fambot.actions.FamBotActions;
import fambot.input.DataPacket;
import fambot.output.ControlsOutput;

public class TestController extends Controller{
	private String state = "default";
	private int count = 0;

	public void setState(DataPacket input) {
		System.out.println(count++);
		if (count > 150 && count < 300) {
			state = "test";
		} 
		
		if (count > 300) {
			count = 0;
			state = "default";
		}
	}
	
	public ControlsOutput getControl(DataPacket input) {
		System.out.println("ID: " + Thread.currentThread().getId() + "\tState: " + state);
		ControlsOutput output = null;
		switch (state) {
			case "test":
				output = null;//FamBotActions.flipAtBall(input.car, input.ball, this, 6);
				break;
			default:
				output = new ControlsOutput();
		}
		return output;
	}
}
