package fambot.util;

import java.awt.Color;

import fambot.FamBot07;
import fambot.input.DataPacket;
import fambot.input.car.CarData;
import fambot.prediction.BallPredictionHelper;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;

public class DebugDrawer {
	/**
	 * This is a nice example of using the rendering feature.
	 */
	public static void drawDebugLines(FamBot07 bot, DataPacket input) {
		CarData car = input.car;
		
		// Here's an example of rendering debug data on the screen.
		Renderer renderer = BotLoopRenderer.forBotLoop(bot);

		// Draw a line from the car to the ball
		renderer.drawLine3d(Color.LIGHT_GRAY, car.position, input.ball.position);

		// Draw a line that points out from the nose of the car.
		renderer.drawLine3d(Color.BLUE, car.position.plus(car.orientation.noseVector.scaled(150)),
				car.position.plus(car.orientation.noseVector.scaled(300)));

		if (input.ball.hasBeenTouched) {
			float lastTouchTime = car.elapsedSeconds - input.ball.latestTouch.gameSeconds;
			Color touchColor = input.ball.latestTouch.team == 0 ? Color.BLUE : Color.ORANGE;
			renderer.drawString3d((int) lastTouchTime + "s", touchColor, input.ball.position, 2, 2);
		}

		try {
			// Draw 3 seconds of ball prediction
			BallPrediction ballPrediction = RLBotDll.getBallPrediction();
			BallPredictionHelper.drawTillMoment(ballPrediction, car.elapsedSeconds + 3, Color.CYAN, renderer);
		} catch (RLBotInterfaceException e) {
			e.printStackTrace();
		}
	}
	
	public static void positionMarker(FamBot07 bot, DataPacket input) {
		
	}
}
