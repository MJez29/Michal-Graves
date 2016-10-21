//Michal Jez, Shadman Hassan
//13/06/2016
//Cloud objects fly past the helicopter in the main menu to give it the appearance of moving

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

public class Cloud
{
    public static final BufferedImage[] cloudImgs = ImageTools.initializeImages("Cloud Images\\Cloud ", 5);
    protected static LinkedList<Cloud> clouds;
    protected static int screenWidth, screenHeight;

    public static final int VX = 12;
    public static final int NAME_VX = 6;

    protected int x, y, width, height, vx;

    protected BufferedImage img;

    public Cloud()
    {   //The default constructor
        x = screenWidth + 10;                           //Starts at the right of the screen at a random height
        y = (int) (Math.random() * screenHeight);
        double scaleFactor;                             //Clouds are scaled to make some appear closer than other
        int i = (int) (Math.random() * cloudImgs.length);
        if (i < 3)                  //If just a regular cloud
        {
            scaleFactor = Math.random() + 0.5;
            vx =(int) (VX * scaleFactor);
        }
        else                        //If a cloud with one of the game's makers on it, cloud is slower and larger to increase legibility
        {
            scaleFactor = Math.random() + 1.0;
            vx = (int) (NAME_VX / scaleFactor);

        }
        img = ImageTools.scale(cloudImgs[i], scaleFactor, scaleFactor);
        width = img.getWidth();
        height = img.getHeight();
    }

    public boolean move()
    {   /*
            Moves the cloud
            Returns true if the cloud should be deleted
        */
        x -= vx;
        return x + width < 0;
    }

    public void draw(Graphics g)
    {
        g.drawImage(img, x, y, null);
    }

    public static void initializeClouds(int nw, int nh)
    {   /*
            Creates the LinkedList to avoid a NullPointerException
        */
        clouds = new LinkedList<Cloud> ();
        screenWidth = nw;
        screenHeight = nh;
    }

    public static void clearClouds()
    {
        clouds = null;
    }

    public static void updateScreenSize(int nw, int nh)
    {   //Updates screen size to fit
        screenWidth = nw;
        screenHeight = nh;
    }

    public static void updateAll()
    {   //Updates every cloud
        Iterator it = clouds.iterator();
        while(it.hasNext())
        {
            if (((Cloud) it.next()).move())
                it.remove();
        }

        if ((int) (Math.random() * 100) == 0)
            clouds.addLast(new Cloud());
    }

    public static void drawAll(Graphics g)
    {   //Draws every cloud
        for (Cloud c : clouds)
        {
            c.draw(g);
        }
    }
}
