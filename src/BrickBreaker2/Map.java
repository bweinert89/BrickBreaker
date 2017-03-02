package BrickBreaker2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Controls the map of bricks
 * @author Benjamin Weinert
 *
 */
public class Map {

	//Fields
	private int[][] theMap;
	private int brickHeight, brickWidth,brickWidthOrig, brickHeightOrig;
	private long shrinkTimer;
	private boolean altMap;
	
	public static final int HOR_PAD = 80, VERT_PAD = 50, BET_PAD = 20; //Padding to make sure room on top and sides
	public static final int WEAK_BRICK = 1, STRONG_BRICK = 3;
	
	
	/**
	 * Constructor
	 * @param row = number of rows
	 * @param col = number of columns
	 */
	public Map(int row, int col){
		initMap(row,col);
		
		altMap = false;
		brickWidth = (BBMain.WIDTH - 2*HOR_PAD)/col - BET_PAD;
		brickHeight = (BBMain.HEIGHT/2 - 2*VERT_PAD)/row - BET_PAD;
		brickWidthOrig = (BBMain.WIDTH - 2*HOR_PAD)/col - BET_PAD;
		brickHeightOrig = (BBMain.HEIGHT/2 - 2*VERT_PAD)/row - BET_PAD;
	}//end constructor
	
	/**
	 * Initialize map
	 * 0 = empty
	 * @param row
	 * @param col
	 */
	public void initMap(int row, int col){
		theMap = new int[row][col];
		
		for(int i = 0; i<theMap.length;i++){
			for(int j = 0; j<theMap[i].length;j++){				
				int r = (int)(Math.random()*4);
				theMap[i][j] = r;
			}
		}//end of loop over i,j
		
		/*
		theMap[3][2] = 4;
		theMap[3][6] = 5;
		theMap[2][0] = 6;
		theMap[2][5] = 7;
		theMap[1][2] = 8;
		theMap[1][7] = 4;
		theMap[0][4] = 8;
		theMap[2][2] = 4;
		theMap[3][7] = 8;
		theMap[1][4] = 6;
		*/
		
		for(int i = 0; i<12; i++){
			int t = (int)(Math.random()*5 + 4);
			int x = (int)(Math.random()*5);
			int y = (int)(Math.random()*8);
			
			theMap[x][y] = t;
		}


		
		
	}//end initMap

	public void update(){
		if((System.nanoTime()-shrinkTimer) / 1000 > 10000000 && altMap){
			brickWidth = brickWidthOrig;
			brickHeight = brickHeightOrig;
			altMap = false;
		}
	}
	
	/**
	 * Get Map
	 * @return
	 */
	public int[][] getMapArray(){
		return theMap;
	}
	/**
	 * Change value of brick
	 * @param row
	 * @param col
	 */
	public void setBrick(int row, int col, int value){
		theMap[row][col] = value;
	}
	/**
	 * returns brick width
	 * @return
	 */
	public int getBrickWidth(){
		return brickWidth;
	}
	/**
	 * Returns brick height
	 * @return
	 */
	public int getBrickHeight(){
		return brickHeight;
	}
	
	public void setBrickWidth(int newWidth){
		brickWidth = newWidth;
	}
	
	public void setBrickHeight(int newHeight){
		brickHeight = newHeight;
	}
	
	public void hitBrick(int row, int col){
		theMap[row][col]-=1;
		if(theMap[row][col]<0)
			theMap[row][col]=0;
	}
	
	public boolean isThereAWin(){
		int bricksRemaining = 0;
		boolean thereIsAWin = false;
		for (int row = 0; row<theMap.length;row++){
			for(int col = 0; col<theMap[row].length;col++){
				bricksRemaining += theMap[row][col];
			}
		}
		if(bricksRemaining == 0) thereIsAWin = true;
		return thereIsAWin;
	}
	
	/**
	 * Draw Method for bricks, no update needed
	 * @param g
	 */
	public void draw(Graphics2D g){
		for(int i = 0; i< theMap.length;i++){
			for(int j = 0; j<theMap[i].length;j++){
				if(theMap[i][j]>0){
					if(theMap[i][j]==1)
						g.setColor(new Color(0,200,200));
					if(theMap[i][j]==2)
						g.setColor(new Color(0,150,150));
					if(theMap[i][j]==3)
						g.setColor(new Color(0,100,100));
					if(theMap[i][j]==PowerUp.WIDEPADDLE)
						g.setColor(PowerUp.WIDECOLOR);
					if(theMap[i][j]==PowerUp.FASTBALL)
						g.setColor(PowerUp.FASTCOLOR);
					if(theMap[i][j]==PowerUp.NARROWPADDLE)
						g.setColor(PowerUp.NARROWCOLOR);
					if(theMap[i][j]==PowerUp.SHRINKMAP)
						g.setColor(PowerUp.SHRINKCOLOR);
					if(theMap[i][j]==PowerUp.MULT)
						g.setColor(PowerUp.MULTCOLOR);
					g.fillRect(j*brickWidth+HOR_PAD +j*BET_PAD, i*brickHeight+VERT_PAD + i*BET_PAD, brickWidth, brickHeight);
					g.setColor(Color.WHITE);
					g.drawRect(j*brickWidth+HOR_PAD+j*BET_PAD, i*brickHeight+VERT_PAD +i*BET_PAD, brickWidth, brickHeight);
					g.setStroke(new BasicStroke(2));
				}
			}
		}//end of loop over i,j	
		
		if(altMap){
			g.setColor(Color.RED);
			g.setFont(new Font("Courier new",Font.BOLD,18));
			g.drawString("Normal Bricks in: "+(10-(System.nanoTime()-shrinkTimer)/1000000000),(int)(0.4*BBMain.WIDTH),BBMain.HEIGHT - 200);
		}
	}
	
	public void setShrinkTimer(){
		shrinkTimer = System.nanoTime();
		altMap = true;
	}

}//end Map class


