package pong;

import java.util.HashSet;

public class PlayerPad extends Pad {
	
	/*
	 * We need to specify whether it will be controlled by LEFT/RIGHT or  by A/D. For this 
	 * we use the integer variable whichPlayer, with values 1 and 2 respectively.
	 */
	public int whichPlayer;
	
	// The constructor calls the constructor for Pad, and also sets the y position accordingly.
	public PlayerPad(int padH, int padW, int padX, int speed, int height, int inset, int whichPlayer) {
		super(padH,padW,padX, speed);
		this.whichPlayer = whichPlayer;
		y = height - padH - inset;
	}
	
	public PlayerPad() {
		this(1,1,0,0,0,0,1);
	}
	
	@Override
	public void resetState(int padH, int padW, int padX, int speed, int height, int inset, int whichPlayer) {
		super.resetState(padH, padW, padX, speed, height, inset, whichPlayer);
		this.whichPlayer = whichPlayer;
		y = height - inset - padH;
	}
	
	/*
	 * The following method takes as input the string of currently pressed keys (as recorded by
	 * the KeyListener interface in the Game class) and updates the position of the paddle
	 * accordingly. The integer width is the width of the frame.
	 * 
	 * Remember that keys contains the elements "LEFT", "RIGHT", "A" or "D" (or none) according
	 * to whether these keys are currently pressed. We use the whichPlayer field to decide whether
	 * to respond to LEFT/RIGHT or to A/D.
	 */
	public void updatePos(HashSet<String> keys, int width) {
		if (whichPlayer == 1) {
			if (keys.contains("LEFT")) {
				/*
				 * The mathematical operation here reads: subtract from x: SPEED if 
				 * x > 0, otherwise 0. In other words, move the bottom pad left unless
				 * it is already as far left as it can go.
				 */
				this.x -= (x > 0) ? speed : 0;
			}
			if (keys.contains("RIGHT")) {
				/*
				 * Similarly here: move the bottom pad right, unless it is already as far right as
				 * it can go.
				 */
				this.x += (x < width - this.width) ? speed : 0;
			}
		}
		else if (whichPlayer == 2) {
			if (keys.contains("A")) {
				this.x -= (x > 0) ? speed : 0;
			}
			if (keys.contains("D")) {
				this.x += (x < width - this.width) ? speed : 0;
			}
		}
	}
}