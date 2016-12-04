import com.recsys.configuration.ApplicationContextConfig;
import com.recsys.dao.interfaces.TaPlaceDAO;
import com.recsys.datacollector.DataCollector;

import com.recsys.datacollector.tadatacollector.PlaceCollector.IPlaceCollector;
import com.recsys.datacollector.tadatacollector.PlaceCollector.PlaceCollectorImpl;
import com.recsys.datacollector.tadatacollector.RestaurantCollector.IRestaurantCollector;
import com.recsys.entities.TaPlace;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by alimert on 6.11.2016.
 */

@Component
public class App {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
        IPlaceCollector dataCollector = context.getBean(IPlaceCollector.class);
        IRestaurantCollector restaurantCollector = context.getBean(IRestaurantCollector.class);
        restaurantCollector.collectData();



    }
}
