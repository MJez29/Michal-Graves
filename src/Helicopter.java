//Michal Jez, Shadman Hassan
//17/06/2016
//Appears in the menu screen
//Creates MenuPopups based on where the mouse is located
//Also updates the visual appearance of the helicopter (spins propellers, draws players riding on top)

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class Helicopter
{
    public static final BufferedImage BODY = ImageTools.initializeImage("Helicopter Images\\Body.png");
    public static final BufferedImage[] SIDE_PROPELLER = ImageTools.initializeImages("Helicopter Images\\Side Propeller ", 4);
    public static final BufferedImage[] TOP_PROPELLER = ImageTools.initializeImages("Helicopter Images\\Top Propeller ", 4);

    public static final BufferedImage COCKPIT_ALPHA = ImageTools.initializeImage("Helicopter Images\\Window Alpha.png");
    public static final BufferedImage PLAYER_ALPHA = ImageTools.initializeImage("Helicopter Images\\Sarge Alpha.png");

    protected Player[] players;

    //Co-ordinates of the top left corner of the helicopter and the centers of each propeller
    protected int x, y, sideX, sideY, topX, topY;

    //Half of each dimension of the 2 propellers
    protected int sideHalfWidth, sideHalfHeight, topHalfWidth, topHalfHeight;

    //Variables involved in incrementing the side frame
    protected int sideFrame;

    //Variables involved in incrementing and decrementing the top frame
    protected int topFrame;
    protected int topRate;

    //Rectangles for hovering the mouse over different parts of the helicopter
    protected Rectangle startGameRect;          //Located below the Players feet
    protected Rectangle quitGameRect;           //Located where the helicopter's cockpit is
    protected Rectangle p1Rect, p2Rect, p3Rect; //Located where the Player's are

    //Flags that store which option the mouse is hovering over
    protected boolean overStartGame;
    protected boolean overQuitGame;
    protected boolean overP1, overP2, overP3;
    
    protected boolean hasP1, hasP2, hasP3;      //True if that Player has been added to the helicopter

    public static final int playerSeparation = 100;          //The distance between the left X-values of adjacent Players

    //The motion of the helicopter is sinusoidal these values are part of the function for calculating the height
    private double aoc, amp, k;

    protected Menu menu;

    public Helicopter(int w, int h, Menu m)
    {
        x = w / 2 - BODY.getWidth() / 2;
        y = h / 2 - BODY.getHeight() / 2;

        topX = x + 520 - TOP_PROPELLER[0].getWidth() / 2;
        topY = y + 12 - TOP_PROPELLER[0].getHeight() / 2;
        sideX = x + 15 - SIDE_PROPELLER[0].getWidth() / 2;
        sideY = y + 115 - SIDE_PROPELLER[0].getHeight() / 2;

        startGameRect = new Rectangle(x + 270, y + 310, 360, 40);
        quitGameRect = new Rectangle(x + 675, y + 175, 80, 160);
        p1Rect = new Rectangle(x + 290, y + 242, 48, 68);
        p2Rect = new Rectangle(x + 290 + playerSeparation, y + 242, 48, 68);
        p3Rect = new Rectangle(x + 290 + 2 * playerSeparation, y + 242, 48, 68);
        
        //Only 1 Player is onscreen at the start
        hasP1 = true;
        hasP2 = false;
        hasP3 = false;

        sideFrame = 0;
        topFrame = 0;
        topRate = 1;

        menu = m;

        players = new Player[3];
        try {
            players[0] = new Player(290, 310 - 68, "sarge", 1);
        } catch (Exception e) {}
    }

    public boolean[] getPlayersToMake()
    {
        return new boolean[] { hasP1, hasP2, hasP3 };
    }

    public void addPlayer(int playerNum)
    {   /*
            Makes the new specified Player
        */
        players[playerNum - 1] = new Player(290 + (playerNum - 1) * playerSeparation, 242, "sarge", playerNum);
        switch(playerNum)
        {
            case 1:
                hasP1 = true;
                break;
            case 2:
                hasP2 = true;
                break;
            case 3:
                hasP3 = true;
                break;
        }
    }

    public void removePlayer(int playerNum)
    {   /*
            Removes the desired Player from the helicopter
            Changes to the controls will remain intact as they were just before the Player was removed
            Players can be added back once removed
        */
        players[playerNum - 1] = null;
        switch(playerNum)
        {
            case 1:
                hasP1 = false;
                break;
            case 2:
                hasP2 = false;
                break;
            case 3:
                hasP3 = false;
                break;
        }
    }

    public MenuPopup update(int mx, int my)
    {   /*
            Updates the helicopter's position and look based on Player factors
        */
        updatePropellerFrames();
        return checkMousePosition(mx, my);
    }

    protected void updatePropellerFrames()
    {   /*
            Updates the frames for the helicopter's propeller
        */

        //Side-propeller frames
        sideFrame++;
        if (sideFrame >= SIDE_PROPELLER.length) {
            sideFrame = 0;
        }

        //Top-propeller frames
        topFrame += topRate;
        if (topFrame >= TOP_PROPELLER.length || topFrame < 0) {
            topRate *= -1;
            topFrame += topRate;
        }
    }

    protected MenuPopup checkMousePosition(int mx, int my)
    {   /*
            Checks the mouse position for collisions with different parts of the helicopter to activate different features
        */
        overStartGame = startGameRect.contains(mx, my);
        if (overStartGame) return new StartGamePopup(startGameRect.x, startGameRect.y + startGameRect.height, startGameRect.width, startGameRect.height);

        overQuitGame = quitGameRect.contains(mx, my);
        if (overQuitGame) return new QuitGamePopup(quitGameRect.x, quitGameRect.y, quitGameRect.width, quitGameRect.height);
        
        overP1 = p1Rect.contains(mx, my);
        if (overP1) return (hasP1) ? new PlayerPopup(p1Rect.x, p1Rect.y, p1Rect.width, p1Rect.height, menu, 1) :
                new AddPlayerPopup(p1Rect.x, p1Rect.y, p1Rect.width, p1Rect.height, MenuPopup.Option.ADD_PLAYER_1);

        overP2 = p2Rect.contains(mx, my);
        if (overP2) return (hasP2) ? new PlayerPopup(p2Rect.x, p2Rect.y, p2Rect.width, p2Rect.height, menu, 2) :
                new AddPlayerPopup(p2Rect.x, p2Rect.y, p2Rect.width, p2Rect.height, MenuPopup.Option.ADD_PLAYER_2);

        overP3 = p3Rect.contains(mx, my);
        if (overP3) return (hasP3) ? new PlayerPopup(p3Rect.x, p3Rect.y, p3Rect.width, p3Rect.height, menu, 3) :
                new AddPlayerPopup(p3Rect.x, p3Rect.y, p3Rect.width, p3Rect.height, MenuPopup.Option.ADD_PLAYER_3);
        

        return null;            //Nothing special is to be done
    }

    protected void adjustForResize(int nw, int ny)
    {   /*
            Adjusts the x-coordinates of the helicopter so that it remains centered when the screen is resized
        */
        x = nw / 2 - BODY.getWidth() / 2;
        y = ny / 2 - BODY.getHeight() / 2;
        topX = x + 520 - TOP_PROPELLER[0].getWidth() / 2;
        topY = y + 12 - TOP_PROPELLER[0].getHeight() / 2;
        sideX = x + 15 - SIDE_PROPELLER[0].getWidth() / 2;
        sideY = y + 115 - SIDE_PROPELLER[0].getHeight() / 2;
        startGameRect.x = x + 270;
        startGameRect.y = y + 310;
        quitGameRect.x = x + 675;
        quitGameRect.y = y + 175;
        p1Rect.x = x + 290;
        p1Rect.y = y + 242;
        p2Rect.x = x + 290 + playerSeparation;
        p2Rect.y = y + 242;
        p3Rect.x = x + 290 + 2 * playerSeparation;
        p3Rect.y = y + 242;
    }

    public void draw(Graphics g)
    {
        //Draws the helicopter body and propellers
        g.drawImage(BODY, x, y, null);
        g.drawImage(SIDE_PROPELLER[sideFrame], sideX, sideY, null);
        g.drawImage(TOP_PROPELLER[topFrame], topX, topY, null);



        //Highlights which area the mouse is hovering over (can be none of them or 1 of them but not more than that)
        if (overStartGame)
        {   /*
                Highlights the space onscreen occupied by the Start Game button
                Only highlights when the mouse is hovering over it
            */
            g.setColor(new Color(255, 255, 255, 123));
            g.fillRect(startGameRect.x, startGameRect.y, startGameRect.width, startGameRect.height);
        }
        else if (overQuitGame)
        {   /*
                The window is oddly shaped so an image is used to apply alpha rather than a simple shape
            */
            g.drawImage(COCKPIT_ALPHA, quitGameRect.x, quitGameRect.y, null);
        }
        else if (overP1 && !hasP1)
        {   /*
                The Player is oddly shaped so an image is used to apply alpha rather than a simple shape
            */
            g.drawImage(PLAYER_ALPHA, p1Rect.x, p1Rect.y, null);
        }
        else if (overP2 && !hasP2)
        {   /*
                The Player is oddly shaped so an image is used to apply alpha rather than a simple shape
            */
            g.drawImage(PLAYER_ALPHA, p2Rect.x, p2Rect.y, null);
        }
        else if (overP3 && !hasP3)
        {   /*
                The Player is oddly shaped so an image is used to apply alpha rather than a simple shape
            */
            g.drawImage(PLAYER_ALPHA, p3Rect.x, p3Rect.y, null);
        }

        //Draws the players
        for (Player p : players)
        {
            try {
                p.draw(g, -x, -y, false);                  //Error occurs if one of the Players has not been added to the game yet
            } catch (NullPointerException e) {}
        }

    }
}
