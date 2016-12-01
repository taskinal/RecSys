package com.recsys.datacollector.tadatacollector.PlaceCollector;

import com.recsys.datacollector.tadatacollector.dataobjects.TripAdvisorCategory;

import java.util.List;

/**
 * Created by alimert on 21.11.2016.
 */
public class ThingsToDoCategory extends TripAdvisorCategory {

    private String id ;

    private List<SubThingsToDoCategory> subCategoryList ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SubThingsToDoCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubThingsToDoCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

}
