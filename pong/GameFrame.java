package pong;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GameFrame extends JFrame {
	
	/*
	 * This class extends JFrame, and acts as the frame in which the game content is held. It
	 * also contains a number of object fields, corresponding to the different phases of the program
	 * (these are: title screen; 1 player; 2 players).
	 * 
	 * The keyboard input is processed by key bindings which are attached to the various component
	 * objects corresponding to the phases. They are not processed by the frame, which is why
	 * we do not set the focus here.
	 */
	
	// Set variables for the screen sizes
	private final int TITLE_SCREEN_WIDTH = 400;
	private final int TITLE_SCREEN_HEIGHT = 400;
	private final int GAME_WIDTH = 300;
	private final int GAME_HEIGHT = 700;
		
	// Instantiate objects controlling each of the phases.
	TitleScreen titleScreen = new TitleScreen();
	OnePlayer onePlayer = new OnePlayer();
	TwoPlayers twoPlayers = new TwoPlayers();
		
	// In the constructor we set-up the frame and put the titleScreen inside it.
	public GameFrame() {
		
		// Set the content of the frame.
		setContentPane(titleScreen);
		
		// Now some simple cosmetic settings.
		setTitle("Pong");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		switchToTitleScreen();
	}
	

	// Method to switch to the title screen phase.
	public void switchToTitleScreen() {
		setSize(TITLE_SCREEN_WIDTH,TITLE_SCREEN_HEIGHT);
		setContentPane(titleScreen);
		setVisible(true);
		titleScreen.resetState();
	}
	
	// Method to switch to the one player phase.
	public void switchToOnePlayer() {
		setSize(GAME_WIDTH,GAME_HEIGHT);
		setContentPane(onePlayer);
		setVisible(true);
		onePlayer.first = true;
	}

	// Method to switch to the two player phase.
	public void switchToTwoPlayers() {
		setSize(GAME_WIDTH,GAME_HEIGHT);
		setContentPane(twoPlayers);
		setVisible(true);
		twoPlayers.first = true;
	}

	
}
