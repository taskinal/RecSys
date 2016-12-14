import com.recsys.configuration.ApplicationContextConfig;
import com.recsys.datacollector.tadatacollector.PlaceCollector.IPlaceCollector;
import com.recsys.entities.TaPlace;
import com.recsys.entities.TaUser;
import com.recsys.service.Implementations.TaUserServiceImpl;
import com.recsys.service.Interfaces.GenericService;
import com.recsys.service.Interfaces.TaPlaceService;
import com.recsys.service.Interfaces.TaUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static java.util.UUID.randomUUID;

/**
 * Created by alimert on 6.11.2016.
 */

@Component
public class App {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
        //IPlaceCollector dataCollector = context.getBean(IPlaceCollector.class);
        //IRestaurantCollector restaurantCollector = context.getBean(IRestaurantCollector.class);
        //dataCollector.collectData();
        //dataCollector.writeToDb();

        //TaUserService userService= context.getBean(TaUserService.class);

        TaPlaceService placeServoce = context.getBean(TaPlaceService.class);



        //context.getBean("")
        //TaUser user = new TaUser() ;
        //user.setId(java.util.UUID.randomUUID().toString());
        //user.setUsername("kamil") ;

        //userService.add(user);


        //TaUser user2 =  userService.getItemByAttr("name","kamil");
        //TaUser user2 = userService.get("49ce47ef-8373-4d87-b28f-0c415ee29243");

        TaPlace place = placeServoce.getItemByAttr("id","3613736");


    int a = 5 ;

    }
}
