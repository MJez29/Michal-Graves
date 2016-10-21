//This class is responsible for managing the state of the game, it manages what screen should be displayed to the user
//Whether its the startup screen, the help screen, the actual game portion, etc...

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.io.*;
import java.util.Scanner;

public class StateManager extends JFrame implements ActionListener
{
	private Timer framerate;
	
	private Game game;
	
	private State curState;
    
    public StateManager() throws IOException
    {
    	super("The Poutine Initiative");
    	setSize(800,600);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	
    	framerate = new Timer(10, this);
    	framerate.start();
    	
    	curState = State.GAME;
    	
    	game = new Game(this);
    	add(game);
    	setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt)
 	{	//Fired every time the framerate timer goes off
 		switch (curState)
 		{
 			case GAME:
 				if (game != null && game.isReady)
 				{
 					game.refresh();
 				}
 				break;
 		}
 	}
 	
 	public static void main(String[] args) throws IOException
 	{
 		new StateManager();
 	}
}