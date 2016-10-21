//Michal Jez, Shadman Hassan
//17/06/2016
//The base class of every Enemy
//Has methods to check physics, jump, kill, etc...
//Has static methods to add players, kill players and more

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class Enemy extends Person implements ActionListener, Comparable<Enemy>
{
    protected static HashMap<Long, Enemy> enemies;         //All the enemies in the game
    protected static LinkedList<Enemy> cowards;         //The EnemySoldiers in the COWARD alert state
    protected static GameMap gm;            //So that gameMap in game does not need to be passed in every frame
    protected static SubMap sm;             //In the same boat as gm

    protected javax.swing.Timer refreshRate;

    public static final int JUMP_VY = -6;

    protected BufferedImage curSprite;              //The current image of the Enemy
    protected BufferedImage[] curSprites;           //The current sprites that the enemy is cycling through
    protected double curFrame;                      //The index of the current frame
    protected double frameInc;                      //The amount that is added to curFrame each frame

    protected BufferedImage[] walkingFrames;
    protected EnemyState state;

    protected boolean updateSprite;                 //True if the sprite should be updated this frame

    protected int framesBetweenShots;               //The number of frames that must pass for the enemy to be able to shoot
    protected int curShotFrame;                     //The current number of frames that have passed since the last shot

    protected boolean isJumping;

    protected int curDepth;                 //The current distance away the enemy is seeing

    protected LinkedList<SearchRect> curScanBlocks;         //The current blocks the soldier is scanning

    public final long creationTime;                     //For uniqueness, allows

    public Enemy(int nx, int ny, int nw, int nh, int ur)
    {   //ur - Update rate

        super(nx, ny, nw, nh, false);
        refreshRate = new javax.swing.Timer(ur, this);
        bulletCooldown.stop();          //Also calls actionPerformed() to update the enemy which is unwanted
        creationTime = Calendar.getInstance().getTime().getTime();
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
    }

    protected void start()
    {
        refreshRate.start();
    }

    protected void update() {}

    //Returns whether or not to go through the code to update the Enemy based on whether it will be drawn that frame
    public boolean shouldUpdate() { canShoot = true; return sm.getUpdateRect().contains(lx, ty); }

    @Override
    public void draw(Graphics g, int nlx, int nty)
    {   //Draws the enemy
        if (curSprite != null)          //NullPointerExceptions are annoying
        {
            g.drawImage(curSprite, lx - nlx, ty - nty, null);
        } else if (bottomSprite != null && topSprite != null) {
            g.drawImage(topSprite, lx - nlx, ty - nty, null);
            g.drawImage(bottomSprite, lx - nlx, ty - nty, null);
        } else {
            g.setColor(Color.red);
            g.fillRect(lx - nlx, ty - nty, width, height);
        }
    }

    public boolean checkVerticalPhysics()
    {   //Checks to see if the enemy collides with a block above it while jumping or if it is standing on something
        //Return true if they should fall downwards
        if (vy < 0) {
            return checkTopLeftCorner(gm) && checkTopRightCorner(gm);
        } else {
            try {
                return checkBelowBottomLeftCorner(gm) && checkBelowBottomRightCorner(gm);
            } catch (Exception e) {
                kill();
                enemies.remove(this);           //Error occurs if they fall of the edge
                return false;
            }
        }
    }

    public boolean checkHorizontalPhysics()
    {   //Checks to see if the enemy collides with a block on their left or right
        //Returns true if they collide with a block in the direction they are heading
        if (dir == Direction.LEFT)
        {
            return !checkTopLeftCorner(gm) || !checkBottomLeftCorner(gm);
        }
        else
        {
            return !checkTopRightCorner(gm) || !checkBottomRightCorner(gm);
        }
    }

    public void checkPhysics() {}

    public void jump()
    {
        if (!isJumping)
        {
            moveY(-GameMap.BLOCK_HEIGHT * 3 / 4);
            isJumping = true;
            vy = JUMP_VY;
        }
    }

    @Override
    public void kill()
    {
        for (int i  = 0; i < 50; i++)
        {
            Particle.makeBloodParticle(lx, ty, width, height, Direction.NULL);
        }
        state = EnemyState.DEAD;
        refreshRate.stop();
    }

    @Override
    public void shoot()
    {   /*
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
                {
                    Bullet.makeBullet(lx + width / 2, ty + height / 2 - 8, isPlayer, dir, isPlayer);
                }
            }
            curShotFrame = 0;
        }
    }

    public void shootWithoutCheck()
    {   /*
            The enemy shoots a bullet regardless if they have cooled down enough to be able to pass the curShotFrame test
        */
        Bullet.makeBullet(lx + width / 2, ty + height / 2 - 8, isPlayer, dir, isPlayer);
        curShotFrame = 0;
    }

    public int compareTo(Enemy e)
    {
        return 0;
    }

    public void updateSprite() {}

    //-----------------------------------------------------------Static Methods-------------------------------------------------------------------
    //Methods relating to all the enemies in the game

    public static void setEnemies(HashMap<Long, Enemy> e)
    {   //Sets the enemies when the biome is generated
        enemies = e;
        cowards = new LinkedList<Enemy> ();
    }

    public static void startTimers()
    {
        for (Enemy e : enemies.values())
        {
            e.start();
        }
    }

    public static void drawAll()
    {   //Draws all the enemies
        Graphics g = sm.getGraphics();
        int lx = sm.getIntX();
        int ty = sm.getIntY();
        for (Enemy e : enemies.values())
        {
            e.draw(g, lx, ty);
        }
    }

    public static void doAll()
    {   //Update everything that needs to be updated for the enemy
        try {
            for (Enemy e : enemies.values())
            {
                e.checkPhysics();
                e.updateSprite();
            }
        } catch (Exception err) {}            //Error occurs if the enemy is also being changed in actionPerformed()
    }
    /*
    public static void checkPhysicsAll()
    {
        try {
            for (Enemy e : enemies)
            {
                e.checkPhysics();
            }
        } catch (Exception err) {}            //Error occurs if the enemy is also being changed in actionPerformed()
    }
    */
    public static void setGameMap(GameMap g) { gm = g; sm = gm.getSubMap(); }

    public static void addEnemy(Enemy e)
    {
        enemies.put(e.creationTime, e);
    }

    public static HashMap<Long, Enemy> getEnemies() { return enemies; }

    public static void removeEnemy(Enemy e) { enemies.remove(e.creationTime); }

    public static void killAll()
    {   /*
            Called whenever the game moves on from the current biome, gets rid of current Enemies to make new ones
        */
        for (Enemy e : enemies.values())
        {
            e.kill();
        }
        enemies.clear();
    }

    public static void pauseAll()
    {   /*
            Called whenever the game is paused
        */
        for (Enemy e : enemies.values())
        {
            e.refreshRate.stop();
        }
    }
}