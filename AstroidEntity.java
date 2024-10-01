public class AstroidEntity extends Entity implements Consumable,Scrollable{

    private static final String ASTROID_IMAGE_FILE = "LiebAssets/Astroid1.gif";
    //Dimensions of the AvoidEntity    
    private static final int ASTROID_WIDTH = 214;
    private static final int ASTROID_HEIGHT = 66;
    //Speed that the avoid moves each time the game scrolls
    private static final int ASTROID_SCROLL_SPEED = 25;
    
    public AstroidEntity(){
        this(0, 0);        
    }
    
    public AstroidEntity(int x, int y){
        super(x, y, ASTROID_WIDTH, ASTROID_HEIGHT, ASTROID_IMAGE_FILE);  
    }

    public int getScrollSpeed() {
        return ASTROID_SCROLL_SPEED;
    }

    public void scroll(){
        setX(getX() - ASTROID_SCROLL_SPEED);
    }

    public int getPointsValue() {
        return 0;
    }

    public int getDamageValue() {
        return 0;
    }
    
}
