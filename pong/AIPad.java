package pong;

public class AIPad extends Pad {

	public AIPad(int padH, int padW, int padX, int speed, int height, int inset) {
		super(padH,padW,padX, speed);
		y = inset;
	}
	
	public AIPad() {
		this(1,1,0,0,0,0);
	}
	
	@Override
	public void resetState(int padH, int padW, int padX, int speed, int height, int inset, int whichPlayer) {
		super.resetState(padH, padW, padX, speed, height, inset, whichPlayer);
		y = inset;
	}
	
	/*
	 * The following method implements the paddle's AI: it moves based on the position of the ball,
	 * constantly trying to align itself horizontally with the ball. The width parameter is the
	 * width of the frame.
	 */
	public void updatePos(double ballX, int width) {
		double delta = ballX - x;
		if (delta > 0) {
			x += (x < width - this.width) ? speed : 0;
		}
		else if (delta < 0) {
			x -= (x > 0) ? speed : 0;
		}
	}
	
}
