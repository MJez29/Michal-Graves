/**
 * @(#)Player.java
 *
 *
 * @author
 * @version 1.00 2016/4/23
 */

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

public class Person extends Rectangle
{
    protected int speed;
    protected int vx = 0;
    protected int vy;
    protected int width;
    protected int height;
    protected boolean isJumping;
    protected boolean isShooting;
    protected boolean isIdle = true;
    protected BufferedImage topSprite; //Current top and bottom sprites used by the person
    protected BufferedImage bottomSprite;

    public Person(int nx, int ny)
    {
        super(nx, ny, 20, 20);
    }

    public int getIntX()	{	return x;	}
    public int getIntY()	{	return y;	}
    public int getIntWidth()	{	return width;	}
    public int getIntHeight()	{	return height;	}

    public void setIsShooting(boolean b)	{	isShooting = b;	}
    public void setIsJumping(boolean b)		{	isJumping = b;	}
    public void setIsIdle(boolean b)		{	isIdle= b;	}
    public void setIsMovingLeft(boolean b)	{   vx = b ? -5 : 0;}
    public void setIsMovingRight(boolean b)	{	vx = b ? 5 : 0; }
    public void setIsMovingUp(boolean b)	{	vy = b ? -5 : 0; }
    public void setIsMovingDown(boolean b)	{	vy = b ? 5 : 0; }

    public void move(){
        x += vx;
        y += vy;
    }
}