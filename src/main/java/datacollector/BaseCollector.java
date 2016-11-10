package datacollector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by alimert on 6.11.2016.
 */
public class BaseCollector implements ICollector {

    private URL url ;
    private Document doc ;
    private Elements links ;



    public void setReviews()  {

        try {
            this.doc = Jsoup.connect("https://www.tripadvisor.com/Attractions-g293974-Activities-Istanbul.html")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                    .get() ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.links = doc.select("a");

    }






}
