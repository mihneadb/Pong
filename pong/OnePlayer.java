package pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;

/*
 * The KeyListener is so that the instances of Game can listen for key strokes, and
 * the ActionListener interface is so that they can listen for action events, in this
 * case triggers from the timer (to implement the game loop).
 */
public class OnePlayer extends JPanel implements KeyListener, ActionListener {
	
	// Variables to record the height and width of the panel.
	private int height = getHeight();
	private int width = getWidth();
	
	// Scores object to record the scores.
	private Scores scores = new Scores();

	/**
	 * The Timer "t" fires every 5 milliseconds, and refers to "this",
	 * namely the current object (an instance of the Game class). In
	 * general, Timer(n,x) sets up a timer which fires every "n" milliseconds,
	 * with reference to object "x". Here the "with reference to" means that
	 * the given object should contain a method called "actionPerformed" 
	 * (which takes a single ActionEvent variable; don't worry about this)
	 * which is then triggered every "n" milliseconds. In our case, the object is 
	 * an instance of Game, and the method "actionPerformed" can be found below.
	 * 
	 * Needs to be public because we have to access it from Game in order to stop it when
	 * the escape key is pressed.
	 */
	public Timer t = new Timer(5, this);

	/*
	 * As a data structure, this creates a set whose elements are Strings. It is backed by a
	 * hash table, which basically means that it functions as a dictionary (with keys indexing 
	 * values).
	 * 
	 * More importantly, this data structure is used to keep track of which keys are being pressed.
	 * The currently pressed keys are added to keys in the keysPressed method below.
	 */
	private HashSet<String> keys = new HashSet<String>();
	
	// Used to execute special code on the first iteration of the timer; see below.
	private boolean first = true;
	
	/**
	 * Initialise variables for the paddle(s). In my reworking of this,
	 * these will be contained in a separate Pad class.
	 */
	private final int BOTTOM_SPEED = 2;
	private final int TOP_SPEED = 2;
	private int padH = 10, padW = 40;
	private int inset = 10;
	
	/*
	 * Initialise the two paddle objects. Since not all of the game parameters have been set yet,
	 * we take the default initialisation for the moment; we will reset all the fields later.
	 */
	private PlayerPad playerPad = new PlayerPad();
	private AIPad aiPad = new AIPad();
	
	/**
	 * Initialise variables for the ball. In my reworking of this,
	 * these will be contained in a separate Ball class.
	 */
	private final double INIT_VEL_X = 2.5, INIT_VEL_Y = 2.5;
	private double ballSize = 20;
	
	/*
	 * Initialise the ball object. This depends on the parameters given above.
	 */
	private Ball ball = new Ball();
	
	/*
	 *  Zero-variable constructor for the class. Since an instantiation of this class appears
	 *  as a field in the Game class, the constructor is called when the Game object is created
	 *  (in Main).
	 *  
	 *  As such, this isn't really the "true" constructor. There is only ever one OnePlayer object,
	 *  which we simply reset every time a new game is started (perhaps we will change this later).
	 *  The "constructor" is really the resetState() method, which sets up the game in its initial
	 *  state and starts the timer (the timer is stopped externally, from the Game class, whenever
	 *  the escape key is pressed).
	 */
	public OnePlayer() {	
		resetState();
	}
	
	/*
	 * The @Override just indicates that we are overriding a method from a superclass (in
	 * this case javax.swing.JPanel).
	 * 
	 * Sometimes, if we are extending an abstract class, there are certain abstract (i.e. empty)
	 * methods in the superclass which must be overridden. This is not the case here (JPanel is
	 * not an abstract class), but it is worth bearing in mind.
	 * 
	 * The "protected" modifier ensures that the method will be visible to any subclasses (but is
	 * not public in the full sense).
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		/*
		 * The "super" keyword refers to the superclass; in this case JPanel. In this context
		 * it allows us to use the overridden method JPanel.paintComponent (in later lines
		 * we then add more stuff, so that the override actually changes something).
    	 */
		super.paintComponent(g);
		
		/*
		 * The class is java.awt.Graphics2D (awt standing for "abstract window toolkit"). It extends
		 * java.awt.Graphics. It has a specified Component onto which it draws (presumably the frame),
		 * and in-built methods for drawing things (see for instance g2d.fill below).
		 * 
		 * The meaning of the following line is to convert g from a Graphics object to a Graphics2D 
		 * object, which we name g2d. We will then be able to use the methods of g2d (in particular
		 * g2d.fill(Shape s)) to draw things.
		 */
		Graphics2D g2d = (Graphics2D) g;
		
		/*
		 * getHeight and getWidth are methods inherited from JPanel. As you might guess, they return
		 * the height and the width of the panel (ultimately, they are inherited from JComponent).
		 */
		height = getHeight();
		width = getWidth();
		
		/*
		 * There's an annoying issue when transitioning from the title screen to the gameplay.
		 * When we resize the Game frame, sometimes (though not always) things don't happen
		 * fast enough, which means that the game timer starts before the frame has been drawn.
		 * This causes the ball to be placed in the top-left corner, which then means it bounces
		 * off the walls, which then messes up the starting velocity and the scores.
		 * 
		 * I believe that this is really a hardware issue. My current fix is to include the
		 * following lines, which are called every time we enter the one player phase. (The first
		 * field is reset in the resetState() command which is called when we start the transition.)
		 */
		if (first) {
			resetState();
			first = false;
		}
	
		// Draw all the objects.
		g2d.fill(this.playerPad);
		g2d.fill(this.aiPad);
		g2d.fill(this.ball);
		
		// Print the scores to the panel, as strings.
		String scoreB = "Bottom: " + new Integer(scores.getScoreBottom()).toString();
		String scoreT = "Top: " + new Integer(scores.getScoreTop()).toString();
//		String scoreB = "velX: " + new Double(ball.getVelX()).toString();
	//	String scoreT = "velY: " + new Double(ball.getVelY()).toString();
		g2d.drawString(scoreB, 10, height / 2);
		g2d.drawString(scoreT, width - 50, height / 2);
	}
	
	// The following method is that which is called on each iteration of the game loop.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Reverse horizontal velocity of ball if it collides with the left or right walls.
		ball.detectLRCollision(width);
		
		/*
		 * Reverse vertical velocity of ball, and record which wall was hit, if it collides with
		 * the top or bottom walls (in my reworking of this, we could change this so that if it
		 * collides with top or bottoms walls, the ball gets reset in the middle with a fixed velocity).
		 */
		String scorer = ball.detectTBCollision(height);
		//Update scores, depending on which wall was hit.
		if (scorer=="T") {
			scores.bottomScores();
		}
		if (scorer=="B") {
			scores.topScores();
		}
		
		// Reverse vertical velocity of ball if it collides with bottom pad.
		ball.detectPlayerPadCollision(height, padH, padW, playerPad.getX(), inset);
		
		// Reverse vertical velocity of ball if it collides with top pad.
		ball.detectAIPadCollision(height, padH, padW, aiPad.getX(), inset);

		// Move the ball one frame, as dictated by the current velocities.
		ball.updatePos();
		
		// Move the player's paddle as dictated by key presses.
		playerPad.updatePos(keys, width);
		
		// Move the AI paddle as dictated by the ball position.
		aiPad.updatePos(ball.getX(), width);
		
		/*
		 * This is a method of JPanel; in fact, the no-parameter version comes from the superclass
		 * Component. In effect, this calls the paintComponent() method which we overrode above. I
		 * think it also first gets rid of the previously drawn image (so things don't get drawn on
		 * top of each other).
		 */
		repaint();
	}

	/*
	 * The following methods process the keystrokes which are received. Since Game implements the 
	 * keyListener interface, these are sensitive to keystrokes: the former is called whenever a key
	 * is pressed, the latter whenever one is released. Which key was pressed or released is recorded in
	 * the KeyEvent parameter e.
	 * 
	 * Looking at the documentation, one sees that the KeyEvent class has a large number of statics,
	 * which are essentially numbers indexing the various keys which could be pressed. So, for instance,
	 * e.VK_A is a static int which gives some number (in this case the same as the ASCII number)
	 * representing the "A" key.
	 * 
	 * On the other hand, there is a method called getKeyCode, which returns the number corresponding to
	 * the key which has been pressed. So in the methods below, code is the integer corresponding to
	 * the pressed key, and the switch statement compares code to the values KeyEvent.VK_LEFT,
	 * KeyEvent.VK_RIGHT to see if they agree.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			keys.add("LEFT");
			break;
		case KeyEvent.VK_RIGHT:
			keys.add("RIGHT");
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			keys.remove("LEFT");
			break;
		case KeyEvent.VK_RIGHT:
			keys.remove("RIGHT");
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	/*
	 * A simple method which resets the object to its initial state. Used when we want to restart
	 * a game without creating a new OnePlayer object.
	 */
	public void resetState() {
		int height = getHeight();
		int width = getWidth();
		playerPad.resetState(padH, padW, width/2 - padW/2, BOTTOM_SPEED, height, inset);
		aiPad.resetState(padH, padW, width/2 - padW/2, TOP_SPEED, height, inset);
		ball.resetState(width/2 - ballSize/2, height/2 - ballSize/2, ballSize, INIT_VEL_X, INIT_VEL_Y);
		scores.resetState();
		first = true;
		t.start();
	}
}