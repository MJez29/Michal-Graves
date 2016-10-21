//Michal Jez
//06/06/2016
//This is the class of the menu that appears when someone starts the application
//They will have the option to start a new game, access options and quit the application

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Menu extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{
    public boolean isReady;						//Indicates to the StateManager whether or not the game is ready

    public static BufferedImage MICHAL_GRAVES = ImageTools.initializeImage("logo.png");

    private StateManager stateManager;

    protected Helicopter helicopter;                    //The helicopter that appears in the middle of the screen

    protected boolean mouseDown;

    protected MenuPopup menuPopup;

    //protected HelpButton helpButton;

    protected Font font;

    protected int mx, my;                       //The mouse coordinates

    private int alpha;

    //The text that is displayed when the user is to select a new keyboard key
    //keyText is always the same
    //subKeyText can be "" or "Invalid key. Try again."
    private static final String keyText = "Enter a key. Esc to Cancel.";
    private static String subKeyText = "";

    protected boolean[] keys;
    protected boolean gettingKey;               //True if the menu is waiting for the Player to press a key on the keyboard
    protected int key;      //Which key the menu is collecting for the PlayerPopup

    protected MenuPopup.Option selectedOption;  //The option that is selected by the user as the screen fades to black

    public Menu(int nw, int nh, StateManager sm)
    {
        stateManager = sm;

        helicopter = new Helicopter(nw, nh, this);

        font = new Font(Font.DIALOG, Font.ITALIC, 50);

        Cloud.initializeClouds(800, 600);                   //Starts to make new clouds to simulate the appearance of the helicopter moving

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void addNotify()
    {	/*
            Will be called when the game is ready to begin
        */
        super.addNotify();
        requestFocus();
        isReady = true;
    }

    public void refresh()
    {   /*
            Updates the menu
        */
        if (alpha > 0)          //If the screen is fading out
        {
            alpha++;
            helicopter.updatePropellerFrames();         //Players cannot control the mouse anymore
            if (alpha >= 255)
            {   /*
                    The only case where the alpha increases is to start the game
                */
                MenuPopup.saveKBC();
                stateManager.startNewGame();
            }
        }
        else if (gettingKey)            //If the menu is waiting for the user to press a key
        {
            helicopter.updatePropellerFrames();
        }
        else if (menuPopup == null)         //Nothing special occurs
        {
            menuPopup = helicopter.update(mx, my);
        }
        else            //If a popup has been activated
        {
            helicopter.updatePropellerFrames();         //Just updates propellers, nothing else
            switch (menuPopup.checkMouse(mx, my, mouseDown))
            {
                case NULL:
                    break;
                case DELETE_POPUP:
                    menuPopup = null;
                    break;
                case START_GAME:
                    menuPopup = null;
                    alpha++;
                    return;
                case QUIT:
                    menuPopup = null;
                    MenuPopup.saveKBC();
                    stateManager.quitGame();
                    return;
                case ADD_PLAYER_1:
                    helicopter.addPlayer(1);
                    menuPopup = null;
                    break;
                case ADD_PLAYER_2:
                    helicopter.addPlayer(2);
                    menuPopup = null;
                    break;
                case ADD_PLAYER_3:
                    helicopter.addPlayer(3);
                    menuPopup = null;
                    break;
                case REMOVE_PLAYER_1:
                    helicopter.removePlayer(1);
                    menuPopup = null;
                    break;
                case REMOVE_PLAYER_2:
                    helicopter.removePlayer(2);
                    menuPopup = null;
                    break;
                case REMOVE_PLAYER_3:
                    helicopter.removePlayer(3);
                    menuPopup = null;
                    break;
            }
        }

        Cloud.updateAll();                  //Moves the clouds

        repaint();
    }

    public void adjustLayout(int nw, int nh)
    {   /*
            Called whenever the screen is resized
            Adjusts the layout of the helicopter and title on the screen
        */
        helicopter.adjustForResize(nw, nh);
        Cloud.updateScreenSize(nw, nh);
    }

    public boolean[] getPlayersToMake()
    {
        return helicopter.getPlayersToMake();
    }

    @Override
    public void paintComponent(Graphics g)
    {   /*
            Updates the screen
        */
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, w, h);

        g.drawImage(MICHAL_GRAVES, w / 2 - MICHAL_GRAVES.getWidth() / 2, 0, null);

        Cloud.drawAll(g);

        helicopter.draw(g);

        try {
            menuPopup.draw(g);          //Error occurs if there is no MenuPopup to draw currently
        } catch (NullPointerException e) {}

        if (gettingKey)
        {   /*
                Drawing is slightly different when the program is waiting for the user to press a key
                To avoid confusion, they user cannot do anything else until they press a key
            */

            //Fills the screen with a translucent black rectangle to put it out of focus
            g.setColor(new Color(0, 0, 0,  175));
            g.fillRect(0, 0, w, h);

            //Draws instructions to the screen
            g.setFont(font);
            g.setColor(Color.RED);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(keyText, w / 2 - fm.stringWidth(keyText) / 2, h / 2 - 55);
            g.drawString(subKeyText, w / 2 - fm.stringWidth(subKeyText) / 2, h / 2 + 5);
        }
        else if (alpha > 0)
        {
            g.setColor(new Color(0, 0, 0, alpha));
            g.fillRect(0, 0, w, h);
        }
    }

    public void getNewKey(int k)
    {   /*
            Waits for a new key pressed from the user
        */
        gettingKey = true;
        keys = new boolean[KeyEvent.KEY_LAST + 1];
        addKeyListener(this);
    }

    //----------------------------------------------------- MouseMotionListener Methods ---------------------------------------------------------
    @Override
    public void mouseMoved(MouseEvent e)
    {	/*If the mouse is moved*/
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point s = getLocationOnScreen();
        mx = p.x - s.x;      //Position relative to the panel
        my = p.y - s.y;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {	/*If the mouse is dragged*/
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point s = getLocationOnScreen();
        mx = p.x - s.x;      //Position relative to the panel
        my = p.y - s.y;
    }

    //-------------------------------------------------------- MouseListener Methods -----------------------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e)
    {	/*If the mouse has been released*/
        mouseDown = false;
    }
    public void mouseClicked(MouseEvent e){}

    public void mousePressed(MouseEvent e)
    {	/*
            If a mouse button has been pressed, it updates the state of the variable tracking it
    	    so that the button is now being pressed
    	*/
        if (e.getButton() == MouseEvent.BUTTON1)			//Left click
        {
            mouseDown = true;
        }
    }

    //---------------------------------------------------------- KeyListener Methods -----------------------------------------------------------
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (((PlayerPopup) menuPopup).setKey(keyCode) || keyCode == 27)
        {   /*
                If the selection of a new key is successful the program resumes as it was before and stops checking for keys
                If the user pressed an invalid key they must try again
                OR
                If the user pressed ESCAPE the key selection stops and the screen returns back to normal
            */
            gettingKey = false;
            removeKeyListener(this);            //No need to listen to keys when nothing will be done with them
            subKeyText = "";
        }
        else subKeyText = "Invalid Key. Try again.";
    }

    public void keyReleased(KeyEvent e)
    {
        keys[e.getKeyCode()] = false;
    }
}