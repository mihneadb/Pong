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
		super(padH,padW,padX,speed);
		resetState(padH,padW,padX,speed,height,inset,whichPlayer);
	}
	
	public PlayerPad() {
		this(1,1,0,0,0,0,1);
	}
	
	@Override
	public void resetState(int padH, int padW, int padX, int speed, int height, int inset, int whichPlayer) {
		super.resetState(padH, padW, padX, speed, height, inset, whichPlayer);
		this.whichPlayer = whichPlayer;
		switch (whichPlayer) {
		case 1:
			y = height - inset - padH;
			break;
		case 2:
			y = inset;
		}
	}
	
	public void updatePos(HashSet<String> keys, int width) {
		switch (whichPlayer) {
		case 1:
			if (keys.contains("LEFT")) {
				if (!keys.contains("RIGHT")) {
					/*
					 * The mathematical operation here reads: subtract from this.x: SPEED if 
					 * this.x > 0, otherwise 0. In other words, move the bottom pad left unless
					 * it is already as far left as it can go.
					 */
					x -= (x > 0) ? speed : 0;
				}
			}
			else if (keys.contains("RIGHT")) {
				if (!keys.contains("LEFT")) {
					/*
					 * Similarly here: move the bottom pad right, unless it is already as far right as
					 * it can go.
					 */
					x += (x < width - this.width) ? speed : 0;
				}
			}
			break;
		case 2:
			if (keys.contains("1")) {
				if (!keys.contains("3")) {
					x -= (x > 0) ? speed : 0;
				}
			}
			else if (keys.contains("3")) {
				if (!keys.contains("1")) {
					x += (x < width - this.width) ? speed : 0;
				}
			}
			break;
		}
	}
	
}