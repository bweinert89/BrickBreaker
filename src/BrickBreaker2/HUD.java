package BrickBreaker2;

import java.awt.*;

public class HUD {
	
	//field
	private int score, mult, multOrig;
	private long multTimer;
	private boolean altMult;
	
	/**
	 * Constructor
	 */
	public HUD(){
		init();
	}

	/**
	 * Init method
	 */
	public void init(){
		score = 0;
		mult = 1;
		multOrig = 1;
	}
	
	public void update(){
		if((System.nanoTime()-multTimer) / 1000 > 5000000 && altMult){
			mult=multOrig;
			altMult = false;
		}
	}
	
	/**
	 * Draw method
	 * @param g
	 */
	public void draw(Graphics2D g){
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New",Font.PLAIN,14));
		g.drawString("Score: "+score, 20, 20);
		
		if(altMult){
			g.setColor(Color.RED);
			g.setFont(new Font("Courier new",Font.PLAIN,14));
			g.drawString("Multiplier: "+mult,20,34);
		}
	}
	
	
	/**
	 * Return the score
	 * @return
	 */
	public int getScore(){
		return score;
	}
	
	public void resetScore(){
		score = 0;
	}
	
	public void addScore(int scoreToAdd){
		score+=(scoreToAdd*mult);
	}
	
	public void setMultTimer(){
		multTimer = System.nanoTime();
		altMult = true;
	}

	public int getMult() {
		return mult;
	}

	public void setMult(int mult) {
		this.mult = mult;
	}
	
}//end of HUD
