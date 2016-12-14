package com.recsys.datacollector;

import org.jsoup.nodes.Document;

/**
 * Created by alimert on 21.11.2016.
 */
public interface DataCollector {

    Document connect(String url) ;

    void collectData() ;

}
