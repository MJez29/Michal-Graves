//Michal Jez
//17/06/2016
//Unused class

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class FlashingPlatform extends Platform
{
	private int frame, maxFrame, n;					//The current frame, the max frame it can go to, what to add to frame each loop

    public FlashingPlatform(int x, int y)
    {
    	super(x, y);
    	frame = 0;
    }

	public FlashingPlatform(int x, int y, BufferedImage[] bis)
	{
		this(x, y);
		imgs = bis;
		maxFrame = imgs.length - 1;
		n = 1;
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
    	frame += n;
		if (frame > maxFrame) n *= -1;
    }
}