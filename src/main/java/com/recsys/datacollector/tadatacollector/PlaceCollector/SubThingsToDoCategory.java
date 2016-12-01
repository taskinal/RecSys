package com.recsys.datacollector.tadatacollector.PlaceCollector;

import com.recsys.datacollector.Constants;
import com.recsys.datacollector.tadatacollector.PlaceCollector.ThingsToDoCategory;
import com.recsys.datacollector.tadatacollector.PlaceCollector.TripAdvisorThingToDo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alimert on 21.11.2016.
 */
public class SubThingsToDoCategory extends ThingsToDoCategory {


    private int placeCount;

    private List<String> urlList ;

    private List<TripAdvisorThingToDo> places ;

    public void createUrlList(){

        urlList = new LinkedList<String>();
        if(this.getUrl()!=null){

            this.urlList.add(this.getUrl());
        }

        for(int i = 1; i <= placeCount /PlaceOnPage() ; i++ ){
            String str  = this.getUrl();
            str  = new StringBuilder(str).insert(str.indexOf("Istanbul"),"oa"+Integer.toString(i*PlaceOnPage())+"-").toString() ;
            this.urlList.add(str);
        }
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public List<TripAdvisorThingToDo> getPlaces() {
        return places;
    }

    public void setPlaces(List<TripAdvisorThingToDo> places) {
        this.places = places;
    }

    private int PlaceOnPage(){

        return Constants.PLACES_ON_SUB_CATEGORY_PAGE;

    }







}
