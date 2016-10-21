/**
 * @(#)Explosive.java
 *
 *
 * @author 
 * @version 1.00 2016/3/27
 */

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Explosive extends Block
{
	public static final BufferedImage EXPLOSIVE = ImageTools.initializeImage("Explosives Images\\Explosive.png");
	
    public Explosive(int nx, int ny) 
    {
    	super(nx, ny, "explosive");
    }
    
    public void draw(Graphics g)
    {
    	g.drawImage(EXPLOSIVE, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    }
}