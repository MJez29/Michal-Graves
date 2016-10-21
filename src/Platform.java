//Michal Jez
//17/06/2016
//A block that is indestructible (Integer.MAX_VALUE health)

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
	public static final BufferedImage START_LEFT = ImageTools.initializeImage("Starting Point Images\\End.png");
	public static final BufferedImage START_RIGHT = ImageTools.flipHorizontally(START_LEFT);
	public static final BufferedImage START_MIDDLE = ImageTools.initializeImage("Starting Point Images\\Middle.png");

	public static final BufferedImage METAL = ImageTools.initializeImage("Platform Images\\Platform.png");

	//Images related to the checkpoint found at the end of each biome
	//Although they arent necessarilly bridges, they behave the same around the player
	public static final BufferedImage[] CHECKPOINT_BASE = { 	ImageTools.initializeImage("Checkpoint Images\\Base Left.png"),
			ImageTools.initializeImage("Checkpoint Images\\Base Center.png"),
			ImageTools.flipHorizontally(ImageTools.initializeImage("Checkpoint Images\\Base Left.png"))	};


	public static final int STARTING_HEALTH = Integer.MAX_VALUE;

	public enum SubType
	{
		WATCH_TOWER,
		START
	};

	private Color WATCH_TOWER_COL = new Color(255, 187, 87);
	private Color START_COL = new Color(86, 108, 119);

    public Platform(int x, int y) 
    {
    	super(x, y, "platform", STARTING_HEALTH);
    }

	public Platform(int x, int y, BufferedImage bi)
	{
		super(x, y, "platform", bi, STARTING_HEALTH);
	}

	public Platform(int x, int y, Platform.SubType s)
	{
		super(x, y, "platform", STARTING_HEALTH);
		switch(s)
		{
			case WATCH_TOWER:
				particleCol = WATCH_TOWER_COL; break;
			case START:
				particleCol = START_COL; break;
		}
	}

	public Platform(int x, int y, BufferedImage bi, Platform.SubType s)
	{
		this(x, y, s);
		img = bi;
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
    {	//Initializes the block with the correct tile
		if (img != null) return;
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