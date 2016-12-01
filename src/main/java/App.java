import datacollector.BaseCollector;

import java.io.IOException;

/**
 * Created by alimert on 6.11.2016.
 */
public class App {

    public static void main(String[] args) throws IOException {
        BaseCollector collect = new BaseCollector() ;
        collect.setReviews();
    }
}
