package com.recsys.datacollector.tadatacollector;

import com.recsys.datacollector.BaseCollector;
import com.recsys.datacollector.Constants;
import com.recsys.datacollector.tadatacollector.utils.TripAdvisorUtils;
import com.recsys.service.Interfaces.TaPlaceService;
import com.recsys.service.Interfaces.TaRestaurantCategoryService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;


/**
 * Created by alimert on 10.11.2016.
 */
public abstract class TripAdvisorDataCollector extends BaseCollector {

    protected static final String getAttractionsUrl(){
        return Constants.ATTRACTIONS_URL;
    }

    protected static final String getRestaurantsUrl(){
        return Constants.RESTAURANTS_URL;
    }

    public static final TripAdvisorUtils getUtil(){
        return new TripAdvisorUtils();
    }

    public Map getPlaceMap(){

        return new HashMap<String,String>();
    }

    public abstract void collectData() ;

}
