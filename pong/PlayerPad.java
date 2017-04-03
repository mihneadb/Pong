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
	
	public void moveRight(int width) {
		this.x += (x < width - this.width) ? speed : 0;
	}
	
	public void moveLeft() {
		this.x -= (x > 0) ? speed : 0;	
	}
	
}