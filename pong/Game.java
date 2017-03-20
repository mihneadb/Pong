package pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;


public class Game extends JPanel implements KeyListener, ActionListener {
	
	private int height, width;

	/**
	 * The Timer "t" fires every 5 milliseconds, and refers to "this",
	 * namely the current object (an instance of the Game class). In
	 * general, Timer(n,x) sets up a timer will fire every "n" milliseconds,
	 * with reference to object "x". Here the "with reference" means that
	 * the given object should contain a method called "actionPerformed" 
	 * (which takes a single ActionEvent variable; don't worry about this)
	 * which is then triggered every "n" milliseconds. In our case, the object is 
	 * an instance of Game, and the method "actionPerformed" can be found below.
	 */
	private Timer t = new Timer(5, this);
	private boolean first;
	
	private HashSet<String> keys = new HashSet<String>();
	
	/**
	 * Initialise variables for the player's paddle. In my reworking of this,
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
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		first = true;
		
		/**
		 * Set the initial delay before the timer starts. The subsequent between-calls
		 * delay has already been set at 5 milliseconds in the definition of "t" above.
		 */
		t.setInitialDelay(100);
		
		//Start the timer (i.e. the flow of the game).
		t.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		height = getHeight();
		width = getWidth();

		//Set-up initial positions.
		if (first) {
			bottomPadX = width / 2 - padW / 2;
			topPadX = bottomPadX;
			ballX = width / 2 - ballSize / 2;
			ballY = height / 2 - ballSize / 2;
			first = false;
		}
		
		// bottom pad
		Rectangle2D bottomPad = new Rectangle(bottomPadX, height - padH - inset, padW, padH);
		g2d.fill(bottomPad);
		
		// top pad
		Rectangle2D topPad = new Rectangle(topPadX, inset, padW, padH);
		g2d.fill(topPad);
		
		// ball
		Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
		g2d.fill(ball);
		
		// scores
		String scoreB = "Bottom: " + new Integer(scoreBottom).toString();
		String scoreT = "Top: " + new Integer(scoreTop).toString();
		g2d.drawString(scoreB, 10, height / 2);
		g2d.drawString(scoreT, width - 50, height / 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// side walls
		if (ballX < 0 || ballX > width - ballSize) {
			velX = -velX;
		}
		// top / down walls
		if (ballY < 0) {
			velY = -velY;
			++ scoreBottom;
		}
		
		if (ballY + ballSize > height) {
			velY = -velY;
			++ scoreTop;
		}
		// bottom pad
		if (ballY + ballSize >= height - padH - inset && velY > 0)
			if (ballX + ballSize >= bottomPadX && ballX <= bottomPadX + padW)
				velY = -velY;

		// top pad
		if (ballY <= padH + inset && velY < 0)
			if (ballX + ballSize >= topPadX && ballX <= topPadX + padW)
				velY = -velY;

		ballX += velX;
		ballY += velY;
		
		// pressed keys
		if (keys.size() == 1) {
			if (keys.contains("LEFT")) {
				bottomPadX -= (bottomPadX > 0) ? SPEED : 0;
			}
			else if (keys.contains("RIGHT")) {
				bottomPadX += (bottomPadX < width - padW) ? SPEED : 0;
			}
		}
		
		// AI
		double delta = ballX - topPadX;
		if (delta > 0) {
			topPadX += (topPadX < width - padW) ? SPEED : 0;
		}
		else if (delta < 0) {
			topPadX -= (topPadX > 0) ? SPEED : 0;
		}
		
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

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
}
