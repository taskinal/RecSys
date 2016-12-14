package com.recsys.datacollector.tadatacollector.PlaceCollector;

import com.recsys.datacollector.Constants;
import com.recsys.datacollector.tadatacollector.dataobjects.TripAdvisorPlace;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alimert on 26.11.2016.
 */
public class TripAdvisorThingToDo extends TripAdvisorPlace {


    private List<String> pageUrlList;

    private String extraCategories ;

    private String suggestedVisitingHour;


    public List<String> getPageUrlList() {
        return pageUrlList;
    }

    public void setPageUrlList(List<String> pageUrlList) {
        this.pageUrlList = pageUrlList;
    }

    public String getExtraCategories() {
        return extraCategories;
    }

    public void setExtraCategories(String extraCategories) {
        this.extraCategories = extraCategories;
    }

    public String getSuggestedVisitingHour() {
        return suggestedVisitingHour;
    }

    public void setSuggestedVisitingHour(String suggestedVisitingHour) {
        this.suggestedVisitingHour = suggestedVisitingHour;
    }

    public void createReviewPageUrls(){

        int num = super.getNumOfEnglishReviews();
        //max 10 sayfa crawling
        pageUrlList = new LinkedList<String>();
        int pageCovered = num > 100 ? 10 : (num/10)+1 ;

        if(num%reviewNumOnPage() == 0){
            pageCovered = pageCovered-1;
        }

        for(int i = 0 ; i < pageCovered ; i++ ){
            if(i==0){
                this.pageUrlList.add(super.getUrl());
            }
            else {
                String str = this.getUrl();
                str = new StringBuilder(str).insert(str.indexOf("Reviews")+7, "-or" + Integer.toString(i * 10)).toString();
                this.pageUrlList.add(str);
            }
        }
    }

    private int reviewNumOnPage (){

        return Constants.REVIEWS_ON_PLACE_PAGE;

    }
}
