//Michal Jez, Shadman Hassan
//17/06/2016
//This class is the parent to all the blocks in the game
//It provides @Overrideable methods for drawing, removing health and getters and setters for characteristics of the block
//Such as if the block can be shot through, jumped through, etc...

import java.awt.*;
import java.awt.image.*;

public class Block extends Rectangle
{
	private Color colour;
	
	public final String type;			//The type of block, cannot change so it can be public
	protected String subType;				//The subtype of the block, can change so must be protected

	protected int health;				//The amount of health a block has before it breaks
	
	private boolean isAir;
	protected boolean isTrenchBlock;		//If the block is part of a trench
	protected boolean canJumpThrough;		//If the block can be jumped through
	protected boolean canShootThrough;		//If the block can be shot through
	protected boolean canFallThrough;		//If the block can be fallen through

	//protected BlockType blockType;

	protected Color particleCol;				//The colour that the particles of the destroyed block will be

	public static final int BLOCK_WIDTH = 50;	//Dimensions of each block
	public static final int BLOCK_HEIGHT = 50;

	protected Rectangle gameMapRect;			//The rectangle of the block in the GameMap image

	protected BufferedImage img;
	protected BufferedImage[] imgs;

    public Block(int x, int y, String t)
    {	//Constructor given x, y, block type
		//Base constructor
    	super(x, y, BLOCK_WIDTH, BLOCK_HEIGHT);
    	type = t;
    	canShootThrough = "air".equals(type) || "ladder".equals(type);
		canJumpThrough = "air".equals(type) || "ladder".equals(type) || "bridge".equals(type);
		canFallThrough = "air".equals(type) || "ladder".equals(type);
    	isAir = t.equals("air");
		gameMapRect = new Rectangle(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    }

	public Block(int x, int y, String t, Color c)
	{	//Constructor given x, y, block type, particle color
		this(x, y, t);
		particleCol = c;
	}

	public Block(int x, int y, String t, Color c, int h)
	{	//Constructor given x, y, block type, particle color, block health
		this(x, y, t, c);
		health = h;
	}

    public Block(int x, int y, String t, BufferedImage bi)
    {	//Constructor given x, y, block type, block image
    	this(x, y, t);
    	img = bi;
    }

	public Block(int x, int y, String t, BufferedImage bi, int h)
	{	//Constructor given x, y, block type, block image
		this(x, y, t, bi);
		health = h;
	}

	public Block(int x, int y, String t, BufferedImage bi, Color c, int h)
	{	//Constructor given x, y, block type, block image
		this(x, y, t, bi, h);
		particleCol = c;
	}

	public Block(int x, int y, String t, int h)
	{
		this(x, y, t);
		health = h;
	}

	public Block(int x, int y, String t, String s, BufferedImage bi)
	{	//Constructor given x, y, block type, block subtype, block image
		this(x, y, t, bi);
		subType = s;
	}

	public Block(int x, int y, String t, String s)
	{	//Constructor given x, y, block type,  block subtype
		this(x, y, t);
		subType = s;
	}

	public Block(int x, int y, int w, int h, String t, BufferedImage bi, Color c, int hlth)
	{	//For the spawner block which is 2 blocks wide
		super(x, y, w, h);
		type = t;
		img = bi;
		particleCol = c;
		health = hlth;
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

	public int getIntX()
	{
		return x;
	}

	public int getIntY()
	{
		return y;
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

	public Rectangle getGameMapRect() { return gameMapRect; }

	public boolean isTrenchBlock()
	{
		return isTrenchBlock;
	}
    
    public void draw(Graphics g)
    {
    	if (img != null)					//If there is an image, draw it
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    	else								//Otherwise draw the block with its default colour
    	{
    		g.setColor(colour);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, width * BLOCK_WIDTH, height * BLOCK_HEIGHT);
    	}
    }

	public void reduceHealth(int amnt, Direction d, GameMap gm)
	{	/*
			Occurs when a block is shot or an explosion goes off near it
		*/
		health -= amnt;
		if (health <= 0)
		{
			gm.destroyBlock(x, y, d);
		}
	}

	public boolean canShootThrough()
	{
		return canShootThrough;
	}

	public boolean canJumpThrough()
	{
		return canJumpThrough;
	}

	public boolean canFallThrough()
	{
		return canFallThrough;
	}
    
    public Color getParticleCol()
	{
		return particleCol;
	}
    
    public void initialize(Block[][] biome) {}
}