package fambot;

import fambot.boost.BoostManager;
import fambot.controller.Controller;
import fambot.controller.TestController;
import fambot.input.DataPacket;
import fambot.output.ControlsOutput;
import fambot.util.DebugDrawer;
import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

public class FamBot07 implements Bot {

	private final int playerIndex;
	private Controller controller = new TestController();

	public FamBot07(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	/**
	 * This is where we keep the actual bot logic. This function shows how to chase
	 * the ball. Modify it to make your bot smarter!
	 */
	private ControlsOutput processInput(DataPacket input) {
		//overwrite the incorrect touch from the previous goal or reset
		System.out.println(input.gameInfo.isKickoffPause());
		if (input.gameInfo.isKickoffPause()) {
			input.ball.hasBeenTouched = false;
		}
		
		// This is optional!
		DebugDrawer.drawDebugLines(this, input);

		controller.setState(input);
		return controller.getControl(input);
	}
	
	
	

	@Override
	public int getIndex() {
		return this.playerIndex;
	}

	/**
	 * This is the most important function. It will automatically get called by the
	 * framework with fresh data every frame. Respond with appropriate controls!
	 */
	@Override
	public ControllerState processInput(GameTickPacket packet) {

		if (packet.playersLength() <= playerIndex || packet.ball() == null || !packet.gameInfo().isRoundActive()) {
			// Just return immediately if something looks wrong with the data. This helps us
			// avoid stack traces.
			return new ControlsOutput();
		}

		// Update the boost manager and tile manager with the latest data
		BoostManager.loadGameTickPacket(packet);

		// Translate the raw packet data (which is in an unpleasant format) into our
		// custom DataPacket class.
		// The DataPacket might not include everything from GameTickPacket, so improve
		// it if you need to!
		DataPacket dataPacket = new DataPacket(packet, playerIndex);

		// Do the actual logic using our dataPacket.
		ControlsOutput controlsOutput = processInput(dataPacket);

		return controlsOutput;
	}

	@Override
	public void retire() {
		System.out.println("Retiring sample bot " + playerIndex);
	}
}
