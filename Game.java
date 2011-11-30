import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;


public class Game extends JPanel implements KeyListener, ActionListener {
	
	private int height, width;
	private Timer t = new Timer(2, this);
	
	// pad
	private final int SPEED = 8;
	private int padH = 10, padW = 40;
	private int padX;
	private int inset = 10;
	
	// ball
	private double ballX, ballY, velX = 0.5, velY = 0.5, ballSize = 20;
	

	public Game() {
		this(400, 800);
	}
	
	public Game(int width, int height) {
		this.height = height;
		this.width = width;
		padX = width / 2 - padW / 2;

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		t.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		
		Rectangle2D pad = new Rectangle(padX, 760, padW, padH);
		g2d.fill(pad);
		
		Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
		g2d.fill(ball);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ballX < 0 || ballX > width - ballSize)
			velX = -velX;
		if (ballY < 0 || ballY > height)
			velY = -velY;
		ballX += velX;
		ballY += velY;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
				
		switch (code) {
		case KeyEvent.VK_LEFT:
			padX -= SPEED;
			break;
		case KeyEvent.VK_RIGHT:
			padX += SPEED;
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
