package pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;

import pong.GamePhase.PressedLeft;
import pong.GamePhase.PressedRight;
import pong.GamePhase.ReleasedLeft;
import pong.GamePhase.ReleasedRight;
import pong.TitleScreen.LeftAction;
import pong.TitleScreen.RightAction;

/*
 * The following abstract class contains most of the code which runs the main game. It is
 * abstract because it is never instantiated; rather, its extensions OnePlayer and
 * TwoPlayers are instantiated, which control the different game modes. Since these modes
 * would obviously involve a significant overlap of code, the choice was made to make
 * them both a subclass of this superclass. The additional code required primarily
 * concerns the paddles.
 * 
 * The ActionListener interface is so that they can listen for action events, in this
 * case triggers from the timer (to implement the game loop).
 */
public abstract class GamePhase extends JPanel implements ActionListener {
	
	// Action object to pass to getActionMap().put(-).
	private PressedLeft pressedLeft = new PressedLeft();
	private ReleasedLeft releasedLeft = new ReleasedLeft();
	private PressedRight pressedRight = new PressedRight();
	private ReleasedRight releasedRight = new ReleasedRight();		
	private PressedEsc pressedEsc = new PressedEsc();
	
	/*
	 *  Set object to keep track of the currently pressed keys (protected so that
	 *  subclasses can access it.
	 */
	protected HashSet<String> keys = new HashSet<String>();
	
	// Variables to record the height and width of the panel.
	protected int height;
	protected int width;
	
	// Scores object to record the scores.
	private Scores scores = new Scores();

	/**
	 * The Timer "t" fires every 5 milliseconds, and refers to "this",
	 * namely the current object (an instance of the GamePhase class). In
	 * general, Timer(n,x) sets up a timer which fires every "n" milliseconds,
	 * with reference to object "x". Here the "with reference to" means that
	 * the given object should contain a method called "actionPerformed" 
	 * (which takes a single ActionEvent variable; don't worry about this)
	 * which is then triggered every "n" milliseconds. In our case, the object is 
	 * an instance of GamePhase, and the method "actionPerformed" can be found below.
	 * 
	 * Needs to be public because we have to access it from GameFrame in order to stop it when
	 * the escape key is pressed.
	 */
	public Timer t = new Timer(5, this);

	/*
	 *  Used to execute special code on the first iteration of the timer; see below. needs to be
	 *  public so that we can access it from GameFrame.
	 */
	public boolean first = true;
	
	// Variables for the initial states of the paddles.
	protected final int BOTTOM_SPEED = 2;
	protected final int TOP_SPEED = 2;
	protected int padH = 10, padW = 40;
	protected int inset = 10;
	
	// Variables for the initial state of the ball.
	private final double INIT_VEL_X = 2.5, INIT_VEL_Y = 2.5;
	private double ballSize = 20;
	
	/*
	 * Initialise the ball object. This depends on the parameters given above (or at least,
	 * it will do once we reset everything in resetState(-).
	 */
	protected Ball ball = new Ball();
	
	/*
	 *  Zero-variable constructor for the class. Since an instantiation of this class appears
	 *  as a field in the GameFrame class, the constructor is called when the GameFrame object
	 *  is created (in Main).
	 *  
	 *  As such, this isn't really the "true" constructor. There is only ever one GamePhase object,
	 *  which we simply reset every time a new game is started (perhaps we will change this later).
	 *  The "constructor" is really the resetState(-) method, which sets up the game in its initial
	 *  state and starts the timer (the timer is stopped externally, from the GameFrame class, whenever
	 *  the escape key is pressed).
	 */
	public GamePhase() {
		/*
		 * Set the key bindings for the LEFT, RIGHT and ESCAPE keys, since these will be used
		 * in both of the subclasses. The bindings for the extra keys which are only used in the 
		 * TwoPlayers subclass will be set in the constructor of that class.
		 */
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "pressed LEFT");
		actionMap.put("pressed LEFT", pressedLeft);
		
		inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "released LEFT");
		actionMap.put("released LEFT", releasedLeft);
		
		inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "pressed RIGHT");
		actionMap.put("pressed RIGHT", pressedRight);
		
		inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "released RIGHT");
		actionMap.put("released RIGHT", releasedRight);
		
		getInputMap(GamePhase.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ESCAPE"), "pressed ESCAPE");
		getActionMap().put("pressed ESCAPE", pressedEsc);
		
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
		
		// Move the ball one frame, as dictated by the current velocities.
		ball.updatePos();
		
		/*
		 * This is a method of JPanel; in fact, the no-parameter version comes from the superclass
		 * Component. In effect, this calls the paintComponent() method which we overrode above. I
		 * think it also first gets rid of the previously drawn image (so things don't get drawn on
		 * top of each other).
		 */
		repaint();
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
		g2d.fill(ball);
		
		// Print the scores to the panel, as strings.
		String scoreB = "Bottom: " + new Integer(scores.getScoreBottom()).toString();
		String scoreT = "Top: " + new Integer(scores.getScoreTop()).toString();
		g2d.drawString(scoreB, 10, height / 2);
		g2d.drawString(scoreT, width - 50, height / 2);
	}
			
	/*
	 * The following nested classes respond to left and right keystrokes, via the key bindings
	 * which are initialised in the constructor. As we can see, the only action is to add or 
	 * remove from the "keys" set; this set is then passed to playerPad.udpatePos(-) on each
	 * call of the timer; this way we keep the movement of the player's pad synchronised with
	 * the frame rate.
	 */
	public class PressedLeft extends AbstractAction {
		public PressedLeft() {}	
		public void actionPerformed(ActionEvent e) {
			System.out.println("GamePhase.PressedLeft");
			keys.add("LEFT");
		}
	}
	
	public class ReleasedLeft extends AbstractAction{
		public ReleasedLeft(){}
		public void actionPerformed(ActionEvent e) {
			System.out.println("GamePhase.ReleasedLeft");
			keys.remove("LEFT");
		}
	}
	
	public class PressedRight extends AbstractAction {
		public PressedRight() {}	
		public void actionPerformed(ActionEvent e) {
			System.out.println("GamePhase.PressedRight");
			keys.add("RIGHT");
		}
	}
	
	public class ReleasedRight extends AbstractAction {
		public ReleasedRight() {}
		public void actionPerformed(ActionEvent e) {
			System.out.println("GamePhase.ReleasedRight");
			keys.remove("RIGHT");
		}
	}
	
	/*
	 * The following action class is different to those above: we don't just add to keys, because
	 * pressing escape causes a different event to occur (namely leaving the current game).
	 */
	public class PressedEsc extends AbstractAction {
		public PressedEsc() {}
		public void actionPerformed(ActionEvent e) {
			System.out.println("OnePlayer.PressedEsc");
			t.stop();
			GameFrame gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(GamePhase.this);
			gameFrame.switchToTitleScreen();
		}
	}
	
	/*
	 * A simple method which resets the object to its initial state. Used when we want to restart
	 * a game without creating a new OnePlayer object.
	 */
	public void resetState() {
		/*
		 *  Set focusable attributes so that the key bindings will work (maybe these don't
		 *  actually do anything?).
		 */
//		setFocusable(true);
//		setFocusTraversalKeysEnabled(false);
		
		// Reset the positions and velocities of all the objects.
		int height = getHeight();
		int width = getWidth();
		ball.resetState(width/2 - ballSize/2, height/2 - ballSize/2, ballSize, INIT_VEL_X, INIT_VEL_Y);
		scores.resetState();
		first = true;
		
		t.start();
	}
}