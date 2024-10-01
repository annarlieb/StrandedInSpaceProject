import java.awt.event.*;

public class LiebGame extends SimpleGame{

    private int currentLevel;
    private int currentOxygen;
    private boolean betweenLevels;
    private boolean keepPlaying = true;
    private int ticksWhenPaused;

    private static final String BACKGROUND_IMAGE = "LiebAssets/OuterSpace.png";  
    private static final String L1SPLASH = "LiebAssets/L1Splash.png";  
    private static final String L2SPLASH = "LiebAssets/L2Splash.png";  
    private static final String L3SPLASH = "LiebAssets/L3Splash.png"; 
    private static final String L4SPLASH = "LiebAssets/L4Splash.png"; 
    private static final String L5SPLASH = "LiebAssets/L5Splash.png"; 

    private static final String INTRO_SPLASH_FILE = "LiebAssets/StartingSplash.png";   
    private static final String ALLLEVELSCOMPLETESPLASH = "LiebAssets/AllLevelsCompleteSplash.png"; 
    private static final String DONTPLAYAGAINSPLASH = "LiebAssets/DontPLayAgainSplash.png"; 
    private static final String LEVEL_FAILED_SPLASH = "LiebAssets/LevelFailedSplash.png";
    private static final String LEVEL_COMPLETE_SF = "LiebAssets/LevelComplete.png";

    private static final String[] PRE_LEVEL_SPLASH_SCREENS = {INTRO_SPLASH_FILE, L1SPLASH, L2SPLASH, L3SPLASH, L4SPLASH, L5SPLASH};

    private static final int OXYGEN_LOSS_INTERVAL = 100;

    private static final int YES_LEFT = 120;
    private static final int YES_RIGHT = 384;
    private static final int YES_TOP = 371;
    private static final int YES_BOTTOM = 506;
    private static final int NO_LEFT = 517;
    private static final int NO_RIGHT = 780;

    private static final int STARTING_OXYGEN = 5;
    private static final int MAX_OXYGEN = 15;

    private static final int[] SPACESHIP_DISTANCE = {0,SPAWN_INTERVAL*20,SPAWN_INTERVAL*30,SPAWN_INTERVAL*30,SPAWN_INTERVAL*30,SPAWN_INTERVAL*25};

    private static final int ASTROID_SPAWN_INTERVAL = SPAWN_INTERVAL*5;
    private static final int ASTROID_WARNING_INTERVAL = 90;
    private static final int ASTROID_HEIGHT = 66;
    private static final int WARNING_WIDTH = 75;

    private static final int RL_SWITCHED_LEVEL = 3; // level where rght & left keys are switched
    private static final int UD_SWITCHED_LEVEL = 5; // level where up & down keys are switched
    private static final int NO_AVOID_LEVEL = 1; // level with no avoids

    // leves with astroids
    private static final int ASTROIDLEVEL1 = 4;
    private static final int ASTROIDLEVEL2 = 5;
    
    // for levels 4 & 5:
    private int nextAstroidPosition = 0;
    private WarningEntity currentWE;

    // after completed level 5 (final level) current level will be 6
    private static final int ALL_LEVELS_COMPLETE = 6;

    public static final int KEY1 = KeyEvent.VK_1; // 1 KEY
    public static final int KEY2 = KeyEvent.VK_2; // 2 KEY
    public static final int KEY3 = KeyEvent.VK_3; // 3 KEY
    public static final int KEY4 = KeyEvent.VK_4; // 4 KEY
    public static final int KEY5 = KeyEvent.VK_5; // 5 KEY



    public LiebGame(){
        super();
        this.currentLevel = 0; 
    }

    protected void preGame(){ // override simple game
        this.preGame(currentLevel);
    }

    protected void preGame(int currentLevel){
        displayList.clear(); // clear before super pre game so old player not in game but new player is
        super.preGame();
        currentOxygen = STARTING_OXYGEN;
        betweenLevels = false;
        this.setSplashImage(PRE_LEVEL_SPLASH_SCREENS[currentLevel]);
        setTitleText("STRANDED IN SPACE");
        
    }

    private void playAgain(int currentLevel){
        play();
    }

    protected void updateGame(){
        super.updateGame();
        setBackgroundImage(BACKGROUND_IMAGE);
        updateOxygenLevel();  
    }

    protected void spawnNewEntities(){ // override simple game
        super.spawnNewEntities(); 
        generateSpaceship();
        if (currentLevel == ASTROIDLEVEL1 || currentLevel == ASTROIDLEVEL2){
            generateAstroidWarning();
            generateAstroid();
        }
    }

    protected void generateAstroidWarning(){
        if ((ticksElapsed + ASTROID_WARNING_INTERVAL) % ASTROID_SPAWN_INTERVAL == 0){
            nextAstroidPosition = rand.nextInt(getWindowHeight() - ASTROID_HEIGHT); // save in instance var so next asteroid at the same height
            WarningEntity wEntity = new WarningEntity(DEFAULT_WIDTH - WARNING_WIDTH, nextAstroidPosition);
            displayList.add(wEntity);
            currentWE = wEntity;
        }
    }

    protected void generateAstroid(){
        if (ticksElapsed != 0 && ticksElapsed % ASTROID_SPAWN_INTERVAL == 0){
            displayList.remove(currentWE); // remove the warning
            AstroidEntity astEntity = new AstroidEntity(DEFAULT_WIDTH, nextAstroidPosition);
            displayList.add(astEntity);
        }
    }

    private void generateSpaceship(){
        if (!isPaused && ticksElapsed == SPACESHIP_DISTANCE[currentLevel]){ 
            SpaceshipEntity ss = new SpaceshipEntity(DEFAULT_WIDTH-150, DEFAULT_HEIGHT/5);
            displayList.add(ss);
        }
    }

    protected void generateAvoidEntity(){ // override simple game
        if (currentLevel > NO_AVOID_LEVEL){
            super.generateAvoidEntity();
        }
    }

    protected void generateGetEntity(){ // override simple game now get oxygenentity
        OxygenEntity gEntity = new OxygenEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - GET_ENT_HEIGHT));
        displayList.add(gEntity);
        checkOtherEntityCollision(gEntity);
    }

    protected void generateRareGetEntity(){ // override simple game
        RareOxygenEntity rgEntity = new RareOxygenEntity(DEFAULT_WIDTH, rand.nextInt(getWindowHeight() - GET_ENT_HEIGHT));
        displayList.add(rgEntity);
        checkOtherEntityCollision(rgEntity);
    }

    protected void handlePlayerCollision(Consumable collidedWith){ // override simple game
        if (collidedWith instanceof SpaceshipEntity){
            handleLevelComplete();
            return;
        }
        else if (collidedWith instanceof AvoidEntity || collidedWith instanceof AstroidEntity){
            handleLevelFailed();
            return;
        }
        else if (collidedWith instanceof OxygenEntity){
            currentOxygen = Math.min(currentOxygen+((OxygenEntity)collidedWith).getOxygenValue(),MAX_OXYGEN);
            displayOxygenEntity();
        }
        super.handlePlayerCollision(collidedWith);
    }

    protected void updateOxygenLevel(){
        if (!isPaused && ticksElapsed % OXYGEN_LOSS_INTERVAL == 0){
            this.currentOxygen--;
            if (currentOxygen == 0){
                setTitle();
                handleLevelFailed();
                return; 
            }
            displayOxygenEntity();  
        }
    }

    public void displayOxygenEntity(){ // for oxygen level on top left corner
        OxygenLevelEntity oLevel = new OxygenLevelEntity(10, 10, 300, 100, currentOxygen);
        displayList.add(oLevel);
    }

    public void setTitle(){
        if (!betweenLevels){
            setTitleText("Current Level: " + currentLevel + "  |  Oxygen Level: "+ (int)((double)currentOxygen/MAX_OXYGEN*100) + "%  |  Distance to Spaceship: " + getDistancePercentToSpaceShip() + "%");
        }
        else if (currentLevel == ALL_LEVELS_COMPLETE){
            setTitleText("CONGRATULATIONS! YOU WON!");
        }
    }

    public int getDistancePercentToSpaceShip(){
        int spaceshipAppearsAt = SPACESHIP_DISTANCE[currentLevel];
        int currentDistance = spaceshipAppearsAt - ticksElapsed;
        int distance = Math.max((int)((double)currentDistance/spaceshipAppearsAt*100),0);
        return (100 - distance);
    }

    protected void handleLevelFailed(){
        setSplashImage(LEVEL_FAILED_SPLASH); 
        betweenLevels = true;
    }

    protected void handleLevelComplete(){ // did question situation here too
        currentLevel++;
        if (currentLevel != ALL_LEVELS_COMPLETE){
            setSplashImage(LEVEL_COMPLETE_SF); 
        }
        else{
            setSplashImage(ALLLEVELSCOMPLETESPLASH);
            postGame();
        }
        betweenLevels = true;
    }

    protected MouseEvent handleMouseClick(MouseEvent click){ // override simple game //FIX HERE CHECK NOT LEVEL 6
        super.handleMouseClick(click);
        if (getSplashImage() == LEVEL_COMPLETE_SF || getSplashImage() == LEVEL_FAILED_SPLASH){
            checkPlayAgain(click);
        }
        return click; //returns the mouse event for any child classes overriding this method
    }

    protected void checkPlayAgain(MouseEvent click){
        if (click.getY() > YES_TOP && click.getY() < YES_BOTTOM){
            if (click.getX() < YES_RIGHT && click.getX() > YES_LEFT){
                playAgain(currentLevel);
            }
            if (click.getX() < NO_RIGHT && click.getX() > NO_LEFT){
                setSplashImage(DONTPLAYAGAINSPLASH);
                keepPlaying = false;
            }
        }
    }

    protected boolean isGameOver(){
        if (!keepPlaying){ 
            return true;
        }
        return false;
        
    }
    
    protected void postGame(){ 
        super.setTitleText("THANKS FOR PLAYING!");
    }

    // for level 3 & 5 where right/left or up/down keys are switched
    protected void handleMovementKey(int key){ 
        if (currentLevel == RL_SWITCHED_LEVEL){
            if (key == LEFT_KEY){
                key = RIGHT_KEY;
            }
            else if (key == RIGHT_KEY){
                key = LEFT_KEY;
            }
        }
        else if (currentLevel == UD_SWITCHED_LEVEL){
            if (key == UP_KEY){
                key = DOWN_KEY;
            }
            else if (key == DOWN_KEY){
                key = UP_KEY;
            }  
        }
        super.handleMovementKey(key);
    }

    protected void handlePauseUnpause(){
        if (isPaused){
            isPaused = false;
            ticksElapsed = ticksWhenPaused; // if pause when warning on
            return;
        }
        else{
            isPaused = true;
            ticksWhenPaused = ticksElapsed;
        }
        return;
    }

    protected void handleKeyOnSplash(int key){
        if (key == ADVANCE_SPLASH_KEY && getSplashImage() != LEVEL_FAILED_SPLASH && getSplashImage() != LEVEL_COMPLETE_SF){
            handleEnterOnSplash(); 
            return;
        }
        // FOR DEBUGGING!! WHEN ON A SPLASH SCREEN YOU CAN CHANGE WHAT LEVEL YOU ARE ON
        if (key == KEY1 || key == KEY2|| key == KEY3 || key == KEY4 || key == KEY5 ){ // click number key when on splash screen to change the current level
            if (key == KEY1)
                currentLevel = 1;
            if (key == KEY2)
                currentLevel = 2;
            if (key == KEY3)
                currentLevel = 3;
            if (key == KEY4)
                currentLevel = 4;
            if (key == KEY5)
                currentLevel = 5;
            preGame(currentLevel);
        }        
    }


    protected void handleEnterOnSplash(){ 
        if (getSplashImage() != LEVEL_FAILED_SPLASH && getSplashImage() != LEVEL_COMPLETE_SF){
            if (currentLevel == 0){
                currentLevel = 1;
                displayList.clear(); 
                preGame(currentLevel);
                
            }
            else if (currentLevel == ALL_LEVELS_COMPLETE){
                currentLevel = 0;
                preGame(currentLevel);
                
            }
            else{
                super.setSplashImage(null);
                ticksElapsed = 0;
            }
        } 
    }

}
