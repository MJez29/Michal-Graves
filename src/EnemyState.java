//Michal Jez
//17/06/2016
//The various states that the enemy can be in

public enum EnemyState
{
    SCANNING,                                   //Is scanning for the player
    WALKING,                                    //Walking from one end if the watchtower to the other
    INFORMING,                                  //Telling the trench troops where to go
    ALERT,                                      //The enemy currently sees the player
    RAISING_BINOCULARS,                         //State that the enemy commander is in when they are raising their binoculars (At that time they cant see anything)
    LOWERING_BINOCULARS,                        //State when they are lowering their binoculars
    EXITING_TRENCH,                             //When the EnemyTrencher is exiting the trench
    STANDING,                                   //Enemy is not doing anything
    SHOOTING,                                   //The Enemy shoots randomly but does not move
    DEAD                                        //The enemy is dead
}