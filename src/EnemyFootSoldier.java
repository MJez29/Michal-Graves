//Michal Jez
//16/06/2016
//This is the class of the soldiers that travel on foot in the game
//All of these enemies have the same sprites
//The derived classes are EnemySoldier and EnemyTrencher
//This class provides methods for selecting sprites, shooting, checking physics, etc...

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class EnemyFootSoldier extends Enemy
{
    //-------------------------------------------------------- Sprites -------------------------------------------------------
    //Top sprites
    public static final BufferedImage[] TOP_SHOOTING_RIGHT = ImageTools.initializeImages("Sprites\\soldier\\shoot", 2);
    public static final BufferedImage[] TOP_SHOOTING_LEFT = ImageTools.flipHorizontally(TOP_SHOOTING_RIGHT);

    public static final BufferedImage[] TOP_IDLE_RIGHT = ImageTools.initializeImages("Sprites\\soldier\\idle", 2);
    public static final BufferedImage[] TOP_IDLE_LEFT = ImageTools.flipHorizontally(TOP_IDLE_RIGHT);

    public static final BufferedImage[] BOTTOM_IDLE_RIGHT = ImageTools.initializeImages("Sprites\\soldier\\standing", 2);
    public static final BufferedImage[] BOTTOM_IDLE_LEFT = ImageTools.flipHorizontally(BOTTOM_IDLE_RIGHT);

    public static final BufferedImage[] BOTTOM_WALKING_RIGHT = ImageTools.initializeImages("Sprites\\soldier\\walking", 5);
    public static final BufferedImage[] BOTTOM_WALKING_LEFT = ImageTools.flipHorizontally(BOTTOM_WALKING_RIGHT);

    public static final BufferedImage[] BOTTOM_JUMPING_RIGHT = ImageTools.initializeImages("Sprites\\soldier\\jumping", 2);
    public static final BufferedImage[] BOTTOM_JUMPING_LEFT = ImageTools.flipHorizontally(BOTTOM_JUMPING_RIGHT);

    public static final BufferedImage[] BOTTOM_FALLING_RIGHT = ImageTools.initializeImages("Sprites\\soldier\\falling", 2);
    public static final BufferedImage[] BOTTOM_FALLING_LEFT = ImageTools.flipHorizontally(BOTTOM_FALLING_RIGHT);

    //public static final HashMap<String, HashMap<Direction, BufferedImage[]>> allSprites = initializeAllSprites();

    protected BufferedImage[] curTopSprites, curBottomSprites;

    private boolean inAirLastFrame;                 //True if the enemy was in the air the last frame

    protected double curBottomFrame, curTopFrame;     //The frames of curBottomSprites and curTopSprites respectively

    public EnemyFootSoldier(int nx, int ny, int w, int h, int ur)
    {
        super(nx, ny, w, h, ur);
        curFrame = 0.0;
        updateSprite = false;
        frameInc = 0.05;
        curSprite = null;
    }

    @Override
    public void shoot()
    {
        /*
            The enemy attempts to shoot
        */
        if (curShotFrame >= framesBetweenShots)
        {   /*
                If their gun has cooled down enough they can shoot
                How often they shoot varies from enemy type
            */
            switch (dir)
            {
                case LEFT:case RIGHT:
                    Bullet.makeBullet(lx + width / 2, ty + height / 2 - 8, isPlayer, dir, isPlayer);
                    curTopSprites = (dir == Direction.LEFT) ? TOP_SHOOTING_LEFT : TOP_SHOOTING_RIGHT;
                    curTopFrame = 0.0;
                    break;
            }
            curShotFrame = 0;
        }
    }

    @Override
    public void shootWithoutCheck()
    {   /*
            The enemy shoots a bullet regardless if they have cooled down enough to be able to pass the curShotFrame test
        */
        Bullet.makeBullet(lx + width / 2, ty + height / 2 - 8, isPlayer, dir, isPlayer);
        curTopSprites = (dir == Direction.LEFT) ? TOP_SHOOTING_LEFT : TOP_SHOOTING_RIGHT;
        curTopFrame = 0.0;
        curShotFrame = 0;
    }

    @Override
    public void updateSprite()
    {
        if (!updateSprite) return;
        curTopFrame += frameInc;
        curBottomFrame += frameInc;

        updateBottomSprite();
        updateTopSprite();
    }

    private void updateBottomSprite()
    {   /*
            Chooses which bottom sprite is to be drawn
            May differ from what is supposed to be drawn if the Enemy is jumping or falling
        */
        if (vy < 0)                 //If the Enemy is jumping
        {
            if (curBottomFrame >= 2.0)            //Once they have cycled through both frames they stay in the last frame until their velocity changes hit the ground
                curBottomFrame = 1.0;
            bottomSprite = ((dir == Direction.LEFT) ? BOTTOM_JUMPING_LEFT : BOTTOM_JUMPING_RIGHT) [(int) curBottomFrame];
        }
        else if (vy > 0)                    //If the Enemy is falling
        {
            if (curBottomFrame >= 2.0)            //Once they have cycled through both frames they stay in the last frame until they hit the ground
                curBottomFrame = 1.0;
            bottomSprite = ((dir == Direction.RIGHT) ? BOTTOM_FALLING_RIGHT : BOTTOM_FALLING_LEFT) [(int) curBottomFrame];
        }
        else                    //If the enemy is on flat ground
        {
            if ((int) curBottomFrame >= curBottomSprites.length)                  //If curFrame is outside the range of the image indexes
            {
                curBottomFrame = 0.0;
            }
            bottomSprite = curBottomSprites[(int) curBottomFrame];
        }
    }

    public void updateTopSprite()
    {
        if ((curTopSprites == TOP_SHOOTING_LEFT || curTopSprites == TOP_SHOOTING_RIGHT) && (int) curTopFrame >= curTopSprites.length)
        {
            curTopSprites = (dir == Direction.LEFT) ? TOP_IDLE_LEFT : TOP_IDLE_RIGHT;
            curTopFrame = 0.0;
        }
        else if ((curTopSprites == TOP_IDLE_LEFT || curTopSprites == TOP_IDLE_RIGHT) && (int) curTopFrame >= curTopSprites.length)
        {
            curTopFrame = 0.0;
        }
        topSprite = curTopSprites[(int) curTopFrame];
    }

    public void orientTopSprites()
    {   /*
            Orients the top sprites so that they are facing the right way
        */
        if (curTopSprites == TOP_IDLE_LEFT || curTopSprites == TOP_IDLE_RIGHT)
        {
            curTopSprites = (dir == Direction.LEFT) ? TOP_IDLE_LEFT : TOP_IDLE_RIGHT;
        }
        else
        {
            curTopSprites = (dir == Direction.LEFT) ? TOP_SHOOTING_LEFT : TOP_SHOOTING_RIGHT;
        }
    }

    public void orientBottomSprites()
    {
        if (curBottomSprites == BOTTOM_IDLE_LEFT || curBottomSprites == BOTTOM_IDLE_RIGHT)
        {
            curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_IDLE_LEFT : BOTTOM_IDLE_RIGHT;
        }
        else if (curBottomSprites == BOTTOM_WALKING_LEFT || curBottomSprites == BOTTOM_WALKING_RIGHT)
        {
            curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_WALKING_LEFT : BOTTOM_WALKING_RIGHT;
        }
    }
    /*
    private static void initializeAllSprites()
    {
        String[] fileNames = { "shoot", "standing", "falling", "jumping", "shooting", }
        allSprites.put("Top Idle", new HashMap<Direction, BufferedImage[]> ())
    }
    */
}
