 /*
Takes all the objects and data from the Game class and puts it in a visible frame for the User to see. Execution of the game occurs here. 

*/
import java.awt.BorderLayout; // imports Borderlayout class which allows the addition of borders

import javax.swing.JFrame;//Import jframe class which allows the creation of the canvas


public class Main {
	public static void main(String[] args) {
		JFrame frm = new JFrame(); //Creates a JFrame object that graphics can be put on
		frm.setTitle("Pong"); //Sets the title of the tab of the frame to "Pong"
		Game g = new Game(); //Creates a game object
		frm.setContentPane(g); //Sets the ContentPane property to the value of the Game object g. Allows all graphics within the g objects to be drawn to the frame
		frm.setSize(300, 700); //Sets the size of the frame, width of 300 and height of 700
		frm.setResizable(false); //Forbids the user to resize the frame
		frm.setVisible(true); //Sets the frame to visible
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closes the frame if the exit button is pressed
	}

}
