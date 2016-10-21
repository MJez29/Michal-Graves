//Michal Jez, Shadman Hassan
//10/05/2016
//This type of block creates an explosion when it is destroyed
//Creates an instance of the Explosion class when it explodes

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Explosive extends Block
{
	public static final BufferedImage EXPLOSIVE = ImageTools.initializeImage("Explosives Images\\Explosive.png");

    public static final Color pCol = new Color(255, 0, 0);

    public static final int HEALTH = 1;                 //Explosives are 1 hit kills
	
    public Explosive(int nx, int ny) 
    {
    	super(nx, ny, "explosive", EXPLOSIVE, pCol, HEALTH);
    }

    @Override
    public void reduceHealth(int amnt, Direction d, GameMap gm)
    {   //Reduces the health of the block
        health -= amnt;
        if (health <= 0)
        {   //If the block has been destroyed it creates an explosion
            Explosion.makeExplosion(x * GameMap.BLOCK_WIDTH + GameMap.BLOCK_WIDTH / 2, y * GameMap.BLOCK_HEIGHT + GameMap.BLOCK_HEIGHT / 2, Explosion.EXPLOSIVE_BLOCK);
            gm.destroyBlock(x, y, d);
            for (int ix = x - 5; ix < x + 6; ix++) {             //Destroys surrounding blocks
                for (int iy = y - 3; iy < y + 4; iy++) {
                    try {
                        gm.getAt(ix, iy).reduceHealth(250, Direction.NULL, gm);
                    } catch (Exception e) {}            //Error occurs if it is out bounds
                }
            }

            //The rectangle of the explosion
            Rectangle rect = new Rectangle((x - 5) * BLOCK_WIDTH, (y - 3) * BLOCK_HEIGHT, 11 * BLOCK_WIDTH, 7 * BLOCK_HEIGHT);
            Iterator it = Enemy.getEnemies().values().iterator();
            while (it.hasNext())            //Cycles through every enemy to see if the explosion kills any of them
            {
                Enemy e = (Enemy) it.next();
                if (e.intersects(rect))
                {   e.kill(); it.remove();   }
            }
            it = Player.getPlayers().iterator();
            while (it.hasNext())            //Cycles through every Player to see if they are close enough to be killed
            {
                Player p = (Player) it.next();
                if (p.intersects(rect))
                {
                    p.kill();
                    it.remove();
                }
            }
        }
    }
}