package Tetris;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class TetrisMenu extends JPanel implements ActionListener{
	static AppFrame gameFrame;
	static JFrame menuFrame;
	JPanel panel;
	JButton startGame, help, highScore, quit;
	Image menuBackGround, logo;
	
	public TetrisMenu() {
		setLayout(new GridBagLayout());
		menuFrame=new JFrame();
		panel=new JPanel();
		startGame=new JButton("New Game");
		help=new JButton("Help");
		highScore=new JButton("Highscore");
		quit=new JButton("Quit");
		panel.setLayout(new GridLayout(4,1));
		panel.add(startGame);
		panel.add(help);
		panel.add(highScore);
		panel.add(quit);
		add(panel);
		
		startGame.addActionListener(this);
		help.addActionListener(this);
		highScore.addActionListener(this);
		quit.addActionListener(this);
		
		try {
			menuBackGround=ImageIO.read(new File("Images//Menu.jpg"));
			logo=ImageIO.read(new File("Images//Logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menuFrame.setSize(300,300);
		menuFrame.add(this);
		menuFrame.setTitle("Tetris");
		menuFrame.setLocationRelativeTo(null);
		menuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		menuFrame.setVisible(true);
		menuFrame.setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startGame) 
		{
			gameFrame=new AppFrame(); 
			menuFrame.setVisible(false);
		}
		else if(e.getSource()==help)//This button gives info about in-game controls
		{
			JOptionPane.showMessageDialog(new JPanel(), "-Press the ⬅ ➡ keys to move the block from side to side.\n"
					+ "-Press and hold the ⬇ key to accelerate the block.\n"
					+ "-Press the space key to rotate the block.", "Info",JOptionPane.INFORMATION_MESSAGE);
			
		}
		else if(e.getSource()==highScore)//This button shows the current highscore
		{
			 String data="";
			 try {
			      File file = new File("Tetris' Highscore.txt");
			      Scanner scanner = new Scanner(file);
			      while (scanner.hasNextLine()) {
			    	  data = scanner.nextLine();  
			      }
			      JOptionPane.showMessageDialog(new JPanel(), "Highscore: "+data, "Info",JOptionPane.INFORMATION_MESSAGE);
			      scanner.close();
			    } catch (FileNotFoundException ex) {
			      ex.printStackTrace();
			    }
		}
		else if(e.getSource()==quit) //lets you quit the game
		{
			int res=JOptionPane.showConfirmDialog(new JPanel(), "Are you sure?", "Select an Option", JOptionPane.YES_NO_OPTION);
			if(res==JOptionPane.YES_OPTION)
			{
				System.exit(0);
			} 
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(menuBackGround,0,0,getWidth(), getHeight(),null);
		g.drawImage(logo,50,10,getWidth()/2+50, getHeight()/2-70,null);
	}
	
	public static void main(String[] args) {
		new TetrisMenu();	
	}
}