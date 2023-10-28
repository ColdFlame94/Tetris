package Tetris;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Square extends JPanel{
	 Color color;
	 
	 public Square() {
		 color=Color.BLACK; 
	 }
	
	public void paintComponent(Graphics g) {
		 g.setColor(Color.DARK_GRAY);
		 g.drawRect(0, 0, getWidth(), getHeight());
		 g.setColor(color);
		 g.fillRect(1, 1, getWidth()-1, getHeight()-1);
	}	 
}