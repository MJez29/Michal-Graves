//Every bullet shot by a person in the game is stored as a Bullet object
//This class provides functionality to move the bullet, draw it and check for
//Collisions

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.io.*;
import java.util.Scanner;
import java.awt.image.*;
import javax.imageio.*;

public class Bullet
{
	public static final BufferedImage BULLET = ImageTools.initializeImage("Bullet Images\\Bullet.png");
	
	private int lx, rx;				//The x-coords of the vertical edges of the bullet
	private int ty, by;				//The y-coords of the horizontal edges of the bullet
	
	//Direction dir;

	private int v;					//The velocity of the bullet
	
	private BufferedImage img;		//The image of the current bullet object
	
    public Bullet(int nlx, int nly) 
    {
    	lx = nlx;							ty = nly;
    	rx = lx + BULLET.getWidth() - 1;	by = ty + BULLET.getHeight() - 1;    	
    }
    
    /*public void draw(SubMap sm)
    {
    	sm.getGraphics().drawImage(BULLET, lx - sm.getX(), ly - sm.getY(), null);
    }*/
    
    
}