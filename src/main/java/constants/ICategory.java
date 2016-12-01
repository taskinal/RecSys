package constants;

/**
 * Created by alimert on 10.11.2016.
 */
public interface ICategory {

    static final int SIGHT_AND_LANDMARK = 1 ;
    static final int MUSEUM             = 2 ;
    static final int SHOPPING           = 3 ;
    static final int NATURE_AND_PARKS   = 4 ;
    static final int RESTAURANT         = 5 ;
    static final int FOOD_AND_DRINK     = 6 ;
    static final int NIGHTLIFE          = 7 ;


    interface ISubCategory{

        static final int SACRED_AND_RELIGIOUS   = 8  ;
        static final int HISTORIC               = 9  ;
        static final int POI_AND_LANDMARK       = 10 ;
        static final int ARCHITECTURAL          = 11 ;
        static final int CHURCHES_AND_CATHEDRALS= 12 ;
        static final int NEIGHBORHOODS          = 13 ;
        static final int ANCIENT_RUIN           = 14 ;

    }

}
