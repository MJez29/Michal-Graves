//Michal Jez
//07/05/2016
//The container class of all the blocks that change visually during the course of the game
//It has a timer which updates all of the flashing blocks on the main biome image

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class FlashingBlocks implements ActionListener
{
    private HashMap<Integer, Block> flashingBlocks;                 //The blocks to make flash
    private GameMap gm;
    private javax.swing.Timer timer;

    public FlashingBlocks(GameMap g)
    {
        flashingBlocks = new HashMap<Integer, Block> ();
        timer = new javax.swing.Timer(500, this);
        gm = g;
    }

    public void addFlashingBlock(Block b)
    {   //Adds a new flashing block
        //The key is defined as x + 1000y
        flashingBlocks.put(b.getIntX() + 1000 * b.getIntY(), b);
    }

    public void deleteFlashingBlock(Block b)
    {   //Deletes the block
        flashingBlocks.remove(b.getIntX() + 1000 * b.getIntY());
    }

    public void setGameMap(GameMap g)
    {
        gm = g;
    }

    public void startTimer()
    {   //Starts the timer
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {   //Gets fired every time it is time to update the FlashingBlocks
        Graphics g = gm.getGraphics();
        for (Block b : flashingBlocks.values())
        {
            b.draw(g);
        }
    }

    public void removeAll()
    {   /*
            Called whenever the game moves on from the current biome
            Serves 2 purposes:
                - Clear memory
                - Not have the timers from the last biome be drawn on the current biome
        */
        gm = null;
        timer.stop();
        flashingBlocks.clear();
    }
}
