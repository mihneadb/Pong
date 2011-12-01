import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frm = new JFrame();
		frm.setTitle("Pong");
		Game g = new Game();
		frm.setContentPane(g);
		frm.setSize(300, 700);
		frm.setResizable(false);
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
