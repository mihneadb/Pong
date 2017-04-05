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
 * Class implementing the two players version of the game. Most of the code is contained in
 * the GamePhase superclass. The additonal code here is primarily concerned with the
 * behaviour of the paddles, and setting extra key bindings to cover the "1" and "3" keys.
 */
public class TwoPlayers extends GamePhase {
	
	// Action object to pass to getActionMap().put(-).
	private Pressed1 pressed1 = new Pressed1();
	private Released1 released1 = new Released1();
	private Pressed3 pressed3 = new Pressed3();
	private Released3 released3 = new Released3();	
	
	/*
	 * Initialise the two paddle objects. Since not all of the game parameters have been set yet,
	 * we take the default initialisation for the moment; we will reset all the fields later.
	 */
	private PlayerPad bottomPad = new PlayerPad();
	private PlayerPad topPad = new PlayerPad();
	
	public TwoPlayers() {

		// Call the GamePhase constructor.
		super();
		
		/*
		 * Set the extra key bindings for the keys which are not used in the one player mode.
		 */
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke("pressed 1"), "pressed 1");
		actionMap.put("pressed 1", pressed1);
		
		inputMap.put(KeyStroke.getKeyStroke("released 1"), "released 1");
		actionMap.put("released 1", released1);
		
		inputMap.put(KeyStroke.getKeyStroke("pressed 3"), "pressed 3");
		actionMap.put("pressed 3", pressed3);
		
		inputMap.put(KeyStroke.getKeyStroke("released 3"), "released 3");
		actionMap.put("released 3", released3);
		
		resetState();
	}
	
	// The following method is that which is called on each iteration of the game loop.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Timer action of GamePhase, which includes repaint();
		super.actionPerformed(e);
		
		/*
		 * Methods which detect and react to collisions of the ball with the
		 * pads. We couldn't have these in the superclass GamePhase because
		 * that class does not contain the pad objects.
		 */
		
		// Reverse vertical velocity of ball if it collides with bottom pad.
		ball.detectPlayerPadCollision(height, padH, padW, bottomPad.getX(), inset);
		
		// Reverse vertical velocity of ball if it collides with top pad.
		ball.detectAIPadCollision(height, padH, padW, topPad.getX(), inset);

		/*
		 *  Update the position of the player's pad, as dictated by the set keys of currently
		 *  pressed keys, and the width of the frame.
		 */
		bottomPad.updatePos(keys, width);
		topPad.updatePos(keys, width);
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
		
		// Draw the paddles, which are not drawn in GamePhase.
		g2d.fill(bottomPad);
		g2d.fill(topPad);
	}
	
	/*
	 * We need new key bindings for the keys which are not pressed in the OnePlayer mode (namely those
	 * controlling the top pad).
	 */
	public class Pressed1 extends AbstractAction {
		public Pressed1() {}	
		public void actionPerformed(ActionEvent e) {
			System.out.println("TwoPlayers.Pressed1");
			keys.add("1");
		}
	}
	
	public class Released1 extends AbstractAction{
		public Released1(){}
		public void actionPerformed(ActionEvent e) {
			System.out.println("TwoPlayers.Released1");
			keys.remove("1");
		}
	}
	
	public class Pressed3 extends AbstractAction {
		public Pressed3() {}	
		public void actionPerformed(ActionEvent e) {
			System.out.println("TwoPlayers.Pressed3");
			keys.add("3");
		}
	}
	
	public class Released3 extends AbstractAction {
		public Released3() {}
		public void actionPerformed(ActionEvent e) {
			System.out.println("TwoPlayers.Released3");
			keys.remove("3");
		}
	}
	
	/*
	 * Extend the resetState(-) of GamePhase by also resetting the paddles.
	 */
	@Override
	public void resetState() {
		bottomPad.resetState(padH, padW, width/2 - padW/2, BOTTOM_SPEED, height, inset,1);
		topPad.resetState(padH, padW, width/2 - padW/2, TOP_SPEED, height, inset,2);
		super.resetState();
	}
}