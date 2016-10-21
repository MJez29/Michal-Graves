/**
 * @(#)Platform.java
 *
 *
 * @author 
 * @version 1.00 2016/3/26
 */

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Platform extends Block
{
	public static final BufferedImage WATCH_TOWER_LEFT = ImageTools.initializeImage("Watch Tower Images\\Platform Left Side.png");
	public static final BufferedImage WATCH_TOWER_RIGHT = ImageTools.flipHorizontally(WATCH_TOWER_LEFT);
	public static final BufferedImage[] WATCH_TOWER_MIDDLES = ImageTools.initializeImages("Watch Tower Images\\Platform ", 5);

    public Platform(int x, int y) 
    {
    	super(x, y, "platform");
    }
    
    @Override
    public void draw(Graphics g)
    {
    	if (img == null)
    	{
    		g.setColor(Color.gray);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    	}
    	else
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    }
    
    public void initialize(Block[][] biome)
    {
    	int w = biome.length - 1;
    	
    	//If there is no platform block to the left of the block
    	if (x > 0 && !biome[x - 1][y].getType().equals("platform"))
    	{
    		img = WATCH_TOWER_LEFT;
    	}
    	//If there is no platform block to the right of the block
    	else if (x < w && !biome[x + 1][y].getType().equals("platform"))
    	{
    		img = WATCH_TOWER_RIGHT;
    	}
    	//If there are platform blocks to both sides of the block
    	else
    	{
    		img = WATCH_TOWER_MIDDLES[(int)(Math.random() * WATCH_TOWER_MIDDLES.length)];
    	}
    }
}