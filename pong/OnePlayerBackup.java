package pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;

import pong.TitleScreen.LeftAction;
import pong.TitleScreen.RightAction;

/*
 * The KeyListener is so that the instances of Game can listen for key strokes, and
 * the ActionListener interface is so that they can listen for action events, in this
 * case triggers from the timer (to implement the game loop).
 */
public class OnePlayerBackup extends JPanel implements ActionListener {
	
	// Action objects to pass to getActionMap().
	private PressedLeft pressedLeft = new PressedLeft();
	private ReleasedLeft releasedLeft = new ReleasedLeft();
	private PressedRight pressedRight = new PressedRight();
	private ReleasedRight releasedRight = new ReleasedRight();
	private PressedEsc pressedEsc = new PressedEsc();
	
	// Set object to keep track of the currently pressed keys.
	private HashSet<String> keys = new HashSet<String>();
	
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
	 *  Used to execute special code on the first iteration of the timer; see below. Public so
	 *  that we can access it from GameFrame.
	 */
	
	public boolean first = true;
	
	// Variables for the initial states of the paddles.
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
	public OnePlayerBackup() {
		/*
		 * Map the key bindings. You might wonder why I don't do this in the PlayerPad class. Arguably
		 * this is what I should have done, however, the PlayerPad class does not extend Component (or
		 * any of its subclasses), and as such does not have the necessary getInputMap, getActionMap
		 * methods. Perhaps this indicates that PlayerPad *should* extend Component (or JPanel or
		 * something), but for the moment I'll leave it as it is.
		 * 
		 * A word on the key bindings. The method getInputMap(int condition) takes an integer argument,
		 * which is one of: JComponent.WHEN_FOCUSED, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT and 
		 * JComponent.WHEN_IN_FOCUSED_WINDOW. The argument determines when the corresponding action
		 * methods will be called in response to key strokes. In our case, the window GameFrame
		 * always has focused, so we want to react to keystrokes when our panel (OnePlayer) is
		 * in the frame.
		 * 
		 * There is also a getInputMap() method without any arguments, which is simply the above method
		 * with the default JComponent.WHEN_FOCUSED argument.
		 */
		InputMap inputMap = getInputMap(OnePlayerBackup.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "pressed LEFT");
		actionMap.put("pressed LEFT", pressedLeft);
		
		inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "released LEFT");
		actionMap.put("released LEFT", releasedLeft);
		
		inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "pressed RIGHT");
		actionMap.put("pressed RIGHT", pressedRight);
		
		inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "released RIGHT");
		actionMap.put("released RIGHT", releasedRight);
	
		inputMap.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "pressed ESC");
		actionMap.put("pressed ESC", pressedEsc);
		
		resetState();
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
		
		/*
		 *  Update the position of the player's pad, as dictated by the set keys of currently
		 *  pressed keys, and the width of the frame.
		 */
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
		g2d.fill(playerPad);
		g2d.fill(aiPad);
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
			System.out.println("OnePlayer.PressedLeft");
			keys.add("LEFT");
		}
	}
	
	public class ReleasedLeft extends AbstractAction{
		public ReleasedLeft(){}
		public void actionPerformed(ActionEvent e) {
			System.out.println("OnePlayer.ReleasedLeft");
			keys.remove("LEFT");
		}
	}
	
	public class PressedRight extends AbstractAction {
		public PressedRight() {}	
		public void actionPerformed(ActionEvent e) {
			System.out.println("OnePlayer.PressedRight");
			keys.add("RIGHT");
		}
	}
	
	public class ReleasedRight extends AbstractAction {
		public ReleasedRight() {}
		public void actionPerformed(ActionEvent e) {
			System.out.println("OnePlayer.ReleasedRight");
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
			GameFrame gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(OnePlayerBackup.this);
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
		playerPad.resetState(padH, padW, width/2 - padW/2, BOTTOM_SPEED, height, inset,2);
		aiPad.resetState(padH, padW, width/2 - padW/2, TOP_SPEED, height, inset,0);
		ball.resetState(width/2 - ballSize/2, height/2 - ballSize/2, ballSize, INIT_VEL_X, INIT_VEL_Y);
		scores.resetState();
		first = true;
		
		t.start();
	}
}