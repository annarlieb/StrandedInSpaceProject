public class OxygenLevelEntity extends Entity{
    
    private static final String OXYGEN0 = "LiebAssets/OxygenLevel1.png";
    private static final String OXYGEN1 = "LiebAssets/OxygenLevel1.png";
    private static final String OXYGEN2 = "LiebAssets/OxygenLevel2.png";
    private static final String OXYGEN3 = "LiebAssets/OxygenLevel3.png";
    private static final String OXYGEN4 = "LiebAssets/OxygenLevel4.png";
    private static final String OXYGEN5 = "LiebAssets/OxygenLevel5.png";
    private static final String OXYGEN6 = "LiebAssets/OxygenLevel6.png";
    private static final String OXYGEN7 = "LiebAssets/OxygenLevel7.png";
    private static final String OXYGEN8 = "LiebAssets/OxygenLevel8.png";
    private static final String OXYGEN9 = "LiebAssets/OxygenLevel9.png";
    private static final String OXYGEN10 = "LiebAssets/OxygenLevel10.png";
    private static final String OXYGEN11 = "LiebAssets/OxygenLevel11.png";
    private static final String OXYGEN12 = "LiebAssets/OxygenLevel12.png";
    private static final String OXYGEN13 = "LiebAssets/OxygenLevel13.png";
    private static final String OXYGEN14 = "LiebAssets/OxygenLevel14.png";
    private static final String OXYGEN15 = "LiebAssets/OxygenLevel15.png";
    private static final String[] images = {OXYGEN0, OXYGEN1,OXYGEN2,OXYGEN3,OXYGEN4,OXYGEN5,OXYGEN6,OXYGEN7,OXYGEN8,OXYGEN9,OXYGEN10,OXYGEN11,OXYGEN12,OXYGEN13,OXYGEN14,OXYGEN15};

    public OxygenLevelEntity(int x, int y, int width, int height, String imageName) {
        super(x, y, width, height, imageName);
        
    }

    public OxygenLevelEntity(int x, int y, int width, int height, int currentOxygen) {
        super(x, y, width, height, images[currentOxygen]);
        
    }


}