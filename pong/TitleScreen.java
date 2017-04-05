package pong;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;

/*
 * This class controls the behaviour of the game when we are in the title screen phase. This is the
 * phase which initially appears when the game is run.
 */
public class TitleScreen extends JPanel implements ActionListener {

	// Used to execute special code on the first iteration of the timer; see below.
	private boolean first = true;
	
	// Action objects to process key events.
	private LeftAction leftAction = new LeftAction();
	private RightAction rightAction = new RightAction();
	private SpaceAction spaceAction = new SpaceAction();
	
	// Set of strings to keep track of currently pressed buttons.
	private HashSet<String> keys = new HashSet<String>();
	
	// GraphicsTools object to allow us access to the methods in the GraphicsTools class.
	private GraphicsTools graphicsTools = new GraphicsTools();
		
	// Output strings to be drawn (see the paintComponent method below).
	private String onePlayer = "1 Player";
	private String twoPlayers = "2 Players";
	
	// Fonts for the drawn text.
	Font buttonsFont = new Font("buttonsFont",1,15);
	Font headerFont = new Font("headerFont",Font.BOLD,40);

	// String to keep track of the currently selected button.
	public String currentSelection = "1Player";

	// Rectangle objects which will serve as the button borders.
	private Rectangle headerButton = new Rectangle();
	private Rectangle onePlayerButton = new Rectangle();
	private Rectangle twoPlayersButton = new Rectangle();
	
	// Timer: to update the screen to respond to key presses.
	private Timer t = new Timer(50,this);
	
	/*
	 * Constructor method. Since our instantiation of this class will be as a field in a GameFrame
	 * object, the constructor is called right at the start of the program. This will be true for all
	 * our phase objects in gameFrame.
	 */
	public TitleScreen() {
		/*
		 * We assign the key bindings which will determine how this component reacts to input. 
		 * A word on the key bindings here. The method getInputMap() takes an integer argument, which 
		 * is one of: Component.WHEN_FOCUSED, Component.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT and 
		 * Component.WHEN_IN_FOCUSED_WINDOW. The argument determines when the corresponding action
		 * methods will be called in response to key strokes. In our case, the window GameFrame
		 * always has focused, so we want to react to keystrokes when our panel (OnePlayer) is
		 * in the frame.
		 */
		InputMap inputMap = getInputMap(TitleScreen.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "LEFT");
		actionMap.put("LEFT", leftAction);
		
		inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "RIGHT");
		actionMap.put("RIGHT", rightAction);
		
		inputMap.put(KeyStroke.getKeyStroke("SPACE"), "SPACE");
		actionMap.put("SPACE", spaceAction);
		
		// Start the timer that implements the game loop for this component.
		t.start();
	}
	
	/*
	 * The action to be performed on each iteration of the timer; in this case, repainting
	 * the component (i.e. the above method).
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Reset the button positions (this only needs to be do once).
		if (first) {
			int height = getHeight();
			int width = getWidth();
			resetButtonPositions(height,width);
			first = false;
		}
		// Repaint the Component (see the paintComponent() method above).
		repaint();
	}
	
	/*
	 * Method which repaints the component. It uses colours to keep track of the currently selected
	 * button.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		/*
		 * Draw the header. Be careful; there is something strange happening here with variables
		 * passed into the methods having their values changed (perhaps some data encapsulation
		 * issue?).
		 */
		drawHeader(g2d);
		
		// Draw the buttons.
		drawButtons(g2d);
	}
	
	/*
	 * A method which draws the header, based on the state of the fields headerButton, headerFont.
	 * Takes as input the Graphics2D object which does the drawing.
	 */
	private void drawHeader(Graphics2D g2d) {
		g2d.setFont(headerFont);
		graphicsTools.drawRectangleBorder(g2d, this, headerButton, 5, Color.black);
		graphicsTools.drawCenteredString(g2d, "PONG", headerButton, headerFont, Color.black);
	}
	
	/*
	 * A method which draws the buttons, with colouring based on the state of currentSelection. We
	 * use a switch statement for this. The other object fields which the output depends on are 
	 * onePlayerButton, onePlayer, twoPlayersButton, twoPlayers.
	 */
	private void drawButtons(Graphics2D g2d) {
		g2d.setFont(buttonsFont);
		switch (currentSelection) {
		case "1Player":
			graphicsTools.drawRectangleBorder(g2d, this, onePlayerButton, 3, Color.red);
			graphicsTools.drawCenteredString(g2d, onePlayer, onePlayerButton, buttonsFont, Color.red);
			graphicsTools.drawRectangleBorder(g2d, this, twoPlayersButton, 3, Color.black);
			graphicsTools.drawCenteredString(g2d, twoPlayers, twoPlayersButton, buttonsFont, Color.black);
			break;
		case "2Players":
			graphicsTools.drawRectangleBorder(g2d, this, onePlayerButton, 3, Color.black);
			graphicsTools.drawCenteredString(g2d, onePlayer, onePlayerButton, buttonsFont, Color.black);
			graphicsTools.drawRectangleBorder(g2d, this, twoPlayersButton, 3, Color.red);
			graphicsTools.drawCenteredString(g2d, twoPlayers, twoPlayersButton, buttonsFont, Color.red);
			break; 
		}
	}
	
	/*
	 * Initialises the positions of the buttons, given the height and width of the frame.
	 */
	private void resetButtonPositions(int height, int width) {
		headerButton.width = width/2;
		headerButton.height = height/4;
		headerButton.x = width/2 - headerButton.width/2;
		headerButton.y = height/4;
		
		onePlayerButton.width = width/4;
		onePlayerButton.height = height/6;
		onePlayerButton.x = width/3 - onePlayerButton.width/2;
		onePlayerButton.y = 2*(height/3);
		
		twoPlayersButton.width = width/4;
		twoPlayersButton.height = height/6;
		twoPlayersButton.x = 2*(width/3) - twoPlayersButton.width/2;
		twoPlayersButton.y = 2*(height/3);
	}


	/*
	 * The following nested classes are Action classes, which control this component's response
	 * to keystrokes, via the key bindings which are set in the constructor.
	 */
	public class LeftAction extends AbstractAction {
		public LeftAction() {}
		
		public void actionPerformed(ActionEvent e) {
			System.out.println("TitleScreen.LeftAction");
			currentSelection = "1Player";
		}
	}
	
	public class RightAction extends AbstractAction {
		public RightAction() {}
		
		public void actionPerformed(ActionEvent e) {
			System.out.println("TitleScreen.RightAction");
			currentSelection = "2Players";
		}
	}
	
	public class SpaceAction extends AbstractAction {
		public SpaceAction() {}
	
		public void actionPerformed(ActionEvent e) {
			System.out.println("TitleScreen.SpaceAction");
			/*
			 *  We have to move to the next phase, depending on the value of currentSelection. But
			 *  in either case, we have to call a method of the containing GameFrame. Trying to work
			 *  out how to do this caused me a lot of headaches, but eventually I worked it out.
			 *  
			 *  We first assign our containing GameFrame to a local variable name. TitleScreen.this
			 *  gives us the TitleScreen object containing this SpaceAction object (in our case,
			 *  just titleScreen), and SwingUtilities.getWindowAncestor(x) returns the window 
			 *  containing x. Then the (GameFrame) passes that window to the GameFrame class, so 
			 *  that we end up with an object of type GameFrame (in fact, we end up with gameFrame).
			 */
			t.stop();
			
			GameFrame gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(TitleScreen.this);
			/*
			 * Now we make the choice, depending on currentSelection.
			 */
			switch (currentSelection) {
			case "1Player":
				gameFrame.switchToOnePlayer();
				break;
			case "2Players":
				gameFrame.switchToTwoPlayers();
				break;
			}
		}
	}
	
	public void resetState() {
		/*
		 * The documentation for setFocusable does not explain things very well. But there is a
		 * StackOverflow answer which seems to say: when a Component is in focus, it is the Component
		 * which reacts to input via key bindings; it will be *its* action classes which are invoked.
		 * 
		 * For us, this should be the current component. Although it should be focusable by default, let
		 * us be careful and explicitly set it (this is what setFocusable(true) does).
		 * 
		 * As for the second line: reading the documentation, it seems that there is some subtle issue
		 * about how key strokes are recorded (and consequently which methods are called). Let us pass
		 * over this for the moment, and come back to it later if it causes any problems (I don't
		 * think it will).
		 */
//		setFocusable(true);
//		setFocusTraversalKeysEnabled(false);
		currentSelection = "1Player";
		t.start();
		first = true;
	}

}
