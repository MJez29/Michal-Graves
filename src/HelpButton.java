//Michal Jez
//13/06/2016
//May be added at a future date
//Currently unused

import java.awt.*;

public class HelpButton extends Rectangle
{
    protected boolean pressedOnButton;
    protected boolean wasPressed;

    protected Font font;

    protected int textX;

    public HelpButton(int nx, int ny)
    {
        super(nx, ny - 50, 50, 50);
        font = new Font(Font.DIALOG, Font.BOLD, 50);
    }

    public void setBottomY(int by)
    {   /*
            X-value will always remain constant but Y-value may change depending on the size of the screen
            42 is the answer to the universe... turns out its also the magic constant to make the help button line up properly
            Coincidence?
            I think not.
        */
        y = by - height - 42;
    }

    public MenuPopup.Option checkMouse(int mx, int my, boolean isPressed)
    {   /*
            Does various things based on different values of variables related to the mouse
            - If pressed while on the button, depending on where they release the mouse they game may start
            - If the mouse is out of bounds and the mouse is up the popup goes away
        */
        if (isPressed)
        {
            if (!wasPressed)
            {
                pressedOnButton = contains(mx, my);
            }
        }
        else
        {
            if (contains(mx, my) && wasPressed && pressedOnButton)
            {   /*
                    If the mouse was pressed down on the button and it was just released inside the start game rectangle
                */
                return MenuPopup.Option.HELP;
            }
            pressedOnButton = false;
        }
        wasPressed = isPressed;
        return MenuPopup.Option.NULL;         //Do nothing out of the ordinary
    }

    public void draw(Graphics g)
    {   /*
            Draws the help button
        */
        g.setColor(pressedOnButton ? Color.BLACK : Color.RED);
        g.setFont(font);
        if (textX == 0)
        {
            FontMetrics fm = g.getFontMetrics();
            textX = x + width / 2 - fm.stringWidth("?") / 2;
        }
        g.drawString("?", textX, y + height);
    }
}
