package pong;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JFrame implements KeyListener {
	
	/*
	 * This class extends JFrame, and acts as the frame in which the game content is held. It
	 * also contains a number of object fields, corresponding to the different phases of the program
	 * (these are: title screen; 1 player; 2 players).
	 * 
	 * It implements KeyListener, which means it receives keyboard input. In the program it is the
	 * only active KeyListener. It passes on its input to the (inactive) KeyListener methods of one
	 * of the game phase objects, depending on which one is currently active (which we keep track of
	 * using the currentPhase field).
	 *
	 */
	
	// Set variables for the screen sizes
	private final int TITLE_SCREEN_WIDTH = 400;
	private final int TITLE_SCREEN_HEIGHT = 400;
	private final int GAME_WIDTH = 300;
	private final int GAME_HEIGHT = 700;
	
	// Declare and initialise a field to keep track of the current phase.
	private String currentPhase = "TitleScreen";
	
	// Instantiate objects controlling each of the phases.
	private TitleScreen titleScreen = new TitleScreen();
	private OnePlayer onePlayer = new OnePlayer();
	
	public Game() {
		
		/*
		 * The constructor method sets up the title screen.
		 */
		
		// Set the content of the frame.
		setContentPane(titleScreen);
		
		/*
		 * The Game class implements the keyListener interface, which consists of three methods
		 * (keyTyped, keyPressed and keyReleased) which are invoked whenever a key is typed,
		 * pressed or released. Below, these methods are defined for this class. As such, Game can
		 * be viewed as a key listener, so that the method call addKeyListener(this) (a method 
		 * inherited from Component) means that we add Game to the list of currently active
		 * keyListeners (so that the relevant methods will be called when keys are pressed).
		 * 
		 * In this program, Game is the only active key listener (i.e. the only one for which we
		 * invoke addKeyListener(this). Other objects implement the keyListener interface, but their
		 * keyListener methods are called from the keyListener methods of Game. This allows us to have
		 * different reactions to different inputs depending on the current phase.
		 */
		addKeyListener(this);
		
		/*
		 * The documentation for setFocusable does not explain things very well. But there is a
		 * StackOverflow answer which seems to say: when a Component is in focus, it is the Component
		 * (or rather, in this case, the keyListener) which reacts to input; it will be *its* keyTyped,
		 * keyPressed and keyReleased methods which are called.
		 * 
		 * For us, this should be the Game component. Although it should be focusable by default, let
		 * us be careful and explicitly set it (this is what setFocusable(true) does).
		 * 
		 * As for the second line: reading the documentation, it seems that there is some subtle issue
		 * about how key strokes are recorded (and consequently which methods are called). Let us pass
		 * over this for the moment, and come back to it later if it causes any problems (I don't
		 * think it will).
		 */
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		// Now some simple cosmetic settings.
		setTitle("Pong");
		setSize(TITLE_SCREEN_WIDTH, TITLE_SCREEN_HEIGHT);		
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();
		if ((code == KeyEvent.VK_SPACE) && (this.currentPhase == "TitleScreen")) {
			/*
			 * In this case, we need to leave the title screen and go elsewhere. As such, we reset
			 * the content of Game accordingly.
			 */
			switch (this.titleScreen.currentSelection) {
			case "1Player":
				setSize(GAME_WIDTH,GAME_HEIGHT);
				setContentPane(onePlayer);
				setVisible(true);
				onePlayer.resetState();
				currentPhase = "OnePlayer";
				break;
			case "2Players":
				// We haven't implemented the 2 players phase yet.
				break;
			}
			}
		
		if ((code == KeyEvent.VK_ESCAPE) && (this.currentPhase == "OnePlayer")) {
			/*
			 * Here we leave the current game and return to the title screen.
			 */
			onePlayer.t.stop();
			setSize(TITLE_SCREEN_WIDTH, TITLE_SCREEN_HEIGHT);
			setContentPane(titleScreen);
			setVisible(true);
			titleScreen.resetState();
			currentPhase = "TitleScreen";
		}
		
		switch (this.currentPhase) {
		case "TitleScreen":
			this.titleScreen.keyPressed(e);
			break;
		case "OnePlayer":
			this.onePlayer.keyPressed(e);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		switch (this.currentPhase) {
		case "TitleScreen":
			this.titleScreen.keyReleased(e);
			break;
		case "OnePlayer":
			this.onePlayer.keyReleased(e);
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
