//Michal Jez
//10/06/2016
//An instance of this class is created every time in the menu the mouse highlights a part of the helicopter to give options to the player as to what
//The want to do
//If the mouse strays too far away the popup will disappear
//
//Start Game:
//      - A button saying "START GAME" pops up and they can press it to start the game
//Quit Game:
//      - A button saying "QUIT GAME" pops up and they can press it to start the game
//Add Player:
//      - A hologram of a new Player to add with a plus sign above
//Player Popup
//      - Buttons to change the keyboard controls appear and a button to remove the player

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

//Base class that has the basic functionality
public class MenuPopup
{
    protected static KeyboardControls kbc;
    
    public enum Option
    {
        DELETE_POPUP,
        START_GAME,
        QUIT,
        ADD_PLAYER_1,
        ADD_PLAYER_2,
        ADD_PLAYER_3,
        REMOVE_PLAYER_1,
        REMOVE_PLAYER_2,
        REMOVE_PLAYER_3,
        HELP,
        NULL
    }

    protected Polygon popup;            //The polygon representing the shape of the popup

    protected Font font;

    protected boolean wasPressed;
    protected boolean pressedOnButton;

    //The Rectangle that the mouse must be contained in to keep the
    Rectangle mouseRect;

    public MenuPopup()
    {
        font = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
    }

    public Option checkMouse(int mx, int my, boolean isPressed) { return Option.NULL; }

    public void draw(Graphics g)
    {   /*
            Draws the polygon background for the popup
        */
        g.setColor(Color.WHITE);
        g.fillPolygon(popup);
        g.setColor(Color.RED);
        g.drawPolygon(popup);
    }

    public static void makeKBC()
    {
        kbc = new KeyboardControls(3);
    }

    public static void saveKBC()
    {
        try {
            kbc.saveAll();          //Error occurs if none of the controls have been loaded in the first place
        } catch(NullPointerException e) {}
    }
}

//MenuPopup in which the Player can start the game
//A button saying "START GAME" appears
class StartGamePopup extends MenuPopup
{
    protected Rectangle startGameRect;

    protected int textX;

    public StartGamePopup(int x, int y, int w, int h)
    {   /*
            (x, y): The bottom-left coordinate of the start game rect in the Helicopter class
            (w, h): The dimensions of the start game rect in the Helicopter class
        */

        startGameRect = new Rectangle(x + 10, y - h - 90, w - 20, 50);
        mouseRect = new Rectangle(x, y - h - 100, w, h + 100);
        popup = new Polygon(new int[] {x, x + w, x + w, x + w - 170, x + 180, x + 170, x},
                            new int[] {y - h - 100, y - h - 100, y - h - 30, y - h - 30, y - h, y - h - 30, y - h - 30}, 7);
    }

    public Option checkMouse(int mx, int my, boolean isPressed)
    {   /*
            Does various things based on different values of variables related to the mouse
            - If pressed while on the button, depending on where they release the mouse they game may start
            - If the mouse is out of bounds and the mouse is up the popup goes away
        */
        if (isPressed)
        {
            if (!wasPressed)
            {
                pressedOnButton = startGameRect.contains(mx, my);
            }
        }
        else
        {
            if (!mouseRect.contains(mx, my))
            {   /*
                    If the mouse is not being pressed and is out of bounds the popup is not required
                    Return value tells the menu to get rid of the popup
                */
                return Option.DELETE_POPUP;
            }
            else if (startGameRect.contains(mx, my) && wasPressed && pressedOnButton)
            {   /*
                    If the mouse was pressed down on the button and it was just released inside the start game rectangle
                */
                return Option.START_GAME;
            }
            pressedOnButton = false;
        }
        wasPressed = isPressed;
        return Option.NULL;         //Do nothing out of the ordinary
    }

    @Override
    public void draw(Graphics g)
    {
        super.draw(g);          //Draws the polygon box

        g.setColor(pressedOnButton ? Color.BLACK : Color.RED);
        g.fillRect(startGameRect.x, startGameRect.y, startGameRect.width, startGameRect.height);

        g.setColor(Color.WHITE);
        g.setFont(font);
        if (textX == 0)
        {
            FontMetrics fm = g.getFontMetrics();
            textX = startGameRect.x + startGameRect.width / 2 - fm.stringWidth("START GAME") / 2;
        }
        g.drawString("START GAME", textX, startGameRect.y + 45);
    }
}

//Popup that appears for the Player to quit the application
class QuitGamePopup extends MenuPopup
{
    protected Rectangle quitGameRect;

    protected int textX;

    public QuitGamePopup(int x, int y, int w, int h)
    {   /*
            (x, y): The top-left coordinate of the quit game rect in the Helicopter class
            (w, h): The dimensions of the quit game rect in the Helicopter class
        */

        quitGameRect = new Rectangle(x - 190, y + 30, 150, h - 60);
        mouseRect = new Rectangle(x - 200, y, w + 200, h);
        popup = new Polygon(new int[] {x - 200, x - 30, x - 30, x, x - 30, x - 30, x - 200},
                new int[] {y + 20, y + 20, y + 70, y + h / 2, y + h - 70, y + h - 20, y + h - 20}, 7);
    }

    public Option checkMouse(int mx, int my, boolean isPressed)
    {   /*
            Does various things based on different values of variables related to the mouse
            - If pressed while on the button, depending on where they release the mouse they application may terminate
            - If the mouse is out of bounds and the mouse is up the popup goes away
        */
        if (isPressed)
        {
            if (!wasPressed)
            {
                pressedOnButton = quitGameRect.contains(mx, my);
            }
        }
        else
        {
            if (!mouseRect.contains(mx, my))
            {   /*
                    If the mouse is not being pressed and is out of bounds the popup is not required
                    Return value tells the menu to get rid of the popup
                */
                return Option.DELETE_POPUP;
            }
            else if (quitGameRect.contains(mx, my) && wasPressed && pressedOnButton)
            {   /*
                    If the mouse was pressed down on the button and it was just released inside the start game rectangle
                */
                return Option.QUIT;
            }
            pressedOnButton = false;
        }
        wasPressed = isPressed;
        return Option.NULL;         //Do nothing out of the ordinary
    }

    @Override
    public void draw(Graphics g)
    {
        super.draw(g);          //Draws the polygon box

        g.setColor(pressedOnButton ? Color.BLACK : Color.RED);
        g.fillRect(quitGameRect.x, quitGameRect.y, quitGameRect.width, quitGameRect.height);

        g.setColor(Color.WHITE);
        g.setFont(font);
        if (textX == 0)
        {
            FontMetrics fm = g.getFontMetrics();
            textX = quitGameRect.x + quitGameRect.width / 2 - fm.stringWidth("QUIT") / 2;
        }
        g.drawString("QUIT", textX, quitGameRect.y + 45);
    }
}

//Popup that appears for the user to change keyboard controls and remove a specific Player
class PlayerPopup extends MenuPopup
{
    //Keys that the Players can use as controls will not be null
    public static final BufferedImage[] keyImgs = ImageTools.initializeImages("Keyboard Key Images\\", KeyEvent.KEY_LAST + 1);
    public static final BufferedImage back = ImageTools.initializeImage("Helicopter Images\\Key Popup.png");

    //Rectangles where the various key buttons are displayed
    protected Rectangle leftKeyRect, rightKeyRect, upKeyRect, downKeyRect, jumpKeyRect, shootKeyRect, evadeKeyRect, removeRect;

    //True if the mouse was pressed down on that key button
    protected boolean pressedOnLeftKey, pressedOnRightKey, pressedOnUpKey, pressedOnDownKey, pressedOnJumpKey, pressedOnShootKey, pressedOnEvadeKey, pressedOnRemove;

    //The images of each key
    protected BufferedImage leftKeyImg, rightKeyImg, upKeyImg, downKeyImg, jumpKeyImg, shootKeyImg, evadeKeyImg;

    protected Menu menu;

    //The area that the mouse can be is broken down into 2 Rectangles: the mouseRect and the playerRect
    protected Rectangle playerRect;

    protected int playerNum;                //Which Player this popup is being created for

    protected int textX;

    protected int controlToGet;             //Which control the popup is getting right now (Ex. KeyboardControls.LEFT, RIGHT, etc...

    public PlayerPopup(int x, int y, int w, int h, Menu m, int pn)
    {
        menu = m;
        if (kbc == null) {
            makeKBC();
        }

        playerRect = new Rectangle(x, y, w, h);
        mouseRect = new Rectangle(x + w / 2 - 150, y - 275, 300, 275);
        leftKeyRect = new Rectangle(mouseRect.x + 90, mouseRect.y + 10, 50, 50);
        rightKeyRect = new Rectangle(mouseRect.x + 90, mouseRect.y + 10 + 60, 50, 50);
        upKeyRect = new Rectangle(mouseRect.x + 90, mouseRect.y + 10 + 60 * 2, 50, 50);
        downKeyRect = new Rectangle(mouseRect.x + 90, mouseRect.y + 10 + 60 * 3, 50, 50);
        evadeKeyRect = new Rectangle(mouseRect.x + 240, mouseRect.y + 10, 50, 50);
        shootKeyRect = new Rectangle(mouseRect.x + 240, mouseRect.y + 10 + 60, 50, 50);
        jumpKeyRect = new Rectangle(mouseRect.x + 240, mouseRect.y + 10 + 60 * 2, 50, 50);

        removeRect = new Rectangle(mouseRect.x + 160, mouseRect.y + 190, 130, 50);

        playerNum = pn;
    }

    public Option checkMouse(int mx, int my, boolean isPressed)
    {   /*
            Does various things based on different values of variables related to the mouse
            - If pressed while on the button, depending on where they release the mouse they application may terminate
            - If the mouse is out of bounds and the mouse is up the popup goes away
        */
        if (isPressed)
        {
            if (!wasPressed)
            {
                pressedOnLeftKey = leftKeyRect.contains(mx, my);
                pressedOnRightKey = rightKeyRect.contains(mx, my);
                pressedOnUpKey = upKeyRect.contains(mx, my);
                pressedOnDownKey = downKeyRect.contains(mx, my);
                pressedOnJumpKey = jumpKeyRect.contains(mx, my);
                pressedOnShootKey = shootKeyRect.contains(mx, my);
                pressedOnEvadeKey = evadeKeyRect.contains(mx, my);
                pressedOnRemove = removeRect.contains(mx, my);
            }
        }
        else
        {
            if (!mouseRect.contains(mx, my) && !playerRect.contains(mx, my))
            {   /*
                    If the mouse is not being pressed and is out of bounds the popup is not required
                    Return value tells the menu to get rid of the popup
                */
                return Option.DELETE_POPUP;
            }
            else if (wasPressed)
            {   /*
                    If the mouse was pressed down on the button and it was just released inside the a button rectangle
                */
                if (pressedOnLeftKey && leftKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.LEFT;
                    menu.getNewKey(KeyboardControls.LEFT);
                }
                else if (pressedOnRightKey && rightKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.RIGHT;
                    menu.getNewKey(KeyboardControls.RIGHT);
                }
                else if (pressedOnUpKey && upKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.UP;
                    menu.getNewKey(KeyboardControls.UP);
                }
                else if (pressedOnDownKey && downKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.DOWN;
                    menu.getNewKey(KeyboardControls.DOWN);
                }
                else if (pressedOnJumpKey && jumpKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.JUMP;
                    menu.getNewKey(KeyboardControls.JUMP);
                }
                else if (pressedOnShootKey && shootKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.SHOOT;
                    menu.getNewKey(KeyboardControls.SHOOT);
                }
                else if (pressedOnEvadeKey && evadeKeyRect.contains(mx, my))
                {
                    controlToGet = KeyboardControls.EVADE;
                    menu.getNewKey(KeyboardControls.EVADE);
                }
                else if (pressedOnRemove && removeRect.contains(mx, my))
                {
                    switch (playerNum)
                    {
                        case 1:
                            return Option.REMOVE_PLAYER_1;
                        case 2:
                            return Option.REMOVE_PLAYER_2;
                        case 3:
                            return Option.REMOVE_PLAYER_3;
                    }
                }
            }
            pressedOnLeftKey = false;
            pressedOnRightKey = false;
            pressedOnUpKey = false;
            pressedOnDownKey = false;
            pressedOnJumpKey = false;
            pressedOnShootKey = false;
            pressedOnEvadeKey = false;
        }
        wasPressed = isPressed;
        return Option.NULL;         //Do nothing out of the ordinary
    }

    public boolean setKey(int n)
    {   /*
            Attempts to set a key inputted by the keyboard as the new key for a specific character motion
            Returns true if the assigning of the new key was successful
            Otherwise returns false and the user will have to press another key
        */
        if (keyImgs[n] == null) return false;               //Invalid key
        else if (kbc.usesKey(n)) return false;              //Key is already in use
        else kbc.setKey(controlToGet, n, playerNum);        //Key passes inspection and is updated

        return true;
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(back, mouseRect.x, mouseRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.LEFT, playerNum)], leftKeyRect.x, leftKeyRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.RIGHT, playerNum)], rightKeyRect.x, rightKeyRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.UP, playerNum)], upKeyRect.x, upKeyRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.DOWN, playerNum)], downKeyRect.x, downKeyRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.EVADE, playerNum)], evadeKeyRect.x, evadeKeyRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.SHOOT, playerNum)], shootKeyRect.x, shootKeyRect.y, null);
        g.drawImage(keyImgs[kbc.getKey(KeyboardControls.JUMP, playerNum)], jumpKeyRect.x, jumpKeyRect.y, null);

        //Draws the Rectangle of the remove button
        g.setColor(pressedOnRemove ? Color.BLACK : Color.RED);
        g.fillRect(removeRect.x, removeRect.y, removeRect.width, removeRect.height);

        //Drawing the Remove button text("-")
        g.setColor(Color.WHITE);
        g.setFont(font);
        if (textX == 0)
        {
            FontMetrics fm = g.getFontMetrics();
            textX = removeRect.x + removeRect.width / 2 - fm.stringWidth("X") / 2;
        }
        g.drawString("X", textX, removeRect.y + 45);

    }
}

//The popup that pop's up to allow the user to add another Player to the screen
class AddPlayerPopup extends MenuPopup
{
    protected Rectangle addRect;

    protected Option player;            //Which Player this popup is to add

    protected int textX;                //Leftmost position of text

    public AddPlayerPopup(int x, int y, int w, int h, Option op)
    {
        mouseRect = new Rectangle(x, y - 100, w, h + 100);
        addRect = new Rectangle(x + 5, y - 90, w - 10, 50);
        popup = new Polygon(new int[] {x, x + w, x + w, x + w / 2 + 10, x + w / 2,  x + w / 2 - 10, x},
                new int[] {y - 100, y - 100, y - 30, y - 30, y, y - 30, y - 30}, 7);
        player = op;
    }

    public Option checkMouse(int mx, int my, boolean isPressed)
    {   /*
            Does various things based on different values of variables related to the mouse
            - If pressed while on the button, depending on where they release the mouse a Player may be added
            - If the mouse is out of bounds and the mouse is up the popup goes away
        */
        if (isPressed)
        {
            if (!wasPressed)
            {
                pressedOnButton = addRect.contains(mx, my);
            }
        }
        else
        {
            if (!mouseRect.contains(mx, my))
            {   /*
                    If the mouse is not being pressed and is out of bounds the popup is not required
                    Return value tells the menu to get rid of the popup
                */
                return Option.DELETE_POPUP;
            }
            else if (addRect.contains(mx, my) && wasPressed && pressedOnButton)
            {   /*
                    If the mouse was pressed down on the button and it was just released inside the start game rectangle
                */
                return player;
            }
            pressedOnButton = false;
        }
        wasPressed = isPressed;
        return Option.NULL;         //Do nothing out of the ordinary
    }

    @Override
    public void draw(Graphics g)
    {
        super.draw(g);          //Draws the polygon box

        g.setColor(pressedOnButton ? Color.BLACK : Color.RED);
        g.fillRect(addRect.x, addRect.y, addRect.width, addRect.height);

        g.setColor(Color.WHITE);
        g.setFont(font);
        if (textX == 0)
        {
            FontMetrics fm = g.getFontMetrics();
            textX = addRect.x + addRect.width / 2 - fm.stringWidth("+") / 2;
        }
        g.drawString("+", textX, addRect.y + 45);
    }
}