public class SpaceshipEntity extends Entity implements Consumable {

    private static final String SS_IMAGE_FILE = "LiebAssets/Spaceship.png";

    private static final int SS_WIDTH = 200;
    private static final int SS_HEIGHT = 500;


    public SpaceshipEntity(){
        this(0,0);
    }

    public SpaceshipEntity(int x, int y){
        super(x, y, SS_WIDTH, SS_HEIGHT, SS_IMAGE_FILE);  
    }

    public SpaceshipEntity(int x, int y, int SS_WIDTH, int SS_HEIGHT, String SS_IMAGE_FILE){
        super(x, y, SS_WIDTH, SS_HEIGHT, SS_IMAGE_FILE);
    }

    public int getPointsValue() {
        return 0;
    }

    public int getDamageValue() {
        return 0;
    }


    
}