/**
 * @(#)Player.java
 *
 *
 * @author 
 * @version 1.00 2016/4/23
 */

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class Player extends Person {
    private HashMap<String,BufferedImage[]> sprites = new HashMap<String,BufferedImage[]>();
    //HashMap of all sprites
        //Key: sprite animation name Value: array of images of frames
    private String[] spriteNames = {"7dodge","2idle","1inAir","1jumping","2shootSide","2shootUp",
    "1standing","7walking"};
    //Array of the names of the different sprite animations
        //Naming convention: "[Number of frames of Sprite][Sprite Description]"

    private String ch;
    private String name;

    //Loads the character name and all of the sprites of the character in the HashMap
    public Player(int nx, int ny, String cha, int playerNum)
    {
        super(nx, ny);
        ch = cha;
        for (String name: spriteNames){
            int numSprites = Integer.parseInt(name.substring(0,1));
            String fileName = name.substring(1);
            sprites.put(fileName,ImageTools.initializeImages("Sprites\\"+ch+"\\"+fileName,numSprites));
        }
        width = sprites.get("idle")[0].getWidth();
        height = sprites.get("idle")[0].getHeight();
        name = "Player "+playerNum;
    }

    //Returns the player's name
    public String getName(){return name;}

    //Draws the player's appropriate sprites on the screen
    public void draw(SubMap sm){
        chooseTopSprite();
        chooseBottomSprite();
        sm.getGraphics().drawImage(topSprite,x-topSprite.getWidth()/2-sm.getIntX(),y-topSprite.getHeight()/2-sm.getIntY(),null);
        sm.getGraphics().drawImage(bottomSprite,x-bottomSprite.getWidth()/2-sm.getIntX(),y-bottomSprite.getHeight()/2-sm.getIntY(),null);
    }

    //Determines which top sprite of player to use
    public void chooseTopSprite(){
        if (isIdle){
            topSprite = sprites.get("idle")[0];
        }
    }

    //Determines which bottom sprite of players to use
    public void chooseBottomSprite(){
        if (isIdle){
            bottomSprite = sprites.get("standing")[0];
        }
    }
}