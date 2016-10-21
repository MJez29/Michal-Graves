//Michal Jez, Shadman Hassan
//04/05/2016
//This class extends Rectangle keeps track of the 4 corners of the object so that it is easier to do collision detection
//And allows for shorter code in methods

import java.awt.*;

public class FourCornerRect extends Rectangle
{
    protected int lx, rx;				//The x-coords of the vertical edges of the Rect
    protected int ty, by;				//The y-coords of the horizontal edges of the Rect

    public FourCornerRect(int nlx, int nly, int w, int h)
    {
        super(nlx, nly, w, h);

        lx = nlx;
        ty = nly;
        rx = lx + width - 1;
        by = ty + height - 1;
    }

    public FourCornerRect(int nlx, int nly, int s)
    {   //Called when the Rectangle is actually a Square
        super(nlx, nly, s, s);

        lx = nlx;
        ty = nly;
        rx = lx + s - 1;
        by = ty + s - 1;
    }

    public void moveX(int amnt)
    {   //Moves the x-coords
        lx += amnt;
        rx += amnt;
        x = lx;
    }

    public void moveY(int amnt)
    {   //Moves the y-coords
        ty += amnt;
        by += amnt;
        y = ty;
    }

    public void setBy(int nby)
    {   //Sets the y-coords
        by = nby;
        ty = by - height + 1;
        y = ty;
    }

    public void setLx(int nlx)
    {   //Sets the x-coords
        lx = nlx;
        rx = lx + width - 1;
        x = lx;
    }

    //Returns true if there is a block at that position
    public boolean checkTopLeftCorner(GameMap gm)
    {
        return gm.getAt(lx / GameMap.BLOCK_WIDTH, ty / GameMap.BLOCK_HEIGHT).canJumpThrough();
    }

    public boolean checkTopRightCorner(GameMap gm)
    {
        return gm.getAt(rx / GameMap.BLOCK_WIDTH, ty / GameMap.BLOCK_HEIGHT).canJumpThrough();
    }

    public boolean checkBottomLeftCorner(GameMap gm)
    {   //Checks the bottom left corner
        return gm.getAt((lx) / GameMap.BLOCK_WIDTH, by / GameMap.BLOCK_HEIGHT).canFallThrough();
    }

    public boolean checkBottomRightCorner(GameMap gm)
    {   //Checks the bottom right corner
        return gm.getAt((rx) / GameMap.BLOCK_WIDTH, by / GameMap.BLOCK_HEIGHT).canFallThrough();
    }

    public boolean checkBelowBottomLeftCorner(GameMap gm)
    {   //Checks 1 unit below the bottom left corner
        return gm.getAt((lx + 1) / GameMap.BLOCK_WIDTH, (by + 1) / GameMap.BLOCK_HEIGHT).canFallThrough();
    }

    public boolean checkBelowBottomRightCorner(GameMap gm)
    {   //Checks 1 unit below the bottom right corner
        return gm.getAt((rx - 1) / GameMap.BLOCK_WIDTH, (by + 1) / GameMap.BLOCK_HEIGHT).canFallThrough();
    }

    public boolean checkBelowBottomMiddle(GameMap gm)
    {   //Checks the bottom middle point
        return gm.getAt((lx+width/2) / GameMap.BLOCK_WIDTH, (by + 1) / GameMap.BLOCK_HEIGHT).canFallThrough();
    }

    public boolean checkAboveTopMiddle(GameMap gm)
    {   //Checks the bottom middle point
        return gm.getAt((lx+width/2) / GameMap.BLOCK_WIDTH, (ty - 1) / GameMap.BLOCK_HEIGHT).canJumpThrough();
    }
}