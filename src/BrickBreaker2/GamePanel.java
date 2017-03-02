package BrickBreaker2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;


/**
 * The class which defines the panel in the frame
 * Uses a thread as it repaints, common in complex games,
 * not really needed for this
 * @author Benjamin Weinert
 *
 */
public class GamePanel extends JPanel{

	//fields
	boolean running;
	private BufferedImage image; //stores images to memory
	private Graphics2D g;
	MyKeyListener theKeyListener;
	int nx;
	int ny;
	
	
	//entities
	private Ball theBall;
	private Paddle thePaddle;
	private Map theMap;
	private HUD theHUD;
	private ArrayList<PowerUp> powerUps;
	private ArrayList<BrickSplosion>brickSplosions;
	
	private boolean screenShakeActive;
	private long screenShakeTimer;
	
	public GamePanel(){
		init();
	}

	/**
	 * Init method
	 */
	public void init(){
		nx = 5;
		ny = 8;
		screenShakeTimer = System.nanoTime();
		
		theBall = new Ball();
		thePaddle = new Paddle();
        theMap = new Map(nx,ny);
		theHUD = new HUD();
		powerUps = new ArrayList<PowerUp>();
        brickSplosions = new ArrayList<BrickSplosion>();
		
		theKeyListener = new MyKeyListener();
		addKeyListener(theKeyListener);
		
		//booleans
		running = true;
		screenShakeActive = false;
		
		image = new BufferedImage(BBMain.WIDTH,BBMain.HEIGHT,BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics(); //tells g it draws on image

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	 * Code to start the game
	 */
	public void playGame() {
		//Game loop

		drawStart();
		
		while(running){
			//update everything
			update();
			
			//Render everything
			draw();
			
			//Display image on panel
			repaint();
			
			try{
				Thread.sleep(20); //otherwise repaints to fast to see
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}//end of running loop

		System.out.println("Ending playGame");

	}//end of playGame
	
	/**
	 * Determine if a collision has occurred
	 */
	public void checkCollisions(){
		
		Rectangle ballRect = theBall.getRect();
		Rectangle paddleRect = thePaddle.getRect();
		
		
		//PowerUps
		for(int i =0; i<powerUps.size();i++){
			Rectangle puRect= powerUps.get(i).getRect();
			
			//Wide Paddle Power Up
			if(paddleRect.intersects(puRect)){
				if(powerUps.get(i).getType()==PowerUp.WIDEPADDLE && !powerUps.get(i).getWasUsed()){
					thePaddle.setWidth(thePaddle.getWidth()*2);
					powerUps.get(i).setWasUsed(true);
				}
				if(powerUps.get(i).getType()==PowerUp.FASTBALL && !powerUps.get(i).getWasUsed()){
					theBall.setDx(theBall.getDx()*2);
					theBall.setDy(theBall.getDy()*2);
					theBall.setSpeedTimer();
					powerUps.get(i).setWasUsed(true);
				}
				if(powerUps.get(i).getType()==PowerUp.NARROWPADDLE && !powerUps.get(i).getWasUsed()){
					thePaddle.setWidth((int)(thePaddle.getWidth()*0.5));
					powerUps.get(i).setWasUsed(true);
				}
				if(powerUps.get(i).getType()==PowerUp.SHRINKMAP && !powerUps.get(i).getWasUsed()){
					theMap.setBrickWidth((int)(0.5*theMap.getBrickWidth()));
					theMap.setBrickHeight((int)(0.5*theMap.getBrickHeight()));					
					theMap.setShrinkTimer();
					powerUps.get(i).setWasUsed(true);
				}
				if(powerUps.get(i).getType()==PowerUp.MULT && !powerUps.get(i).getWasUsed()){
					theHUD.setMult(theHUD.getMult()*2);
					theHUD.setMultTimer();
					powerUps.get(i).setWasUsed(true);
				}
			}
		}//End of PowerUps
		
		//Call hits paddle
		if(ballRect.intersects(paddleRect)){
			
			//playSound("file:./Resources/woodhit.aif",0);
			
			//Fix for not getting stuck
			theBall.setY(thePaddle.YPOS - theBall.getSize());
			
			theBall.setDy(-theBall.getDy());
			if(theBall.getX()+theBall.getSize()< thePaddle.getX() + thePaddle.getWidth()/6)
				theBall.setDx(theBall.getDx()-1.5);
			if((theBall.getX()+theBall.getSize()>= thePaddle.getX() + thePaddle.getWidth()/6)
					&& (theBall.getX()+theBall.getSize()< thePaddle.getX() + thePaddle.getWidth()/3))
				theBall.setDx(theBall.getDx()-0.5);		
			if((theBall.getX()>= thePaddle.getX() + 0.6667*thePaddle.getWidth())
					&& (theBall.getX()<= thePaddle.getX() + 0.8333*thePaddle.getWidth()))
				theBall.setDx(theBall.getDx()+0.5);
			if(theBall.getX()>= thePaddle.getX() + 0.8333*thePaddle.getWidth())
				theBall.setDx(theBall.getDx()+1.5);			
		}
		
		A: for(int row = 0; row<theMap.getMapArray().length; row++){
			for(int col = 0; col<theMap.getMapArray()[row].length;col++){
				if(theMap.getMapArray()[row][col]>0){
					int brickx = col*theMap.getBrickWidth()+theMap.HOR_PAD + col*theMap.BET_PAD; 
					int bricky = row*theMap.getBrickHeight() + theMap.VERT_PAD + row*theMap.BET_PAD;
					int brickWidth = theMap.getBrickWidth();
					int brickHeight = theMap.getBrickHeight();
				
					Rectangle brickRect = new Rectangle(brickx,bricky,brickWidth,brickHeight);
					if(ballRect.intersects(brickRect)){
						
						//If weakest brick add to brick explosion
						if(theMap.getMapArray()[row][col]==theMap.WEAK_BRICK){
							brickSplosions.add(new BrickSplosion(brickx, bricky,theMap));
							screenShakeActive=true;
							screenShakeTimer = System.nanoTime();
							theHUD.addScore(20);
						}
						//If power up brick, add to power ups and remove from map
						if(theMap.getMapArray()[row][col]>theMap.STRONG_BRICK){
							powerUps.add(new PowerUp(brickx,bricky,theMap.getMapArray()[row][col],brickWidth,brickHeight));
							theMap.setBrick(row, col, 0);
							theHUD.addScore(5);
						}
						//Else weaken brick
						else{
							int sc = 5*(5-theMap.getMapArray()[row][col]);
							theHUD.addScore(sc);
							theMap.hitBrick(row, col);
						}
						if((theBall.getX()+theBall.getSize()-1) <= brickx || (theBall.getX()+1)>= (brickx+brickWidth))
							theBall.setDx(-theBall.getDx());
						else
							theBall.setDy(-theBall.getDy());
						
						theHUD.addScore(10);
						
						break A; //once find a collision move on
					}
				}	
			}
		}
		
	}//end of checkCollisions
	
	/**
	 * Update method
	 */
	public void update(){
		checkCollisions();
		theBall.update();
		thePaddle.update();
		theMap.update();
		theHUD.update();
	
		for(PowerUp pu : powerUps)
			pu.update();
		
		for(int i = 0; i < brickSplosions.size();i++){
			brickSplosions.get(i).update();
			if(!brickSplosions.get(i).getIsActive()){
				brickSplosions.remove(i);
			}
			
		if((System.nanoTime() - screenShakeTimer)/1000000 > 300 & screenShakeActive){
			screenShakeActive = false;
		}
		
		}
			
	}//end of update
	
	/**
	 * Draw to off screen
	 */
	public void draw(){
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, BBMain.WIDTH, BBMain.HEIGHT);
		
		theMap.draw(g);
		theBall.draw(g);
		thePaddle.draw(g);
		theHUD.draw(g);
		drawPowerUps();
		if(theMap.isThereAWin()){
			drawWin();
			running=false;
		}
		if(theBall.youLose()){
			drawLoser();
			running=false;
		}
		
		for(BrickSplosion bs: brickSplosions)
			bs.draw(g);

	}//end of draw method

	/**
	 * Method to Display
	 * Overridden from JPanel
	 */
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D)g;
		
		int x = 0;
		int y = 0;
		if(screenShakeActive){
			x =(int)(Math.random()*10 - 5);
			y =(int)(Math.random()*10 - 5);
		}
		
		g2.drawImage(image, x, y, BBMain.WIDTH,BBMain.HEIGHT,null);
		g2.dispose();
	}
	
	public void playSound(String soundFile, int times){
		
		try{
			URL soundLocation = new URL(soundFile);
			AudioInputStream audio = AudioSystem.getAudioInputStream(soundLocation);
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			clip.loop(times);
			clip.start();
		}
		catch(UnsupportedAudioFileException uae){
			System.out.println(uae);
		}
		catch(IOException ioe){
			System.out.println(ioe);
		}
		catch(LineUnavailableException lua){
			System.out.println(lua);
		}
	}

	public void drawWin(){
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New",Font.BOLD,50));
		g.drawString("Winner!!!!!!", (int)(BBMain.WIDTH/2-250),BBMain.HEIGHT/2+50);
	}
	
	public void drawLoser(){
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New",Font.BOLD,100));
		g.drawString("Loser!!!!!!", (int)(BBMain.WIDTH/2-250),BBMain.HEIGHT/2+50);
		//g.setFont(new Font("Courier New",Font.BOLD,25));
		//g.drawString("Press Enter to restart", 200,150);
	}
	
	public void drawPowerUps(){
		for(PowerUp pu : powerUps){
			pu.draw(g);
		}	
	}
	
	/**
	 * Method for start screen
	 */
	public void drawStart(){
		for(int i = 3; i>=0; i--){
			g.setColor(Color.WHITE);
			g.fillRect(0,0,BBMain.WIDTH,BBMain.WIDTH);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier New",Font.BOLD,100));
			if(i>0)
				g.drawString(""+i,BBMain.WIDTH/2-40, BBMain.HEIGHT/2);				
			else	
				g.drawString("START!!!",BBMain.WIDTH/2-240, BBMain.HEIGHT/2);
			repaint();
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
				
			
		}
	}
	
	private class MyKeyListener implements KeyListener{
		
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyReleased(KeyEvent e) {}
		
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_RIGHT){
				if(thePaddle.getX() < BBMain.WIDTH - thePaddle.getWidth())
					thePaddle.moveRight();
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT){
				if(thePaddle.getX() > 0)	
					thePaddle.moveLeft();
			}		
/*
 * Try asking, repaint panel /restart/ new game/ does not draw to frame			
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				if(!running){
					init();
					repaint();
					playGame();
					running = true;
					theBall.setX(200);
					theBall.setY(200);
					theBall.setDx(1);
					theBall.setDy(3);
					thePaddle.resetX();
					if(theBall.youLose())theHUD.resetScore();
					theMap = new Map(nx,ny);
					theBall.restart();
					repaint();
					update();
					draw();
					System.out.println(" Running = "+running);
					playGame();
				}
			}
			*/
			
		}
	}
	

	
}//end of GamePanel





