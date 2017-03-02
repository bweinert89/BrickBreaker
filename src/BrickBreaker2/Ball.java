package BrickBreaker2;

import java.awt.*;

/**
 * Class which defines ball position 
 * and direction
 * @author Benjamin Weinert
 *
 */
public class Ball {
	
	protected double x, y, dx, dy;
	private double dxOrig, dyOrig;
	private int ballSize = 30;
	private boolean altSpeed;
	boolean loser;
	private long speedTimer;
	
	/**
	 * Constructor for initial ball position and speed
	 */
	public Ball(){
		x=200;
		y=300;
		dx = 3;
		dy = 5;
		dxOrig = 3;
		dyOrig = 5;
		loser = false;
		altSpeed = false;
	}
	
	/**
	 * updates balls position and movement
	 */
	public void update(){
		setPosition();
		if((System.nanoTime()-speedTimer) / 1000 > 10000000 && altSpeed){
			dx = dxOrig;
			dy = dyOrig;
			altSpeed = false;
		}
	}
	
	/**
	 * Updates position by speed
	 * Checks if wall has been hit
	 */
	public void setPosition(){
		x+=dx;
		y+=dy;
		
		if(x<0)
			dx = -dx;
		if(y<0)
			dy = -dy;
		if(x >= BBMain.WIDTH - ballSize - 1)
			dx = -dx;
		if(y > BBMain.HEIGHT - ballSize)
			dy = -dy;		
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.DARK_GRAY);
		g.setStroke(new BasicStroke(4));
		g.drawOval((int)x, (int)y, ballSize, ballSize);
		
		if(altSpeed){
			g.setColor(Color.RED);
			g.setFont(new Font("Courier new",Font.BOLD,18));
			g.drawString("Slow Down in: "+(10-(System.nanoTime()-speedTimer)/1000000000),(int)(0.45*BBMain.WIDTH),BBMain.HEIGHT - 150);
		}
	}
	
	/**
	 * Return rectangle
	 * @return
	 */
	public Rectangle getRect(){
		return new Rectangle((int)x,(int)y,ballSize,ballSize);
	}
	/**
	 * Set y direction
	 * @param theDy
	 */
	public void setDy(double theDy){
		dy = theDy;
	}
	/**
	 * Return y direction
	 * @return
	 */
	public double getDy(){
		return dy;
	}
	/**
	 * Set the x direction
	 * @param theDx
	 */
	public void setDx(double theDx){
		dx = theDx;
	}
	/**
	 * Return x direction
	 * @return
	 */
	public double getDx(){
		return dx;
	}
	
	/**
	 * Return ball size
	 * @return
	 */
	public int getSize(){
		return ballSize;
	}
	/**
	 * Return x position
	 * @return
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Set x position
	 * @param xPos
	 */
	public void setX(double xPos){
		x = xPos;
	}
	
	/**
	 * Set y position
	 * @param yPos
	 */
	public void setY(double yPos){
		y = yPos;
	}
	
	
	public boolean youLose(){
		if(y>BBMain.HEIGHT - ballSize*2)loser =true;
		return loser;
	}

	public void restart(){
		loser = false;
	}
	
	public void setSpeedTimer(){
		altSpeed = true;
		speedTimer = System.nanoTime();
	}
	

}

