/*
Provides main functionality of the game such as ball related data (movement, position, etc.), graphics, and user input.
*/

//Library imports required for graphics and event listeners (user inputs)
import java.awt.*; 
import java.awt.event.*; //Event listener import for user inputs 
import java.awt.geom.Ellipse2D; //Graphic import to draw ellipses
import java.awt.geom.Rectangle2D; //Graphic import to draw Rectangles
import java.util.HashMap; 
import java.util.HashSet;

import javax.swing.*; //Import for drawing graphics


public class Game extends JPanel implements KeyListener, ActionListener {
	
	private int height, width; //integer declarations for screen height and width.
	private Timer t = new Timer(5, this); //A timer object set to control refresh rate of game
	private boolean first;
	
	private HashSet<String> keys = new HashSet<String>();
	
	//All data information pertaining to pad speed, size and position.
	private final int SPEED = 1; // A constant integer representing the pad's movement speed.
	private int padH = 10, padW = 40; //Two integers representing the pad height and width.
	private int bottomPadX, topPadX; 
	private int inset = 10;
	
	// All data pertaining to the the ball, such as ball position, speed and size.
	private double ballX, ballY, velX = 1, velY = 1, ballSize = 20;//Declerations for the ball's X position on the screen, Y position, Velocity in the X direction and Y direction as well as radius. 
	
	// score
	private int scoreTop, scoreBottom;//Integers holding two seperate score systems, the score on top and score on bottom.
	
	public Game() {
		addKeyListener(this); //Allows a "listener" to the game allowing all key inputs to be recorded.
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		first = true;
		t.setInitialDelay(100);
		t.start();
	}
	
	
	/*
	Creates all graphics related components within the game. 
	*/
	@Override
	protected void paintComponent(Graphics g) {//Takes in parameter g, which is a graphics object that all graphics are drawn on.
		super.paintComponent(g);//Allows the usage of the original paintComponent main class.
		Graphics2D g2d = (Graphics2D) g;//Creating a 2D graphics object by typecasting graphics object g
		height = getHeight();//stores height of screen
		width = getWidth();//stores width of screen

		// Refers to the position of the ball and pad when the game first starts up
		if (first) {//If the game is starting up for the first time
			bottomPadX = width / 2 - padW / 2;//set the bottompad's X position to the center of the screen//
			topPadX = bottomPadX;//Set the toppad's X position equal to the bottomPads X position.
			ballX = width / 2 - ballSize / 2; //Set the X position of the ball to the center
			ballY = height / 2 - ballSize / 2;//Set the Y position of the ball to the center
			first = false; //Set the boolean first to false to indicate that we are no longer starting up for the first time/
		}
		
		
		Rectangle2D bottomPad = new Rectangle(bottomPadX, height - padH - inset, padW, padH);
		//Creates a 2D rectangle with 4 parameters, bottomPad's X position, bottomPad's Y position, pad width, pad height
		g2d.fill(bottomPad);
		
		//Creates a 2D rectangle with 4 parameters, topPad's X position, topPad's Y position, pad width, pad height
		Rectangle2D topPad = new Rectangle(topPadX, inset, padW, padH);
		g2d.fill(topPad);
		
		// Creates ball object by using ellipses graphics class. Takes in 4 parameters, Ball's x position, Ball's y position, Ball's radius, Ball's radius.
		Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
		g2d.fill(ball);
		
		
		String scoreB = "Bottom: " + new Integer(scoreBottom).toString();//Create a string that displays the score of the bottom pad
		String scoreT = "Top: " + new Integer(scoreTop).toString();//Create a string that displays the score of the top pad
		g2d.drawString(scoreB, 10, height / 2);//Display the string
		g2d.drawString(scoreT, width - 50, height / 2);//Display the string
	}
	//
	@Override
	public void actionPerformed(ActionEvent e) {
		//Check if the ball collides with the side walls
		if (ballX < 0 || ballX > width - ballSize) {//If the ball's X position meets with either wall
			velX = -velX;//Reverse the velocity, making the ball go in the other direction
		}
		//if the ball hits the bottom wall
		if (ballY < 0) {//check if the ball's Y position meets the bottom walls
			velY = -velY;//Reverse the ball's y velocity
			++ scoreBottom;//Increase the bottom score system
		}
		
		if (ballY + ballSize > height) { //If the ball hits the top wall
			velY = -velY; //Reverse the velocity in the y direction
			++ scoreTop; //Increase the top score system
		}
		
		if (ballY + ballSize >= height - padH - inset && velY > 0) //Check to see is within the pad's height
			if (ballX + ballSize >= bottomPadX && ballX <= bottomPadX + padW)//Check to see if the ball's x position is within the pad's reach
				velY = -velY;//Reverse the ball's Y velocity

		
		if (ballY <= padH + inset && velY < 0) //Check to see if the ball is within the top pad's height range
			if (ballX + ballSize >= topPadX && ballX <= topPadX + padW)//Check to see if the ball's X position is within the pad's reach
				velY = -velY;// Reverse the Y velocity

		ballX += velX; //Moves the ball in the X direction
		ballY += velY; //Moves the ball in the Y direction
		
		//
		if (keys.size() == 1) {
			if (keys.contains("LEFT")) { //If "left" key is pressed
				bottomPadX -= (bottomPadX > 0) ? SPEED : 0; //Move the bottom pad to the left by the value of speed
			}
			else if (keys.contains("RIGHT")) {//If "right" key is pressed
				bottomPadX += (bottomPadX < width - padW) ? SPEED : 0;//Move bottom pad to the right by the value of speed
 			}
		}
		
		
		double delta = ballX - topPadX; // double variable representing difference betwen BAll po
		if (delta > 0) {// if the difference is greater than 0, meaning the pad is to the left of the ball
			topPadX += (topPadX < width - padW) ? SPEED : 0;//Move the top pad to the right by the value of speed
		}
		else if (delta < 0) { // if the difference is less than 0
			topPadX -= (topPadX > 0) ? SPEED : 0; //Move the top pad to the left by the value of speed
		}
		
		repaint(); //redraw the canvas with all the updated positions
	}

	@Override
	public void keyTyped(KeyEvent e) {} //Required method due to interface use

	@Override
	public void keyPressed(KeyEvent e) {// Method to respond when user presses key
		int code = e.getKeyCode(); //Store what key was pressed in integer variable
		switch (code) { // Create conditionals based on which key has been pressed
		case KeyEvent.VK_LEFT: // If the left arrow key has been pressed 
			keys.add("LEFT"); //Add the string "LEFT" to the hash set
			break;// break the code 
		case KeyEvent.VK_RIGHT: //If the right arrow key has been pressed 
			keys.add("RIGHT"); //Add the string "RIGHT" to the hash set
			break; //break the code
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { //method to respond when user releases key
		int code = e.getKeyCode(); //Store what key was released in integer variable
		switch (code) { //Create conditionals based on which key has been released
		case KeyEvent.VK_LEFT: //If the left arrow key has been released 
			keys.remove("LEFT"); //Remove the "LEFT" string from the hash set
			break;//Break the code
		case KeyEvent.VK_RIGHT: //If the right arrow key has been released
			keys.remove("RIGHT"); //Remove the "RIGHT" String from the hash set
			break; //Break the code
		}
	}
}
