public class OxygenEntity extends GetEntity{

    private static final String OXYGEN_IMAGE_FILE = "LiebAssets/Oxygen.png";
    
    private static final int OXYGEN_VALUE = 1;

    public OxygenEntity(){
        this(0, 0);        
    }
    
    public OxygenEntity(int x, int y){
        super(x, y, OXYGEN_IMAGE_FILE);  
    }
    
    public OxygenEntity(int x, int y, String imageFileName){
        super(x, y, imageFileName);
    }

    public int getOxygenValue(){
        return OXYGEN_VALUE;
    }



}