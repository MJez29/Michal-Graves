//Michal Jez
//08/05/2016
//Boxes are boxes that are randomly placed throughout the map, they have low health

import java.awt.*;
import java.util.*;
import java.awt.image.*;

public class Box extends Block
{
    //Default box image
    public static final BufferedImage BOX = ImageTools.initializeImage("Box Images\\Box.png");

    public static final Color pCol = new Color(152, 108, 49);       //The particle color

    public static final int STARTING_HEALTH = 10;                   //The health that boxes start off with

    public Box(int x, int y)
    {
        super(x, y, "box", BOX, pCol, STARTING_HEALTH);
    }


}
