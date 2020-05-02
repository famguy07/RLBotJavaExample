package fambot.controller;

import fambot.FamBotMemory;
import fambot.boost.BoostManager;
import fambot.boost.BoostPad;
import fambot.input.DataPacket;
import fambot.input.ball.BallData;
import fambot.input.car.CarData;
import fambot.output.ControlsOutput;
import fambot.vector.Vector2;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.QuickChatSelection;

public class FamBotActions {
	public static void flyUp() {
	       // System.out.println("X: "+ ball3.x + "\tY: " + ball3.y + "\tZ: " + ball3.z);

//      if (ball3.z > 400) {
//    	return new ControlsOutput()
//    			.withJump()
//                .withSteer(goLeft ? -1 : 1)
//                .withThrottle(1);
//    }
//    return new ControlsOutput()
//            .withSteer(goLeft ? -1 : 1)
//            .withThrottle(1);
	}

	public static ControlsOutput getNearestLargeBoost(DataPacket input) {
		CarData car = input.car;
		
		BoostPad pad = BoostManager.nearestBoost(car.position.flatten(), true, true);

		return new ControlsOutput()
				.withSteer(steer2d(car, pad.getLocation().flatten(), null))
				.withThrottle(1);
	}

	public static ControlsOutput ballChase(DataPacket input) {
		BallData ball = input.ball;
		CarData car = input.car;

		return new ControlsOutput()
				.withSteer(steer2d(car, ball.position.flatten(), ball.velocity.flatten()))
				.withThrottle(1)
				.withBoost(true);
	}

	//TODO implement leading with targetDir
	public static float steer2d(CarData car, Vector2 targetPos, Vector2 targetDir) {
		Vector2 pointer = targetPos.minus(car.position.flatten());
		double radians = car.velocity.flatten().correctionAngle(pointer);
		//System.out.println("ID: " + Thread.currentThread().getId() + "\tSteering Radians: " + radians);
		return -5 * (float) Math.max(-.2, Math.min(radians, .2));
	}
	
	public static void spamChat(int playerIndex) {
		RLBotDll.sendQuickChat(playerIndex, false, QuickChatSelection.Compliments_NiceOne);
	}
	
	private static ControlsOutput hitBall(CarData car, BallData ball) {
		return null;
	}
	
	public static ControlsOutput kickoff(DataPacket input, FamBotMemory memory) {	
		CarData car = input.car;
		BallData ball = input.ball;
		
		if (memory.getKickoffBoost() == null || !memory.getKickoffBoost().isActive()) {
			memory.setKickoffBoost(BoostManager.nearestBoost(car.position.flatten(), false, true));
			if (car.position.flatten().distance(ball.position.flatten()) < car.position.flatten().distance(memory.getKickoffBoost().getLocation().flatten())) {
				memory.setKickoffBoost(null);
			}
		}
		if (car.hasWheelContact) {
			double distance = car.position.flatten().distance(ball.position.flatten());
			if (memory.getKickoffJumpTimer() == 0 && distance < 850) {
				System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff First Jump");
				memory.incrementKickoffJumpTimer();
				return new ControlsOutput()
						.withJump()
						.withThrottle(1);
				
			} else {
				System.out.println("ID: " + Thread.currentThread().getId() + "\tBoostPad: " + (memory.getKickoffBoost() == null ? "null" : (memory.getKickoffBoost().isActive() ? "Active" : "Inactive")));
				if(false && memory.getKickoffBoost() != null && memory.getKickoffBoost().isActive()) {
					System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff Boost");
					float steer = steer2d(input.car, memory.getKickoffBoost().getLocation().flatten(), null);
					return new ControlsOutput()
							.withBoost()
							.withThrottle(1)
							.withSteer(steer);
					
				} else {
					System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff Ball Chase");
					return ballChase(input);
					
				}
			}
		} else {
			if (memory.getKickoffJumpTimer() > 10) {
				System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff Flip");
				return new ControlsOutput()
						.withPitch(-1)
						.withJump();
				
			} else {
				System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff Pre Flip\tTick Counter: " + memory.getKickoffJumpTimer());
				memory.incrementKickoffJumpTimer();
				return new ControlsOutput();
			}
		}
	}
}
