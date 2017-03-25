package pong;

public class AIPad extends Pad {

	public AIPad(int padH, int padW, int padX, int speed, int height, int inset) {
		super(padH,padW,padX, speed);
		this.y = inset;
	}
	
	public AIPad() {
		this(1,1,0,0,0,0);
	}
	
	@Override
	public void resetState(int padH, int padW, int padX, int speed, int height, int inset) {
		super.resetState(padH, padW, padX, speed, height, inset);
		this.y = inset;
	}
	
	/*
	 * The following method implements the paddle's AI: it moves based on the position of the ball,
	 * constantly trying to align itself horizontally with the ball. The width parameter is the
	 * width of the frame.
	 */
	public void updatePos(double ballX, int width) {
		double delta = ballX - this.x;
		if (delta > 0) {
			this.x += (this.x < width - this.width) ? this.speed : 0;
		}
		else if (delta < 0) {
			this.x -= (this.x > 0) ? this.speed : 0;
		}
	
	}
	
}
