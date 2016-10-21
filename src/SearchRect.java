//Michal Jez
//15/05/2016
//This class contains a rectangle that that an enemy commander can currently see
//It also finds what the commander sees the next frame

import java.awt.*;
import java.util.*;

public class SearchRect extends Rectangle
{
    protected Direction dir;                    //The direction the search rectangle increases

    protected int blocksAway;            //The number of horizontal blocks away this is from increasing in height
    protected int blocksDist;           //The total number of blocks in between each increase in height

    protected int commanderY;

    protected Direction increaseIn;             //The direction to increase the search in

    public SearchRect(int nx, int ny, int nw, int nh, int c, int ba, int bd, Direction d, Direction inc)
    {   //(nx, ny) - The coordinates of the top left corner
        //(nw, nh) - The dimensions of the SearchRect
        //c - The Y-coord of the Person performing the search
        //ba - The number of blocks away from the next increase in height
        //bd - The width of the interval before every increase in height
        //d - The Direction the SearchRect is searching (either left or right)
        //inc - The Direction the SearchRect increases its searching height
        super(nx, ny, nw, nh);
        blocksAway = ba;
        blocksDist = bd;
        dir = d;
        increaseIn = inc;
        commanderY = c;
    }

    public LinkedList<SearchRect> searchNext(GameMap gm)
    {   //Searches the next column of blocks that the Person can see derived from what they can currently see
        LinkedList<SearchRect> toReturn = new LinkedList<SearchRect> ();
        switch (dir)
        {
            case LEFT:          //If searching to the left
            {
                x -= 2 * GameMap.BLOCK_WIDTH;           //To try to avoid copy and pasting
            }
            case RIGHT:         //If searching to the right
            {
                x += GameMap.BLOCK_WIDTH;
                if (blocksAway-- <= 0)             //If the height of the search is to increase
                {
                    switch (increaseIn)
                    {
                        case UP:            //If the search increases upwards
                        {
                            int topY = y / GameMap.BLOCK_HEIGHT - 1;                    //The highest position that he can see (moves up 1 block)
                            int bottomY = (y + height) / GameMap.BLOCK_HEIGHT - 1;      //The lowest position that he can see (moves up 1 block)
                            int curX = x / GameMap.BLOCK_WIDTH;                         //Current x-val
                            SearchRect curRect = null;                                  //The current rect to search
                            boolean hitSolid = false;
                            for (int iy = topY; iy < bottomY; iy++)                    //Goes through every y-val
                            {
                                try {
                                    if (gm.getAt(curX, iy).canShootThrough())               //If he can see through the current spot
                                    {
                                        if (curRect == null) {          //If this is the top block; Makes a new SearchRect
                                            curRect = new SearchRect(curX * GameMap.BLOCK_WIDTH, iy * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, commanderY, blocksDist, blocksDist, dir, Direction.UP);
                                        } else {                        //Otherwise ncreases the rect height
                                            curRect.height += GameMap.BLOCK_HEIGHT;
                                        }
                                    } else {            //If he cant see through the current block
                                        if (curRect != null) {          //If the block above was see-through
                                            toReturn.addFirst(curRect); //Adds it to the List of SearchRects that will be returned
                                            curRect = null;
                                        }
                                        hitSolid = true;
                                    }
                                } catch (Exception e) { continue; }
                            }
                            if (curRect != null) {          //If the last block was see-through
                                toReturn.addFirst(curRect);
                            }
                            break;
                        }
                        case DOWN:
                        {
                            int topY = y / GameMap.BLOCK_HEIGHT + 1;                    //The highest position that he can see
                            int bottomY = (y + height) / GameMap.BLOCK_HEIGHT + 1;      //The lowest position that he can see
                            int curX = x / GameMap.BLOCK_WIDTH;                         //Current x-val
                            SearchRect curRect = null;                                  //The current rect to search
                            boolean hitSolid = false;
                            for (int iy = topY; iy < bottomY; iy++)                    //Goes through every y-val
                            {
                                try {
                                    if (gm.getAt(curX, iy).canShootThrough())               //If he can see through the current spot
                                    {
                                        if (curRect == null) {          //If this is the top block
                                            curRect = new SearchRect(x, iy * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, commanderY, blocksDist, blocksDist, dir, Direction.DOWN);
                                        } else {                        //Increases the rect height
                                            curRect.height += GameMap.BLOCK_HEIGHT;
                                        }
                                    } else {            //If he cant see through the current block
                                        if (curRect != null) {          //If the block above was see-through
                                            toReturn.addFirst(curRect);
                                            curRect = null;
                                        }
                                        hitSolid = true;
                                    }
                                } catch (Exception e) { continue; }          //Error occurs if they are scanning outside of bounds
                            }
                            if (curRect != null) {          //If the block above was see-through
                                toReturn.addFirst(curRect);
                            }
                            break;
                        }
                        case UPANDDOWN:
                        {
                            int topY = y / GameMap.BLOCK_HEIGHT - 1;                    //The highest position that he can see
                            int bottomY = (y + height) / GameMap.BLOCK_HEIGHT + 1;      //The lowest position that he can see
                            int curX = x / GameMap.BLOCK_WIDTH;                         //Current x-val
                            boolean hitSolid = false;
                            SearchRect curRect = null;                                  //The current rect to search
                            for (int iy = topY; iy < bottomY; iy++)                    //Goes through every y-val
                            {
                                try {
                                    if (gm.getAt(curX, iy).canShootThrough())               //If he can see through the current spot
                                    {
                                        if (curRect == null) {          //If this is the top block
                                            Direction d;
                                            if (iy < commanderY) d = Direction.UP;
                                            else if (iy > commanderY) d = Direction.DOWN;
                                            else d = Direction.UPANDDOWN;
                                            curRect = new SearchRect(x, iy * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, commanderY, blocksDist, blocksDist, dir, d);
                                        } else {                        //Increases the rect height
                                            curRect.height += GameMap.BLOCK_HEIGHT;
                                        }
                                    } else {            //If he cant see through the current block
                                        if (curRect != null) {          //If the block above was see-through
                                            toReturn.addFirst(curRect);
                                            curRect = null;
                                        }
                                        hitSolid = true;
                                    }
                                }  catch (Exception e) { continue; }          //Error occurs if they are scanning outside of bounds
                            }
                            if (curRect != null) {          //If the block above was see-through
                                if (!hitSolid) curRect.increaseIn = Direction.UPANDDOWN;
                                toReturn.addFirst(curRect);
                            }
                            break;
                        }
                        case NULL:
                        {
                            int topY = y / GameMap.BLOCK_HEIGHT;                    //The highest position that he can see
                            int bottomY = (y + height) / GameMap.BLOCK_HEIGHT;      //The lowest position that he can see
                            int curX = x / GameMap.BLOCK_WIDTH;                         //Current x-val
                            SearchRect curRect = null;                                  //The current rect to search
                            for (int iy = topY; iy < bottomY; iy++)                    //Goes through every y-val
                            {
                                try {
                                    if (gm.getAt(curX, iy).canShootThrough())               //If he can see through the current spot
                                    {
                                        if (curRect == null) {          //If this is the top block
                                            curRect = new SearchRect(x, iy * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, commanderY, Integer.MAX_VALUE, Integer.MAX_VALUE, dir, Direction.NULL);
                                        } else {                        //Increases the rect height
                                            curRect.height += GameMap.BLOCK_HEIGHT;
                                        }
                                    } else {            //If he cant see through the current block
                                        if (curRect != null) {          //If the block above was see-through
                                            toReturn.addFirst(curRect);
                                            curRect = null;
                                        }
                                    }
                                }  catch (NullPointerException e) { continue; }          //Error occurs if they are scanning outside of bounds
                            }
                            if (curRect != null) {          //If the block above was see-through
                                toReturn.addFirst(curRect);
                            }
                            break;
                        }
                    }
                }
                else {
                    int topY = y / GameMap.BLOCK_HEIGHT;                    //The highest position that he can see
                    int bottomY = (y + height) / GameMap.BLOCK_HEIGHT;      //The lowest position that he can see
                    int curX = x / GameMap.BLOCK_WIDTH;                         //Current x-val
                    boolean hitSolid = false;
                    SearchRect curRect = null;                                  //The current rect to search
                    for (int iy = topY; iy < bottomY; iy++)                    //Goes through every y-val
                    {
                        try {
                            if (gm.getAt(curX, iy).canShootThrough())               //If he can see through the current spot
                            {
                                if (curRect == null) {          //If this is the top block
                                    curRect = new SearchRect(x, iy * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, commanderY, blocksAway, blocksDist, dir, increaseIn);
                                } else if (curRect.dir != Direction.NULL) {                        //Increases the rect height
                                    curRect.height += GameMap.BLOCK_HEIGHT;
                                }
                            } else {            //If he cant see through the current block
                                if (curRect != null) {          //If the block above was see-through
                                    //if (hitSolid && iy - 1 > commanderY && curRect.height > (iy - 1 - commanderY) * GameMap.BLOCK_HEIGHT) curRect.increaseIn = Direction.NULL;
                                    toReturn.addFirst(curRect);
                                    curRect = null;
                                }
                                hitSolid = true;
                            }
                        } catch (Exception e) { continue; }          //Error occurs if they are scanning outside of bounds
                    }
                    if (curRect != null) {          //If the block above was see-through
                        toReturn.addFirst(curRect);
                    }
                }
            }
        }
        return toReturn;
    }

    public SearchRect searchNextRect(GameMap gm)
    {   //Searches the next block over, called when the SearchRect is 1 block high
        switch (dir)
        {
            case LEFT:
            {
                x -= 2 * GameMap.BLOCK_WIDTH;
            }
            case RIGHT:
            {
                x += GameMap.BLOCK_WIDTH;
                try {
                    if (gm.getAt(x / GameMap.BLOCK_WIDTH, y / GameMap.BLOCK_HEIGHT).canShootThrough() && gm.getAt((x + 1) / GameMap.BLOCK_WIDTH, y / GameMap.BLOCK_HEIGHT).canShootThrough()) ;
                    {
                        return this;
                    }
                } catch (Exception e) {}            //Error occurs if it tries to search out of bounds of the GameMap
                break;
            }
        }
        return null;
    }
}