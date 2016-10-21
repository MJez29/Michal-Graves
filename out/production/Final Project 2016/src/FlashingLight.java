/**
 * @(#)FlashingLight.java
 *
 *
 * @author 
 * @version 1.00 2016/4/10
 */

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class FlashingLight extends Air
{
	public static final BufferedImage[] CHECKPOINT_LEFT_RED = ImageTools.initializeImages("Checkpoint Images\\Red\\Light Left ", 3);
	public static final BufferedImage[] CHECKPOINT_RIGHT_RED = ImageTools.flipHorizontally(CHECKPOINT_LEFT_RED);
	public static final BufferedImage[] CHECKPOINT_TOP_RED = ImageTools.initializeImages("Checkpoint Images\\Red\\Light Top ", 3);
	
	public static final BufferedImage[] CHECKPOINT_LEFT_GREEN = ImageTools.initializeImages("Checkpoint Images\\Green\\Light Left ", 3);
	public static final BufferedImage[] CHECKPOINT_RIGHT_GREEN = ImageTools.flipHorizontally(CHECKPOINT_LEFT_GREEN);
	public static final BufferedImage[] CHECKPOINT_TOP_GREEN = ImageTools.initializeImages("Checkpoint Images\\Green\\Light Top ", 3);
	
	private int frame;

    public FlashingLight(int x, int y) 
    {
    	super(x, y, "flashing light");
    	frame = 0;
    }
    
    @Override
    public void draw(Graphics g)
    {
    	if (imgs[frame] == null)
    	{
    		g.setColor(Color.blue);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    	}
    	else
    	{
    		g.drawImage(imgs[frame], x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    	frame++;
    }
    
}