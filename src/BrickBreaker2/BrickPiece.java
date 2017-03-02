package BrickBreaker2;
import java.awt.*;

public class BrickPiece {
	
	//fields
	private int x, y; 
	private double dx, dy, gravity;
	private Color color;
	private int size;

	
	private Map theMap;
	
	/**
	 * Constructor
	 * @param brickx
	 * @param bricky
	 * @param theMap
	 */
	public BrickPiece(int brickx, int bricky, Map theGameMap){
		
		theMap = theGameMap;
		x = brickx + theMap.getBrickWidth()/2;
		y = bricky + theMap.getBrickHeight()/2;
		dx = (Math.random()*30 -15);
		dy = (Math.random()*30 -15);
		size = (int)(Math.random()*15+2);
		color = new Color(0,200,200);
		gravity = 0.8;	
	}
	
	public void update(){
		x+=dx;
		y+=dy;
		dy+=gravity;
	}
	
	public void draw(Graphics2D g){
		
		g.setColor(color);
		g.fillRect((int)x, (int)y, size, size);
		
	}
	
	
	
	

}
