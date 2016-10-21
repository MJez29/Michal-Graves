/*Player.java

  This class is used to create players. It has fields that store the different sprites of the specific player, the
  player's current top and bottom sprites and the unique keycodes for the player's different actions. It also handles
  all of the player's different actions (such as shooting, climbing, jumping, etc.) and it's collision and physics as
  well.
 */

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Player extends Person {
    protected static LinkedList<Player> players = new LinkedList<Player> ();

    private HashMap<String,BufferedImage[]> sprites = new HashMap<String,BufferedImage[]>();
    //HashMap of all sprites
    //Key: sprite animation name Value: array of images of frames
    private String[] spriteNames = {"7dodge","2idle","2falling","2jumping","2shootSide","2shootUp","2shootDown",
            "2standing","7walking","2latch","2latchShootSide","2latchShootUp","2latchShootDown"};
    //Array of the names of the different sprite animations
    //Naming convention: "[Number of frames of Sprite][Sprite Description]"

    private String ch;
    private String name;

    private int shoot,evade,jump,left,right,up,down; //The integers representing the keycodes used
    //for each player action

    private double topIndex,bottomIndex; //Counters used as indices of the correct top/bottom sprites in sprite array

    private  double ay = 0.45; //acceleration due to gravity affecting player
    private double jumpVy = -13.5; //y velocity player has when they initiate a jump

    private int playerNum;

    private static Font nameFont = new Font(Font.DIALOG, Font.BOLD, 20);

    private int textX = Integer.MIN_VALUE, textW = 0;                  //What to add to lx to get where the text is supposed to go

    private boolean[] keys = new boolean[KeyEvent.KEY_LAST+1]; //Array of active buttons of keyboard

    //Loads the character name and all of the sprites of the character in the HashMap
    public Player(int nx, int ny, String cha, int pn)
    {
        super(nx, ny, 48, 68, true);

        playerNum = pn;
        vx = 0;
        vy = 0;

        ch = cha;
        for (String name : spriteNames){
            int numSprites = Integer.parseInt(name.substring(0,1));
            String fileName = name.substring(1);
            sprites.put(fileName,ImageTools.initializeImages("Sprites\\"+ch+"\\"+fileName,numSprites));
        }
        width = sprites.get("idle")[0].getWidth();
        height = sprites.get("idle")[0].getHeight();
        name = "Player "+playerNum;

        try {
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("controls" + playerNum + ".txt")));          //IOException

            //Loads correct key configurations
            left = Integer.parseInt(inFile.nextLine());
            right = Integer.parseInt(inFile.nextLine());
            up = Integer.parseInt(inFile.nextLine());
            down = Integer.parseInt(inFile.nextLine());
            shoot = Integer.parseInt(inFile.nextLine());
            jump = Integer.parseInt(inFile.nextLine());
            evade = Integer.parseInt(inFile.nextLine());

            inFile.close();
        } catch (IOException e) {
            System.out.println("Error Loading Player " + playerNum);
        }
        chooseBottomSprite();
        chooseTopSprite();
    }

    public Player(int nlx, int nty, String cha)
    {   /*
            Constructor called when the Player is not actually controllable but just appears as a visual
        */
        super(nlx, nty, 48, 68, true);

        //Loads the Player's sprites
        ch = cha;
        for (String name : spriteNames){
            int numSprites = Integer.parseInt(name.substring(0,1));
            String fileName = name.substring(1);
            sprites.put(fileName,ImageTools.initializeImages("Sprites\\"+ch+"\\"+fileName,numSprites));
        }

        chooseBottomSprite();
        chooseTopSprite();
    }

    //Returns the player's name
    public String getName(){return name;}

    //Getters that are required for the pause menu
    public int getShootKey() { return shoot; }
    public int getUpKey() { return up; }
    public int getDownKey() { return down; }

    //Draws the player's appropriate sprites on the screen
    public void draw(Graphics g, int smx, int smy, boolean withName){
        chooseBottomSprite();
        chooseTopSprite();
        if (facingDir == Direction.LEFT){
            bottomSprite = ImageTools.flipHorizontally(bottomSprite);
            topSprite = ImageTools.flipHorizontally(topSprite);
        }
        g.drawImage(bottomSprite,lx + width/2 - bottomSprite.getWidth()/2 - smx,ty + height/2 - bottomSprite.getHeight()/2 - smy,null);
        g.drawImage(topSprite,lx + width/2 - topSprite.getWidth()/2 - smx,ty + height/2 - topSprite.getHeight()/2 - smy,null);

        if (withName) {
            //Draws the Player's name overhead
            g.setFont(nameFont);
            if (textX == Integer.MIN_VALUE) {
                FontMetrics fm = g.getFontMetrics();
                textW = fm.stringWidth(" P" + Integer.toString(playerNum) + " ");
                textX = 48 / 2 - textW / 2 - 2;
            }
            g.setColor(Color.WHITE);
            g.fillRect(lx + textX - smx, ty - 30 - smy, textW, 25);
            g.setColor(Color.RED);
            g.drawRect(lx + textX - smx, ty - 30 - smy, textW, 25);
            g.drawString(" P" + Integer.toString(playerNum), lx + textX + 2 - smx, ty - 8 - smy);
        }
        /*g.setColor(Color.RED);
        g.drawOval(lx - smx,ty - smy,4,4);
        g.drawOval(rx - smx,ty - smy,4,4);
        g.drawOval(lx - smx,by - smy,4,4);
        g.drawOval(rx - smx,by - smy,4,4);*/
    }

    //Uses the input key array to figure out how the player should move
    public void move(boolean [] keyArray, GameMap gm){
        //, Block[][] biome
        keys = keyArray;
        changeKeyFlags();
        findVx(gm);
        findVy(gm);
    }

    @Override
    //Creates a bullet shot by the player
    public void shoot()
    {
        if (canShoot && !evading) {
            if (dir == Direction.LEFT || dir == Direction.RIGHT){
                Bullet.makeBullet(x+width/2, y+height/2-17, isPlayer, dir, isPlayer);
            }
            else if (state == PlayerState.LATCH && facingDir == Direction.LEFT && dir == Direction.UP){
                Bullet.makeBullet(x+1, y+height/2-17, isPlayer, dir, isPlayer);
            }
            else if (state == PlayerState.LATCH && facingDir == Direction.RIGHT && dir == Direction.UP){
                Bullet.makeBullet(x+width-12, y+height/2-17, isPlayer, dir, isPlayer);
            }
            else{
                Bullet.makeBullet(x+width/2, y+height/2, isPlayer, dir, isPlayer);
            }
        }
        canShoot = false;
    }

    //Determines which top sprite of player to use
    public void chooseTopSprite(){
        if (!shooting){
            topIndex = topIndex+0.04 >= 2.0 ? 0 : topIndex+0.04;
            topSprite = sprites.get("idle")[(int)topIndex];
        }
        else if (shooting){
            topIndex = topIndex+0.08 >= 2.0 ? 0 : topIndex+0.08;
            topSprite = sprites.get("shootSide")[(int)topIndex];
            if (dir == Direction.UP){
                topSprite = sprites.get("shootUp")[(int)topIndex];
            }
            else if (dir == Direction.DOWN){
                topSprite = sprites.get("shootDown")[(int)topIndex];
            }
        }

        if (evading){ //When evading, the top and bottom sprites are the same
            topSprite = bottomSprite;
        }

        if (state == PlayerState.LATCH) { //When latching, the top and bottom sprites are the same
            topSprite = bottomSprite;
        }
    }

    //Determines which bottom sprite of players to use. Also determines which direction player is facing.
    public void chooseBottomSprite(){
        if (state == PlayerState.AIR && !evading){
            if (vy<0 && vy<(jumpVy/2)){ //If the player is going up (first half of jumping arc)
                bottomSprite = sprites.get("jumping")[0];
            }
            else if (vy<0 && vy>(jumpVy/2)){ //If the player is going up (second half of jumping arc)
                bottomSprite = sprites.get("jumping")[1];
            }
            else if (vy==0){ //If the player is not moving up/down (middle of jumping arc)
                bottomSprite = sprites.get("standing")[0];
            }
            else if (vy>0 && vy<(Math.abs(jumpVy)/2)){ //If the player is falling down (first part of falling arc)
                bottomSprite = sprites.get("falling")[0];
            }
            else if (vy>0 && vy>(Math.abs(jumpVy)/2)){ //If the player is falling down (first part of falling arc)
                bottomSprite = sprites.get("falling")[1];
            }

            //Finds which direction player is facing
            if (movingRight && !movingLeft){
                dir = Direction.RIGHT;
                facingDir = Direction.RIGHT;
            }
            else if (movingLeft) {
                dir = Direction.LEFT;
                facingDir = Direction.LEFT;
            }
        }

        else if(evading){
            bottomIndex = bottomIndex+0.15 >= 7.0 ? 0 : bottomIndex+0.15;
            bottomSprite = sprites.get("dodge")[(int)bottomIndex];
            invincible = true;
            if (bottomIndex==0.0){
                evading = false;
                invincible = false;
            }
        }
        else if (state == PlayerState.LATCH){
            //If they are aiming up or down when shooting
            if (lookingUp && !lookingDown && shooting){
                bottomIndex = bottomIndex + 0.05 >= 2.0 ? 0 : bottomIndex + 0.05;
                bottomSprite = sprites.get("latchShootUp")[(int)bottomIndex];
            }
            else if (lookingDown && shooting){
                bottomIndex = bottomIndex + 0.05 >= 2.0 ? 0 : bottomIndex + 0.05;
                bottomSprite = sprites.get("latchShootDown")[(int)bottomIndex];
            }
            else{
                if (shooting) { //If they are shooting without a specified direction, (or towards the wall) they shoot away from the wall
                    bottomIndex = bottomIndex + 0.05 >= 2.0 ? 0 : bottomIndex + 0.05;
                    bottomSprite = sprites.get("latchShootSide")[(int) bottomIndex];
                }
                else{ //If they are just latching
                    bottomIndex = bottomIndex + 0.05 >= 2.0 ? 0 : bottomIndex + 0.05;
                    bottomSprite = sprites.get("latch")[(int)bottomIndex];
                }
            }
        }

        //Checks key flags that move the player and change the direction they're shooting
        else if (movingRight && !movingLeft){
            dir = Direction.RIGHT;
            facingDir = Direction.RIGHT;
            bottomIndex = bottomIndex + 0.1 >= 6.0 ? 0 : bottomIndex + 0.1;
            bottomSprite = sprites.get("walking")[(int)bottomIndex];
        }
        else if (movingLeft){
            dir = Direction.LEFT;
            facingDir = Direction.LEFT;
            bottomIndex = bottomIndex+0.1 >= 6.0 ? 0 : bottomIndex+0.1;
            bottomSprite = sprites.get("walking")[(int)bottomIndex];
        }
        else if (movingUp && vy!=0 && state == PlayerState.LADDER ||
                movingDown && vy!=0 && state == PlayerState.LADDER){
            bottomIndex = bottomIndex+0.1 >= 6.0 ? 0 : bottomIndex+0.1;
            bottomSprite = sprites.get("walking")[(int)bottomIndex];
        }
        else {//If not pressing left or right, the bottom sprite is idle
            bottomIndex = bottomIndex+0.05 >= 2.0 ? 0 : bottomIndex+0.05;
            bottomSprite = sprites.get("standing")[(int)bottomIndex];
        }

        //Checks key flags that don't move the player, but change the direction they're shooting
        if (lookingUp && !lookingDown){
            dir = Direction.UP;
        }
        else if (lookingDown){
            dir = Direction.DOWN;
        }
        else{ //If nothing is pressed, the shooting direction reverts back to the facing direction
            dir = facingDir;
        }
    }

    //Changes key flags of player, such as if the player is moving right or not
    //based on the values held in the keys array
    public void changeKeyFlags(){
        shooting = keys[shoot];
        evading = keys[evade] == true ? true : evading;
        evading = state == PlayerState.LATCH ? false : evading;
        jumping = keys[jump];
        movingLeft = keys[left];
        movingRight = keys[right];
        lookingUp = keys[up];
        lookingDown = keys[down];
        movingUp = keys[up];
        movingDown = keys[down];
    }

    //Determines x velocity of player based on direction they are moving
    public void findVx(GameMap gm){
        if (evading){
            if (facingDir==Direction.RIGHT && checkTopRightCorner(gm) && checkBottomRightCorner(gm)){
                vx = 4;
            }
            else if (facingDir==Direction.LEFT && checkTopLeftCorner(gm) && checkBottomLeftCorner(gm)){
                vx = -4;
            }
        }
        else if (movingLeft){
            if (movingRight){         //If both left and right arrows are being pressed, player goes nowhere
                vx = 0;
            }
            else if (checkTopLeftCorner(gm) &&      //If they there are no blocks in the way
                    checkBottomLeftCorner(gm)) {
                vx = -4;
            }
            else{
                vx = 0;
                if (state == PlayerState.AIR){
                    state = PlayerState.LATCH;
                    //Makes the directions opposite when latching (as latching forces the player to face the opposite direction
                    dir = Direction.RIGHT;
                    facingDir = Direction.RIGHT;
                }
            }
        }
        else if (movingRight){
            if (checkTopRightCorner(gm) && checkBottomRightCorner(gm)) {
                vx = 4;
            }
            else{ //if there are blocks in the way, the player latches on to them
                vx = 0;
                if (state == PlayerState.AIR){
                    state = PlayerState.LATCH;
                    dir = Direction.LEFT;
                    facingDir = Direction.LEFT;
                }
            }
        }
        else{
            vx = 0;
        }


        //Checks to see if the Player is about to walk off the end of the map
        //If so it stops them
        if (lx + vx < 5) {
            setLx(5);
        }
        else if (rx + vy > GameMap.BLOCK_WIDTH * (GameMap.BIOME_WIDTH - 1) - 5) {
            setLx(GameMap.BLOCK_WIDTH * (GameMap.BIOME_WIDTH - 1) - 6 - width);
        }
        //Moves the x coordinate one pixel at a time, checking for collision at each pixel as to not get stuck in a
        //block
        else if (state != PlayerState.LATCH) {
            for (int i = 0; i < Math.abs(vx); i++) {
                if (vx < 0 && checkTopLeftCorner(gm) && checkBottomLeftCorner(gm)) {
                    moveX(-1);
                }
                //If there is a block in the way and the player is in the air, they latch onto the block and face the opposite direction
                else if (vx < 0 && !checkTopLeftCorner(gm) && checkBottomLeftCorner(gm) ||
                        vx < 0 && checkTopLeftCorner(gm) && !checkBottomLeftCorner(gm) ||
                        vx < 0 && !checkTopLeftCorner(gm) && !checkBottomLeftCorner(gm)) {
                    if (state == PlayerState.AIR) {
                        state = PlayerState.LATCH;
                        dir = Direction.RIGHT;
                        facingDir = Direction.RIGHT;
                    }
                    vx = 0;
                    break;
                }
                else if (vx > 0 && checkTopRightCorner(gm) && checkBottomRightCorner(gm)) {
                    moveX(1);
                }
                //If there is a block in the way and the player is in the air, they latch onto the block and face the opposite direction
                else if (vx > 0 && !checkTopRightCorner(gm) && checkBottomRightCorner(gm) ||
                        vx > 0 && checkTopRightCorner(gm) && !checkBottomRightCorner(gm) ||
                        vx > 0 && !checkTopRightCorner(gm) && !checkBottomRightCorner(gm)) {
                    if (state == PlayerState.AIR) {
                        state = PlayerState.LATCH;
                        dir = Direction.LEFT;
                        facingDir = Direction.LEFT;
                    }
                    vx = 0;
                    break;
                }
                else {
                    vx = 0;
                    break;
                }
            }
        }
    }

    //Determines y velocity of player based on direction they are moving
    public void findVy(GameMap gm){
        //Checks if there aren't any blocks underneath the player
        try {
            if (checkBelowBottomLeftCorner(gm) &&
                    checkBelowBottomRightCorner(gm)) {
                //If player is not climbing a ladder, or they were climbing a ladder and now there are no ladder blocks at
                //their position or directly below them.
                if (state != PlayerState.LADDER || state == PlayerState.LADDER &&
                        !gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (ty + height / 2) / GameMap.BLOCK_HEIGHT).getType().equals("ladder") &&
                        !gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (by + height / 2) / GameMap.BLOCK_HEIGHT).getType().equals("ladder")) {
                    if (state != PlayerState.LATCH) {
                        state = PlayerState.AIR;
                    }
                }
            }           //Error occurs if they fall off of the map
        } catch(IndexOutOfBoundsException e) { killAndRemove(); }

        if (jumping && state==PlayerState.GROUND && !checkBelowBottomLeftCorner(gm) ||
                jumping && state==PlayerState.GROUND && !checkBelowBottomRightCorner(gm)){
            //If player is on the ground
            if (checkAboveTopMiddle(gm)){
                vy = jumpVy;
                state = PlayerState.AIR;
            }
        }

        if (state == PlayerState.LATCH && jumping){
            state = PlayerState.AIR;
            vy = jumpVy;
        }

        if (state == PlayerState.AIR){
            vy += ay;
        }

        //Checks key flags that move the player up if there is a ladder
        //Checks if there is a ladder block at or directly above the players position
        try {
            if (movingUp && !movingDown && gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (ty + height / 2) / GameMap.BLOCK_HEIGHT).getType().equals("ladder") ||
                    movingUp && !movingDown && gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (ty - height / 2) / GameMap.BLOCK_HEIGHT).getType().equals("ladder") ||
                    movingUp && !movingDown && gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (by) / GameMap.BLOCK_HEIGHT).getType().equals("ladder")) {
                state = PlayerState.LADDER;
                vy = -4;
            } else if (movingDown && !movingUp && gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (ty + height / 2) / GameMap.BLOCK_HEIGHT).getType().equals("ladder") ||
                    movingDown && !movingUp && gm.getAt((lx + width / 2) / GameMap.BLOCK_WIDTH, (by + height / 2) / GameMap.BLOCK_HEIGHT).getType().equals("ladder")) {
                state = PlayerState.LADDER;
                vy = 4;
            } else { //If nothing is pressed, or there are no ladders near the player, their y velocity becomes 0
                if (state != PlayerState.AIR) {
                    vy = 0;
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) { killAndRemove(); }

        //If the player jumps off the ladder, he is no longer hindered by it
        if (state == PlayerState.LADDER && jumping){
            state = PlayerState.AIR;
            vy = jumpVy;
        }

        //Moves the x coordinate one pixel at a time, checking for collision at each pixel as to not get stuck in a
        //block
        for (int i=0; i<Math.abs(vy); i++){
            if (vy<0){ //If player is jumping up
                if (checkAboveTopMiddle(gm)){
                    moveY(-1);
                }
                else{
                    vy = 0;
                    break;
                }
            }
            else if (vy>0){ //If player is falling down
                try {
                    if (checkBelowBottomMiddle(gm)) {
                        moveY(1);
                    } else { //If there is a block underneath him, no y velocity is required
                        state = PlayerState.GROUND;
                        vy = 0;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {                    //Error occurs if the Player falls off the map
                    killAndRemove();
                }
            }
        }
    }

    public void kill()
    {
        bulletCooldown.stop();
        for (int i  = 0; i < 50; i++)
        {
            Particle.makeBloodParticle(lx, ty, width, height, Direction.NULL);
        }
    }

    public void killAndRemove()
    {
        kill();
        players.remove(this);
    }

    //----------------------------------------------------------------Static Methods----------------------------------------------------------------
    //Methods that loop through every player

    public static void drawAll(SubMap sm)
    {   //Draws every Player
        Graphics g = sm.getGraphics();
        int x = sm.getIntX();                   //For fewer method calls
        int y = sm.getIntY();
        for (Player p : players)
        {
            p.draw(g, x, y, true);
        }
    }

    public static void addPlayer(Player p)
    {   //Adds a new Player
        players.add(p);
    }

    public static void moveAll(boolean [] keyArray, GameMap gm)
    {   //Moves every Player
        for (Player p : players)
        {
            p.move(keyArray, gm);
        }
    }

    public static boolean hasPlayers()
    {   //Returns the number of Players that are alive
        return !players.isEmpty();
    }

    public static void shootAll()
    {   //Allows every player to shoot
        for (Player p : players)
        {
            if (p.shooting)           //If the player is holding down the shoot button
                p.shoot();
        }
    }

    public static void killAll()
    {   /*
            Called when the Players quit the current game
            Stops all the timers of each Player and empties the LinkedList to prepare it to be filled the next game
        */
        for (Player p : players)
        {
            p.kill();
        }
        players.clear();
    }

    public static LinkedList<Player> getPlayers() { return players; }
}