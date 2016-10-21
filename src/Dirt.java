//Michal Jez, Shadman Hassan
//17/06/2016
//Dirt is a derived class from Block
//Dirt has medium health

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class Dirt extends Block
{
	//All the images of the dirt
	public static final BufferedImage DIRT = ImageTools.initializeImage("Dirt Images\\Dirt.png");
	
	public static final BufferedImage GRASS_TOP = ImageTools.initializeImage("Dirt Images\\Grass Top.png");
	public static final BufferedImage GRASS_LEFT = ImageTools.initializeImage("Dirt Images\\Grass Left.png");
	public static final BufferedImage GRASS_RIGHT = ImageTools.flipHorizontally(GRASS_LEFT);
	public static final BufferedImage ROCK_BOTTOM = ImageTools.initializeImage("Dirt Images\\Rock Bottom.png");
		
	public static final BufferedImage GRASS_TOP__GRASS_LEFT = ImageTools.initializeImage("Dirt Images\\Grass Top + Grass Left.png");
	public static final BufferedImage GRASS_TOP__GRASS_RIGHT = ImageTools.flipHorizontally(ImageTools.initializeImage("Dirt Images\\Grass Top + Grass Left.png"));
	public static final BufferedImage GRASS_TOP__GRASS_LEFT__GRASS_RIGHT = ImageTools.initializeImage("Dirt Images\\Grass Top + Grass Left + Grass Right.png");
	public static final BufferedImage GRASS_TOP__GRASS_LEFT__ROCK_BOTTOM = ImageTools.initializeImage("Dirt Images\\Grass Top + Grass Left + Rock Bottom.png");
	public static final BufferedImage GRASS_TOP__GRASS_RIGHT__ROCK_BOTTOM = ImageTools.flipHorizontally(GRASS_TOP__GRASS_LEFT__ROCK_BOTTOM);
	public static final BufferedImage GRASS_TOP__ROCK_BOTTOM = ImageTools.initializeImage("Dirt Images\\Grass Top + Rock Bottom.png");
	public static final BufferedImage GRASS_TOP__GRASS_LEFT__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Top + Grass Left + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_TOP__GRASS_RIGHT__ROCK_LEFT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_TOP__GRASS_LEFT__ROCK_RIGHT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_TOP__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Top + Rock Left Bottom Corner + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_TOP__ROCK_LEFT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Top + Rock Left Bottom Corner.png");
	public static final BufferedImage GRASS_TOP__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_TOP__ROCK_LEFT_BOTTOM_CORNER);
	
	public static final BufferedImage ROCK_LEFT__ROCK_RIGHT__ROCK_BOTTOM = ImageTools.initializeImage("Dirt Images\\Rock Left + Rock Right + Rock Bottom.png");
	public static final BufferedImage GRASS_LEFT__GRASS_RIGHT = ImageTools.initializeImage("Dirt Images\\Grass Left + Grass Right.png");
	public static final BufferedImage ROCK_LEFT__ROCK_BOTTOM__GRASS_RIGHT_TOP_CORNER = ImageTools.initializeImage("Dirt Images\\Rock Left + Rock Bottom + Grass Right Top Corner.png");
	public static final BufferedImage ROCK_RIGHT__ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER = ImageTools.flipHorizontally(ROCK_LEFT__ROCK_BOTTOM__GRASS_RIGHT_TOP_CORNER);
	public static final BufferedImage ROCK_LEFT__ROCK_BOTTOM = ImageTools.initializeImage("Dirt Images\\Rock Left + Rock Bottom.png");
	public static final BufferedImage ROCK_RIGHT__ROCK_BOTTOM = ImageTools.flipHorizontally(ROCK_LEFT__ROCK_BOTTOM);
	public static final BufferedImage GRASS_LEFT__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_RIGHT__ROCK_LEFT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_LEFT__ROCK_RIGHT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_LEFT__GRASS_RIGHT_TOP_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left + Grass Right Top Corner.png");
	public static final BufferedImage GRASS_RIGHT__GRASS_LEFT_TOP_CORNER = ImageTools.flipHorizontally(GRASS_LEFT__GRASS_RIGHT_TOP_CORNER);
	public static final BufferedImage GRASS_LEFT__GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left + Grass Right Top Corner + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_RIGHT__GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_LEFT__GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_LEFT__ROCK_BOTTOM = ImageTools.initializeImage("Dirt Images\\Grass Left + Rock Bottom.png");
	public static final BufferedImage GRASS_RIGHT__ROCK_BOTTOM = ImageTools.flipHorizontally(GRASS_LEFT__ROCK_BOTTOM);

	public static final BufferedImage ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER = ImageTools.initializeImage("Dirt Images\\Rock Bottom + Grass Left Top Corner.png");
	public static final BufferedImage ROCK_BOTTOM__GRASS_RIGHT_TOP_CORNER = ImageTools.flipHorizontally(ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER);
	public static final BufferedImage ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER = ImageTools.initializeImage("Dirt Images\\Rock Bottom + Grass Left Top Corner + Grass Right Top Corner.png");
	
	public static final BufferedImage GRASS_LEFT_TOP_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner.png");
	public static final BufferedImage GRASS_RIGHT_TOP_CORNER = ImageTools.flipHorizontally(GRASS_LEFT_TOP_CORNER);
	public static final BufferedImage ROCK_LEFT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Rock Left Bottom Corner.png");
	public static final BufferedImage ROCK_RIGHT_BOTTOM_CORNER = ImageTools.flipHorizontally(ROCK_LEFT_BOTTOM_CORNER);
	
	public static final BufferedImage ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Rock Left Bottom Corner + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_LEFT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner + Rock Left Bottom Corner.png");
	public static final BufferedImage GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner + Grass Right Top Corner.png");
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner + Rock Left Bottom Corner + Rock Right Bottom Corner.png");
	public static final BufferedImage GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner + Grass Right Top Corner + Rock Left Bottom Corner.png");
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.flipHorizontally(GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER);
	public static final BufferedImage GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER = ImageTools.initializeImage("Dirt Images\\Grass Left Top Corner + Grass Right Top Corner + Rock Left Bottom Corner + Rock Right Bottom Corner.png");

	public static final int STARTING_HEALTH = 50;					//Block health

	protected static final Color pCol = new Color(97, 65, 2);

	public Dirt(int x, int y)
    {
    	super(x,y,"dirt", pCol, STARTING_HEALTH);
    }
    
    public void draw(Graphics g)
    {	//Draws the Dirt
    	if (img != null)
    	{
    		g.drawImage(img, x * BLOCK_WIDTH, y * BLOCK_HEIGHT, null);
    	}
    	else
    	{
    		g.setColor(Color.red);
    		g.fillRect(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    	}
    }
    
    public void initialize(Block[][] biome)
    {	//Initializes the image of the dirt based on the blocks surrounding it
    	if (img != null) return;
    	
    	int w = biome.length - 1;
    	int h = biome[0].length - 1;
    	//Block above is not dirt
    	if (y != 0 && !biome[x][y - 1].getType().equals("dirt"))
    	{
    		//Block to the left is not dirt
    		if (x != 0 && !biome[x - 1][y].getType().equals("dirt"))
    		{
    			//Block to the right is not dirt
    			if (x != w && !biome[x + 1][y].getType().equals("dirt"))
    			{
    				 img = GRASS_TOP__GRASS_LEFT__GRASS_RIGHT;
    			}
    			else
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					img = GRASS_TOP__GRASS_LEFT__ROCK_BOTTOM;
    				}
    				else
    				{
    					//If block to the bottom right isn't dirt
    					if (y != h && x != w && !biome[x + 1][y + 1].getType().equals("dirt"))
    					{
    						img = GRASS_TOP__GRASS_LEFT__ROCK_RIGHT_BOTTOM_CORNER; 
    					}
    					else
    					{
    						img = GRASS_TOP__GRASS_LEFT;
    					}
    				}
    			}
    		}
    		//Block to the left is dirt
    		else
    		{
    			//Block to the right is not dirt
    			if (x != w && !biome[x + 1][y].getType().equals("dirt"))
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					img = GRASS_TOP__GRASS_RIGHT__ROCK_BOTTOM;
    				}
    				else
    				{
    					//Block below to the left is not dirt
    					if (x != 0 && y != h && !biome[x - 1][y + 1].getType().equals("dirt"))
    					{
    						img = GRASS_TOP__GRASS_RIGHT__ROCK_LEFT_BOTTOM_CORNER ;
    					}
    					//Block below to the left is dirt
    					else
    					{
    						img = GRASS_TOP__GRASS_RIGHT;
    					}
    				}
    			}
    			//Block to the right is dirt
    			else
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					img = GRASS_TOP__ROCK_BOTTOM;
    				}
    				//Block below is dirt
    				else
    				{
    					//Blocks to the bottom right and left are dirt
    					if (y == h ||(x != 0 && x != w && y != 0 && biome[x - 1][y + 1].getType().equals("dirt") && biome[x + 1][y + 1].getType().equals("dirt")))
    					{
    						img = GRASS_TOP;
    					}
    					//Blocks to the bottom left and right are not dirt
    					else if (x != 0 && x != w && y != 0 && y != h && !biome[x - 1][y + 1].getType().equals("dirt") && !biome[x + 1][y + 1].getType().equals("dirt"))
    					{
    						img = GRASS_TOP__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    					}
    					
    					//Block to the bottom left is dirt while the one to the bottom right isn't
    					else if (x != 0 && y != h && biome[x - 1][y + 1].getType().equals("dirt"))
    					{
    						img = GRASS_TOP__ROCK_RIGHT_BOTTOM_CORNER;
    					}
    					else
    					{
    						img = GRASS_TOP__ROCK_LEFT_BOTTOM_CORNER;
    					}
    				}
    			}
    		}
    	}
    	//Block above is dirt
    	else 
    	{
    		//If block to the left is not dirt
    		if (x != 0 && !biome[x - 1][y].getType().equals("dirt"))
    		{
    			//If the block to the right is not dirt
    			if (x != w && !biome[x + 1][y].getType().equals("dirt"))
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					img = ROCK_LEFT__ROCK_RIGHT__ROCK_BOTTOM;
    				}
    				//Block below is dirt
    				else
    				{
    					img = GRASS_LEFT__GRASS_RIGHT;
    				}
    			}
    			//Block to the right is dirt
    			else
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					//If the block above to the right is not dirt
    					if (x != w && y != 0 && !biome[x + 1][y - 1].getType().equals("dirt"))
    					{
    						img = ROCK_LEFT__ROCK_BOTTOM__GRASS_RIGHT_TOP_CORNER;
    					}
    					else
    					{
    						img = ROCK_LEFT__ROCK_BOTTOM;
    					}
    				}
    				//Block below is dirt
    				else
    				{
    					if (x != 0 && x != w && y != 0 && y != h)
    					{
    						//True if the block contains dirt
    						boolean tr = biome[x + 1][y - 1].getType().equals("dirt");
    						boolean br = biome[x + 1][y + 1].getType().equals("dirt");
    						//Blocks above and below to the right are dirt
    						if (tr && br)
    						{
    							img = GRASS_LEFT;
    						}
    						//Block above to the right is dirt while block below to the right is not
    						else if (tr && !br)
    						{
    							img = GRASS_LEFT__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Block below to the right is dirt while the block above to the right is not
    						else if (!tr && br)
    						{
    							img = GRASS_LEFT__GRASS_RIGHT_TOP_CORNER;
    						}
    						//Blocks above and below to the right are not dirt
    						else
    						{
    							img = GRASS_LEFT__GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    					}
    					else
    					{
    						img = DIRT;
    					}
    				}
    			}
    		}
    		//If the block to the left is dirt
    		else
    		{
    			//Block to the right is not dirt
    			if (x != w && !biome[x + 1][y].getType().equals("dirt"))
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					//Block to the top left is not dirt
    					if (x != 0 && y != 0 && !biome[x - 1][y - 1].getType().equals("dirt"))
    					{
    						img = ROCK_RIGHT__ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER;
    					}
    					//Block to the top left is dirt
    					else
    					{
    						img = ROCK_RIGHT__ROCK_BOTTOM;
    					}
    				}
    				//Block below is dirt
    				else
    				{
    					if (x != 0 && x != w && y != 0 && y != h)
    					{
    						//True if the block is dirt
    						boolean tl = biome[x - 1][y - 1].getType().equals("dirt");
    						boolean bl = biome[x - 1][y + 1].getType().equals("dirt");
    						//Blocks to the top and bottom left are dirt
    						if (tl && bl)
    						{
    							img = GRASS_RIGHT;
    						}
    						//Block to the top left is dirt but the block to the bottom left is not
    						else if (tl && !bl)
    						{
    							img = GRASS_RIGHT__ROCK_LEFT_BOTTOM_CORNER;
    						}
    						//Block to the top left is not dirt while the one to the bottom left is
    						else if (!tl && bl)
    						{
    							img = GRASS_RIGHT__GRASS_LEFT_TOP_CORNER;
    						}
    						//Blocks above and below to the left are not dirt
    						else
    						{
    							img = GRASS_RIGHT__GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER;
    						}
    					}
    					else
    					{
    						img = DIRT;
    					}
    				}
    			}
    			//Block to the right is dirt
    			else
    			{
    				//Block below is not dirt
    				if (y != h && !biome[x][y + 1].getType().equals("dirt"))
    				{
    					if (x != 0 && x != w && y != 0)
    					{
    						//True if the block is dirt
    						boolean tl = biome[x - 1][y - 1].getType().equals("dirt");
    						boolean tr = biome[x + 1][y - 1].getType().equals("dirt");
    						//Blocks to the top left and right are dirt
    						if (tl && tr)
    						{
    							img = ROCK_BOTTOM;
    						}
    						//Block to the top left is dirt while the one to the top right is not
    						else if (tl && !tr)
    						{
    							img = ROCK_BOTTOM__GRASS_RIGHT_TOP_CORNER;
    						}
    						//Block to the top right is dirt while the one to the top left is not
    						else if (!tl && tr)
    						{
    							img = ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER;
    						}
    						//Blocks to the top right and left are not dirt
    						else
    						{
    							img = ROCK_BOTTOM__GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER;
    						}
    					}
    					else
    					{
    						img = DIRT;
    					}
    				}
    				//Block below is dirt
    				else
    				{
    					if (x != 0 && x != w && y != 0 && y != h)
    					{
    						//True if the block is dirt
    						boolean tl = biome[x - 1][y - 1].getType().equals("dirt");
    						boolean tr = biome[x + 1][y - 1].getType().equals("dirt");
    						boolean br = biome[x + 1][y + 1].getType().equals("dirt");
    						boolean bl = biome[x - 1][y + 1].getType().equals("dirt");
    						//---All 4 corners are dirt---
    						if (tl && tr && br && bl)
    						{
    							img = DIRT;
    						}
    						//---3 Corners are dirt---
    						//Top left is not dirt
    						else if (!tl && tr && br && bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER;
    						}
    						//Top right is not dirt
    						else if (tl && !tr && br && bl)
    						{
    							img = GRASS_RIGHT_TOP_CORNER;
    						}
    						//Bottom right is not dirt
    						else if (tl && tr && !br && bl)
    						{
    							img = ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Bottom left is not dirt
    						else if (tl && tr && br && !bl)
    						{
    							img = ROCK_LEFT_BOTTOM_CORNER;
    						}
    						//---2 Corners are dirt---
    						//Top left and top right are dirt
    						else if (tl && tr && !br && !bl)
    						{
    							img = ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Top left corner and bottom right corner are dirt
    						else if (tl && !tr && br && !bl)
    						{
    							img = GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER;
    						}
    						//Top left and bottom left are dirt
    						else if (tl && !tr && !br && bl)
    						{
    							img = GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Top right and bottom right are dirt
    						else if (!tl && tr && br && !bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER;
    						}
    						//Top right and bottom left are dirt
    						else if (!tl && tr && !br && bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Bottom left and right are dirt
    						else if (!tl && !tr && br && bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER;
    						}
    						
    						//---1 Corner is dirt---
    						//Top left is dirt
    						else if (tl && !tr && !br && !bl)
    						{
    							img = GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Top right is dirt
    						else if (!tl && tr && !br && !bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						//Right bottom is dirt
    						else if (!tl && !tr && br && !bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER;
    						}
    						//Left bottom is dirt
    						else if (!tl && !tr && !br && bl)
    						{
    							img = GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    						
    						//---4 Corners are dirt---
    						else
    						{
    							img = GRASS_LEFT_TOP_CORNER__GRASS_RIGHT_TOP_CORNER__ROCK_LEFT_BOTTOM_CORNER__ROCK_RIGHT_BOTTOM_CORNER;
    						}
    					}
    					else
    					{
    						img = DIRT;
    					}
    				}
    			}
    		}
    	}
    }
}