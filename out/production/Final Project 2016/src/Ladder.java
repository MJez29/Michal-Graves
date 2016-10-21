/**
 * @(#)Ladder.java
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

public class Ladder extends Block
{
	public static final BufferedImage img = initializeImage("Ladder Images\\Ladder.png");

	private static BufferedImage initializeImage(String str)
	{
		try
		{
			return ImageIO.read(new File(str));
		}
		catch (IOException e) { System.out.println(e); return null; }
	}
	
    public Ladder(int x, int y) 
    {
    	super(x,y,"ladder");
    }
    
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