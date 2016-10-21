//Michal Jez, Shadman Hassan
//14/06/2016
//The menu that appears when the game is paused

import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class PauseMenu extends JPanel implements KeyListener
{
    public static BufferedImage PAUSED_IMG = ImageTools.initializeImage("Pause Images\\Paused.png");

    //Each array contains 2 images:
    //  - pos [0]: The image when it is not selected
    //  - pos [1]: The image when it is selected
    public static BufferedImage[] RESUME_IMGS = ImageTools.initializeImages("Pause Images\\Resume ", 2);
    public static BufferedImage[] QUIT_IMGS = ImageTools.initializeImages("Pause Images\\Quit ", 2);

    public boolean isReady;						//Indicates to the StateManager whether or not the PauseMenu is ready

    protected Image back;           //What is displayed in the background

    protected static final int RESUME = 0, QUIT = 1;

    protected StateManager sm;

    protected int[] downKeys, upKeys, shootKeys;

    protected int pos;
    protected int maxPos;

    public PauseMenu(StateManager s, int[] dk, int [] uk, int[] sk)
    {
        sm = s;
        addKeyListener(this);
        downKeys = dk;
        upKeys = uk;
        pos = 0;
        maxPos = QUIT;
        shootKeys = sk;
        setSize(sm.getSize());
    }

    public void addNotify()
    {	/*Will be called when the PauseMenu is ready to begin*/
        super.addNotify();
        requestFocus();
        isReady = true;
    }

    public void refresh()
    {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        int w = getWidth();
        int h = getHeight();
        int y = 50;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        //Draws the title (saying "PAUSED")
        g.drawImage(PAUSED_IMG, w / 2 - PAUSED_IMG.getWidth() / 2, y, null);
        y += PAUSED_IMG.getHeight() + 100;

        //Draws first option ("RESUME")
        BufferedImage temp = (pos == RESUME) ? RESUME_IMGS[1] : RESUME_IMGS[0];
        g.drawImage(temp, w / 2 - temp.getWidth() / 2, y, null);
        y += temp.getHeight() + 50;

        //Draws last option ("QUIT")
        temp = (pos == QUIT) ? QUIT_IMGS[1] : QUIT_IMGS[0];
        g.drawImage(temp, w / 2 - temp.getWidth() / 2, y, null);
        //y += temp.getHeight() + 50;
    }

    private static boolean contains(int[] keys, int key)
    {   /*
            Returns whether the specified key is found inside the array of keys
        */
        for (int k : keys)
        {
            if (k == key)
            {
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------- KeyListener Methods -----------------------------------------------------------
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (keyCode == 27)
        {   /*
                If they press the escape button they return back to the game
            */
            removeKeyListener(this);
            sm.returnToGame();
        }
        else if (contains(downKeys, keyCode))
        {   /*
                If someone presses any of the players' down key buttons the current selected option will move down by 1
            */
            if (++pos > maxPos) pos = 0;
        }
        else if (contains(upKeys, keyCode))
        {   /*
                If someone presses any of the players' up key buttons the current selected option will move up by 1
            */
            if (--pos < 0) pos = maxPos;
        }
        else if (contains(shootKeys, keyCode))
        {   /*
                If someone presses any of the players' shoot key buttons the current selected option will be selected
            */
            switch (pos)
            {
                case RESUME:
                    removeKeyListener(this);
                    sm.returnToGame();
                    break;
                case QUIT:
                    removeKeyListener(this);
                    sm.goToMenu();
                    break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
}