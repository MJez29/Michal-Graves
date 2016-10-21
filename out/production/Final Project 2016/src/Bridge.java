/**
 * @(#)Bridge.java
 *
 *
 * @author 
 * @version 1.00 2016/3/19
 */

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Bridge extends Block
{
    public static final BufferedImage BRIDGE = ImageTools.initializeImage("Bridge Images\\Bridge.png");
    
    //Images related to the checkpoint found at the end of each biome
    //Although they arent necessarilly bridges, they behave the same around the player
	public static final BufferedImage[] CHECKPOINT_BASE = { 	ImageTools.initializeImage("Checkpoint Images\\Base Left.png"),
																ImageTools.initializeImage("Checkpoint Images\\Base Center.png"),
																ImageTools.flipHorizontally(ImageTools.initializeImage("Checkpoint Images\\Base Left.png"))	};
	
    public Bridge(int x, int y) 
    {
    	super(x,y,"bridge");
    }
    
    public Bridge(int x, int y, BufferedImage bi) 
    {
    	super(x,y,"bridge", bi);
    }
    
    public void draw(Graphics g)
    {
    	if (img != null)
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    	else
    	{
    		g.setColor(Color.gray);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    	}
    }
    
    @Override
    public void initialize(Block[][] biome)
    {
    	if (img == null)
    	{
    		img = BRIDGE;
    	}
    }
}