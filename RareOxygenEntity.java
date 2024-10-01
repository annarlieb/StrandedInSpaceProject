public class RareOxygenEntity extends OxygenEntity {
    
    private static final String RAREOXYGEN_IMAGE_FILE = "LiebAssets/OxygenTank.png";
    private static final int OXYGEN_VALUE = 3;


    public RareOxygenEntity(){
        this(0,0);
    }
 
    public RareOxygenEntity(int x, int y){
        super(x, y, RAREOXYGEN_IMAGE_FILE);  
    }

    public int getOxygenValue(){
        return OXYGEN_VALUE;
    }
    
}
