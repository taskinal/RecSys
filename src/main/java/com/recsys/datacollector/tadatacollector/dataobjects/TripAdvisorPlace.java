package com.recsys.datacollector.tadatacollector.dataobjects;

import dataobjects.datacollectordataobjects.Place;
import dataobjects.datacollectordataobjects.Review;

import java.util.List;

/**
 * Created by alimert on 2.12.2016.
 */
public class TripAdvisorPlace extends Place {

    private int ranking;

    private List<String> reviewLinksList ;

    private String rating ;

    private int numOfReviews ;

    private int numOfEnglishReviews ;

    private List<Review> reviews ;

    private String neighborhood ;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public List<String> getReviewLinksList() {
        return reviewLinksList;
    }

    public void setReviewLinksList(List<String> reviewLinksList) {
        this.reviewLinksList = reviewLinksList;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public int getNumOfEnglishReviews() {
        return numOfEnglishReviews;
    }

    public void setNumOfEnglishReviews(int numOfEnglishReviews) {
        this.numOfEnglishReviews = numOfEnglishReviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

}
