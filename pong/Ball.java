package pong;

import java.awt.geom.Ellipse2D;

public class Ball extends Ellipse2D.Double {

	/*
	 * See docs/classes.md for details on the overall class structure. There, as explained, we do
	 * not have to implement all of the fields and methods we need because some (but not all) are
	 * inherited from Ellipse2D.Double. These are the fields ballX, ballY, ballSize (inherited as
	 * x,y,height) and the methods getBallX(), getBallY() (inherited as getX(), getY()).
	 */
	
	/*
	 * We initialise the fields which we require. They are all double-length floating-point numbers.
	 * 
	 * We don't need ballX, ballY, since these fields are inherited from Ellipse2D.Double as
	 * this.x and this.y, respectively. Similarly, ballSize is obtained as this.height or
	 * this.width (assuming we have called the constructor correctly to get a circle, rather
	 * than a more general ellipse).
	 */
	private double velX, velY;
	
	/*
	 * We define a constructor method which takes as input the size (diameter) of the ball and the
	 * initial X and Y positions.
	 */
	public Ball(double ballX, double ballY, double ballSize, double velX, double velY) {
		/*
		 * The super keyword here calls the constructor of the superclass (in this case Ellipse2D.Double).
		 * We use ballSize twice because we want the height and the width of the ellipse to be the same,
		 * i.e. we want a circle.
		 */
		super(ballX, ballY, ballSize, ballSize);
		this.velX = velX;
		this.velY = velY;
	}
	
	/*
	 * We define an alternative no-variables constructor that sets simple values.
	 */
	public Ball() {
		this(0,0,1,1,1);
	}
	
	/*
	 * We define a method which allows us to completely reset all the ball's fields. Think of this
	 * as a constructor, but which can be called after the ball has been constructed.
	 */
	public void resetState(double x, double y, double ballSize, double velX, double velY) {
		this.x = x;
		this.y = y;
		this.height = ballSize;
		this.width = ballSize;
		this.velX = velX;
		this.velY = velY;
	}
	
	/*
	 * A method updating the position of the ball. This will be called on each call of the timer.
	 */
	public void updatePos() {
		this.x += this.velX;
		this.y += this.velY;
	}
	
	/* 
	 * A method which detects collision with the left and right walls, by updating the horizontal
	 * velocity accordingly. The input is the width of the frame (to catch the collision with the
	 * right wall).
	 */
	public void detectLRCollision(int frameWidth) {
		if (this.x < 0 || this.x > frameWidth - this.width) {
			this.velX = -this.velX;
		}
	}
	
	/*
	 * A method which detects collision with the top and right walls, by updating the vertical
	 * velocity accordingly and returning "T" if the top wall is hit and "B" if the bottom wall is hit.
	 * The input is the height of the frame (to catch the collision with the bottom wall).
	 */
	public String detectTBCollision(int frameHeight) {
		String side = "";
		
		if (this.y < 0) {
			this.velY = -this.velY;
			side = "T";
		}
		
		if (this.y + this.height > frameHeight) {
			this.velY = -this.velY;
			side = "B";
		}
		
		return side;
	}
	
	/*
	 * A method which detects collision with the player's pad and reacts by updating the vertical
	 * velocity. Eventually this will take as input a PlayerPad object and inset, but for the moment
	 * we give as input the individual values: frameHeight, padH, padW, padX, inset.
	 */
	public void detectPlayerPadCollision(int frameHeight, int padH, int padW, double padX, int inset) {
		if (this.y+ this.height >= frameHeight - padH - inset && this.velY > 0)
			if (this.x + this.width >= padX && this.x <= padX + padW)
				this.velY = -this.velY;
	}
	
	/*
	 * A method which detects collision with the AI pad and reacts by updating the vertical velocity.
	 * Eventually this will take as input an AIPad object and inset, but for the moment we give as 
	 * input the individual values: frameHeight, padH, padW, padX, inset.
	 */
	public void detectAIPadCollision(int frameHeight, int padH, int padW, double padX, int inset) {
		if (this.y <= padH + inset && this.velY < 0)
			if (this.x + this.width >= padX && this.x <= padX + padW)
				this.velY = -this.velY;
	}
}
