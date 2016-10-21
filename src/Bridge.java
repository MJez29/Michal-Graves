//Michal Jez, Shadman Hassan
//17/06/2016
//Bridges are structures that are 1 block high but span from one end of the map to the other
//Players can jump through bridges

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Bridge extends Block
{
    public static final BufferedImage BRIDGE = ImageTools.initializeImage("Bridge Images\\Bridge.png");

	public static final Color pCol = new Color(103, 65, 27);

	public static final int STARTING_HEALTH = 30;
    
	public Bridge(int x, int y)
    {
    	super(x,y,"bridge", pCol, STARTING_HEALTH);
    }
    
    public Bridge(int x, int y, BufferedImage bi) 
    {
	super(x,y,"bridge", bi, pCol, STARTING_HEALTH);
    }
    
    public void draw(Graphics g)
    {	//Draws the image, or a rectangle if the image is null
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
    {	//Initializes to the default image
    	if (img == null)
    	{
    		img = BRIDGE;
    	}
    }
}