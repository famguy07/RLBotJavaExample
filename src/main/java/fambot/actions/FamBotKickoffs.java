package fambot.actions;

import fambot.boost.BoostManager;
import fambot.boost.BoostPad;
import fambot.controller.Controller;
import fambot.input.DataPacket;
import fambot.input.ball.BallData;
import fambot.input.car.CarData;
import fambot.output.ControlsOutput;
import fambot.vector.Vector2;

public class FamBotKickoffs {
	private static final int[][] kickoffBoosts = {
			{11},		//Blue Right 0
			{10},		//Blue Left 1
			{7, 13},	//Blue Back 2
			{22},		//Orange Right 3
			{23},		//Orange Left 4
			{26, 20}	//Orange Back 5
	};
	//third value is the index of the corresponding boosts above
	private static final int[][] kickoffLocations = {
			{-2048, -2560, 0},	//Blue Right
			{2048, -2560, 1},	//Blue Left
			{0, -4608, 2},		//Blue Back
			{-256, -3840, 2},	//Blue Back Right
			{256, -3840, 2},	//Blue Back Left
			{2048, 2560, 3},	//Orange Right
			{-2048, 2560, 4},	//Orange Left
			{0, 4608, 5},		//Orange Back
			{256, 3840, 5},		//Orange Back Right
			{-256, 3840, 5},	//Orange Back Left
	};
	public static ControlsOutput kickoffKill(DataPacket input, Controller controller) {	
		CarData car = input.car;
		BallData ball = input.ball;
		
		double distance = car.position.flatten().distance(ball.position.flatten());
		if (distance < 850) {
			return FamBotActions.flipAtBall(car, ball, controller, 8);
			
		} else {
			if(controller.getKickoffBoost() == -1) {
				for (int i = 0; i < kickoffLocations.length; i++) {
					double dist = car.position.flatten().distance(new Vector2(kickoffLocations[i][0], kickoffLocations[i][1]));
					System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff Location Distance: " + dist);
					if (dist < 1) {
						controller.setKickoffLocation(i);
						controller.setKickoffBoost(kickoffBoosts[kickoffLocations[i][2]][0]);
						break;
					}
				}
			}
			
			if(controller.getKickoffBoost() != -1) {
				BoostPad pad = BoostManager.getBoosts().get(controller.getKickoffBoost());
				//check for active pad and change to second pad or ball as target
				if (!pad.isActive()) {
					if (kickoffBoosts[kickoffLocations[controller.getKickoffLocation()][2]].length > 1) {
						controller.setKickoffBoost(kickoffBoosts[kickoffLocations[controller.getKickoffLocation()][2]][1]);
						pad = BoostManager.getBoosts().get(controller.getKickoffBoost());
					} else {
						controller.setKickoffLocation(-1);
						controller.setKickoffBoost(-1);
						return FamBotActions.ballChase(input); 
					}
				}
				float steer = FamBotActions.steer2d(car, pad.getLocation().flatten());
				return new ControlsOutput()
						.withBoost()
						.withThrottle(1)
						.withSteer(steer);
				
			} else {
				System.out.println("ID: " + Thread.currentThread().getId() + "\tKickoff Ball Chase");
				return FamBotActions.ballChase(input);
				
			}
		}
	}
}
