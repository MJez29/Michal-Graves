//Michal Jez
//17/05/2016
//EnemySoldiers are soldiers that are found randomly around the map
//They walk around until they find higher ground
//They occasionally scan surrounding terrain for Players and have a variety of ways to engage the Player upon sight

import java.awt.image.*;
import java.util.*;
import java.awt.*;

public class EnemySoldier extends EnemyFootSoldier
{
    public static final int SCAN_DEPTH = 8;             //When scanning this is how far away they can see
    public static final int CAUTIOUS_DEPTH = 15;          //When they are in the CAUTIOUS ALERT state they scan more vigourously

    protected static final int FRAMES_BETWEEN_SHOTS = 10;               //The number of frames that must pass for the enemy to be able to shoot

    public static final int UPDATE_RATE = 200;

    public static final int NORMAL_VX = 10;
    public static final int COWARD_VX = 25;
    public static final int KAMIKAZE_VX = 20;

    protected int framesSinceCautious;          //The number of frames since the Enemy has become CAUTIOUS
    protected int framesSinceLastSighting;      //The number of frames since the Enemy has last seen a Player

    protected boolean firstScan;            //When they scan they check both directions, is true if they're scanning the first Direction

    protected Player target;

    protected EnemyAlertState alertState;           //The sub-state of the enemy when he has been alerted by a player

    protected int cowardHoleDepth;           //When a coward starts digging a hole in the dirt they will dig such a deep hole and then just camp there

    public static final int WIDTH = 48;
    public static final int HEIGHT = 68;

    public EnemySoldier(int nx, int ny)
    {
        super(nx, ny - HEIGHT, WIDTH, HEIGHT, UPDATE_RATE);
        //topSearch = new SearchRect(lx, ty, GameMap.BLOCK_WIDTH * 2, GameMap.BLOCK_HEIGHT, ty, true, dir, Direction.NULL);
        //bottomSearch = new SearchRect(lx, ty + GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH * 2, GameMap.BLOCK_HEIGHT, ty, true, dir, Direction.NULL);
        curDepth = 0;

        //Enemy starts facing the left
        dir = Direction.LEFT;
        //curSprites =
        curScanBlocks = new LinkedList<SearchRect>();

        //How often the enemy can shoot
        curShotFrame = 0;
        framesBetweenShots = FRAMES_BETWEEN_SHOTS;

        //Enemy starts off scanning
        state = EnemyState.WALKING;
        vx = NORMAL_VX;

        curTopSprites = TOP_IDLE_LEFT;
        curBottomSprites = BOTTOM_IDLE_LEFT;
    }

    @Override
    public void update()
    {   //Updates the enemy soldier
        if (!super.shouldUpdate()) { updateSprite = false; return; }            //If the Enemy is not onscreen this frame there's in no need to update it
        else updateSprite = true;

        curShotFrame++;
        switch (state) {
            case DEAD:
            {
                break;
            }
            case SCANNING: {
                int len = curScanBlocks.size();             //How many items are in the LinkedList, as the for loop loops it will add new items at the back
                LinkedList<Player> players = Player.getPlayers();
                SearchRect sr;
                for (int i = 0; i < len; i++) {             //Goes through the items that were in the LinkedList before the for loop started iterating
                    sr = curScanBlocks.removeFirst();
                    for (Player p : players) {              //Checks every block to see if it intersects with the player
                        if (sr.intersects(p))               //If it does that means the player has been seen
                        {
                            state = EnemyState.ALERT;
                            target = p;
                            setAlertState();
                            return;
                        }
                    }
                    curScanBlocks.addAll(sr.searchNext(gm));
                }
                curDepth++;
                if (curDepth > SCAN_DEPTH)
                {
                    if (firstScan)          //If they have to scan the other direction
                    {
                        curDepth = 0;
                        dir = (dir == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;                   //Flips direction
                        orientTopSprites();
                        curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_IDLE_LEFT : BOTTOM_IDLE_RIGHT;
                        firstScan = false;
                        curScanBlocks = new LinkedList<SearchRect>();
                        curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                                (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 3, 3, dir, Direction.UPANDDOWN));
                    }
                    else {
                        state = EnemyState.WALKING;
                        curDepth = 0;
                        dir = (Math.random() < 0.5) ? Direction.RIGHT : Direction.LEFT;
                        orientTopSprites();
                        curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_WALKING_LEFT : BOTTOM_WALKING_RIGHT;
                    }
                }
                break;
            }
            case WALKING:
            {
                if ((int) (Math.random() * 25.0) == 0)          //Occasionally while walking they scan their surrounding terrain
                {
                    startScanning();
                    break;
                }
                switch (dir)
                {
                    //If the enemy is walking towards the right
                    case RIGHT:
                    {
                        int rightX = (rx + NORMAL_VX) / GameMap.BLOCK_WIDTH;
                        if (isJumping) {
                            if (checkTopRightCorner(gm) && checkBottomRightCorner(gm) && gm.getAt(rightX, (ty + height / 2) / GameMap.BLOCK_HEIGHT).canJumpThrough()) {
                                vx = NORMAL_VX;
                            }
                        }
                        else {
                        /*
                            This enemy will try to find the highest ground possible and stay there

                                    +---+
                                    | 1 |
                                    +---+
                         +---+      | 2 |
                         |En-|      +---+
                         |emy|      | 3 |
                         +---+      +---+
                                    | 4 |
                                    +---+

                        Checks block3 first
                        If there is a block there
                            Checks block 2
                            If there is a block there
                                Can't go there, this is the rightmost position
                            Else
                                Check block1
                                If there is a block there
                                    Can't go there, this is the rightmost position
                                Else
                                    Jump up to see what's there
                        Else
                            Check block4
                            If there is a block there
                                Enemy can move straight to the left
                        */

                            //Checks block3
                            if (!gm.getAt(rightX, by / GameMap.BLOCK_HEIGHT).canShootThrough()) {          //Block at block3
                                //Checks block2
                                if (!gm.getAt(rightX, ty / GameMap.BLOCK_HEIGHT).canShootThrough()) {      //Block at block2
                                    dir = Direction.LEFT;          //Gone as high as they can go
                                    startScanning();               //Scans the terrain below
                                } else {          //No block at block2
                                    //Checks block1
                                    if (!gm.getAt(rightX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canJumpThrough()) {            //Block at block1
                                        dir = Direction.LEFT;
                                        startScanning();
                                    } else {
                                        vx = (NORMAL_VX);         //Enemy goes up a block
                                        jump();
                                    }
                                }
                            }
                            else {          //No block at block3
                                if (!gm.getAt(rightX, (by + GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canShootThrough() &&          //Block at block4
                                        gm.getAt(rightX, ty / GameMap.BLOCK_HEIGHT).canJumpThrough()) {          //No block at block3
                                    vx = (NORMAL_VX);
                                }
                                else {
                                    startScanning();
                                }
                            }
                        }
                        if (rightX >= GameMap.BIOME_WIDTH - 2) { vx = 0; startScanning(); }
                        break;
                    }



                    //If the enemy is walking towards the left
                    case LEFT:
                    {
                        int leftX = (lx - NORMAL_VX) / GameMap.BLOCK_WIDTH;

                        if (isJumping) {
                            if (checkTopLeftCorner(gm) && checkBottomLeftCorner(gm) && gm.getAt(leftX, (ty + height / 2) / GameMap.BLOCK_HEIGHT).canJumpThrough()) {
                                vx = -NORMAL_VX;
                            }
                        }
                        else {
                        /*
                            This enemy will try to find the highest ground possible and stay there

                                +---+
                                | 1 |
                                +---+
                                | 2 |     +---+
                                +---+     |En-|
                                | 3 |     |emy|
                                +---+     +---+
                                | 4 |
                                +---+

                        Checks block3 first
                        If there is a block there
                            Checks block 2
                            If there is a block there
                                Can't go there, this is the leftmost position
                            Else
                                Check block1
                                If there is a block there
                                    Can't go there, this is the leftmost position
                                Else
                                    Jump up to see what's there
                        Else
                            Check block4
                            If there is a block there
                                Enemy can move straight to the left
                        */

                            //Checks block3
                            if (!gm.getAt(leftX, by / GameMap.BLOCK_HEIGHT).canShootThrough()) {          //Block at block3
                                //Checks block2
                                if (!gm.getAt(leftX, ty / GameMap.BLOCK_HEIGHT).canShootThrough()) {      //Block at block2
                                    dir = Direction.RIGHT;          //Gone as high as they can go
                                    startScanning();                //Scans the terrain below
                                } else {          //No block at block2
                                    //Checks block1
                                    if (!gm.getAt(leftX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canJumpThrough()) {            //Block at block1
                                        dir = Direction.RIGHT;
                                        startScanning();
                                    } else {
                                        vx = (-NORMAL_VX);         //Enemy goes up a block
                                        jump();
                                    }
                                }
                            }
                            else {          //No block at block3
                                try {
                                    if (!gm.getAt(leftX, (by + GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canShootThrough() &&          //Block at block4
                                            gm.getAt(leftX, ty / GameMap.BLOCK_HEIGHT).canJumpThrough()) {          //No block at block3
                                        vx = (-NORMAL_VX);
                                    } else {
                                        startScanning();
                                    }
                                } catch (Exception e) { startScanning(); }
                            }
                        }
                        if (leftX <= 1) { vx = 0; startScanning(); }
                        break;
                    }
                }
                orientTopSprites();
                curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_WALKING_LEFT : BOTTOM_WALKING_RIGHT;
                break;
            }
            case ALERT:
            {
                //if (alertState == null) alertState = EnemyAlertState.KAMIKAZE;
                //if (target == null) return;
                switch (alertState)
                {
                    case KAMIKAZE:          //If they will basically charge the player and will most likely result in their death
                    {
                        switch (dir)
                        {
                            case LEFT:          //If the enemy is aiming left
                            {
                                int dx = lx - target.getIntX();         //The x-dist between the enemy and their target
                                if (Math.abs(dx) > 800)         //If the target is too far away they lose sight of it
                                {
                                    startScanning();
                                    return;
                                }
                                if (dx > 75)         //If they are too far to the right
                                {
                                    int leftX = (lx - KAMIKAZE_VX) / GameMap.BLOCK_WIDTH;
                                    vx = (gm.getAt(leftX, by / GameMap.BLOCK_HEIGHT).canShootThrough() && gm.getAt(leftX, ty / GameMap.BLOCK_HEIGHT).canShootThrough()) ?
                                            -KAMIKAZE_VX : 0;           //Moves the Enemy closer if they can
                                }
                                else if (dx > 0)             //If they are facing the wrong way
                                {
                                    int rightX = (rx + KAMIKAZE_VX) / GameMap.BLOCK_WIDTH;
                                    vx = (gm.getAt(rightX, by / GameMap.BLOCK_HEIGHT).canShootThrough() && gm.getAt(rightX, ty / GameMap.BLOCK_HEIGHT).canShootThrough()) ?
                                            KAMIKAZE_VX : 0;           //Moves the Enemy back if they can
                                }
                                else
                                {
                                    dir = Direction.RIGHT;
                                    orientTopSprites();
                                    orientBottomSprites();
                                }
                                break;
                            }
                            case RIGHT:          //If the enemy is aiming left
                            {
                                int dx = target.getIntX() - lx;
                                if (Math.abs(dx) > 800)
                                {
                                    startScanning();
                                    return;
                                }
                                if (dx > 75)         //If they are too far to the left
                                {
                                    int rightX = (rx + KAMIKAZE_VX) / GameMap.BLOCK_WIDTH;
                                    vx = (gm.getAt(rightX, by / GameMap.BLOCK_HEIGHT).canShootThrough() && gm.getAt(rightX, ty / GameMap.BLOCK_HEIGHT).canShootThrough()) ?
                                            KAMIKAZE_VX : 0;           //Moves the Enemy closer if they can
                                }
                                else if (dx > 0)        //If they are facing the wrong way
                                {
                                    int leftX = lx / GameMap.BLOCK_WIDTH;
                                    vx = (gm.getAt(leftX, by / GameMap.BLOCK_HEIGHT).canShootThrough() && gm.getAt(leftX, ty / GameMap.BLOCK_HEIGHT).canShootThrough()) ?
                                            -KAMIKAZE_VX : 0;           //Moves the Enemy back if they can
                                }
                                else
                                {
                                    dir = Direction.LEFT;
                                    orientTopSprites();
                                    orientBottomSprites();
                                }
                                break;
                            }

                        }
                        int dy = ty - target.getIntY();
                        if (dy < 75)           //If their y-dist from the player is not too far
                        {
                            if (dy > 10) jump();            //If Player is too high up, jump to try to reach him
                            /*if (dy < 75)*/ shoot();           //If the player is close enough shoot at him
                        }
                        else if (Math.abs(dy) > 500)            //If the Player's y-dist is too great
                        {
                            startScanning();            //Forget about him
                        }
                        break;
                    }
                    case COWARD:
                    {
                        if (cowardHoleDepth >= SCAN_DEPTH * GameMap.BLOCK_WIDTH / 2) {          //If the coward has dug a deep enough of a hole
                            alertState = EnemyAlertState.CAMPING_COWARD;                        //They start camping in wait for a player to show up
                            dir = (dir == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;   //Face the entrance of the hole
                            curTopSprites = (dir == Direction.LEFT) ? TOP_IDLE_LEFT : TOP_IDLE_RIGHT;
                            curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_IDLE_LEFT : BOTTOM_IDLE_RIGHT;
                            return;
                        }
                        if (target.getIntX() > lx)          //If the Player is to the right of the enemy it will run to the left
                        {
                            dir = Direction.LEFT;
                            orientTopSprites();
                            orientBottomSprites();
                            int leftX = (lx - COWARD_VX) / GameMap.BLOCK_WIDTH;

                            if (isJumping) {
                                if (checkTopLeftCorner(gm) && checkBottomLeftCorner(gm) && gm.getAt(leftX, (ty + height / 2) / GameMap.BLOCK_HEIGHT).canJumpThrough()) {
                                    vx = -COWARD_VX;
                                }
                                else vx = 0;
                                if (vy >= 0) {
                                    shoot();
                                }
                            }
                            else {
                                /*

                                        +-+ +-+
                                        |1| |0|
                                        +-+ +-+
                                        +-+
                                        |2|
                                        +-+         +---+
                                        +-+         |En-|
                                        |3|         |emy|
                                        +-+         +---+
                                        +-+
                                        |4|
                                        +-+
                                 */
                                //True if there is a block at this location
                                boolean block0 = !gm.getAt(leftX + 1, ty / GameMap.BLOCK_HEIGHT).canJumpThrough();
                                boolean block1 = !gm.getAt(leftX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canJumpThrough();
                                boolean block2 = !gm.getAt(leftX, ty / GameMap.BLOCK_HEIGHT).canShootThrough();
                                boolean block3 = !gm.getAt(leftX, by / GameMap.BLOCK_HEIGHT).canShootThrough();
                                boolean block4 = !gm.getAt(leftX, (by + GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canShootThrough();
                                checkCowardHole(block1, block2, block3, block4);
                                if (block3) {          //Block at block3
                                    //Checks block2
                                    if (block2) {      //Block at block2
                                        vx = 0;
                                        shoot();
                                    } else {          //No block at block2
                                        //Checks block1
                                        if (block1) {            //Block at block1
                                            shoot();
                                            vx = 0;
                                        } else if (!block0) {           //If no block above their head
                                            vx = (-COWARD_VX);         //Enemy goes up a block
                                            jump();
                                        } else {
                                            shoot();
                                            vx = 0;
                                        }
                                    }
                                }
                                else {          //No block at block3
                                    try {
                                        if (!block2) {          //No block at block2
                                            vx = (-COWARD_VX);
                                        } else {
                                            vx = 0;
                                            //The enemy tries to destroy the top block so they wait until they can shoot
                                            if (curShotFrame >= framesBetweenShots) jump();
                                        }
                                    } catch (Exception e) { startScanning(); }
                                }
                            }
                        }
                        else
                        {
                            dir = Direction.RIGHT;
                            orientTopSprites();
                            orientBottomSprites();
                            int rightX = (rx + COWARD_VX) / GameMap.BLOCK_WIDTH;

                            if (isJumping) {
                                if (checkTopRightCorner(gm) && checkBottomRightCorner(gm) && gm.getAt(rightX, (ty + height / 2) / GameMap.BLOCK_HEIGHT).canJumpThrough()) {
                                    vx = -COWARD_VX;
                                }
                                if (vy > -1.5 && vy < 1.5) {
                                    shoot();
                                }
                            }
                            else {
                                /*

                                        +-+
                                        |1|
                                        +-+
                                        +-+
                                        |2|
                              +---+     +-+
                              |En-|     +-+
                              |emy|     |3|
                              +---+     +-+
                                        +-+
                                        |4|
                                        +-+
                                 */
                                boolean block0 = !gm.getAt(rightX - 1, ty / GameMap.BLOCK_HEIGHT).canJumpThrough();
                                boolean block1 = !gm.getAt(rightX, (ty - GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canJumpThrough();
                                boolean block2 = !gm.getAt(rightX, ty / GameMap.BLOCK_HEIGHT).canShootThrough();
                                boolean block3 = !gm.getAt(rightX, by / GameMap.BLOCK_HEIGHT).canShootThrough();
                                boolean block4 = !gm.getAt(rightX, (by + GameMap.BLOCK_HEIGHT) / GameMap.BLOCK_HEIGHT).canShootThrough();
                                checkCowardHole(block1, block2, block3, block4);
                                if (block3) {          //Block at block3
                                    //Checks block2
                                    if (block2) {      //Block at block2
                                        vx = 0;
                                        shoot();
                                    } else {          //No block at block2
                                        //Checks block1
                                        if (block1) {            //Block at block1
                                            shoot();
                                            vx = 0;
                                        } else if (!block0) {
                                            vx = (COWARD_VX);         //Enemy goes up a block
                                            jump();
                                        } else {
                                            shoot();
                                            vx = 0;
                                        }
                                    }
                                }
                                else {          //No block at block3
                                    try {
                                        if (!block2) {          //No block at block2
                                            vx = (COWARD_VX);
                                        } else {
                                            vx = 0;
                                            //The enemy tries to destroy the top block so they wait until they can shoot
                                            if (curShotFrame >= framesBetweenShots) jump();
                                        }
                                    } catch (Exception e) { startScanning(); }
                                }
                            }
                        }
                        break;
                    }
                    case CAMPING_COWARD:            //Camping cowards stand with their backs against the wall of the hole they've dug and wait for a Player to show up
                    {
                        Rectangle canSee = null;            //The rectangle that the enemy can see
                        vx = 0;
                        switch (dir)
                        {
                            case LEFT:
                                canSee = new Rectangle(lx - (SCAN_DEPTH / 2 + 4) * GameMap.BLOCK_WIDTH, ty, (SCAN_DEPTH / 2 + 4) * GameMap.BLOCK_WIDTH, height);
                            default:
                                if (canSee == null)
                                {
                                    canSee = new Rectangle(rx, ty, (SCAN_DEPTH / 2 + 4) * GameMap.BLOCK_WIDTH, height);
                                }
                                LinkedList<Player> players = Player.getPlayers();
                                for (Player p : players)
                                {
                                    if (canSee.intersects(p))           //Checks to see if they can see the Player
                                    {
                                        shoot();
                                        return;
                                    }
                                } break;
                        }
                        break;
                    }
                    case CAUTIOUS:
                    {   //When the enemy is in the CAUTIOUS ALERT state they scan to the left and right and if they see a Player they shoot in their direction
                        if (framesSinceLastSighting < 25) {         //If they spot the Player, they shoot in their direction for 5s
                            shoot();
                        }
                        else if (framesSinceLastSighting < 100) {   //They scan for the Player for 15 more seconds
                            if (curScanBlocks == null) {
                                curScanBlocks = new LinkedList<SearchRect> ();
                                curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                                        (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 0, 7, dir, Direction.UPANDDOWN));
                            }
                            else {
                                int len = curScanBlocks.size();             //How many items are in the LinkedList, as the for loop loops it will add new items at the back
                                LinkedList<Player> players = Player.getPlayers();
                                SearchRect sr;
                                for (int i = 0; i < len; i++) {             //Goes through the items that were in the LinkedList before the for loop started iterating
                                    sr = curScanBlocks.removeFirst();
                                    for (Player p : players) {              //Checks every block to see if it intersects with the player
                                        if (sr.intersects(p))               //If it does that means the player has been seen
                                        {
                                            shoot();
                                            setAlertState();            //Either the enemy will go back into CAUTIOUS mode or they may change to COWARD or KAMIKAZE
                                            return;
                                        }
                                    }
                                    curScanBlocks.addAll(sr.searchNext(gm));

                                }
                                curDepth++;
                                if (curDepth > CAUTIOUS_DEPTH || curScanBlocks.isEmpty())
                                {   //If the Enemy has searched to their max depth or they cannot search any further due to blocks being in their way
                                    //They start scanning again
                                    curDepth = 0;
                                    curScanBlocks = new LinkedList<SearchRect>();
                                    curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                                            (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 0, 7, dir, Direction.UPANDDOWN));
                                }
                            }
                        }
                        else {          //If its been too long since they spot a Player they return to their SCANNING state
                            startScanning();
                        }
                        framesSinceLastSighting++;
                    }
                }
            }
        }
        moveX(vx);
    }

    @Override
    public void checkPhysics()
    {   //For fluid movements every enemy moves each frame but scans for the players only when their Timer goes off
        if (checkVerticalPhysics())         //If they are falling
        {
            moveY((int) vy);
            vy += ay;
        }
        else                                //If they are standing on solid ground
        {
            if (vy < 0) vy = 0;         //If their head collided with a block
            else            //If their feet collided with the ground
            {
                vy = 0;
                isJumping = false;
                setBy((by + 1) / GameMap.BLOCK_HEIGHT * GameMap.BLOCK_HEIGHT - 1);
            }
        }
        if (state == EnemyState.ALERT && alertState == EnemyAlertState.COWARD && isJumping && vy >= 0)
            shoot();
        /*
        if (checkHorizontalPhysics())           //If they collide with a block on their left or right
        {
            switch (state)
            {
                case WALKING:
                {
                    if (dir == Direction.LEFT) setLx(lx / GameMap.BLOCK_WIDTH * GameMap.BLOCK_WIDTH + 5);
                    else setLx(lx / GameMap.BLOCK_WIDTH * GameMap.BLOCK_WIDTH - 5);

                    if (Math.random() >= 0.5)           //Either they will try to jump above their obstacle
                    {
                        jump();
                    }
                    else                                //Or turn around
                    {
                        dir = (dir == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
                    }
                    break;
                }
                case ALERT:
                {
                    if (dir == Direction.LEFT) setLx(lx / GameMap.BLOCK_WIDTH * GameMap.BLOCK_WIDTH + GameMap.BLOCK_WIDTH + 5);
                    else setLx(lx / GameMap.BLOCK_WIDTH * GameMap.BLOCK_WIDTH - 5);
                    jump();
                }
            }
        }
        else
        {*/
            //moveX(vx);
        //}
    }

    protected void checkCowardHole(boolean b1, boolean b2, boolean b3, boolean b4)
    {   //Checks to see if the enemy is digging a hole
        if (b1 && !b2 && !b3 && b4) {
            cowardHoleDepth += COWARD_VX;
        } else if (!b1 || !b4) {
            cowardHoleDepth = 0;
        }
    }

    protected void startScanning()
    {   //Creates the first SearchRect for the enemy
        firstScan = true;
        vx = 0;
        state = EnemyState.SCANNING;
        curTopSprites = (dir == Direction.LEFT) ? TOP_IDLE_LEFT : TOP_IDLE_RIGHT;
        curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_IDLE_LEFT : BOTTOM_IDLE_RIGHT;
        curTopFrame = curBottomFrame = 0.0;
        curScanBlocks = new LinkedList<SearchRect>();
        curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 3, 3, dir, Direction.UPANDDOWN));
    }

    protected void setAlertState()
    {   /*Chooses a random EnemyAlertSta8te that the enemy will be in*/
        int n = (int) (Math.random() * 4);
        switch (n)
        {
            case 0:
                alertState = EnemyAlertState.COWARD;
                cowardHoleDepth = 0;
                cowards.addLast(this);
                break;
            case 1:
                alertState = EnemyAlertState.KAMIKAZE;
                curBottomSprites = (dir == Direction.LEFT) ? BOTTOM_WALKING_LEFT : BOTTOM_WALKING_LEFT;
                break;
            default:
                curScanBlocks = null;
                curDepth = 0;
                framesSinceCautious = 0;
                framesSinceLastSighting = 0;
                alertState = EnemyAlertState.CAUTIOUS;
        }
    }

    @Override
    public void draw(Graphics g, int smx, int smy)
    {
        super.draw(g, smx, smy);
        /*
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
        */
    }
}