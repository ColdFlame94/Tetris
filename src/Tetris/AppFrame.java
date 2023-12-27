package Tetris;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class AppFrame extends JFrame{
	Game game;
	JMenuBar menuBar;
	JButton backToMenu;
	static JLabel gameInfo;
	String space;
	
	public AppFrame() {
		game=new Game();
		menuBar=new JMenuBar();
		gameInfo=new JLabel("   Score: "+game.currScore+ "   Highscore: "+game.highScore);
		backToMenu=new JButton("Back");
		menuBar.add(gameInfo);
		
		Thread gameThread=new Thread(game);
		gameThread.start();
		
		add(menuBar, BorderLayout.NORTH);
		add(game, BorderLayout.CENTER);
		setSize(400,800);
		setTitle("Tetris");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}
}