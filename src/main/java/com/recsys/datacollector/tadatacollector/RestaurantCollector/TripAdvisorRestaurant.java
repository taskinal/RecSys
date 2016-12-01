package com.recsys.datacollector.tadatacollector.RestaurantCollector;

import com.recsys.datacollector.tadatacollector.dataobjects.TripAdvisorPlace;

import java.util.List;
import java.util.Map;

/**
 * Created by alimert on 28.11.2016.
 */
public class TripAdvisorRestaurant extends TripAdvisorPlace {

    private String type ;
    private static int counter;
    private String scrapUrl;

    private Map<String,Double> ratingSummary ;
    private Map<String,List<String>> detailsMap ;

    public TripAdvisorRestaurant() {
        counter++;
    }

    public static void reset (){

        counter = 0 ;
    }

    public static int getCounter(){

        return counter ;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScrapUrl() {
        return scrapUrl;
    }

    public void setScrapUrl(String scrapUrl) {
        this.scrapUrl = scrapUrl;
    }

    public Map<String,Double> getRatingSummary() {
        return ratingSummary;
    }

    public void setRatingSummary(Map<String,Double> ratingSummary) {
        this.ratingSummary = ratingSummary;
    }

    public Map<String, List<String>> getDetailsMap() {
        return detailsMap;
    }

    public void setDetailsMap(Map<String, List<String>> detailsMap) {
        this.detailsMap = detailsMap;
    }


}
