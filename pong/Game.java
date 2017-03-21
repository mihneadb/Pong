package pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener {
	
	//Variables to record the height and width of the panel.
	private int height, width;

	/**
	 * The Timer "t" fires every 5 milliseconds, and refers to "this",
	 * namely the current object (an instance of the Game class). In
	 * general, Timer(n,x) sets up a timer which fires every "n" milliseconds,
	 * with reference to object "x". Here the "with reference to" means that
	 * the given object should contain a method called "actionPerformed" 
	 * (which takes a single ActionEvent variable; don't worry about this)
	 * which is then triggered every "n" milliseconds. In our case, the object is 
	 * an instance of Game, and the method "actionPerformed" can be found below.
	 */
	private Timer t = new Timer(5, this);

	/* 
	 * Define a boolean variable which is true before setting up the initial positions,
	 * and then turns false once these have been set. Think "first" for "first iteration."
	 */
	private boolean first;
	
	/*
	 * As a data structure, this creates a set whose elements are Strings. It is backed by a
	 * hash table, which basically means that it functions as a dictionary (with keys indexing 
	 * values).
	 * 
	 * More importantly, this data structure is used to keep track of which keys are being pressed.
	 * The currently pressed keys are added to keys in the keysPressed method below.
	 */
	private HashSet<String> keys = new HashSet<String>();
	
	/**
	 * Initialise variables for the paddle(s). In my reworking of this,
	 * these will be contained in a separate Paddle class.
	 */
	private final int SPEED = 1;
	private int padH = 10, padW = 40;
	private int bottomPadX, topPadX;
	private int inset = 10;
	
	/**
	 * Initialise variables for the ball (puck?). In my reworking of this,
	 * these will be contained in a separate Puck class.
	 */
	private double ballX, ballY, velX = 1, velY = 1, ballSize = 20;
	
	/**
	 * Variables keeping track of the scores. These can remain in the Game class
	 * in my reworking.
	 */
	private int scoreTop, scoreBottom;
	
	//Zero-variable constructor for the Game class (this is called in Main).
	public Game() {
		
		/*
		 * The Game class implements the keyListener interface, which consists of three methods
		 * (keyTyped, keyPressed and keyReleased) which are invoked whenever a key is typed,
		 * pressed or released. These methods are defined for this class below. As such, Game can
		 * be viewed as a key listener, so that the method call addKeyListener(this) means that
		 * we add Game to the list of currently active keyListeners (so that the relevant methods
		 * will be called when keys are pressed).
		 */
		addKeyListener(this);
		
		/*
		 * The documentation for setFocusable does not explain things very well. But there is a
		 * StackOverflow answer which seems to say: when a Component is in focus, it is the Component
		 * (or rather, in this case, the keyListener) which receives input; it will be its keyTyped,
		 * keyPressed and keyReleased methods which are called.
		 * 
		 * For us, this should be the Game component. Although it should be focusable by default, let
		 * us be careful and explicity set it (this is what setFocusable(true) does).
		 * 
		 * As for the second line: reading the documentation, it seems that there is some issue about
		 * how key strokes are recorded (and consequently which methods are called). Let us pass
		 * over this for the moment, and come back to it later if it causes any problems (I don't
		 * think it will).
		 */
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		/*
		 * Variable used to keep track of whether we are at the start of a game or not;
		 * here we obviously are.
		 */
		first = true;
		
		/**
		 * Set the initial delay before the timer starts. The subsequent between-calls
		 * delay has already been set at 5 milliseconds in the definition of "t" above.
		 */
		t.setInitialDelay(100);
		
		//Start the timer (i.e. the flow of the game).
		t.start();
	}
	
	/*
	 * The @Override just indicates that we are overriding a method from a superclass (in
	 * this case javax.swing.JPanel).
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		/*
		 * The "super" keyword refers to the superclass; in this case JPanel. In this context
		 * it allows us to use the overridden method JPanel.paintComponent (in later lines
		 * we then add more stuff, so that the override actually changes something.
    	 */
		super.paintComponent(g);
		
		/*
		 * The class is java.awt.Graphics2D (awt standing for "abstract window toolkit"). It extends
		 * java.awt.Graphics. It has a specified Component onto which it draws (presumably the frame),
		 * and in-built methods for drawing things (see for instance g2d.fill below).
		 */
		Graphics2D g2d = (Graphics2D) g;
		
		/*
		 * getHeight and getWidth are methods inherited from JPanel. As you might guess, they return
		 * the height and the width of the panel (ultimately, they are inherited from JComponent).
		 */
		height = getHeight();
		width = getWidth();

		//Set-up initial positions. The first variable ensures we do not re-do this on each iteration.
		if (first) {
			bottomPadX = width / 2 - padW / 2;
			topPadX = bottomPadX;
			ballX = width / 2 - ballSize / 2;
			ballY = height / 2 - ballSize / 2;
			first = false;
		}
		
		/*
		 * Create bottom pad (in my reworking, bottomPad should be a class which extends Rectangle).
		 * The parameters are, in order: horizontal position, vertical position, width, height.
		 * See the documentation: there is some confusion about whether this is an instantiation of 
		 * Rectangle2D or of Rectangle (which is a subclass of Rectangle2D). In any case, it seems
		 * that only Rectangle has a constructor of the correct format.
		 * 
		 * This class has four fields, giving 2D position and 2D dimensions. Note that there is no
		 * colour field (presumably this comes later?).
		 */
		Rectangle2D bottomPad = new Rectangle(bottomPadX, height - padH - inset, padW, padH);
		g2d.fill(bottomPad);
		
		/*
		 * Create top pad (ditto, but probably a different class to bottomPad because it is computer-
		 * controlled and so has different behaviour.
		 */
		Rectangle2D topPad = new Rectangle(topPadX, inset, padW, padH);
		g2d.fill(topPad);
		
		// Similar, I suppose, to the rectangle classes above.
		Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
		g2d.fill(ball);
		
		/*
		 * Here we print the scores on to the screen, as strings.
		 */
		String scoreB = "Bottom: " + new Integer(scoreBottom).toString();
		String scoreT = "Top: " + new Integer(scoreTop).toString();
		g2d.drawString(scoreB, 10, height / 2);
		g2d.drawString(scoreT, width - 50, height / 2);
	}
	
	//The following method is that which is called on each iteration of the game loop.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Reverse horizontal velocity of ball if it collides with the left or right walls.
		if (ballX < 0 || ballX > width - ballSize) {
			velX = -velX;
		}
		
		/*
		 * Reverse vertical velocity of ball, and adjust scores accordingly, if it collides with
		 * the top or bottom walls (in my reworking of this, we could change this so that if it
		 * collides with top or bottoms walls, the ball gets reset in the middle with a fixed velocity).
		 */
		if (ballY < 0) {
			velY = -velY;
			++ scoreBottom;
		}
		
		if (ballY + ballSize > height) {
			velY = -velY;
			++ scoreTop;
		}
		
		// Reverse vertical velocity of ball if it collides with bottom pad.
		if (ballY + ballSize >= height - padH - inset && velY > 0)
			if (ballX + ballSize >= bottomPadX && ballX <= bottomPadX + padW)
				velY = -velY;

		// Reverse vertical velocity of ball if it collides with top pad.
		if (ballY <= padH + inset && velY < 0)
			if (ballX + ballSize >= topPadX && ballX <= topPadX + padW)
				velY = -velY;

		// Move the ball as dictated by the current velocities.
		ballX += velX;
		ballY += velY;
		
		/*
		 * Now we have to deal with keypresses. Remember that keys contains the elements "LEFT" or
		 * "RIGHT" (or none) according to whether the left or right keys are currently pressed.
		 */
		if (keys.size() == 1) {
			if (keys.contains("LEFT")) {
				/*
				 * The mathematical operation here reads: subtract from bottomPadX: SPEED if 
				 * bottomPadX > 0, otherwise 0. In other words, move the bottom pad left unless
				 * it is already as far left as it can go.
				 */
				bottomPadX -= (bottomPadX > 0) ? SPEED : 0;
			}
			else if (keys.contains("RIGHT")) {
				/*
				 * Similarly here: move the bottom pad right, unless it is already as far right as
				 * it can go.
				 */
				bottomPadX += (bottomPadX < width - padW) ? SPEED : 0;
			}
		}
		
		/*
		 * Finally we must deal with the AI, i.e. the behaviour of the top pad. Basically the AI
		 * works by constantly trying to align the pad horizontally with the ball.
		 * 
		 * In more detail: define a number delta which is the (signed) difference between the ball's
		 * position and the paddle's position. Then if delta is positive (i.e. if the ball is further
		 * right than the paddle) move right, and if delta is negative (i.e. the paddle is further
		 * right) move left (again, only if we are not already at the end).
		 */
		double delta = ballX - topPadX;
		if (delta > 0) {
			topPadX += (topPadX < width - padW) ? SPEED : 0;
		}
		else if (delta < 0) {
			topPadX -= (topPadX > 0) ? SPEED : 0;
		}
		
		/*
		 * This is a method of JPanel; in fact, the no-parameter version comes from the superclass
		 * Component. As one might expect, it paints the component.
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
	
	/*
	 * We don't use the keyTyped method, but it is part of the keyListener interface so we have to
	 * include it
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

}