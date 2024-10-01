public class WarningEntity extends Entity{

    private static final String WARNING_IMAGE_FILE = "LiebAssets/Warning.gif";
       
    private static final int WARNING_WIDTH = 75;
    private static final int WARNING_HEIGHT = 75;

    public WarningEntity (int x, int y){
        super(x, y, WARNING_WIDTH, WARNING_HEIGHT, WARNING_IMAGE_FILE);

    }

    public WarningEntity(int x, int y, int width, int height, String imageName) {
        super(x, y, width, height, imageName);
        
    }

    
}