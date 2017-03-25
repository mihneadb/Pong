package pong;

import java.awt.geom.Rectangle2D;

public class Pad extends Rectangle2D.Double {
	
	/*
	 *  The "protected" modifier means that the field will be able to be accessed by subclasses,
	 *  which is something we will require.
	 */
	protected int speed;
	
	public Pad(int padH, int padW, int padX, int speed) {
		this.height = padH;
		this.width = padW;
		this.x = padX;
		this.speed = speed;
	}
	
	/*
	 * As in the Ball class, we have a method which resets the state of Pad. This will be overridden
	 * (extended) in each of the subclasses PlayerPad and AIPad, to account for the different required
	 * values of y.
	 * 
	 * Note that the parameters height and inset are not used here; but they will be used in the later
	 * overrides, so we have to include them here as well since otherwise the parameter structure will
	 * be wrong.
	 */
	public void resetState(int padH, int padW, int padX, int speed, int height, int inset) {
		this.height = padH;
		this.width = padW;
		this.x = padX;
		this.speed = speed;
	}

}
