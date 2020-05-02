package fambot.input.car;


import fambot.boost.BoostPad;
import fambot.vector.Vector3;

/**
 * Basic information about the car.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class CarData {

    public final Vector3 position; // (0,0,0) center field
    public final Vector3 velocity;
    public final CarOrientation orientation;
    public final double boost; // 0 to 100
    public final boolean hasWheelContact;
    public final boolean isSupersonic;
    public final int team; // 0 = blue, 1 = orange

    /**
     * This is not really a car-specific attribute, but it's often very useful to know. It's included here
     * so you don't need to pass around DataPacket everywhere.
     */
    public final float elapsedSeconds;

    public CarData(rlbot.flat.PlayerInfo playerInfo, float elapsedSeconds) {
        this.position = new Vector3(playerInfo.physics().location());
        this.velocity = new Vector3(playerInfo.physics().velocity());
        this.orientation = CarOrientation.fromFlatbuffer(playerInfo);
        this.boost = playerInfo.boost();
        this.isSupersonic = playerInfo.isSupersonic();
        this.team = playerInfo.team();
        this.hasWheelContact = playerInfo.hasWheelContact();
        this.elapsedSeconds = elapsedSeconds;
    }
    
    
}
