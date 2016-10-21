//Michal Jez
//22/05/2016
//This enum represents the different sub-states of EnemyState.Alert

public enum EnemyAlertState
{
    KAMIKAZE,           //They charge the player, guns a' firin'
                        //They run at the player and pay no attention to their surroundings
                        //If the player is above they will try to jump and close the gap
                        //If they get too far from the player they will return back to their SCANNING/WALKING states

    CAUTIOUS,           //They approach the player gradually and are continuously scanning for the player

    COWARD,              //They run away from the Player and try to alert other Enemies to engage the Player

    CAMPING_COWARD      //The coward has dug a hole and now they just stand in it waiting for a Player to show up
}