package BrickBreaker2;

import java.awt.*;
import java.util.*;

/**
 * Class that controls explosions of the brick class
 * @author Benjamin Weinert
 *
 */
public class BrickSplosion {

	//fields
	private ArrayList<BrickPiece> pieces;
	private int x,y;
	private Map theMap;
	private boolean isActive;
	private long startTime;
	
	/**
	 * Constructor
	 * @param thex
	 * @param they
	 * @param gameMap
	 */
	  public BrickSplosion(int thex, int they, Map gameMap){
	 
		x = thex;
		y = they;
		theMap = gameMap;
		isActive =true;
		startTime = System.nanoTime();
		pieces = new ArrayList<BrickPiece>();
		init();
	}//end constructor
	
	  /**
	   * Init method
	   */
	  public void init(){
		  int randNum = (int)(Math.random()*20+5);
		  for(int i = 0; i<randNum;i++){
			pieces.add(new BrickPiece(x,y,theMap));  
		  }
	  }//end init
	  
	  /**
	   * Update Method
	   */
	  public void update(){
		  for(BrickPiece bp : pieces){
			  bp.update();
		  }
		  
		  if((System.nanoTime()-startTime)/1000000 > 2000 && isActive){
			  isActive = false;
		  }
	  }//end of update
	  
	  /**
	   * Draw method
	   * @param g
	   */
	  public void draw(Graphics2D g){
		  
		  for(BrickPiece bp : pieces){
			  bp.draw(g);
		  }
	  }//end of draw
	  
	  public boolean getIsActive(){
		  return isActive;
	  }
	  
}
