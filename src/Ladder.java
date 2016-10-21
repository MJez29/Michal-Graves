//Michal Jez
//08/05/2016
//This class provides the implementation of blocks that the player can climb
//Ladder blocks are indestructible and can be shot through

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Ladder extends Block
{
	public static final BufferedImage img = ImageTools.initializeImage("Ladder Images\\Ladder.png");

	public static final int STARTING_HEALTH = Integer.MAX_VALUE;
	
    public Ladder(int x, int y) 
    {
    	super(x,y,"ladder", STARTING_HEALTH);
    }

	@Override
    public void draw(Graphics g)
    {
    	if (img != null)
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    	else
    	{
    		g.setColor(Color.green);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    	}
    }
}