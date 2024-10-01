import java.awt.*;
import java.awt.event.*;
import java.util.*;



//A Simple version of the scrolling game, featuring Avoids, Gets, and RareGets
//Players must reach a score threshold to win
//If player runs out of HP (via too many Avoid collisions) they lose
public class SimpleGame extends ScrollingGameEngine {
    
    //Dimensions of game window
    protected static final int DEFAULT_WIDTH = 900;
    protected static final int DEFAULT_HEIGHT = 600;  
    
    //Starting PlayerEntity coordinates
    private static final int STARTING_PLAYER_X = 0;
    private static final int STARTING_PLAYER_Y = 100;
    
    //Score needed to win the game
    private static final int SCORE_TO_WIN = 300;
    
    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    private static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    private static final int SPEED_CHANGE = 20;    
    
    private static final String INTRO_SPLASH_FILE = "assets/splash.gif";        
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    
    //Interval that Entities get spawned in the game window
    //ie: once every how many ticks does the game attempt to spawn new Entities
    protected static final int SPAWN_INTERVAL = 45;
    
    //A Random object for all your random number generation needs!
    public static final Random rand = new Random();
    
    protected static final int AVOID_ENT_HEIGHT = 100; // changed for LiebGame
    protected static final int GET_ENT_HEIGHT = 50;
    private static final int PAUSE_KEY = 'P';

    
    
    //Player's current score & hp
    protected int score = 0; // start at 0
    protected int hp = 3; // start at 3
    
    //Stores a reference to game's PlayerEntity object for quick reference
    //(This PlayerEntity will also be in the displayList)
    protected PlayerEntity player;  

    protected boolean isPaused;

    

    public SimpleGame(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public SimpleGame(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }
    
    
    //Performs all of the initialization operations that need to be done before the game starts
    protected void preGame(){
        this.setBackgroundColor(Color.BLACK);
        this.setSplashImage(INTRO_SPLASH_FILE);
        player = new PlayerEntity(STARTING_PLAYER_X, STARTING_PLAYER_Y);
        displayList.add(player); 
    }
    
    //Called on each game tick
    protected void updateGame(){
        //scroll all scrollable Entities on the game board
        if (isPaused)
           return;

        scrollEntities();   

        //Spawn new entities only at a certain interval
        if (ticksElapsed % SPAWN_INTERVAL == 0){            
            spawnNewEntities();
            garbageCollectEntities();
        }
        checkPlayerCollisions();
        setTitle();
        
        //Update the title text on the top of the window
        //        
    }

    public void setTitle(){
        setTitleText("HP: " + hp + ", Score: " + score);
    }
    
    public void checkPlayerCollisions(){
        for (int i = 0; i < displayList.size(); i++){
            Entity e = displayList.get(i);
            if (player.isCollidingWith(e)){
                if (e instanceof Consumable)
                    handlePlayerCollision((Consumable)e);
            }
        }
    }


    
    //Scroll all scrollable entities per their respective scroll speeds
    protected void scrollEntities(){
        for (int i = 0; i < displayList.size(); i++){
            Entity e = displayList.get(i);
            if (e instanceof Scrollable){ 
                ((Scrollable)e).scroll();
            }
        }
    }
    
    //Handles "garbage collection" of the displayList
    //Removes entities from the displayList that are no longer relevant
    //(i.e. will no longer need to be drawn in the game window).
    protected void garbageCollectEntities(){
        for (int i = 0; i < displayList.size() -1; i++){
            Entity e = displayList.get(i);
            if (e.getRightX() <= 0){
                displayList.remove(e);
            }      
        }   
    }
    
    
    //Called whenever it has been determined that the PlayerEntity collided with a consumable
    protected void handlePlayerCollision(Consumable collidedWith){
        score += collidedWith.getPointsValue();
        hp += collidedWith.getDamageValue();
        displayList.remove((Consumable)collidedWith);
        if (isGameOver()){
            postGame();
        }
    }
    
    
    //Spawn new Entities on the right edge of the game board
    protected void spawnNewEntities(){
        int numAvoidsSpawned = rand.nextInt(2);
        for (int i = 0; i < numAvoidsSpawned; i++){
            generateAvoidEntity();
        }

        int numGetsSpawned = rand.nextInt(2);
        for (int i = 0; i < numGetsSpawned; i++){
            generateGetEntity();
        }

        int numRareGetsSpawned = rand.nextInt(2);
        for (int i = 0; i < numRareGetsSpawned; i++){
            generateRareGetEntity();
        }

    }

    protected void generateAvoidEntity(){
        AvoidEntity aEntity = new AvoidEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - AVOID_ENT_HEIGHT));
        displayList.add(aEntity); 
        checkOtherEntityCollision(aEntity);
    }

    protected void generateGetEntity(){
        GetEntity gEntity = new GetEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - GET_ENT_HEIGHT));
        displayList.add(gEntity);
        checkOtherEntityCollision(gEntity);
    }

    protected void generateRareGetEntity(){
        RareGetEntity rgEntity = new RareGetEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - GET_ENT_HEIGHT));
        displayList.add(rgEntity);
        checkOtherEntityCollision(rgEntity);
    }

    protected void checkOtherEntityCollision(Entity checking){
        for (int i = 0; i < displayList.size() -1; i++){
            Entity e = displayList.get(i);
            if (checking.isCollidingWith(e)){ 
                displayList.remove(checking);
                return; // so it doesnt have to go through entire list
            }
        }
    }

    
    //Called once the game is over, performs any end-of-game operations
    protected void postGame(){
        if (score == SCORE_TO_WIN){
            super.setTitleText("GAME OVER - You Won!");
        }
        else{
            super.setTitleText("GAME OVER - You Lose!");
        }
    }
    
    
    //Determines if the game is over or not
    //Game can be over due to either a win or lose state
    protected boolean isGameOver(){
        if (score >= 300 || hp == 0){
            return true;
        }
           
        return false;   //****   placeholder... implement me!   ****
       
    }
    
    
    
    //Reacts to a single key press on the keyboard
    protected void handleKeyPress(int key){        
        setDebugText("Key Pressed!: " + KeyEvent.getKeyText(key));
        
        //if a splash screen is active, only react to the "advance splash" key... nothing else!
        /*  
        if (getSplashImage() != null){
            if (key == ADVANCE_SPLASH_KEY)
                handleEnterOnSplash();  // ADDED FOR LIEB GAME
            return;
        }
        */

        // added for lieb game and commented out above

        if (getSplashImage() != null){
            handleKeyOnSplash(key);
            return;
        }

        if (key ==  PAUSE_KEY){
            handlePauseUnpause();
        }

        if (isPaused){
            return;
        }


        if (key == RIGHT_KEY || key == LEFT_KEY || key == UP_KEY || key == DOWN_KEY)
            handleMovementKey(key);

        else if (key == SPEED_UP_KEY){
            if (getGameSpeed() < MAX_GAME_SPEED)
                setGameSpeed(getGameSpeed() + SPEED_CHANGE);
        }
        else if (key == SPEED_DOWN_KEY){ 
            if (getGameSpeed() > SPEED_CHANGE) 
                setGameSpeed(getGameSpeed() - SPEED_CHANGE);
        }
    }    
    
    // added for override in Lieb game

    protected void handlePauseUnpause(){
        if (isPaused){
            isPaused = false;
            return;
        }
        isPaused = true;
        return;
    }

    protected void handleMovementKey(int key){
        if (key == RIGHT_KEY){
            if(player.getRightX()!= getWindowWidth()){
                int newX = Math.min(getWindowWidth() - player.getWidth() , player.getX()+player.getMovementSpeed()); 
                player.setX(newX);
                return; // so doesnt have to check everhting
            }
        }
        if (key == LEFT_KEY){
            if(player.getX()!= 0){
                int newX = Math.max(0,player.getX()-player.getMovementSpeed());
                player.setX(newX);
                return; // so doesnt have to check everhting

            }
        }
        else if (key == UP_KEY){
            if(player.getY()!= 0){
                int newY = Math.max(0,player.getY()-player.getMovementSpeed());
                player.setY(newY);
                return; // so doesnt have to check everhting

            }       
        }
        else if (key == DOWN_KEY){ 
            if(player.getBottomY()!= getWindowHeight()) {
                int newY = Math.min(getWindowHeight() - player.getHeight() , player.getY()+player.getMovementSpeed()); 
                player.setY(newY);
                return; // so doesnt have to check everhting

            }   
        }
    }
    
    protected void handleKeyOnSplash(int key){
        if (key == ADVANCE_SPLASH_KEY)
            handleEnterOnSplash();  // ADDED FOR LIEB GAME

    }

    protected void handleEnterOnSplash(){
        super.setSplashImage(null);
    }

    //Handles reacting to a single mouse click in the game window
    //Won't be used in Simple Game... you could use it in Creative Game though!
    protected MouseEvent handleMouseClick(MouseEvent click){
        if (click != null){ //ensure a mouse click occurred
            int clickX = click.getX();
            int clickY = click.getY();
            setDebugText("Click at: " + clickX + ", " + clickY);
        }
        return click; //returns the mouse event for any child classes overriding this method
    }

    // ADDED FOR LIEBGAME!!!!
    public int getScore(){
        return score;
    }

    public int getHP(){
        return hp;
    }
    
    
    
    
}
