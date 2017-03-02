package BrickBreaker2;

import javax.swing.*;

public class BBMain {
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 728;

	public static void main(String[] args) {
		
		GamePanel thePanel = new GamePanel();
		
		JFrame gameFrame = new JFrame("Brick Breaker");
		gameFrame.setSize(WIDTH, HEIGHT);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.add(thePanel);		
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
		thePanel.setFocusable(true);
		thePanel.requestFocusInWindow();

		thePanel.playGame();
		
	}

}
