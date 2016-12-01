import com.recsys.datacollector.DataCollector;
import com.recsys.datacollector.tadatacollector.PlaceCollector.PlaceCollectorImpl;
import com.recsys.datacollector.tadatacollector.RestaurantCollector.IRestaurantCollector;
import com.recsys.datacollector.tadatacollector.RestaurantCollector.RestaurantCollectorImpl;
import constants.IUrls;
import com.recsys.datacollector.tadatacollector.TripAdvisorDataCollector;

import java.io.IOException;

/**
 * Created by alimert on 6.11.2016.
 */
public class App {

    public static void main(String[] args) throws IOException {
        //DataCollector tripAdvisorData = new TripAdvisorDataCollector() ;
        //tripAdvisorData.collectData(IUrls.TRIP_ADVISOR_ISTANBUL_URL);


        DataCollector ta = new RestaurantCollectorImpl() ;
        ta.collectData();
    }
}
