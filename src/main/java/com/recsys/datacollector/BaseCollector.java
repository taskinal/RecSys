package com.recsys.datacollector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by alimert on 6.11.2016.
 */
@Service
public abstract class BaseCollector implements DataCollector{

    public Document connect(String url)  {

        Document mainPage = null ;

        try {
            mainPage = Jsoup.connect(url)
                    .userAgent(UserAgent())
                    .referrer(Refferer())
                    .timeout(RequestTimeout())
                    .get() ;
        }
        catch (IOException e) {
            e.getMessage();
        }
        return mainPage ;

    }

    protected static final String UserAgent(){

        return Constants.USER_AGENT;

    }

    protected static final String Refferer(){

        return Constants.REFERRER;
    }

    protected static final int RequestTimeout(){

        return Constants.TRIP_ADVISOR_REQUEST_TIMEOUT;
    }

    public abstract void collectData();

}
