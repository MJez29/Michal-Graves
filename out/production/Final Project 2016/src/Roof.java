/**
 * @(#)Roof.java
 *
 *
 * @author 
 * @version 1.00 2016/4/3
 */

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
    }
    
    
}