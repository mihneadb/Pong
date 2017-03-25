package pong;

import java.util.HashSet;

public class PlayerPad extends Pad {
	
	// The constructor calls the constructor for Pad, and also sets the y position accordingly.
	public PlayerPad(int padH, int padW, int padX, int speed, int height, int inset) {
		super(padH,padW,padX, speed);
		this.y = height - padH - inset;
	}
	
	public PlayerPad() {
		this(1,1,0,0,0,0);
	}
	
	@Override
	public void resetState(int padH, int padW, int padX, int speed, int height, int inset) {
		super.resetState(padH, padW, padX, speed, height, inset);
		this.y = height - inset - padH;
	}
	
	/*
	 * The following method takes as input the string of currently pressed keys (as recorded by
	 * the KeyListener interface in the Game class) and updates the position of the paddle
	 * accordingly. The integer width is the width of the frame.
	 * 
	 * Remember that keys contains the elements "LEFT" or "RIGHT" (or none) according to whether
	 * the left or right keys are currently pressed.
	*/
	public void updatePos(HashSet<String> keys, int width) {
		if (keys.size() == 1) {
			if (keys.contains("LEFT")) {
				/*
				 * The mathematical operation here reads: subtract from this.x: SPEED if 
				 * this.x > 0, otherwise 0. In other words, move the bottom pad left unless
				 * it is already as far left as it can go.
				 */
				this.x -= (this.x > 0) ? this.speed : 0;
			}
			else if (keys.contains("RIGHT")) {
				/*
				 * Similarly here: move the bottom pad right, unless it is already as far right as
				 * it can go.
				 */
				this.x += (this.x < width - this.width) ? this.speed : 0;
			}
		}
	}
	
}
