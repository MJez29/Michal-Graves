//Michal Jez, Shadman Hassan
//11/05/2016
//This class is responsible of drawing explosions onto the screen

import java.util.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Explosion implements ActionListener
{
    //Images of the explosion
    private static HashMap<Integer, Explosion> explosions = new HashMap<Integer, Explosion> ();

    //Images of the explosion produced when an explosive block is shot
    public static BufferedImage[] EXPLOSIVE_BLOCK = ImageTools.initializeImages("Explosives Images\\Explosive Block Explosion\\frame ", 10);

    protected BufferedImage[] frames;           //All the frames of the Explosion
    protected BufferedImage frame;              //The current frame of the Explosion
    protected int numFrames;                    //The number of frames to cycle through
    protected int frameIndex;                   //The index of the current frame
    protected javax.swing.Timer timer;

    protected int x, y;                         //The center of the explosion

    public Explosion(int cx, int cy, BufferedImage[] bis)
    {
        x = cx;
        y = cy;
        frames = bis;
        frame = bis[0];
        numFrames = bis.length;
        frameIndex = 0;
        timer = new javax.swing.Timer(75, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {   //Moves the explosion to the next frame
        frameIndex++;
        if (frameIndex == numFrames)        //If there are no more frames to be drawn
        {
            timer.stop();
            deleteExplosion(this);
            return;
        }
        frame = frames[frameIndex];
    }

    public void draw(Graphics g, int lx, int ly)
    {
        g.drawImage(frame, x - frame.getWidth() / 2 - lx, y - frame.getHeight() / 2 - ly, null);
    }

    public static void drawAll(SubMap sm)
    {   //Draws all the explosions
        Graphics g = sm.getGraphics();          //Access once instead of for every explosion
        int lx = sm.getIntX();
        int ly = sm.getIntY();
        for (Explosion e : explosions.values())
        {
            e.draw(g, lx, ly);
        }
    }

    public static void deleteExplosion(Explosion e)
    {
        explosions.remove(e.x + e.y * 10000, e);
    }

    public static void makeExplosion(int x, int y, BufferedImage[] bis)
    {
        explosions.put(x + y * 10000, new Explosion(x, y, bis));
    }
}
