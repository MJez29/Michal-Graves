//Michal Jez
//18/05/2016
//This class represents the text that appears over an enemy's head when they see the player

import java.awt.*;
import java.util.*;

public class EnemyExclamation
{
    protected static HashMap<Integer, EnemyExclamation> exclamations = new HashMap<Integer, EnemyExclamation> ();

    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 125);

    private static final Color COL = new Color(255, 0, 0);

    protected int lx, by;

    protected String text;

    public EnemyExclamation(int nlx, int nby, String str)
    {
        lx = nlx;
        by = nby;
        text = str;
    }

    public static void addExclamation(EnemyExclamation e)
    {
        exclamations.put(e.lx, e);
    }

    public static void drawAll(SubMap sm)
    {
        Graphics g = sm.getGraphics();
        int x = sm.getIntX();
        int y = sm.getIntY();
        g.setFont(FONT);
        g.setColor(COL);
        for (EnemyExclamation e : exclamations.values())
        {
            g.drawString(e.text, e.lx - x, e.by - y);
        }
    }
}
