package fambot.actions;

import fambot.boost.BoostManager;
import fambot.boost.BoostPad;
import fambot.controller.Controller;
import fambot.input.DataPacket;
import fambot.input.ball.BallData;
import fambot.input.car.CarData;
import fambot.output.ControlsOutput;
import fambot.vector.Vector2;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.QuickChatSelection;

public class FamBotActions {
	public static ControlsOutput getNearestLargeBoost(DataPacket input) {
		CarData car = input.car;
		
		BoostPad pad = BoostManager.nearestBoost(car.position.flatten(), true, true);

		return new ControlsOutput()
				.withSteer(steer2d(car, pad.getLocation().flatten()))
				.withThrottle(1);
	}

	public static ControlsOutput ballChase(DataPacket input) {
		BallData ball = input.ball;
		CarData car = input.car;

		return new ControlsOutput()
				.withSteer(steer2d(car, ball.position.flatten()))
				.withThrottle(1)
				.withBoost(true);
	}
	
	public static ControlsOutput ballChaseWithFlip(DataPacket input, Controller controller) {
		BallData ball = input.ball;
		CarData car = input.car;

		double distance = car.position.flatten().distance(ball.position.flatten());
		if (distance < 400) {
			return flipAtBall(car, ball, controller, 6);
			
		} else {
			return new ControlsOutput()
					.withSteer(steer2d(car, ball.position.flatten()))
					.withThrottle(1)
					.withBoost(true);
		}
	}

	//TODO implement leading with targetDir
	protected static float steer2d(CarData car, Vector2 targetPos) {
		Vector2 pointer = targetPos.minus(car.position.flatten());
		double radians = car.velocity.flatten().correctionAngle(pointer);
		//System.out.println("ID: " + Thread.currentThread().getId() + "\tSteering Radians: " + radians);
		return -4 * (float) Math.max(-.25, Math.min(radians, .25));
	}
	
	public static void spamChat(int playerIndex) {
		RLBotDll.sendQuickChat(playerIndex, false, QuickChatSelection.Compliments_NiceOne);
	}
	
	protected static ControlsOutput flipAtBall(CarData car, BallData ball, Controller controller, int flipDelay) {
		if (car.hasWheelContact) {
			System.out.println("ID: " + Thread.currentThread().getId() + "\tInitial Jump");
			controller.resetJumpTimer();
			return new ControlsOutput()
					.withJump();
			
		} else {
			if (controller.getJumpTimer() > flipDelay) {
				Vector2 pointer = ball.position.flatten().minus(car.position.flatten());
				double radians = car.orientation.noseVector.flatten().correctionAngle(pointer);
				float pitch = (float) -Math.cos(radians);
				float yaw = (float) -Math.sin(radians);
				//System.out.println("ID: " + Thread.currentThread().getId() + "\tJump Flip\tPitch: " + pitch + "\tYaw: " + yaw + "\tRadians: " + radians);
				return new ControlsOutput()
						.withPitch(pitch)
						.withYaw(yaw)
						.withJump();
				
			} else {
				//System.out.println("ID: " + Thread.currentThread().getId() + "\tJump Pre Flip\t Counter: " + controller.getJumpTimer());
				controller.incrementJumpTimer();
				return new ControlsOutput()
						.withJump(false);
			}
		}
	}
}
