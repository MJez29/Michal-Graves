/**
 * @(#)Air.java
 *
 *
 * @author 
 * @version 1.00 2016/3/27
 */

import java.awt.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;

public class Air extends Block
{
	//Images related to the watch tower structure
	public static final BufferedImage PILLAR = ImageTools.initializeImage("Watch Tower Images\\Pillar Original.png");
	public static final BufferedImage PILLAR_BOTTOM = ImageTools.initializeImage("Watch Tower Images\\Pillar Bottom.png");

	public static final BufferedImage START_PILLAR = ImageTools.initializeImage("Starting Point Images\\Pillar.png");
	public static final BufferedImage START_PILLAR_BASE = ImageTools.initializeImage("Starting Point Images\\Base.png");

	//Images of the checkpoint tower that goes up from the base
	public static final BufferedImage[] CHECKPOINT_TOWER = ImageTools.initializeImages("Checkpoint Images\\Tower ", 5);

    public Air(int x, int y) 
    {
    	super(x, y, "air");
    }
    
    public Air(int x, int y, String s) 
    {
    	super(x, y, "air");
    	subType = s;
    }

	public Air(int x, int y, BufferedImage bi)
	{
		super(x, y, "air", bi);
	}
    
    public Air(int x, int y, String s, BufferedImage bi)
    {
    	super(x, y, "air", bi);
    	subType = s;
    }
    
    @Override
    public void draw(Graphics g)
    {
    	if (img == null)			//No special image of the air
    	{
    		g.setColor(Color.blue);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    	}
    	else						//Has a special image
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    }
    
    @Override
    public void initialize(Block[][] biome)
    {	/*
    		Checks to see if the block should have a special image associated with it
    	*/
    	if (subType != null && img == null) {
    		if (subType.equals("pillar")) {
	    		if (biome[x][y + 1].getType().equals("dirt")) {
	    			img = PILLAR_BOTTOM;
	    		} else img = PILLAR;
    		}
    	}
    	
    }

	@Override
	public void reduceHealth(int amnt, Direction d, GameMap gm) {}		//Air cannot be destroyed
}