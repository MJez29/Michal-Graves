//Michal Jez
//26/05/2016
//EnemyCommanders are spawned in watchtowers
//They patrol from side to side and scan the terrain around them
//If they see a Player they activate the trench that they are linked to

import java.util.*;
import java.awt.*;
import java.awt.image.*;

public class EnemyCommander extends Enemy
{
    //Enemies have a fixed size
    public static final int WIDTH = 48;
    public static final int HEIGHT = 68;

    //Every 250 ms the AI for the EnemyCommander is updated
    public static final int UPDATE_RATE = 250;

    //The greatest distance away that they can see whilst scanning
    public static final int MAXDEPTH = 20;

    //----------------------------------------------------------- Sprites ---------------------------------------------------------------------
    //Every EnemyCommander has the same sprites

    //The sprites of the enemy when they are raising their binoculars
    private static final BufferedImage[] RAISING_SPRITES_RIGHT = ImageTools.initializeImages("Sprites\\lookout\\scanning", 3);
    private static final BufferedImage[] RAISING_SPRITES_LEFT = ImageTools.flipHorizontally(RAISING_SPRITES_RIGHT);

    //The sprites for the enemy while they are lowering their binoculars
    private static final BufferedImage[] LOWERING_SPRITES_LEFT = ImageTools.invertOrder(RAISING_SPRITES_LEFT);
    private static final BufferedImage[] LOWERING_SPRITES_RIGHT = ImageTools.flipHorizontally(LOWERING_SPRITES_LEFT);

    //The sprites of the enemy while they are scanning the terrain
    //In reality 1 sprite but to keep things simple they are still in an array for less code to take care of this special case
    private static final BufferedImage[] SCANNING_SPRITES_LEFT = new BufferedImage[] { RAISING_SPRITES_LEFT[RAISING_SPRITES_LEFT.length - 1] };
    private static final BufferedImage[] SCANNING_SPRITES_RIGHT = new BufferedImage[] { RAISING_SPRITES_RIGHT[RAISING_SPRITES_RIGHT.length - 1] };

    //The sprites while the enemy is walking from one end of their watchtower to the other
    private static final BufferedImage[] WALKING_SPRITES_RIGHT = ImageTools.initializeImages("Sprites\\lookout\\walking", 5);
    private static final BufferedImage[] WALKING_SPRITES_LEFT = ImageTools.flipHorizontally(WALKING_SPRITES_RIGHT);


    protected int curDepth;                         //The current depth away that they are scanning

    protected int leftmostX, rightmostX;            //The furthest to the right and left that the commander can walk to

    protected EnemyState state;                     //The state of the commander

    protected Direction walkDir, scanDir;           //The directions the commander is scanning and walking

    protected Spawner spawner;                      //The spawner that the enemy controls

    public EnemyCommander(int nx, int ny, Spawner s)
    {   //All EnemyCommanders are initialized in GameMap by an identical function call so only 1 constructor is needed
        super(nx, ny - HEIGHT, WIDTH, HEIGHT, UPDATE_RATE);
        spawner = s;

        //The commander starts off scanning to their left and continuing on from there
        state = EnemyState.SCANNING;
        walkDir = Direction.RIGHT;
        scanDir = Direction.LEFT;
        curScanBlocks = new LinkedList<SearchRect> ();
        curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 0, 1, scanDir, Direction.UPANDDOWN));

        vx = 5;

        //The commander's boundaries
        //Replaces the need to always check their vertical physics since it is guaranteed (unless a glitch occurs such as 2 WatchTowers being spawned on top of each other occurs)
        //Every block they can walk on is indestructible
        leftmostX = nx;
        rightmostX = nx + GameMap.BLOCK_WIDTH * 4;

        curDepth = 0;

        //Sets the current sprites for scanning to the left
        curSprites = SCANNING_SPRITES_LEFT;
        curFrame = 0.0;
        curSprite = curSprites[(int) curFrame];
        updateSprite = false;
    }

    /*
    @Override
    public void draw(Graphics g, int smx, int smy)
    {   //Draws the EnemyCommander
        //Used for testing
        //Also draws the SearchRects that they can see this frame
        super.draw(g, smx, smy);

        Color c = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        g.setColor(c);
        try {
            for (SearchRect sr : curScanBlocks) {
                try {
                    g.fillRect(sr.x - smx, sr.y - smy, sr.width, sr.height);
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
    }
    */

    @Override
    public void move()
    {   //Moves the enemy based on which direction they are walking
        switch (walkDir)
        {
            case LEFT:                      //If walking to the left
            {
                moveX(-vx);
                if (lx <= leftmostX)
                {   //If the enemy has reached the end of their platform
                    //They stop walking and start to raise their binoculars so that they can scan
                    walkDir = Direction.RIGHT;                  //Prepares them to walk the other way after scanning
                    state = EnemyState.RAISING_BINOCULARS;
                    scanDir = Direction.LEFT;

                    //Prepares their search for when their binoculars have been raised
                    curScanBlocks = new LinkedList<SearchRect> ();
                    curScanBlocks.add(new SearchRect((lx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                            (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 0, 1, scanDir, Direction.UPANDDOWN));

                    //Selects the correct sprites
                    curSprites = (scanDir == Direction.LEFT) ? RAISING_SPRITES_LEFT : RAISING_SPRITES_RIGHT;
                    curSprite = curSprites[(int) (curFrame = 0.0)];
                }
                break;
            }
            case RIGHT:                     //If walking to the right
            {
                moveX(vx);
                //If the enemy has reached the end of their platform
                //They stop walking and start to raise their binoculars so that they can scan
                if (lx >= rightmostX)        //If reached end of platform
                {
                    walkDir = Direction.LEFT;       //Prepares them to walk the other way after scanning
                    scanDir = Direction.RIGHT;
                    state = EnemyState.RAISING_BINOCULARS;     //Starts scanning

                    //Prepares their search for when their binoculars have been raised
                    curScanBlocks = new LinkedList<SearchRect> ();
                    curScanBlocks.add(new SearchRect((rx / GameMap.BLOCK_WIDTH) * GameMap.BLOCK_WIDTH,
                            (ty / GameMap.BLOCK_HEIGHT) * GameMap.BLOCK_HEIGHT, GameMap.BLOCK_WIDTH, GameMap.BLOCK_HEIGHT, ty / GameMap.BLOCK_HEIGHT, 0, 1, scanDir, Direction.UPANDDOWN));

                    //Selects the correct sprites
                    curSprites = (scanDir == Direction.LEFT) ? RAISING_SPRITES_LEFT : RAISING_SPRITES_RIGHT;
                    curSprite = curSprites[(int) (curFrame = 0.0)];
                }
                break;
            }
        }
    }

    public void update()
    {   //Updates the AI of the enemy
        //If they are walking it moves them
        //If they are scanning they scan further
        //If they are informing they do stuff

        //If the enemy is outside of the area onscreen there is no need to waste time on updating their AI (and also their sprites)
        if (!super.shouldUpdate()) { updateSprite = false; return; }
        else updateSprite = true;

        LinkedList<Player> players = Player.getPlayers();
        switch (state)
        {
            case SCANNING:
            {   //In the scanning state the enemy searches 1 block over from where they were searching the last update
                int len = curScanBlocks.size();             //How many items are in the LinkedList, as the for loop loops it will add new items at the back

                SearchRect sr;

                //Goes through all the SearchRects that are in the LinkedList before the loop begins iterating
                //Gets the new SearchRects based on the ones that are iterated through
                //And adds them to the end of the LinkedList
                for (int i = 0; i < len; i++) {
                    sr = curScanBlocks.removeFirst();                           //Gets the SearchRect
                    for (Player p : players) {                                  //Checks for Player collisions
                        if (sr.intersects(p))
                        {
                            state = EnemyState.INFORMING;                       //If they see a Player they change states and activate their spawner
                            curScanBlocks = null;
                            spawner.activate(p);

                            return;
                        }
                    }
                    curScanBlocks.addAll(sr.searchNext(gm));
                }
                curDepth++;

                //If the enemy has searched to their maximum depth
                //They stop scanning and lower their binoculars to begin to walk over to the other side
                if (curDepth >= MAXDEPTH) {
                    curDepth = 0;
                    scanDir = (scanDir == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
                    state = EnemyState.LOWERING_BINOCULARS;
                    curSprites = (walkDir == Direction.LEFT) ? LOWERING_SPRITES_RIGHT : LOWERING_SPRITES_LEFT;
                    curSprite = curSprites[0];
                    curFrame = 0.0;
                }
                break;
            }
            case WALKING:
            {   //Moves the enemy closer to the end of their WatchTower
                //The enemy can still spot a Player while they are in this state but they have a much shortened depth and searching abilities
                //To keep things simple they can see everything that is 5 blocks away from them in the direction they are facing
                Rectangle viewRect;
                switch (walkDir)
                {   //Adjusts what they can see according to which direction they are travelling
                    case LEFT:
                    {
                        viewRect = new Rectangle(lx - 5 * GameMap.BLOCK_WIDTH, ty, GameMap.BLOCK_WIDTH * 5, height);
                        break;
                    }
                    default:
                    {
                        viewRect = new Rectangle(rx, ty, GameMap.BLOCK_WIDTH * 5, height);
                        break;
                    }
                }

                //Goes through every player and checks for a collision between the viewRect of the Enemy and the Rectangle that the Player takes up
                for (Player p : players)
                {
                    if (p.intersects(viewRect))
                    {
                        state = EnemyState.INFORMING;
                        curScanBlocks = null;
                        spawner.activate(p);
                        return;
                    }
                }
                move();                                     //Moves them forward
                break;
            }
            case INFORMING:
            {
                //Do stuff
                break;
            }
        }
    }

    public void updateSprite()
    {   //Updates which sprite is to be drawn onscreen
        //For a smoother effect it is better to update the sprite of the enemy every frame instead of every AI update, that makes it look a little more
        //Peculiar

        //If they don't have to update the sprite because the enemy is not onscreen why bother
        if (!updateSprite) return;

        //The frame doesn't change every frame but instead changes gradually
        curFrame += 0.05;

        //If the enemy has reached the last frame of their current sprites
        //Either they enter a new state or restart cycling through their current sprites
        if (curFrame >= (double) (curSprites.length))
        {
            switch (state)
            {
                case RAISING_BINOCULARS:
                {   //If they are done raising their binoculars they begin scanning
                    state = EnemyState.SCANNING;
                    curSprites = (scanDir == Direction.LEFT) ? SCANNING_SPRITES_LEFT : SCANNING_SPRITES_RIGHT;
                    break;
                }
                case LOWERING_BINOCULARS:
                {   //If they are done lowering their binoculars they begin walking
                    state = EnemyState.WALKING;
                    curSprites = (walkDir == Direction.LEFT) ? WALKING_SPRITES_LEFT : WALKING_SPRITES_RIGHT;
                    break;
                }
            }
            curFrame = 0.0;                             //Brings them back to the start
        }
        curSprite = curSprites[(int) curFrame];         //Assigns which sprite is to be drawn onscreen this frame
    }



    @Override
    public void kill()
    {   //So that he stops scanning
        super.kill();
        state = EnemyState.DEAD;
    }
}