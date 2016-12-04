package com.recsys.datacollector.tadatacollector.RestaurantCollector;

import com.recsys.datacollector.tadatacollector.TripAdvisorDataCollector;

import com.recsys.datacollector.tadatacollector.utils.TripAdvisorUtils;
import dataobjects.datacollectordataobjects.Review;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by alimert on 27.11.2016.
 */
public class RestaurantCollectorImpl extends TripAdvisorDataCollector implements IRestaurantCollector {

    private List<RestaurantCategory> restaurantCategories ;

    private static TripAdvisorUtils util = getUtil();

    private final void createMainCategories(){

        Document restaurantsPage = connect(getRestaurantsUrl());
        restaurantCategories = new LinkedList<RestaurantCategory>();
        Elements restaurantsCat = restaurantsPage.select("div.jfy_filter_bar_establishmentTypeFilters").select("a[href]") ;

        for(Element cat : restaurantsCat){
            RestaurantCategory category = new RestaurantCategory();
            category.setName(cat.text());
            category.setUrl(cat.attr("abs:href"));
            category.setScrapUrl(cat.attr("abs:href"));
            restaurantCategories.add(category);
        }
    }

    private final void insertRestaurantsIntoCategories(RestaurantCategory category){

        Document categoryPage = connect(category.getScrapUrl());
        Elements restaurants = categoryPage.select("div.listing");
        List<TripAdvisorRestaurant> restaurantList = new LinkedList();
        for(Element restaurant : restaurants){

            TripAdvisorRestaurant rest = new TripAdvisorRestaurant();
            rest.setName(restaurant.select("h3[class=title]").text());
            rest.setId(restaurant.id());
            rest.setUrl(restaurant.select("a[href]").attr("abs:href"));
            rest.setType(category.getName());
            rest.setScrapUrl(restaurant.select("a[href]").attr("abs:href"));
            rest.setRanking(util.getRankingFromRankingString(restaurant.select("div.popIndex").text()));
            rest.setNumOfReviews(util.getIntFromString(restaurant.select("span.reviewCount").text()));

            Elements rating = restaurant.select("div.rating");
            if(rating.size() != 0 ){
                rest.setRating(rating.select("img").attr("alt"));
            }
            else{
                rest.setRating("0");
            }
            restaurantList.add(rest);
            /*
            Olusturulan restaurantlari say 300 den buyukse yeterli counter i resetle
             */
            if(rest.getCounter() > 5){
                rest.reset();
                category.setRestaurants(restaurantList);
                return ;
            }

        }
        if(!categoryPage.select("div.unified.pagination.js_pageLinks").first().children().get(1).attr("abs:href").equals("")) {
            category.setScrapUrl(categoryPage.select("div.unified.pagination.js_pageLinks").first().children().get(1).attr("abs:href"));
            insertRestaurantsIntoCategories(category);
        }
        else{
            category.setRestaurants(restaurantList);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final void insertReviewUrlsIntoRestaurant(TripAdvisorRestaurant rest){

        Document restaurantPage = connect(rest.getScrapUrl()) ;

        //neighborhoodset
        rest.setNeighborhood(restaurantPage.select("div.heading_details" ).select("div.detail").select("div.neighborhood").text());
        rest.setNumOfEnglishReviews(util.numberOfReviewFromLanguage(restaurantPage.select("div.col.language.extraWidth").select("label").text(), "English"));

        if(rest.getNumOfEnglishReviews()==0){
            return ;
        }

        //All operations done here are about reviews

        Map<String,Double> ratingSummary = new HashMap();
        Map<String,List<String>> contentInfo = new HashMap();
        List<String> reviewUrlList = new LinkedList();
        int pageCounter = 0 ;

        Elements tableSection = restaurantPage.select("div.content_block.details_block.scroll_tabs").select("div.details_tab").select("div.table_section") ;
        Elements ratings = tableSection.select("div.ratingSummary").select("div.ratingRow");
        Elements contents = tableSection.select("div[class=row]") ;
        for(Element rate : ratings){
            String id = rate.text();
            double point = 0;
            if(rate.select("img").size()!= 0) {
                point = util.ratingFromString(rate.select("img").attr("alt"));
            }
            ratingSummary.put(id,point);
        }
        rest.setRatingSummary(ratingSummary);
        for(Element content : contents){
            if(content.select("div[class=title]").size () != 0) {
                String id = content.select("div[class=title]").text();
                List<String> contentList = Arrays.asList(content.select("div.content").text().split(","));
                contentInfo.put(id, contentList);
            }
        }
        rest.setDetailsMap(contentInfo);

        /*
        * Pagination oldugu icin 5 sayfaya kadar baglan review linklerini al review lar daha sonra gezilecek
         */
        while(pageCounter < 5){
            Elements reviewLinks = restaurantPage.select("div.quote") ;
            for(Element url : reviewLinks){
                reviewUrlList.add(url.select("a[href").attr("abs:href"));
            }
            if(!restaurantPage.select("div.unified.pagination").first().children().get(1).attr("abs:href").equals("")) {
                rest.setScrapUrl(restaurantPage.select("div.unified.pagination").first().children().get(1).attr("abs:href"));
            }
            else{
                break ;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            restaurantPage = connect(rest.getScrapUrl());
            pageCounter ++ ;
        }

        rest.setReviewLinksList(reviewUrlList);


    }

    @Override
    public void collectData() {

        createMainCategories();
        for(RestaurantCategory category : restaurantCategories){
            insertRestaurantsIntoCategories(category);
            for(TripAdvisorRestaurant restaurant : category.getRestaurants()){
                insertReviewUrlsIntoRestaurant(restaurant);
                List<Review> reviewList = new LinkedList();
                for(String reviewUrl : restaurant.getReviewLinksList()){
                    Document reviewPage = connect(reviewUrl);
                    Elements reviewBodyProperty = reviewPage.select("p[property=reviewBody]");
                    Elements reviewItemInline = reviewPage.select("div.innerBubble").first().select("div.rating.reviewItemInline");

                    Review review = new Review();
                    review.setUrl(reviewUrl);
                    review.setReviewId(reviewBodyProperty.attr("id"));
                    review.setReviewText(reviewBodyProperty.text());
                    review.setReviewTitle(reviewPage.select("div[property=name]").text());
                    review.setReviewRate(util.ratingFromString(reviewItemInline.select("img").first().attr("alt")));
                    review.setReviewDate(reviewItemInline.select("span.ratingDate").first().attr("content"));
                    reviewList.add(review);

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                restaurant.setReviews(reviewList);
            }
        }



    }









}
