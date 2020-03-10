package main;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MessagePanel extends JPanel{
	/** this message to be displayed */
	private String message = "Welcome to Java";

	/**	The x coordinate where the message is displayed */
	private int xCoordinate = 20;
	private int yCoordinate = 20;
	
	/**	Indicate whether the message is displayed in the center */
	private boolean centered;
	
	/**The interval for moving the message horizontally and vertically */
	private int interval = 10;
	
	/**Construct with default properties */
	public MessagePanel() {
	}

	/**Construct a message panel with a specified message */
	public MessagePanel(String message)
	{
		this.message=message;
	}
	public String getMessage() {
		return message;	
	}
	public void setMessage(String message) {
		this.message=message;
		repaint();
	}
	public int getXCoordinate() {
		return xCoordinate;
	}
	public int getYCoordinate() {
		return yCoordinate;
	}
	public void setXCoordinate(int x) {
		this.xCoordinate = x;
		repaint();
	}
	public void setYCoordinate(int y) {
		this.yCoordinate = y;
		repaint();
	}
	public boolean iscentered() {
		return centered;
	}
	public void setCentered(boolean centered) {
		this.centered = centered;
		repaint();
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval=interval;
		repaint();
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(centered) {
			//Get font metrics for the current font
			FontMetrics fm = g.getFontMetrics();
			//Fine the center location to dispaly
			int stringWidth = fm.stringWidth(message);
			int stringAscent = fm.getAscent();
			//Get the position of the leftmost character in the baseline
			xCoordinate = getWidth()/2 - stringWidth /2;
			yCoordinate = getHeight()/2 + stringAscent /2;
		}
		
		g.drawString(message, xCoordinate, yCoordinate);
	}
	/** Move the message left */
	public void moveLeft() {
		xCoordinate -= interval;
		repaint();
	}
	public void moveRight() {
		xCoordinate += interval;
		repaint();
	}
	public void moveUp() {
		yCoordinate -= interval;
		repaint();
	}
	public void moveDown() {
		yCoordinate += interval;
		repaint();
	}
	/**Override get method for preferredSize*/
	public Dimension getPreferredSize() {
		return new Dimension(200,30); //…Ë÷√√Ê∞Â≥ﬂ¥Á(x,y)
	}
}
