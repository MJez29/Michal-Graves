//Michal Jez, Shadman Hassan
//17/06/2016
//Blocks with low health that are found on the top of watch towers

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Roof extends Block
{
	public static final BufferedImage[] WATCH_TOWER_TOP = ImageTools.initializeImages("Watch Tower Images\\Roof T", 5);
	public static final BufferedImage[] WATCH_TOWER_BOTTOM = ImageTools.initializeImages("Watch Tower Images\\Roof B", 5);

	public static final Color pCol = new Color(248, 191, 93);

	public static final int HEALTH = 5;
	
	public static void initializeAt(Block[][] biome, int sx, int sy)
    {	//Starts at the top left block of the roof and sets the images for every block
    	for (int i = 0; i < 5; i++) {
    		biome[sx + i][sy].setImage(WATCH_TOWER_TOP[i]);
    		biome[sx + i][sy + 1].setImage(WATCH_TOWER_BOTTOM[i]);
    	}
    }
    
    public Roof(int x, int y) 
    {
    	super(x, y, "roof");
		health = HEALTH;
		particleCol = pCol;
    }
    
    
}