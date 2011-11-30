import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frm = new JFrame();
		frm.setTitle("Pong");
		//frm.setLayout(new BorderLayout());
		Game g = new Game();
		//frm.add(g, BorderLayout.CENTER);
		frm.setContentPane(g);
		frm.setSize(g.getWidth(), g.getHeight());
		frm.setResizable(false);
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
