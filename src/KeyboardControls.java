//Michal Jez
//12/06/2016
//This class contains the keyboard controls used by all of the Players
//It is used by the menu to modify different Players' controls before the game starts

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

public class KeyboardControls
{
    //A Player with playerNum being n has their controls stored at index (n - 1)
    private int[] leftKeys, rightKeys, upKeys, downKeys, shootKeys, jumpKeys, evadeKeys;
    private int[][] allKeys;

    public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, SHOOT = 4, JUMP = 5, EVADE = 6;

    private int numPlayers;

    public KeyboardControls(int np)
    {   /*
            Loads each Player's controls and stores them in arrays
        */
        numPlayers = np;
        leftKeys = new int[np];
        rightKeys = new int[np];
        upKeys = new int[np];
        downKeys = new int[np];
        shootKeys = new int[np];
        jumpKeys = new int[np];
        evadeKeys = new int[np];
        //Loads each Player's controls
        for (int i = 0; i < numPlayers; i++)
        {
            try {
                Scanner inFile = new Scanner(new BufferedReader(new FileReader("controls" + (i + 1) + ".txt")));          //IOException
                //Loads correct key configurations
                leftKeys[i] = Integer.parseInt(inFile.nextLine());
                rightKeys[i] = Integer.parseInt(inFile.nextLine());
                upKeys[i] = Integer.parseInt(inFile.nextLine());
                downKeys[i] = Integer.parseInt(inFile.nextLine());
                shootKeys[i] = Integer.parseInt(inFile.nextLine());
                jumpKeys[i] = Integer.parseInt(inFile.nextLine());
                evadeKeys[i] = Integer.parseInt(inFile.nextLine());

                inFile.close();
            } catch (IOException e) {
                System.out.println("Error Loading Player " + (i + 1));
            }
        }
        allKeys = new int[][] { leftKeys, rightKeys, upKeys, downKeys, shootKeys, jumpKeys, evadeKeys };
    }

    public boolean usesKey(int k)
    {   /*
            Checks to see if any Player uses the given key
            Returns true if the key is in use, false otherwise
            The amount of searching that is required to go through every key could be optimized by using advanced data structures like a TreeSet
            But the amount of added complexity for a very micro-optimization is impractical and not very noticeable in the grand scheme of things
            Since the number of operations is <50 either way which the computer can do no sweat
        */
        for (int[] arr : allKeys)
        {
            for (int i : arr)
            {
                if (k == i) return true;
            }
        }
        return false;
    }

    public int getKey(int keyArr, int playerNum)
    {
        return allKeys[keyArr][playerNum - 1];
    }

    public void setKey(int keyArr, int newKey, int playerNum)
    {
        allKeys[keyArr][playerNum - 1] = newKey;
    }

    public void saveAll()
    {   /*
            Replaces the current controls in the text file with the new ones
        */
        for (int i = 0; i < numPlayers; i++)
        {
            try {
                PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter("controls" + (i + 1) + ".txt")));
                for (int[] arr : allKeys)
                {
                    outFile.println(arr[i]);
                }
                outFile.close();
            } catch(IOException e) {}
        }
    }
}
