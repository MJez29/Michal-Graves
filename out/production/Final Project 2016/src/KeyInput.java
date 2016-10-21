//This class alerts the Player class when a user presses and/or releases a button

import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.*;

public class KeyInput extends KeyAdapter
{
	private boolean[] keys = new boolean[KeyEvent.KEY_LAST+1];
	Player user;
	String fileName;					//The name of the .txt file that the controls are saved in
	private int shoot;
	private int jump;
	private int left;					//Correspond to the index of the specific key
	private int right;					//That the player presses to do an action
	private int up;
	private int down;

    public KeyInput(String f,Player p) throws IOException
    {
    	fileName = f;
    	Scanner inFile = new Scanner(new BufferedReader(new FileReader(fileName)));
		user = p;

		//Scans the file until it finds the key configurations of the right player
		while (inFile.nextLine().equals(user.getName())==false){}

		shoot = Integer.parseInt(inFile.nextLine().split(" ")[1]);
    	jump = Integer.parseInt(inFile.nextLine().split(" ")[1]);
    	left = Integer.parseInt(inFile.nextLine().split(" ")[1]);
    	right = Integer.parseInt(inFile.nextLine().split(" ")[1]);
    	up = Integer.parseInt(inFile.nextLine().split(" ")[1]);
    	down = Integer.parseInt(inFile.nextLine().split(" ")[1]);
    	
    	inFile.close();
    }
    
    public void keyTyped(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) 
    {
    	keys[e.getKeyCode()] = true;
    	if (e.getKeyCode() == shoot)		{ user.setIsShooting(true);		}
		else if (e.getKeyCode() == jump)	{ user.setIsJumping(true); 		}
		else if (e.getKeyCode() == left)	{ user.setIsMovingLeft(true); 	}
		else if (e.getKeyCode() == right)	{ user.setIsMovingRight(true); }
		else if (e.getKeyCode() == up)		{ user.setIsMovingUp(true); 	}
		else if (e.getKeyCode() == down)	{ user.setIsMovingDown(true); 	}
    }
    
    public void keyReleased(KeyEvent e) 
    {
		keys[e.getKeyCode()] = false;
		if (e.getKeyCode() == shoot)		{ user.setIsShooting(false);	}
		else if (e.getKeyCode() == jump)	{ user.setIsJumping(false); 	}
		else if (e.getKeyCode() == left)	{ user.setIsMovingLeft(false); 	}
		else if (e.getKeyCode() == right)	{ user.setIsMovingRight(false); }
		else if (e.getKeyCode() == up)		{ user.setIsMovingUp(false); 	}
		else if (e.getKeyCode() == down)	{ user.setIsMovingDown(false); 	}
	}
}