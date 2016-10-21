/**
 * @(#)Trench.java
 *
 *
 * @author 
 * @version 1.00 2016/3/28
 */

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Trench extends Dirt
{
	//Left wall means that the block is on the left half of the trench or spawner
	//Bottom wall means that the block is part of the floor of the trench
	public static final BufferedImage LEFT_WALL = ImageTools.initializeImage("Trench Images\\Trench Side.png");
	public static final BufferedImage RIGHT_WALL = ImageTools.flipHorizontally(LEFT_WALL);
	
	public static final BufferedImage TOP_WALL = ImageTools.initializeImage("Trench Images\\Trench Top Side.png");
	public static final BufferedImage BOTTOM_WALL = ImageTools.flipVertically(TOP_WALL);
	
	public static final BufferedImage BOTTOM_LEFT_CORNER = ImageTools.initializeImage("Trench Images\\Trench Side + 2 Corner Trench.png");
	public static final BufferedImage BOTTOM_RIGHT_CORNER = ImageTools.flipHorizontally(BOTTOM_LEFT_CORNER);
	public static final BufferedImage TOP_RIGHT_CORNER = ImageTools.flipVertically(BOTTOM_RIGHT_CORNER);
	
	public static final BufferedImage SPAWNER_LEFT_SIDE = ImageTools.initializeImage("Trench Images\\Spawner Left Side.png");
	public static final BufferedImage SPAWNER_RIGHT_SIDE = ImageTools.flipHorizontally(SPAWNER_LEFT_SIDE);
	
	public static final BufferedImage TOP_LEFT_CORNER = ImageTools.initializeImage("Trench Images\\Trench Top Left Corner.png");
	
	public static final BufferedImage TOP_CENTER_CORNER = ImageTools.initializeImage("Trench Images\\Trench Top Center Corner.png");
	
    public Trench(int nx, int ny) 
    {
    	super(nx, ny);
    }
    
    public Trench(int nx, int ny, BufferedImage bi) 
    {
    	super(nx, ny);
    	img = bi;
    }
}