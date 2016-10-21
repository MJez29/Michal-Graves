/**
 * @(#)Game.java
 *
 *
 * @author 
 * @version 1.00 2016/3/11
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Game extends JPanel
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
	
	private LinkedList<Player> players;
	
    public Game(StateManager sm)  throws IOException
    {
    	//addNotify() needs to be called for the game to be ready
    	isReady = false;
    	
    	stateManager = sm;
    	gameMap = new GameMap(sm);
    	subMap = gameMap.getSubMap();
    	
    	x = 1250; y = 1250;
    	players = new LinkedList<Player> ();
		Player p1 = new Player(700, 1600,"sarge",1);
    	players.offer(p1);
		//players.offer(new Player(900, 2000,"sarge",2));

		for (Player p : players){
			addKeyListener(new KeyInput("controls.txt",p));
		}
    }
    
    public void addNotify() 
    {	/*Will be called when the game is ready to begin*/
        super.addNotify();
        requestFocus();
        isReady = true;   
    }
    
    public void refresh()
    {
    	long beforeTime = System.nanoTime();
    	x += 5;
		y--;
    	repaint();
    	//players.peek().x = (players.peek().getIntX() - 5);
    	//players.peek().y = (players.peek().getIntY() - 5);
    	now=System.currentTimeMillis(); 
    	//framesCount++; System.out.println(framesCountAvg);
        if(now-framesTimer>1000)
        { 
        	
              framesTimer=now; 
              framesCountAvg=framesCount; 
              framesCount=0; 
        }
		for (Player p: players){
			p.move();
		}
    }
    
    @Override 
    public void paintComponent(Graphics g)
    {
    	gameMap.updateSubMap(g, players);

		for (Player p : players){
			p.draw(subMap);
		}

    	g.drawImage(subMap.getScaledImg(), 0, 0, null);
    }
}