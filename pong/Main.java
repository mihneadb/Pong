package pong;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		// Set-up the frame where everything will be drawn.
		JFrame frm = new JFrame();
		frm.setTitle("Pong");
		
		// Instantiate the Game g and set the content of frm to be g.
		Game g = new Game();
		frm.setContentPane(g);
		
		// Now some simple cosmetic settings.
		frm.setSize(300, 700);		
		frm.setResizable(false);
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		
		}
}