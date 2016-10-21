//Michal Jez, Shadman Hassan
//17/06/2016
//This JPanel appears once all of the enemies are killed
//It displays the number of kills each Player gets
//When a keyboard button is pressed the screen returns to the menu screen

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

public class GameOver extends JPanel implements KeyListener
{
    public boolean isReady;

    public static final BufferedImage GAME_OVER = ImageTools.initializeImage("Game Over Images\\Game Over.png");

    public static final HashMap<String, BufferedImage> NUMBERS = ImageTools.initializeImagesAsHashMap("Game Over Images\\", 10);

    private StateManager sm;
    private int[] kills;
    private BufferedImage[] killCounts;

    private Player[] players;

    public GameOver(StateManager s)
    {
        sm = s;
        kills = Bullet.getKills();

        //Creates the LinkedList containing all of the fallen Players and the images of the number of kills each of them got
        killCounts = new BufferedImage[kills.length];
        players = new Player[kills.length];
        for (int i = 0; i < kills.length; i++)
        {   //Makes the image of the number of kills that this person got
            killCounts[i] = makeNumber(kills[i]);
            players[i] = new Player(10, GAME_OVER.getHeight() + 30 + i * 75, "sarge");
        }
        addKeyListener(this);
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
    {   //Draws the JPanel

        //Draws the background
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        g.drawImage(GAME_OVER, getWidth() / 2 - GAME_OVER.getWidth() / 2, 10, null);

        //Draws each Player and the number of kills they get
        int y = 10 + GAME_OVER.getHeight() + 20;
        for (int i = 0; i < killCounts.length; i++)
        {
            players[i].draw(g, 0, 0, false);
            g.drawImage(killCounts[i], w - killCounts[i].getWidth() - 10, y, null);
            y += 75;
        }
    }

    public void keyTyped(KeyEvent e)
    {
        removeKeyListener(this);
        sm.goToMenu();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {   //Press any key to go back to the menu
        removeKeyListener(this);
        sm.goToMenu();
    }

    public void keyReleased(KeyEvent e) {}

    public static BufferedImage makeNumber(int n)
    {   /*
            Generates an image of the inputted number
        */
        int lg = (int) (Math.log10(n));                 //Finds the number of digits
        if (lg == Integer.MIN_VALUE) lg = 0;            //(int) log10(0) returns Integer.MIN_VALUE

        BufferedImage bi = new BufferedImage((lg + 1) * 50, 50, BufferedImage.TYPE_INT_ARGB);
        String str = Integer.toString(n);
        Graphics g = bi.getGraphics();
        for (int i = 0; i < str.length(); i++)
        {
            g.drawImage(NUMBERS.get("Game Over Images\\" + str.charAt(i)), i * 50, 0, null);            //Builds the image charater by character
        }
        g.dispose();;
        return bi;
    }
}
