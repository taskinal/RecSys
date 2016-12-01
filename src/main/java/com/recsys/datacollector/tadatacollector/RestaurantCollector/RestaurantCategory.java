package com.recsys.datacollector.tadatacollector.RestaurantCollector;

import com.recsys.datacollector.tadatacollector.RestaurantCollector.TripAdvisorRestaurant;
import com.recsys.datacollector.tadatacollector.dataobjects.TripAdvisorCategory;

import java.util.List;

/**
 * Created by alimert on 28.11.2016.
 */
public class RestaurantCategory extends TripAdvisorCategory {


    private String scrapUrl ;

    private List<TripAdvisorRestaurant> restaurants ;

    public String getScrapUrl() {
        return scrapUrl;
    }

    public void setScrapUrl(String scrapUrl) {
        this.scrapUrl = scrapUrl;
    }

    public List<TripAdvisorRestaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<TripAdvisorRestaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
