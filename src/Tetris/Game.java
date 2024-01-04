package Tetris;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable{
	Square squares[];
	Color squareColor;
	String highScore;
	boolean isBlockMovingDown, stopGame, isUnrotateableShape;
	int sleepTime, tempSleepTime, x1, x2, x3, x4, index, currScore, prevBlockNum, clearedRowsCounter;
	int rotations[][]={{0,0,0,0}, //Every array in the matrix represents the coordinates of the variations of every single block
			           {9,0,-9,-18},{-9,0,9,18},
			           {-1,11,0,10},{0,-19,0,-19},{0,-2,0,18},{1,10,0,-9}, 
			           {1,-8,0,9},{9,10,0,1},{-8,0,10,0},{8,8,0,0},
			           {10,0,0,8},{-10,0,0,-8},
				       {0,10,12,0},{0,-10,-12,0},
				       {0,11,0,0},{9,0,0,0},{0,0,0,-11},{-9,-11,0,11},/*{-10,-12,-1,10}*/};
	int unrotateableShapesOnLeft[]= {1,5,8,10,14,16,18};
	int unrotateableShapesOnRight[]= {1,4,6,12,18};
		
	 public Game() {
		 setLayout(new GridLayout(20,10));
		 squares=new Square[200];
		 sleepTime=1500;
		 stopGame=false;
		 prevBlockNum=-1;
		 isUnrotateableShape=false;
		 
		 for(int i=0;i<squares.length;i++)
		 {
			 squares[i]=new Square();
			 add(squares[i]);
		 }
		 
		 try {
		      File file = new File("Tetris' Highscore.txt");//The highscore is stored in a txt file and can be viewed in the menu
		      Scanner scanner = new Scanner(file);
		      while (scanner.hasNextLine()) {
		    	 highScore = scanner.nextLine();//I store the current highscore, so later I can check if it should be updated
		      }
		      scanner.close();
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
		 
		 setFocusable(true);
		 addKeyListener(new TetrisListener());
	 }
	 
	 public class TetrisListener extends KeyAdapter{
		 public void keyPressed(KeyEvent ke)
		 {
			 if(ke.getKeyCode() == KeyEvent.VK_SPACE) 
			 {	
				 int arr[]= {x1,x2,x3,x4};
				 
				 for(int i=0;i<arr.length;i++) //The block won't be rotated if it's near the left/right edge to prevent    
				 {
					 if(findNumInArr(index,unrotateableShapesOnLeft) && arr[i]%10==0 || findNumInArr(index,unrotateableShapesOnRight) 
							 && arr[i]%10==9 || index==1 && arr[i]%10==8)
					 {
						 isUnrotateableShape=true;
					 }
				 }	
				 if(isBlockMovingDown && !isUnrotateableShape)
				 {
					 for(int i=0;i<squares.length;i++)//I rotate the block by repainting the block's coordinates by using the rotations matrix
					 {
						 if(i==x1 || i==x2 || i==x3  || i==x4 )
						 {
							 squares[i].color=Color.BLACK;
							 squares[i].repaint();
						 }
						 if(i==x1+rotations[index][0] || i==x2+rotations[index][1] || i==x3+rotations[index][2] || i==x4+rotations[index][3])
						 {
							 squares[i].color=squareColor;
							 squares[i].repaint();
						 }
					 }				 
//					 for(int i=0;i<arr.length;i++)
//					 {
//						 squares[arr[i]].color=Color.BLACK;
//						 squares[arr[i]].repaint();
//						 squares[arr[i]+rotations[index][i]].color=squareColor;
//						 squares[arr[i]+rotations[index][i]].repaint();	
//					 }	 
					 x1+=rotations[index][0];
					 x2+=rotations[index][1];
					 x3+=rotations[index][2];
					 x4+=rotations[index][3];
					 
					 for(int i=0;i<arr.length;i++)
					 {
						 arr[i]+=rotations[index][i];
					 }
					 
					 checkForBlocksOrFloorBelow(arr);
					 
					 switch(index) { // After drawing, the index increases or reverts to its' first value in order to draw the next rotation
					 case 1,3,4,5,7,8,9,11,13,15,16,17 -> index++;
					 case 2 -> index=1;
					 case 6 -> index=3;
					 case 10 -> index=7;	
					 case 12 -> index=11;
					 case 14 -> index=13;
					 case 18 -> index=15;
					 }
				 }		
				 isUnrotateableShape=false;
			 }
			 else if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
			 { 
				 moveBlockSideways("Right");
				 int arr[]= {x1,x2,x3,x4};
				 checkForBlocksOrFloorBelow(arr);
			 }
			 else if(ke.getKeyCode() == KeyEvent.VK_LEFT)
			 {
				 moveBlockSideways("Left");
				 int arr[]= {x1,x2,x3,x4};
				 checkForBlocksOrFloorBelow(arr);
			 }
			 else if(ke.getKeyCode() == KeyEvent.VK_DOWN) //Makes the block move down faster
			 {
				 if(sleepTime!=50)
				 {
					 tempSleepTime=sleepTime;
				 }
				 sleepTime=50;
			 } 
		 }
		 public void keyReleased(KeyEvent ke) {
			 if(ke.getKeyCode() == KeyEvent.VK_DOWN) 
			 {
				 sleepTime=tempSleepTime;
			 }
		 }
	 }
	 
	@Override
	public void run() {	
		while(!stopGame){
			if(!isBlockMovingDown)
			{
				isBlockMovingDown=true;
				int arr[]= {0,1,3,7,11,13,15};//Every number represents one of the 7 default blocks
				int blockNum;
				
				do {
					blockNum=(int) (Math.random()*7);//I pick a random index to draw a random block based on the index and I make sure the same index won't be picked twice.
				}while(blockNum==prevBlockNum);

				prevBlockNum=blockNum;
				index=arr[blockNum];//The index is saved to use it later for the block's rotations

				switch(arr[blockNum]){//Determines the coordinates of the chosen shape and its' color
				case 0:
					x1=4;
					x2=5;
					x3=14;
					x4=15;
					squareColor=Color.YELLOW;
					break;
				case 1:
					 x1=4;
					 x2=14;
					 x3=24;
					 x4=34;
					 squareColor=Color.CYAN;
					 break;
				case 3:
					 x1=5;
					 x2=13;
					 x3=14;
					 x4=15;
					 squareColor=new Color(255, 130, 10);//Orange
					 break;
				case 7:
					 x1=3;
					 x2=13;
					 x3=14;
					 x4=15;
					 squareColor=Color.BLUE;
					 break;
				case 11:
					 x1=4;
					 x2=5;
					 x3=15;
					 x4=16;
					 squareColor=Color.RED;
					 break;
				case 13:
					 x1=5;
					 x2=6;
					 x3=14;
					 x4=15;
					 squareColor=new Color(70, 190, 10);//Green
					 break;
				case 15:
					 x1=4;
					 x2=13;
					 x3=14;
					 x4=15;
					 squareColor=new Color(168, 0, 188);//Purple
					 break;
				}
			}
			
			int arr[]= {x1,x2,x3,x4};
			
			for(int i=0;i<arr.length;i++) //I repaint the block in the next position below
			{
				squares[arr[i]].color=squareColor;
				squares[arr[i]].repaint();
			}	
			
			for(int i=0;i<arr.length;i++) //I repaint the previous coordinates of the block in black 
			{
				if(arr[i]>9 && !findNumInArr(arr[i]-10,arr))
				{
					squares[arr[i]-10].color=Color.BLACK;
					squares[arr[i]-10].repaint();
				}
			}
			
			checkForBlocksOrFloorBelow(arr);
					
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			x1+=10;
			x2+=10;
			x3+=10;
			x4+=10;
			
			if(!isBlockMovingDown) {
				for(int i=0;i<arr.length;i++)//Checks if the game should be stopped, which happens when the block lands at the very top of the board
				{
					if(arr[i]==14 || arr[i]==15)
					{
						stopGame=true;
						break;
					}
				}
				if(stopGame)//If the game is over, I update the new highscore (if there's one) and return to the main menu
				{
					if(currScore>Integer.parseInt(highScore))
					{
						try {
					      FileWriter fileWriter = new FileWriter("Tetris' Highscore.txt");
					      fileWriter.write(Integer.toString(currScore));
					      fileWriter.close();
					    } catch (IOException e) {
					      e.printStackTrace();
					    }
						JOptionPane.showMessageDialog(new JPanel(), "You've achieved a new highscore!", "Info", JOptionPane.INFORMATION_MESSAGE);
					}
					JOptionPane.showMessageDialog(new JPanel(), "Game Over", "Info", JOptionPane.INFORMATION_MESSAGE);
					TetrisMenu.gameFrame.setVisible(false);
					TetrisMenu.menuFrame.setVisible(true);
				}
				
				int min=190, max=199;
				
				while(min>0) {
					if(checkIfRowCanBeCleared(min,max))//If the row can be cleared, the score is increased and if you reach the milestone, you level up
					{
						for(int j=max;j>=0;j--)//I clear the row by giving each square the color of the square above it
						{
							if(j>9)
							{
								squares[j].color=squares[j-10].color;
								squares[j].repaint();	
							}
						}
						currScore+=100;						
						if(clearedRowsCounter>0)
						{
							currScore+=50;
						}
						AppFrame.gameInfo.setText("   Score: "+currScore+ "   Highscore: "+highScore);
						clearedRowsCounter++;
					}
					else
					{
						min-=10;//If the row wasn't cleared I move on to check the next one above
						max-=10;
						clearedRowsCounter=0;
					}	
				}
			}
		}	
	}
	 
	 public boolean findNumInArr(int num, int arr[]) { 
		 for(int i=0;i<arr.length;i++)
		 {
			 if(arr[i]==num)
			 {
				 return true;
			 }
		 }
		 return false;
	 }
	
	public boolean checkIfRowCanBeCleared(int min, int max){ //If the entire row has no black squares, it can be cleared
		for(int i=min;i<=max;i++)
		{
			if(squares[i].color==Color.BLACK)
			{
				return false;
			}
		}
		return true;
	}
	
	public void moveBlockSideways(String direction) {
		boolean moveSideways=true;
		int arr[]= {x1,x2,x3,x4}, sign=(direction.equals("Right"))?1:-1, num=(direction.equals("Right"))?9:0;//Based on the direction I give values to:
		//num - which is used to check collisions with walls and sign - which is used to check collisions with blocks on the right/left
			 
		 for(int i=0;i<arr.length;i++)
		 {
			if(arr[i]%10==num || squares[arr[i]+sign].color!=Color.BLACK && !findNumInArr(arr[i]+sign,arr))//the block won't be moved if it's near the left/right border,  
			{                                                                                      //or if there are other blocks on its' left/right
				moveSideways=false;
				break;
			}
		 }
		 
		 if(moveSideways && isBlockMovingDown)//Repaints the block if it's been moved and updates the coordinates
		 {
			for(int i=0;i<arr.length;i++)
			{
				squares[arr[i]].color=Color.BLACK;
				squares[arr[i]].repaint();
			}
				
			for(int i=0;i<arr.length;i++)
			{
				squares[arr[i]+sign].color=squareColor;
				squares[arr[i]+sign].repaint();
			}
			x1+=sign;
			x2+=sign;
			x3+=sign;
			x4+=sign;
		 } 
	}
	
	public void checkForBlocksOrFloorBelow(int arr[]){
		for(int i=0;i<squares.length-10;i++)
		{//The block won't move down anymore if it collides with other blocks or the floor  
			if(findNumInArr(i,arr) && squares[i+10].color!=Color.BLACK && !findNumInArr(i+10,arr) 
					|| findNumInArr(i+10,arr) && i+10>=190 && i+10<=199)
			{
				isBlockMovingDown=false;	
			}
		}
	}
}