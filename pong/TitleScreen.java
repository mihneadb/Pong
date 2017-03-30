package pong;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;

/*
 * This class controls the behaviour of the game when we are in the title screen phase. This is the
 * phase which initially appears when the game is run.
 */
public class TitleScreen extends JPanel implements KeyListener, ActionListener {

	// Output strings to be drawn (see the paintComponent method below).
	private String onePlayer = "1 Player";
	private String twoPlayers = "2 Players";

	// String to keep track of the currently selected button.
	public String currentSelection = "1Player";

	// Am I going to use these?
	private Rectangle onePlayerButton = new Rectangle();
	private Rectangle twoPlayerButton = new Rectangle();
	
	// Timer: to update the screen to respond to key presses.
	private Timer t = new Timer(50,this);
	
	// Set of strings to keep track of key presses.
	private HashSet<String> keys = new HashSet<String>();
	
	/*
	 * Constructor method. Since our instantiation of this class will be as a field in a Game
	 * object, the constructor is called right at the start of the program. This will be true for all
	 * our phase objects in game.
	 */
	public TitleScreen() {
		t.start();
	}
	
	/*
	 * Method which repaints the component. It uses colours to keep track of the currently selected
	 * button.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// Set the font for the drawn text.
		Font buttonsFont = new Font("buttonsFont",1,20);
		Font headingFont = new Font("headingFont",Font.ITALIC,50);
		
		// Set the positions for the strings.
		int height = getHeight();
		int width = getWidth();
		int xPos = 2*(width/5);
		int yPos1 = height/2;
		int yPos2 = 3*(height/4);
		
		// Use the keys dictionary to update which button is currently selected.
		updateCurrentSelection(keys);
		
		// Draw the heading
		g2d.setFont(headingFont);
		g2d.drawString("PONG", 2*(width/5), height/4);
		
		// Use a case statement to draw the strings with colouring as controlled by currentSelection.
		g2d.setFont(buttonsFont);
		switch (currentSelection) {
		case "1Player":
			g2d.setColor(Color.red);
			g2d.drawString(this.onePlayer, xPos, yPos1);
			g2d.setColor(Color.black);
			g2d.drawString(this.twoPlayers, xPos, yPos2);
			break;
		case "2Players":
			g2d.setColor(Color.black);
			g2d.drawString(this.onePlayer, xPos, yPos1);
			g2d.setColor(Color.red);
			g2d.drawString(this.twoPlayers, xPos, yPos2);
			break;
		}
	}

	/*
	 * The action to be performed on each iteration of the timer; in this case, repainting
	 * the component (i.e. the above method).
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	/*
	 * We override the abstract (i.e. empty) classes inherited from the keyListener interface, as we
	 * must. In this case, they simply record the relevant key presses (up and down) in a dictionary.
	 * The response to said key presses (in this case, to update the currentSelection field) is given
	 * by the updateCurrentSelection method below.
	 * 
	 * These methods are not called directly in response to key presses, because we never call
	 * addKeyListener(titleScreen). Rather, the corresponding methods in the Game class are
	 * called in response to key presses, and those methods in turn call the methods here if
	 * the current phase of the game demands it.
	 * 
	 * Why do we do this? Because there are certain keys which, if pressed while in the title screen
	 * phase, would require us to remove the title screen altogether. As such, they must be processed
	 * in an external object.
	 * 
	 * In this case, that key is the spacebar, and one can see that the reaction to it is processed in
	 * the keyPressed method of Game (it involves resetting the contentPane of the Game object).
	*/
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_DOWN:
			keys.add("DOWN");
			break;
		case KeyEvent.VK_UP:
			keys.add("UP");
			break;
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_DOWN:
			keys.remove("DOWN");
			break;
		case KeyEvent.VK_UP:
			keys.remove("UP");
			break;
		}	
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	/*
	 *  This method updates the currentSelection variable depending on which key is pressed. Since 
	 *  we only have two buttons, the code below works fine, but if we eventually add more then we 
	 *  need to be a bit more careful.
	 */
	private void updateCurrentSelection(HashSet<String> keys) {
		/*
		 * Updates the current button selection given the input data of the keys.
		 */
		if (keys.size() == 1) {
			if (keys.contains("DOWN")) {
				this.currentSelection = "2Players";
			}
			if (keys.contains("UP")) {
				this.currentSelection = "1Player";
			}
		}
	}
	
	public void resetState() {
		currentSelection = "1Player";
	}


}
