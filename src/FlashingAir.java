//Michal Jez, Shadman Hassan
//07/05/2016
//This class contains Air blocks that have multiple images that need to be cycled through to give the allusion of them flashing

import java.awt.*;
import java.awt.image.*;

public class FlashingAir extends Air
{
    public static final BufferedImage[] CHECKPOINT_LEFT_RED = ImageTools.initializeImages("Checkpoint Images\\Red\\Light Left ", 3);
    public static final BufferedImage[] CHECKPOINT_RIGHT_RED = ImageTools.flipHorizontally(CHECKPOINT_LEFT_RED);
    public static final BufferedImage[] CHECKPOINT_TOP_RED = ImageTools.initializeImages("Checkpoint Images\\Red\\Light Top ", 3);

    public static final BufferedImage[] CHECKPOINT_LEFT_GREEN = ImageTools.initializeImages("Checkpoint Images\\Green\\Light Left ", 3);
    public static final BufferedImage[] CHECKPOINT_RIGHT_GREEN = ImageTools.flipHorizontally(CHECKPOINT_LEFT_GREEN);
    public static final BufferedImage[] CHECKPOINT_TOP_GREEN = ImageTools.initializeImages("Checkpoint Images\\Green\\Light Top ", 3);

    private int frame, maxFrame, n;					//The current frame, the max frame it can go to, what to add to frame each loop

    private BufferedImage[] oppImgs;                //The opposite versions of the current imgs (Ex. reg imgs my be red lights while opps are green)

    public FlashingAir(int x, int y)
    {
        super(x, y);
        frame = 0;
    }

    public FlashingAir(int x, int y, BufferedImage[] bis)
    {
        this(x, y);
        imgs = bis;
        maxFrame = imgs.length - 1;
        n = 1;
    }

    public FlashingAir(int x, int y, BufferedImage[] bis, BufferedImage[] oppBis)
    {
        this(x, y, bis);
        oppImgs = oppBis;
    }

    @Override
    public void draw(Graphics g)
    {   //Draws the current frame of the flashing air
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
        if (frame == maxFrame || frame == 0) n *= -1;
    }

    public void swapImgs()
    {   //Swaps the current imgs with the opposite ones, used at checkpoint, red light becomes green once a Player reaches it
        BufferedImage[] temps = imgs;
        imgs = oppImgs;
        oppImgs = temps;
        temps = null;
    }
}