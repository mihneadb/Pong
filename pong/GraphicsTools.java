package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/*
 * Class containing a number of graphics tools for use in the main program.
 */
public class GraphicsTools {
	
	/**
	 * The following method is adapted from the StackOverflow answer given by Daniel Kvist:
	 * http://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
	 * 
	 * Draw a String centred in the middle of a Rectangle. The rectangle is assumed to already 
	 * exist (and already have been drawn).
	 * 
	 * We make use of this in the TitleScreen class.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to centre the text in.
	 */
	public void drawCenteredString(Graphics2D g2d, String text, Rectangle rect, Font font, Color color) {
	    // Get the FontMetrics
	    FontMetrics metrics = g2d.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font and colour.
	    g2d.setFont(font);
	    g2d.setColor(color);
	    // Draw the String
	    g2d.drawString(text, x, y);
	}
	
	/*
	 * The following method draws the border of a rectangle, by first drawing the rectangle and
	 * then drawing a smaller background-coloured rectangle on top.
	 */
	public void drawRectangleBorder(Graphics2D g2d, JPanel panel, Rectangle rect, int thickness, Color color) {
		g2d.setColor(color);
		g2d.fill(rect);
		Rectangle interiorRect = new Rectangle();
		interiorRect.width = rect.width - (2*thickness);
		interiorRect.height = rect.height - (2*thickness);
		interiorRect.x = rect.x + thickness;
		interiorRect.y = rect.y + thickness;
		Color backgroundColor = panel.getBackground ();
		g2d.setColor(backgroundColor);
		g2d.fill(interiorRect);
		g2d.setColor(color);
	}
	
	public GraphicsTools() {
	}

}
