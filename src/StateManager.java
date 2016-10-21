//Michal Jez, Shadman Hassan
//17/06/2016
//This class is responsible for managing the state of the game, it manages what screen should be displayed to the user
//Whether its the startup screen, the help screen, the actual game portion, etc...
//This class contains the Main method

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.io.*;
import java.util.Scanner;

public class StateManager extends JFrame implements ActionListener, ComponentListener
{
	private Timer framerate;
	
	private Game game;
	private Menu menu;
	private PauseMenu pauseMenu;
	private GameOver gameOver;
	
	private State curState;
    
    public StateManager() throws IOException
    {
    	super("MICHAL GRAVES");
    	setSize(800,600);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);

		addComponentListener(this);

		curState = State.MENU;
    	
    	//game = new Game(this);
		menu = new Menu(getWidth(), getHeight(), this);
    	add(menu);

		setMinimumSize(new Dimension(800, 600));

		framerate = new Timer(10, this);
		framerate.start();
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
			case MENU:
				if (menu != null && menu.isReady)
				{
					menu.refresh();
				}
				break;
			case PAUSED:
				if (pauseMenu != null && pauseMenu.isReady)
				{
					pauseMenu.refresh();
				}
				break;
			case GAME_OVER:
				if (gameOver != null && gameOver.isReady)
				{
					gameOver.refresh();
				}
				break;
 		}
 	}

	public void gameOver()
	{	/*
			Called by Game when all the Players have been killed
		*/
		remove(game);
		curState = State.GAME_OVER;
		gameOver = new GameOver(this);
		add(gameOver);
	}

	public void startNewGame()
	{	/*
			Called by the menu to tell the state manager to switch to the game state because that is what the user selected
		*/
		try {
			remove(menu);					//Throws if going from 1 game to another
			remove(game);					//Throws if going from the menu to a game
		} catch(NullPointerException e) {}
		curState = State.GAME;
		//try {
			game = new Game(this, menu.getPlayersToMake(), true);					//New instance can only be made from Menu
		//} catch(Exception e) {}
		game.setSize(getSize());
		add(game);
	}

	public void continueGame()
	{
		/*
			Called by the game to tell the state manager to go to the next biome
		*/
		try {
			remove(game);
		} catch(NullPointerException e) {}
		curState = State.GAME;
		//try {
		game = new Game(this, menu.getPlayersToMake(), false);					//New instance can only be made from Menu
		//} catch(Exception e) {}
		game.setSize(getSize());
		add(game);
	}

	public void pauseGame(int[] dk, int uk[], int[] sk)
	{	/*
			Called by game to go to the pause game screen
		*/
		curState = State.PAUSED;
		remove(game);					//Removes game but does not delete it in case the Players choose to resume
		pauseMenu = new PauseMenu(this, dk, uk, sk);
		add(pauseMenu);
	}

	public void returnToGame()
	{	/*
			Returns to the game screen from the pause menu
		*/
		remove(pauseMenu);
		curState = State.GAME;
		game.setSize(getSize());			//So that the entire screen is repainted in case the JFrame was resized while the game was paused
		game.unPause();
		add(game);
	}

	public void goToMenu()
	{	/*
			Called by the pause menu or by game over to end the current game and go back to the menu
		*/
		try {
			remove(pauseMenu);					//Error if called by GameOver
		} catch(NullPointerException e) {}
		try {
			remove(gameOver);                    //Error if called by PauseMenu
		} catch(NullPointerException e) {}

		curState = State.MENU;
		game.end();
		menu = new Menu(getWidth(), getHeight(), this);
		menu.setSize(getSize());
		add(menu);
	}

	public void quitGame()
	{
		System.exit(0);
	}

	public void componentHidden(ComponentEvent event)
	{}

	public void componentResized(ComponentEvent event)
	{	/*
			Called whenever the JFrame is resized
			Calls different methods based on which JPanel is currently onscreen
		*/

		switch (curState)
		{
			case MENU:
				menu.adjustLayout(getWidth(), getHeight());
				break;
		}
	}

	public void componentShown(ComponentEvent event)
	{}

	public void componentMoved(ComponentEvent event)
	{}

 	public static void main(String[] args) throws IOException
 	{
 		new StateManager();
 	}


}