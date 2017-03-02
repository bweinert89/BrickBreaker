package BrickBreaker2;

import java.awt.*;

/**
 * Controls the paddle
 * @author Benjamin Weinert
 *
 */
public class Paddle {

	//Fields
	private double x;
	private int width, height, startWidth;
	private long widthTimer;
	private boolean altWidth;
	
	public final static int YPOS = BBMain.HEIGHT - 100;
	
	//Constructor
	public Paddle(){
		width = 200;
		startWidth = 200;
		height = 20;
		altWidth = false;
		x = BBMain.WIDTH/2 - width/2;
	}
	
	//Update
	public void update(){
		if((System.nanoTime()-widthTimer) / 1000 > 4000000 && altWidth){
			width = startWidth;
			altWidth = false;
		}
	}
	
	//Draw
	public void draw(Graphics2D g){
		g.setColor(Color.ORANGE);
		g.fillRect((int)x, YPOS, width, height);
		if(altWidth){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier new",Font.BOLD,18));
			g.drawString("Normal: "+(4-(System.nanoTime()-widthTimer)/1000000000), (int)x, YPOS+18);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle((int)x,YPOS,width,height);
	}
	
	
	public double getX(){
		return x;
	}
	
	public void resetX(){
		x = BBMain.WIDTH/2 - width/2;
	}
	
	public int getWidth(){
		return width;
	}
	public void setWidth(int newWidth){
		altWidth = true;
		width = newWidth;
		setWidthTimer();
	}
	public void moveLeft(){
		x -= 10;
	}
	public void moveRight(){
		x += 10;
	}
	
	public void setWidthTimer(){
		widthTimer = System.nanoTime();
	}
	
}
