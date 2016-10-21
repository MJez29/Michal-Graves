/* Person.java

   This class is a parent class of all objects similar in function to people in the game. It has basic fields such as
   flags for where they are moving, if they are doing a specific action or not, what direction they are facing, etc.
   It extends the FourCornerRect class.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

public class Person extends FourCornerRect implements ActionListener
{
    protected int vx;
    protected double vy;
    public static final double ay = 1.5;
    protected boolean jumping = false; //Flags to check status of certain keys
    protected boolean evading = false;
    protected boolean shooting = false;
    protected boolean movingLeft = false;
    protected boolean movingRight = false;
    protected boolean lookingUp = false;
    protected boolean lookingDown = false;
    protected boolean movingUp = false;
    protected boolean movingDown = false;
    protected PlayerState state = PlayerState.GROUND; //State player is in
    protected BufferedImage topSprite; //Current top and bottom sprites used by the person
    protected BufferedImage bottomSprite;
    protected boolean canShoot;             //True when their gun has cooled enough so that they can shoot another bullet
    protected boolean isPlayer;
    protected boolean invincible = false; //Flag for if the player is unaffected by damage at the time
    protected Timer bulletCooldown;

    protected Direction dir = Direction.RIGHT; //Direction person is shooting
    protected Direction lastDir = Direction.RIGHT; //Last direction person was moving
    protected Direction facingDir = Direction.RIGHT; //Direction person is facing (left or right only)

    public Person(int nx, int ny, int w, int h, boolean b)
    {
        super(nx, ny, w, h);
        isPlayer = b;
        bulletCooldown = new Timer(200, this);
        bulletCooldown.start();
    }

    public int getIntX()	{	return x;	}
    public int getIntY()	{	return y;	}
    public int getIntWidth()	{	return width;	}
    public int getIntHeight()	{	return height;	}

    //Moves the player based on their velocities
    public void move(){
        x += vx;
        y += vy;
    }

    //Creates a bullet shot by the player
    public void shoot()
    {
        if (canShoot) {
            Bullet.makeBullet(x, y, isPlayer, dir, isPlayer);
        }
        canShoot = false;
    }

    public void draw(Graphics g, int lx, int ty) {}

    public void kill() {}           //To be overriden, called when the Person gets killed

    public void actionPerformed(ActionEvent e)
    {
        canShoot = true;
    }
}