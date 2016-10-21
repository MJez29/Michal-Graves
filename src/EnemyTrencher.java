//Michal Jez
//01/06/2016
//This is the class of the Enemy that comes out of trenches
//They run out of the trench and then runs blindly in the direction of the Player

import java.awt.*;
import java.util.LinkedList;

public class EnemyTrencher extends EnemyFootSoldier
{
    public static final int UPDATE_RATE = 200;          //How often the Enemy updates
    public static final int SCAN_DEPTH = 5;             //How far away the Enemy can see

    public static final int WIDTH = 50;
    public static final int HEIGHT = 75;

    public static final int VX = 15;

    protected Player target;

    protected Rectangle trenchRect;                     //The rectangle that the trench occupies

    protected static final int EXIT_TRENCH_VY = -30;

    protected int curDepth;

    public EnemyTrencher(int nx, int ny, Player t, Rectangle tr)
    {   //Creates the enemy
        super(nx, ny, WIDTH, HEIGHT, UPDATE_RATE);
        target = t;
        vx = -VX;
        state = EnemyState.EXITING_TRENCH;
        trenchRect = tr;
        dir = Direction.LEFT;

        //Sets the current sprites
        curBottomSprites = BOTTOM_WALKING_LEFT;
        curTopSprites = TOP_IDLE_LEFT;
        curTopFrame = curBottomFrame = 0.0;
        topSprite = curTopSprites[0];
        bottomSprite = curBottomSprites[0];

        //The EnemyTrenchers can shoot once every 1.6s
        framesBetweenShots = 8;
        curShotFrame = 0;

        //Since they are made while the game is running they have to start updating immediately
        start();
    }

    @Override
    public void update()
    {
        //If the enemy is outside of the area onscreen there is no need to waste time on updating their AI (and also their sprites)
        if (!super.shouldUpdate()) { updateSprite = false; return; }
        else updateSprite = true;

        switch (state)
        {   //The Enemy behaves differently depending on which state they are in
            case EXITING_TRENCH:
            {   //When exiting the trench the Enemy follows a predetermined path
                //Exiting is their sole objective so they are not concerned with the Players at all

                int leftX = (lx + vx) / GameMap.BLOCK_WIDTH;
                /*
                    +-+ +-+
                    |1| |0|
                    +-+ +-+
                    |2| +---+
                    +-+ |En-|
                    |3| |emy|
                    +-+ +---+
                    |4|
                    +-+
                 */
                boolean block0 = gm.getAt(lx / GameMap.BLOCK_WIDTH, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canJumpThrough();
                boolean block1 = gm.getAt(leftX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).isAir();
                boolean block2 = gm.getAt(leftX, ty / GameMap.BLOCK_HEIGHT).isAir();
                boolean block3 = gm.getAt(leftX, by / GameMap.BLOCK_HEIGHT).isAir();
                boolean block4 = gm.getAt(leftX, (by + GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).isTrenchBlock();

                if (!block2 && block0)
                {   /*
                        If they are facing a block at head level and there is no block overhead
                        They do a super jump to exit the trench
                    */
                    setLx(lx / GameMap.BLOCK_WIDTH * GameMap.BLOCK_WIDTH);          //Positions them to fit through the hole overhead
                    exitTrenchJump();
                    //dir = (lx - target.getIntX() > 0) ? Direction.LEFT : Direction.RIGHT;
                    vx = 0;
                    //state = EnemyState.ALERT;
                }
                else if (!block3 && block2 && block1 && block0)
                {   /*
                        If they cannot walk to the left because a block is impeding their way
                        They will jump over it
                    */
                    jump();
                    vx = -VX;
                }
                else
                {
                    dir = (lx - target.getIntX() > 0) ? Direction.LEFT : Direction.RIGHT;
                    orientBottomSprites();
                    orientTopSprites();
                }
                if (!trenchRect.contains(this)) {
                    state = EnemyState.ALERT;

                }
                moveX(vx);
                break;
            }
            case ALERT:
            {   /*
                    In this state the EnemyCommander alerts them where the Player is and they run blindly towards that direction firing
                    As often as they can (their fire rate is low)
                */

                //If the enemy can shoot they shoot
                curShotFrame++;
                shoot();

                if (isJumping)
                {   /*
                        If the enemy is jumping they try to continue to move in their intended direction
                        Different places must be checked for collisions with blocks when compared to them not jumping
                    */
                    int nextX;
                    switch (dir)
                    {   /*
                            Calculates the next place that the Enemy wants to move to based on their current position and direction
                        */
                        case LEFT: {
                            nextX = (lx - VX) / GameMap.BLOCK_WIDTH;
                            if (checkTopLeftCorner(gm) && checkBottomLeftCorner(gm) && gm.getAt(nextX, (ty + height / 2) / GameMap.BLOCK_HEIGHT).canJumpThrough())
                                vx = -VX;
                            else vx = 0;
                            break;
                        }
                        default:            //Really is only going to be (case RIGHT:)
                        {
                            nextX = (rx + VX) / GameMap.BLOCK_WIDTH;
                            if (checkTopRightCorner(gm) && checkBottomRightCorner(gm) && gm.getAt(nextX, (ty + height / 2) / GameMap.BLOCK_HEIGHT).canJumpThrough())
                                vx = VX;
                            else vx = 0;
                            break;
                        }
                    }
                }
                else
                {
                    int nextX, curX;
                    switch (dir) {   /*
                        Calculates the next place that the Enemy wants to move to based on their current position and direction
                    */
                        case LEFT: {
                            vx = -VX;
                            nextX = (lx + vx) / GameMap.BLOCK_WIDTH;
                            curX = lx / GameMap.BLOCK_WIDTH;

                            break;
                        }
                        default:            //Really is only going to be (case RIGHT:)
                        {
                            vx = VX;
                            nextX = (rx + vx) / GameMap.BLOCK_WIDTH;
                            curX = rx / GameMap.BLOCK_WIDTH;
                            break;
                        }
                    }
                /*
                    Dir :   LEFT      RIGHT
                            +-+  +-+  +-+
                            |1|  |0|  |1|
                            +-+  +-+  +-+
                            |2| +---+ |2|
                            +-+ |En-| +-+
                            |3| |emy| |3|
                            +-+ +---+ +-+
                            |4|       |4|
                            +-+       +-+
                 */
                    boolean block0 = gm.getAt(curX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canJumpThrough();
                    boolean block1 = gm.getAt(nextX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).isAir();
                    boolean block2 = gm.getAt(nextX, ty / GameMap.BLOCK_HEIGHT).isAir();
                    boolean block3 = gm.getAt(nextX, by / GameMap.BLOCK_HEIGHT).isAir();
                    boolean block4 = gm.getAt(nextX, (by + GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).isAir();

                    if (block3) {   //Air at block3
                        if (block2) {   //Air at block2 and block3
                            //Nothing changes, they move in their desired direction
                        } else {   //Air at block3, block at block2
                            //Cannot go any further
                            startScanning();
                            vx = -vx;
                        }
                    } else {   //Block at block3
                        if (block2) {   //Air at block2, block at block3
                            if (block0 && block1) {   //Air at block0-2, block at block3
                                jump();
                            } else {   //Cannot go any further
                                vx = -vx;
                                startScanning();
                            }
                        } else {   //Block at block2,3
                            //Cannot go any further
                            startScanning();
                            vx = -vx;
                        }
                    }
                }
                moveX(vx);
                break;
            }
            case SCANNING:
            {   /*
                    In the scanning state the enemy does a very primitive search of their surroundings
                    What works in their favour is that they have a very short depth so if a Player is at near them and at the same elevation
                    It is very possible that they will spot the Player in the nick of time and shoot him
                */
                int len = curScanBlocks.size();             //How many items are in the LinkedList, as the for loop loops it will add new items at the back
                LinkedList<Player> players = Player.getPlayers();
                SearchRect sr;
                for (int i = 0; i < len; i++) {             //Goes through the items that were in the LinkedList before the for loop started iterating
                    sr = curScanBlocks.removeFirst();
                    for (Player p : players) {              //Checks every block to see if it intersects with the player
                        if (sr.intersects(p))               //If it does that means the player has been seen
                        {
                            shoot();
                            state = EnemyState.SHOOTING;
                            return;
                        }
                    }
                    curScanBlocks.addAll(sr.searchNext(gm));

                }
                curDepth++;
                if (curDepth > SCAN_DEPTH || curScanBlocks.isEmpty())
                {   /*
                        If the Enemy has searched to their max depth or they cannot search any further due to blocks being in their way
                        They have a 50% chance of turning around
                        They start scanning again
                    */
                    dir = (Math.random() < 0.5) ? Direction.LEFT : Direction.RIGHT;
                    orientBottomSprites();
                    orientTopSprites();
                    startScanning();
                }
                break;
            }
            case SHOOTING:
            {   /*
                    The enemy shoots blindly in the direction they are facing
                    Turns around occasionally
                */
                if (curShotFrame++ >= framesBetweenShots)
                {
                    //0.1 chance that the enemy will turn around and fire the other way
                    dir = (Math.random() < 0.1) ? (dir == Direction.LEFT ? Direction.RIGHT : Direction.LEFT) : (dir == Direction.LEFT ? Direction.LEFT : Direction.RIGHT);
                    orientBottomSprites();
                    orientTopSprites();
                    shootWithoutCheck();
                    curShotFrame = 0;
                }
                break;
            }
        }
    }

    protected void startScanning()
    {   //Creates the first SearchRect for the enemy
        vx = 0;
        curDepth = 0;
        curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_IDLE_LEFT : BOTTOM_IDLE_RIGHT;
        orientTopSprites();
        state = EnemyState.SCANNING;
        curScanBlocks = new LinkedList<SearchRect> ();
        curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                ty, GameMap.BLOCK_WIDTH, height, ty / GameMap.BLOCK_HEIGHT, Integer.MAX_VALUE, Integer.MAX_VALUE, dir, Direction.NULL));
    }

    @Override
    public void checkPhysics()
    {   /*
            For fluid movements every enemy moves each frame but scans for the players only when their Timer goes off
        */
        if (checkVerticalPhysics())         //If they are falling
        {
            moveY((int) vy);
            vy += ay;
        } else                                //If they are standing on solid ground
        {
            if (vy < 0) vy = 0;         //If their head collided with a block
            else            //If their feet collided with the ground
            {
                vy = 0;
                curFrame = 0.0;
                isJumping = false;
                setBy((by + 1) / GameMap.BLOCK_HEIGHT * GameMap.BLOCK_HEIGHT - 1);
            }
        }
    }

    public void exitTrenchJump()
    {   //The enemy does a super jump to exit the trench

        if (!isJumping)
        {
            vy = EXIT_TRENCH_VY;
            isJumping = true;
        }
    }

    @Override
    public void draw(Graphics g, int smx, int smy)
    {
        super.draw(g, smx, smy);

        Color c = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        g.setColor(c);
        try {
            for (SearchRect sr : curScanBlocks) {
                try {
                    g.fillRect(sr.x - smx, sr.y - smy, sr.width, sr.height);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {}

    }
}