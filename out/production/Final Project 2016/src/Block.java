/**
 * @(#)Block.java
 *
 *
 * @author 
 * @version 1.00 2016/3/11
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.io.*;
import java.util.Scanner;
import java.awt.image.*;
import javax.imageio.*;

public class Block extends Rectangle
{
	private Color colour;
	
	public final String type;			//The type of block, cannot change so it can be public
	protected String subType;				//The subtype of the block, can change so must be protected
	
	private boolean isAir;
	
	public static final int BLOCK_WIDTH = 50;
	public static final int BLOCK_HEIGHT = 50;
	
	protected BufferedImage img;
	protected BufferedImage[] imgs;

    public Block(int x, int y, String t) 
    {
    	super(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
    	type = t;
    	if (type.equals("air")) colour = Color.blue;
    	else if (type.equals("ladder")) colour = Color.gray;
    	else if (type.equals("bridge")) colour = Color.green;
    	isAir = t.equals("air");
    }
    
    public Block(int x, int y, String t, BufferedImage bi)
    {
    	super(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
    	type = t;
    	isAir = t.equals("air");
    	img = bi;
    }
    
    public Color getColour()
    {
    	return colour;
    }
    
    public String getType()
    {
    	return type;
    }
    
    public boolean isAir()
    {
    	return isAir;
    }
    
    public String getSubType()
    {
    	return subType;
    }
    
    public void setSubType(String str)
    {
    	subType = str;
    }
    
    public void setImage(BufferedImage bi)
    {
    	img = bi;
    }
    
    public void setImages(BufferedImage[] bi)
    {
    	imgs = bi;
    }
    
    public void draw(Graphics g)
    {
    	if (img != null)
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    	else
    	{
    		g.setColor(colour);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, width * BLOCK_WIDTH, height * BLOCK_HEIGHT);
    	}
    }
    
    
    
    public void initialize(Block[][] biome)
    {
    	
    }
    
    
}