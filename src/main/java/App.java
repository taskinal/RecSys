import com.recsys.configuration.ApplicationContextConfig;
import com.recsys.datacollector.tadatacollector.PlaceCollector.IPlaceCollector;
import com.recsys.service.Interfaces.GenericService;
import com.recsys.service.Interfaces.TaRestaurantsService;
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
        //IRestaurantCollector restaurantCollector = context.getBean(IRestaurantCollector.class);
        dataCollector.collectData();
        //GenericService service = context.getBean(TaRestaurantsService.class);
        int a =5 ;

    }
}
