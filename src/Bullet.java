//Michal Jez, Shadman Hassan
//05/05/2016
//This class allows the game to create projectiles that players and enemies fire at each other
//It provides static methods to move all the bullets and other methods to move an individual bullet and
//Check for collisions with the map or players
//For the Players it also keeps track of the number of kills each Player gets per game

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;

public class Bullet extends FourCornerRect
{
    private static LinkedList<Bullet> bullets = new LinkedList<Bullet> ();                  //All bullets
	public static final BufferedImage BULLET_HORIZ = ImageTools.initializeImage("Bullet Images\\Bullet Horizontal.png");            //Bullet moving left or right
	public static final BufferedImage BULLET_VERT = ImageTools.initializeImage("Bullet Images\\Bullet Vertical.png");               //Bullet moving up or down

    //The number of kills each Player has
    protected static int[] kills;

    //How much damage each bullet deals
    public static final int REGULAR_DAMAGE = 20;
	
	Direction dir;

    private int damage;             //The damage each bullet does

	private int v;					//The velocity of the bullet
    private boolean shotByPlayer; //True if the bullet was shot by the player, False if shot by enemy
	
	private BufferedImage img;		//The image of the current bullet object

    private int shotBy;             //The index of the Player that shot the bullet

	public Bullet(int nlx, int nly, Direction d, boolean b)
	{   //Default constructor
        super(nlx, nly, (d == Direction.LEFT || d == Direction.RIGHT) ? BULLET_HORIZ.getWidth() : BULLET_VERT.getWidth(),
                        (d == Direction.LEFT || d == Direction.RIGHT) ? BULLET_HORIZ.getHeight() : BULLET_VERT.getHeight());
        shotByPlayer = b;
        switch (d)                  //Initializes the correct image based on the direction the bullet is travelling
		{
			case UP: case DOWN:
			    img = BULLET_VERT;
			    break;
			default:
				img = BULLET_HORIZ;
				break;
		}
		dir = d;
		v = 15;
        damage = REGULAR_DAMAGE;

        bullets.add(this);
	}

    public Bullet(int nlx, int nly, Direction d, boolean b, int np)
    {   /*
            Called when a Player shoots the bullet
            Allows the bullet class to keep track of who killed which enemy
        */
        this(nlx, nly, d, b);
        shotBy = np;
    }

	public void draw(Graphics g, int nlx, int nly)
	{	//Draws the bullet onto the SubMap
		g.drawImage(img, lx - nlx, ty - nly, null);
	}

	public void move()
	{	//Moves the bullet in the correct direction
		switch (dir)
		{
			case UP:
				moveY(-v);
				break;
			case DOWN:
				moveY(v);
				break;
			case LEFT:
				moveX(-v);
				break;
			case RIGHT:
				moveX(v);
				break;
		}
	}

    public boolean checkMapCollisions(GameMap gm)
    {   //Goes through every target to see if it collides with them
        //Returns true if the bullet is to be removed from the Collection of bullets
        Block[][] biome = gm.getBiome();

        switch (dir)
        {
            case UP:
                if (ty < 0) {		//If it is outside the map
                    return true;
                } else {
                    int tempLX = lx / GameMap.BLOCK_WIDTH;
                    int tempRX = rx / GameMap.BLOCK_WIDTH;
                    int tempY = ty / GameMap.BLOCK_HEIGHT;
                    if (!biome[tempLX][tempY].canShootThrough()) {		//If the left-top corner collides with a block
                        biome[tempLX][tempY].reduceHealth(damage, dir, gm);
                        return true;
                    } else if (!biome[tempRX][tempY].canShootThrough()) {	//If the right-top corner collides with a block
                        biome[tempRX][tempY].reduceHealth(damage, dir, gm);
                        return true;
                    }
                }
                break;

            case DOWN:
                if (by >= GameMap.BIOME_HEIGHT * GameMap.BLOCK_HEIGHT) {		//If it is outside the map
                    return true;
                } else {
                    int tempLX = lx / GameMap.BLOCK_WIDTH;
                    int tempRX = rx / GameMap.BLOCK_WIDTH;
                    int tempY = by / GameMap.BLOCK_HEIGHT;
                    if (!biome[tempLX][tempY].canShootThrough()) {		//If the left-top corner collides with a block
                        biome[tempLX][tempY].reduceHealth(damage, dir, gm);
                        return true;
                    } else if (!biome[tempRX][tempY].canShootThrough()) {	//If the right-top corner collides with a block
                        biome[tempRX][tempY].reduceHealth(damage, dir, gm);
                        return true;
                    }
                }
                break;

            case LEFT:
                if (lx < 0) {		//If it is outside the map
                    return true;
                } else {
                    int tempX = lx / GameMap.BLOCK_WIDTH;
                    int tempTY = ty / GameMap.BLOCK_HEIGHT;
                    int tempBY = by / GameMap.BLOCK_HEIGHT;
                    if (!biome[tempX][tempTY].canShootThrough()) {		//If the left-top corner collides with a block
                        biome[tempX][tempTY].reduceHealth(damage, dir, gm);
                        return true;
                    } else if (!biome[tempX][tempBY].canShootThrough()) {	//If the right-top corner collides with a block
                        biome[tempX][tempBY].reduceHealth(damage, dir, gm);
                        return true;
                    }
                }
                break;

            case RIGHT:
                if (rx >= GameMap.BIOME_WIDTH * GameMap.BLOCK_WIDTH) {		//If it is outside the map
                    return true;
                } else {
                    int tempX = rx / GameMap.BLOCK_WIDTH;
                    int tempTY = ty / GameMap.BLOCK_HEIGHT;
                    int tempBY = by / GameMap.BLOCK_HEIGHT;
                    if (!biome[tempX][tempTY].canShootThrough()) {		//If the left-top corner collides with a block
                        biome[tempX][tempTY].reduceHealth(damage, dir, gm);
                        return true;
                    } else if (!biome[tempX][tempBY].canShootThrough()) {	//If the right-top corner collides with a block
                        biome[tempX][tempBY].reduceHealth(damage, dir, gm);
                        return true;
                    }
                }
                break;
            default: break;
        }
        return false;
    }

    protected boolean checkPlayerCollisions(LinkedList<Player> players)
    {   /*
            Checks to see if the bullet collides with any Player
            Returns true if it does
        */
        Iterator it = players.iterator();
        while (it.hasNext())
        {
            Player p = (Player) it.next();
            if (p.intersects(this))
            {
                p.kill();
                it.remove();
                return true;
            }
        }
        return false;
    }

    protected boolean checkEnemyCollisions(HashMap<Long, Enemy> enemies)
    {   /*
            Checks to see if the bullet collides with any Enemy
            Returns true if it does
        */
        Iterator it = enemies.values().iterator();
        while (it.hasNext())
        {
            Enemy e = (Enemy) it.next();
            if (e.intersects(this))
            {
                e.kill();
                kills[shotBy]++;
                it.remove();
                return true;
            }
        }
        return false;
    }

	public static void checkCollisionsAll(GameMap gm)
	{	//Goes through every bullet and checks for collisions between the bullets and their targets and the
		//Bullets and the environment
		Iterator it = bullets.iterator();
        LinkedList<Player> players = Player.getPlayers();
        HashMap<Long, Enemy> enemies = Enemy.getEnemies();

        while (it.hasNext())
        {
            Bullet b = (Bullet) it.next();

            if (b.shotByPlayer && b.checkEnemyCollisions(enemies))
            {   it.remove(); continue;    }
            else if (!b.shotByPlayer && b.checkPlayerCollisions(players))
            {   it.remove(); continue;    }
            if (b.checkMapCollisions(gm))
            {   it.remove(); continue;    }
        }
	}

	public static void moveAll()
	{	//Moves every bullet
		for (Bullet b : bullets)
		{
			b.move();
		}
	}

    public static void makeBullet(int nx, int ny, boolean shotByPlayer, Direction d, boolean b)
    {   //Makes a bullet
        Bullet temp = new Bullet(nx, ny, d, b);
        bullets.offer(temp);
    }

    public static void makeBullet(int nx, int ny, boolean shotByPlayer, Direction d, boolean b, int playerNum)
    {   //Makes a bullet
        Bullet temp = new Bullet(nx, ny, d, b, playerNum);
        bullets.offer(temp);
    }

    public static void drawAll(SubMap sm)
    {   //Draws all the bullets onto the SubMap
        Graphics g = sm.getGraphics();
        int lx = sm.getIntX();
        int ly = sm.getIntY();
        for (Bullet b : bullets)
        {
            b.draw(g, lx, ly);
        }
    }

    public static void clearBullets()
    {
        bullets.clear();
    }

    public static void clearKills()
    {
        kills = null;
    }

    public static void setNumPlayers(int np)
    {
        kills = new int[np];
    }

    public static int[] getKills() { return kills; }

    public Bullet()
    {
        super(0,0,0,0);
    }
}