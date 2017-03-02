package BrickBreaker2;

import java.awt.*;

/**
 * Class in charge of special power up bricks
 * @author Benjamin Weinert
 *
 */
public class PowerUp {
	
	//Fields
	private int x, y, dy, type, width, height;
	private boolean isOnScreen;
	private boolean wasUsed;
	private Color color;

	public final static int WIDEPADDLE = 4;
	public final static int FASTBALL = 5;
	public final static int NARROWPADDLE = 6;
	public final static int SHRINKMAP = 7;
	public final static int MULT = 8;
	public final static Color WIDECOLOR = Color.GREEN;
	public final static Color FASTCOLOR = Color.RED;
	public final static Color NARROWCOLOR = Color.YELLOW;
	public final static Color SHRINKCOLOR = Color.BLACK;
	public final static Color MULTCOLOR = Color.MAGENTA;

	/**
	 * Constructor
	 * @param xStart
	 * @param yStart
	 * @param theType
	 * @param theDy
	 * @param theWidth
	 * @param theHeight
	 */
	public PowerUp(int xStart, int yStart, int theType, int theWidth, int theHeight){
		x = xStart;
		y = yStart;
		type = theType;
		width = theWidth;
		height = theHeight;
		wasUsed = false;
		
		if(type <4)
			type = 4;
		if(type>9)
			type = 9;
		if(type==WIDEPADDLE)
			color = WIDECOLOR;
		if(type==FASTBALL)
			color = FASTCOLOR;
		if(type==NARROWPADDLE)
			color = NARROWCOLOR;
		if(type==SHRINKMAP)
			color = SHRINKCOLOR;
		if(type==MULT)
			color = MULTCOLOR;

		
		dy = (int)(Math.random()*6 +1);
		
	}
	
	public void draw(Graphics2D g){
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	
	public void update(){
		
		y+=dy;
		
		if(y>BBMain.HEIGHT)
			isOnScreen = false;
	}

	public int getX() {return x;}
	public void setX(int x) {this.x = x;}
	public int getY() {return y;}
	public void setY(int y) {this.y = y;}
	public int getDy() {return dy;}
	public void setDy(int dy) {this.dy = dy;}
	public int getType() {return type;}
	public void setType(int type) {this.type = type;}
	public boolean isOnScreen() {return isOnScreen;}
	public void setOnScreen(boolean isOnScreen) {this.isOnScreen = isOnScreen;}
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}
	public Rectangle getRect(){return new Rectangle(x,y,width,height);}
	public boolean getWasUsed() {return wasUsed;}
	public void setWasUsed(boolean wasUsed) {this.wasUsed = wasUsed;}
}
