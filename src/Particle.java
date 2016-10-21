//Michal Jez, Shadman Hassan
//17/06/2016
//Debris that is shot off when a block is destroyed

import java.awt.*;
import java.util.*;

public class Particle extends FourCornerRect
{
    public static LinkedList<Particle> particles = new LinkedList<Particle>();			//All the particles in the air

    private double vx, vy, ay;			//Velocity and acceleration components of the particle

    public Color col;					//The Color of the particle

    private int size;					//Particles are square, each side has the same size

    public Particle(int sx, int sy, int rx, int ry, Direction dir, Color c)
    {	//Creates the particle object flying in a certain direction based off of where the object was hit
        //Dir - the direction that the object that hit the thing was facing
        //Ex.		dir = UP;	//Means the bullet was travelling up and hit the block from below
        //(sx, sy) - the top left corner of the range that the particle can be spawned in
        //(rx, ry) - the range to the right and down from (sx, sy) that the particle can be spawned

        super((int) (Math.random() * rx) + sx, (int) (Math.random() * ry) + sy, (int) (Math.random() * 10) + 5);
        col = c;
        switch (dir)
        {
            case UP:			//Bullet hit the bottom
                vx = ((int) (Math.random() + 0.5) * 2 - 1) * (int) (Math.random() * 5.0);
                vy = 0;
                break;
            case DOWN:			//Bullet hit the top
                vx = ((int) (Math.random() + 0.5) * 2 - 1) * (int) (Math.random() * 5.0);
                vy = -((int) (Math.random() * 20.0) + 5);
                break;
            case LEFT:			//Bullet hit the right side
                vx = (int) (Math.random() * 4.0) + 1;
                vy = ((int) (Math.random() + 0.5) * 2 - 1) * (int) (Math.random() * 20.0 + 10.0);
                break;
            case RIGHT:			//Bullet hit the left side
                vx = -((int) (Math.random() * 4.0) + 1);
                vy = ((int) (Math.random() + 0.5) * 2 - 1) * (int) (Math.random() * 20.0 + 10.0);
                break;
            default: 			//Location is unknown
                vx = ((int) (Math.random() + 0.5) * 2 - 1) * (int) (Math.random() * 5.0);
                vy = ((int) (Math.random() + 0.5) * 2 - 1) * (int) (Math.random() * 20.0 + 10.0);
                break;
        }
        ay = 1.0;
    }

    public void move()
    {	//Moves the particle according to 2D motion laws
        moveX((int) vx);
        moveY((int) vy);
        vy += ay;
    }

    public boolean checkCollisions(GameMap gm)
    {	//Checks to see if the particle collides with a Block in the GameMap,
        //Returns true if there is a collision
        int tempLX = lx / GameMap.BLOCK_WIDTH;
        int tempRX = rx / GameMap.BLOCK_WIDTH;				//Saves 1 operation apiece
        int tempTY = ty / GameMap.BLOCK_HEIGHT;				//But wasted 5 minutes of my time
        int tempBY = by / GameMap.BLOCK_HEIGHT;

        return (tempLX < 0 || tempRX > GameMap.BIOME_WIDTH - 1 ||          //If the particle is out of bounds
                tempTY < 0 || tempBY > GameMap.BIOME_HEIGHT - 1 ||
                !"air".equals(gm.getAt(tempLX, tempTY).getType()) ||
                !"air".equals(gm.getAt(tempLX, tempBY).getType()) ||				        //If any of the 4 corners collides with a block
                !"air".equals(gm.getAt(tempRX, tempTY).getType()) ||               //In the GameMap
                !"air".equals(gm.getAt(tempRX, tempBY).getType()));
    }

    public void draw(SubMap sm)
    {	//Draws the particle onto the current SubMap
        Graphics g = sm.getGraphics();
        g.setColor(col);
        g.fillRect(lx - sm.getIntX(), ty - sm.getIntY(), width, height);
    }

    //--------------------------------------------------Static Methods--------------------------------------------------------
    //These methods loop through every Particle in particles

    public static void moveAll()
    {	//Moves every particle
        for (Particle p : particles)
        {
            p.move();
        }
    }

    public static void checkCollisionsAll(GameMap gm)
    {	//Goes though every particle and checks for collisions
        Block[][] biome = gm.getBiome();
        Iterator it = particles.iterator();
        while (it.hasNext())					//Uses an iterator because elements can be deleted
        {
            Particle p = (Particle) it.next();
            if (p.checkCollisions(gm))		//If the particle collides with a block
            {
                it.remove();					//Delete the particle
            }
        }
    }

    public static void drawAll(SubMap sm)
    {
        for (Particle p : particles)
        {
            p.draw(sm);
        }
    }

    public static void makeParticle(int x, int y, Direction d, Color c)
    {   //Makes a regular particle
        particles.add(new Particle(x, y, GameMap.BLOCK_WIDTH, GameMap.BIOME_HEIGHT, d, c));
    }

    public static void makeBloodParticle(int x, int y, int w, int h, Direction d)
    {   //Makes a blood particle
        particles.add(new BloodParticle(x, y, w, h, d));
    }

    public static void clearParticles()
    {   /*
            Called whenever the current biome is done
            The reason for this method is because all enemies are called using kill() which also makes new particles at the same time
        */
        particles.clear();
    }
}