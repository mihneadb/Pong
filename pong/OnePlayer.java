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
 * Class implementing the one player version of the game. Most of the code is contained in
 * the GamePhase superclass. The additonal code here is primarily concerned with the
 * behaviour of the paddles.
 */
public class OnePlayer extends GamePhase {
	
	/*
	 * Initialise the two paddle objects. Since not all of the game parameters have been set yet,
	 * we take the default initialisation for the moment; we will reset all the fields later.
	 */
	private PlayerPad playerPad = new PlayerPad();
	private AIPad aiPad = new AIPad();
	
	public OnePlayer() {
		/*
		 *  There are no extra key bindings to set up here, so we just call the constructor from
		 *  the superclass. resetState() is overriden, however.
		 */
		super();
		resetState();
	}
	
	// The following method is that which is called on each iteration of the game loop.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Do everything we did in GamePhase, including repaint();
		super.actionPerformed(e);
		
		/*
		 * Methods which detect and react to collisions of the ball with the
		 * pads. We couldn't have these in the superclass GamePhase because
		 * that class does not contain the pad objects.
		 */
		
		// Reverse vertical velocity of ball if it collides with bottom pad.
		ball.detectPlayerPadCollision(height, padH, padW, playerPad.getX(), inset);
		
		// Reverse vertical velocity of ball if it collides with top pad.
		ball.detectAIPadCollision(height, padH, padW, aiPad.getX(), inset);

		/*
		 *  Update the position of the player's pad, as dictated by the set keys of currently
		 *  pressed keys, and the width of the frame.
		 */
		playerPad.updatePos(keys, width);
		
		// Move the AI pad as dictated by the ball position.
		aiPad.updatePos(ball.getX(), width);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		// Draw everything which was drawn in GamePhase.
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw the paddles, which are not drawn in GamePhase.
		g2d.fill(playerPad);
		g2d.fill(aiPad);
	}
	
	
	/*
	 * Extend the resetState(-) of GamePhase by also resetting the paddles.
	 */
	@Override
	public void resetState() {
		playerPad.resetState(padH, padW, width/2 - padW/2, BOTTOM_SPEED, height, inset,1);
		aiPad.resetState(padH, padW, width/2 - padW/2, TOP_SPEED, height, inset,0);
		super.resetState();
	}
}