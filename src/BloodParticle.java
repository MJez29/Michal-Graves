//Michal Jez, Shadman Hassan
//15/06/2016
//The class of blood particles
//Slightly different than regular particles because they stain blocks once they collide with a block other than air

import java.awt.*;

public class BloodParticle extends Particle
{
    public static final Color[] cols = {new Color(74, 4, 4), new Color(138, 7, 7), new Color(164, 8, 2),
            new Color(255, 0, 0), new Color(139, 0, 0), new Color(178, 34, 34)};

    public BloodParticle(int sx, int sy, int rx, int ry, Direction dir)
    {
        super(sx,sy,rx,ry,dir, cols[(int) (Math.random() * cols.length)]);
    }

    @Override
    public void draw(SubMap sm)
    {	//Draws the particle onto the current SubMap
        Graphics g = sm.getGraphics();
        g.setColor(col);
        g.fillOval(lx - sm.getIntX(), ty - sm.getIntY(), width, height);
    }

    @Override
    public boolean checkCollisions(GameMap gm)
    {	//Checks to see if the particle collides with a Block in the GameMap,
        //Returns true if there is a collision
        int tempLX = lx / GameMap.BLOCK_WIDTH;
        int tempRX = rx / GameMap.BLOCK_WIDTH;				//Saves 1 operation apiece
        int tempTY = ty / GameMap.BLOCK_HEIGHT;				//But wasted 5 minutes of my time
        int tempBY = by / GameMap.BLOCK_HEIGHT;

        boolean toReturn =  (tempLX < 0 || tempRX > GameMap.BIOME_WIDTH - 1 ||          //If the particle is out of bounds
                tempTY < 0 || tempBY > GameMap.BIOME_HEIGHT - 1);
        if (toReturn) return toReturn;              //If the particle is out of bounds, return true

        boolean lxty = !"air".equals(gm.getAt(tempLX, tempTY).getType());
        boolean lxby = !"air".equals(gm.getAt(tempLX, tempBY).getType());		        //If any of the 4 corners collides with a block
        boolean rxty = !"air".equals(gm.getAt(tempRX, tempTY).getType());             //In the GameMap
        boolean rxby = !"air".equals(gm.getAt(tempRX, tempBY).getType());

        if (lxty || lxby || rxty || rxby)               //If the particle is to be deleted
        {
            stainMap(gm, lxty, lxby, rxty, rxby);      //Stains the map at that location
        }

        return lxty || lxby || rxty || rxby;
    }

    private void stainMap(GameMap gm, boolean lxty, boolean lxby, boolean rxty, boolean rxby)
    {   //Stains the map with blood
        Graphics g = gm.getGraphics();
        g.setColor(col);
        if (lxty)           //If the left corner collided with ground
        {                   //Stains that block
            g.setClip(lx - lx % GameMap.BLOCK_WIDTH, ty - ty % GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT);
            g.fillOval(lx, ty, width, height);
        }
        else if (lxby)         //If the bottom left corner collided with something
        {
            g.setClip(lx - lx % GameMap.BLOCK_WIDTH, by - by % GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT);
            g.fillOval(lx, by, width, height);
        }
        else if (rxty)          //If the top right corner collided with something
        {
            g.setClip(rx - rx % GameMap.BLOCK_WIDTH, ty - ty % GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT);
            g.fillOval(rx, ty, width, height);
        }
        else if (rxby)          //If the bottom right corner collided with something
        {
            g.setClip(rx - rx % GameMap.BLOCK_WIDTH, by - by % GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT);
            g.fillOval(rx, by, width, height);
        }
    }
}