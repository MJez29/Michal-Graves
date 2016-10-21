//Michal Jez
//17/05/2016
//This class represents an enemy spawner located in a trench
//At set intervals it will generate a new enemy

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.LinkedList;

public class Spawner extends Block implements ActionListener
{
    protected static LinkedList<Spawner> spawners = new LinkedList<Spawner> ();

    public static final BufferedImage SPAWNER_IMG = ImageTools.initializeImage("Trench Images\\Spawner.png");

    public static final int FAST = 5000;
    public static final int MEDIUM = 10000;         //Different speeds that the trench can spawn enemies at
    public static final int SLOW = 5000;

    public static final Color pCol = new Color(2, 217, 217);
    public static final int HEALTH = 600;

    protected Player target;                    //The Player responsible for the activation of this trench

    protected Rectangle trenchRect;             //The rectangle that the trench occupies in the biome

    protected boolean isActivated;              //If the spawner is activated
    protected boolean isDestroyed;              //If the spawner has been destroyed (And can't be reactivated)

    protected int spawnSpeed;               //How many milliseconds pass between each enemy spawned

    protected javax.swing.Timer timer;      //Timer that fires off every time an enemy is to be spawned

    public Spawner(int nx, int ny, int ns)
    {   //Although the block behaves similar to dirt it is not the same as dirt so it gets its own class
        super(nx, ny, BLOCK_WIDTH * 2, BLOCK_HEIGHT, "dirt", SPAWNER_IMG, pCol, HEALTH);
        isActivated = false;
        spawnSpeed = ns;
        timer = new javax.swing.Timer(spawnSpeed, this);
        isTrenchBlock = true;
        trenchRect = new Rectangle((x - 3) * GameMap.BLOCK_WIDTH, (y - 4) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH * 6, GameMap.BLOCK_HEIGHT * 5);
        spawners.add(this);
    }

    @Override
    public void reduceHealth(int amnt, Direction d, GameMap gm)
    {   /*
            Reduces the health of the spawner
        */
        health -= amnt;
        if (health <= 0)
        {
            deactivate();                   //Shuts off spawner if destroyed
            gm.destroyBlock(x, y, d);
            gm.destroyBlock(x + 1, y, d);
        }
    }

    public void activate(Player p)
    {   //Activates the spawner
        if (!isDestroyed) {
            isActivated = true;
            timer.start();
            target = p;
        }
    }

    public void deactivate()
    {   //Deactivates the spawner
        isActivated = false;
        timer.stop();
    }

    public void destroy()
    {
        isDestroyed = true;
        deactivate();
    }

    public void actionPerformed(ActionEvent e)
    {   //Makes a new enemy
        Enemy.addEnemy(new EnemyTrencher(x * GameMap.BLOCK_WIDTH, y * GameMap.BLOCK_HEIGHT - EnemyTrencher.HEIGHT, target, trenchRect));
    }

    //--------------------------------------------------------- Static Methods ------------------------------------------------------------------
    public static void pauseAll()
    {   /*
           Pauses all spawners
        */
        for (Spawner s : spawners)
            s.timer.stop();
    }

    public static void destroyAll()
    {   /*
            Destroys all spawners
        */
        pauseAll();
        spawners.clear();
    }

    public static void startAll()
    {   /*
            Starts all spawners, called after the game is paused
        */
        for (Spawner s : spawners)
        {
            if (s.isActivated) s.timer.start();
        }
    }
}
