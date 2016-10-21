//Michal Jez, Shadman Hassan
//17/06/2016
//This is the class that controls the game that actually works
//The goal is to go from the left end of the biome to the checkpoint on the right side
//As the game goes on, Enemies attempt to kill the Players
//Player can also kill the Enemies
//Kills per individual Player are accumulated throughout the entire game
//The game ends once all of the Players are killed
//Once a Player reaches the checkpoint at the right side the screen fades out and moves on to the next biome

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Game extends JPanel implements KeyListener
{
	public boolean isReady;						//Indicates to the StateManager whether or not the game is ready
	
	private StateManager stateManager;
	private GameMap gameMap;
	private SubMap subMap;

	int x, y;
	
	private long now;
	private int framesCount = 0;
	private int framesCountAvg=0; 
	private long framesTimer=0;

	private boolean allDead;

	private boolean[] keys = new boolean[KeyEvent.KEY_LAST+1]; //Array of keycodes of keyboard

	private FlashingBlocks flashingBlocks;

	private boolean reachedCheckpoint;

	private int alpha;					//The alpha for making the screen fade to black
	
    public Game(StateManager sm, boolean[] makePlayers, boolean newGame)
    {
    	//addNotify() needs to be called for the game to be ready
    	isReady = false;
    	
    	stateManager = sm;
    	gameMap = new GameMap(sm);
    	subMap = gameMap.getSubMap();

		reachedCheckpoint = false;
		alpha = 255;

		allDead = false;

		flashingBlocks = gameMap.getFlashingBlocks();
    	
    	x = 1250; y = 1250;
		int np = 0;
		for (int i = 0; i < makePlayers.length; i++)
		{
			if (makePlayers[i]) {
				Player.addPlayer(new Player(gameMap.getSpawnX(), gameMap.getSpawnY(68), "sarge", i + 1));
				np++;
			}
		}

		if (newGame) { Bullet.setNumPlayers(np); }
		//players.offer(new Player(900, 2000,"sarge",2));

		addKeyListener(this);
		flashingBlocks.startTimer();
		Enemy.setEnemies(gameMap.getEnemies());
		Enemy.setGameMap(gameMap);
		Enemy.startTimers();
		setVisible(true);
    }
    
    public void addNotify() 
    {	/*Will be called when the game is ready to begin*/
        super.addNotify();
        requestFocus();
        isReady = true;   
    }
    
    public void refresh()
    {
		//For framerate
    	/*long beforeTime = System.nanoTime();
    	x += 5;
		y--;

    	now=System.currentTimeMillis(); 
    	framesCount++; System.out.println(framesCountAvg);
        if(now-framesTimer>1000)
        { 
        	
              framesTimer=now; 
              framesCountAvg=framesCount; 
              framesCount=0; 
        }
		*/

		Player.moveAll(keys, gameMap);
		Player.shootAll();

		Bullet.moveAll();
		Bullet.checkCollisionsAll(gameMap);
		Enemy.doAll();
		Particle.moveAll();
		Particle.checkCollisionsAll(gameMap);
		if (alpha > 0)
		{
			alpha += (reachedCheckpoint || allDead) ? 1 : -1;
			if (alpha >= 255)
			{
				end();
				if (allDead)
					stateManager.gameOver();
				else
					stateManager.continueGame();
			}
		}
		else if (gameMap.checkIfReachedCheckpoint())
		{
			Enemy.killAll();
			reachedCheckpoint = true;
			++alpha;
		}

		if (!Player.hasPlayers())
		{
			allDead = true;
			alpha++;
		}

		repaint();
    }

	public void gameOver()
	{

	}

	public void end()
	{	/*
			Called whenever the game moves on from the current biome
			If playersToo is true that means that the game is going to be exited and the application will return to the main menu
			If playersToo is false that means that the game is moving on to the next biome
		*/
		Player.killAll();
		Enemy.killAll();
		flashingBlocks.removeAll();
		Spawner.destroyAll();
		Particle.clearParticles();
		Bullet.clearBullets();
		setVisible(false);
	}

	public void unPause()
	{	/*
			Called by the StateManager to restart the current game
			Enables the timers
		*/
		Spawner.startAll();
		Enemy.startTimers();
	}
    
    @Override 
    public void paintComponent(Graphics g)
    {	/*
    		Draws the game
    	*/
		if (!allDead)			//If they are all dead the screen will stay where it is
    		gameMap.updateSubMap(g);

		Enemy.drawAll();

		Player.drawAll(subMap);

		Bullet.drawAll(subMap);
		Particle.drawAll(subMap);
		Explosion.drawAll(subMap);
		EnemyExclamation.drawAll(subMap);

    	g.drawImage(subMap.getScaledImg(), 0, 0, null);

		if (alpha > 0)
		{
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
    }

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e)
	{
		int k = e.getKeyCode();
		keys[k] = true;
		if (k == 27)					//If they press the escape key (pauses the game)
		{
			LinkedList<Player> players = Player.getPlayers();
			int numPlayers = players.size();

			//Gets the shoot (sk), down (dk) and up (uk) keys of the Players that are currently still alive in the game
			int[] dk = new int[numPlayers];
			int[] uk = new int[numPlayers];
			int[] sk = new int[numPlayers];

			int i = 0;
			for (Player p : players)
			{
				dk[i] = p.getDownKey();
				uk[i] = p.getUpKey();
				sk[i++] = p.getShootKey();
			}

			//Pauses all timers related to enemies
			Spawner.pauseAll();
			Enemy.pauseAll();

			//Changes to the pause screen
			stateManager.pauseGame(dk, uk, sk);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}
}