package com.recsys.datacollector.tadatacollector.PlaceCollector;

import com.recsys.datacollector.Constants;
import com.recsys.datacollector.tadatacollector.TripAdvisorDataCollector;
import com.recsys.datacollector.tadatacollector.utils.TripAdvisorUtils;
import com.recsys.service.Interfaces.TaPlaceService;
import dataobjects.datacollectordataobjects.Review;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alimert on 27.11.2016.
 */

public class PlaceCollectorImpl extends TripAdvisorDataCollector implements IPlaceCollector  {

    private List<ThingsToDoCategory> thingsToDoCategoryList;

    private TripAdvisorUtils util = getUtil();

    private Map placeMap = getPlaceMap();


    @Autowired
    TaPlaceService service ;

    /*
    * Method takes attribute name in link map
    * Finds the link and creates main category list
     */
    private final void createMainCategories(){

        Document thingsToDoPage = this.connect(getAttractionsUrl());//At page there are more than one main categories
        Elements mainCategoryElements = thingsToDoPage.select("div.filter_xor");//Main category elements
        thingsToDoCategoryList = new LinkedList<ThingsToDoCategory>() ;

        for(Element mainCategory : mainCategoryElements){

            if(mainCategory.hasAttr("id")){
                ThingsToDoCategory thingsToDoCategory = new ThingsToDoCategory();
                thingsToDoCategory.setId(util.getNumberFromString(mainCategory.id()));
                thingsToDoCategory.setName(mainCategory.select("span.filter_name").text());
                thingsToDoCategory.setUrl(String.format(CategoryTemplate(), thingsToDoCategory.getId()).replaceAll("\\s+", ""));//Url formati hep ayni ve Constant da tanimli
                thingsToDoCategoryList.add(thingsToDoCategory);//Main thingsToDoCategory listesi olusturuldu her thingsToDoCategory nin icine sub thingsToDoCategory listesi gelecek .
            }
        }
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Main category nin altindaki sub category ler alinir.
    * Main category e ait sub category listesi olusturulur.
    */
    private final void insertSubCategoriesIntoMain(ThingsToDoCategory mainThingsToDoCategory)  {

        List<SubThingsToDoCategory> subCategoryList = new LinkedList();
        Document categoryPage = this.connect(mainThingsToDoCategory.getUrl());
        Elements subCategoryElements = categoryPage.select("div.filter_group").first().children();
        for(Element subCategory : subCategoryElements){

            if(subCategory.children().hasAttr("href")){
                SubThingsToDoCategory subCtg = new SubThingsToDoCategory();
                subCtg.setUrl(subCategory.children().attr("abs:href"));
                subCtg.setName(subCategory.children().select("span.filter_name").html());
                subCtg.setPlaceCount(util.getIntFromString(subCategory.children().select("span.filter_count").html()));
                subCtg.setId(util.getSubCategoryIdFromUrl(subCtg.getUrl()));
                subCtg.createUrlList();//Sub ThingsToDoCategory sayfalarinda pagination var onlarin linki burda yaratiliyor.
                subCategoryList.add(subCtg);
            }
        }
        mainThingsToDoCategory.setSubCategoryList(subCategoryList);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Sub category nin altindaki place leri set eder
    * Her sub category de pagination var dogal olarak sub category nin birden fazla linki var
    * Eger bir place daha once herhangi bir sub category e eklenmisse eklenmez
    * Place in yorumu yoksa rating i 0 setlenir.
     */

    private final void insertPlacesIntoSubCategory(SubThingsToDoCategory subCategory){

        List<TripAdvisorThingToDo> placeList = new LinkedList<TripAdvisorThingToDo>();
        for(String subCategoryUrl : subCategory.getUrlList()){

            Document subCategoryPage = connect(subCategoryUrl) ;
            Elements placeElements = subCategoryPage.select("div.entry");

            for(Element place : placeElements) {
                //Place daha once eklenmediyse
                //Yapilmasinin sebebi bir yerin birden fazla sub category e ait olabilmesi
                if (!placeMap.containsKey(place.id())) {
                    TripAdvisorThingToDo plc = new TripAdvisorThingToDo();
                    plc.setId(util.getNumberFromString(place.id()));
                    plc.setName(place.children().first().children().first().html());
                    plc.setUrl(place.children().first().children().first().absUrl("abs:href"));

                    //If blogu place hakkinda yorum olup olmadigini kontrol ediyor.
                    if (place.select("div.rs").size() != 0) {
                        plc.setRating(place.select("img").first().attr("alt"));
                        plc.setNumOfReviews(util.getIntFromString(place.getElementsContainingOwnText("review").text()));
                    }
                    else {
                        plc.setNumOfReviews(0);
                        plc.setRating("0");
                    }
                    placeMap.put(plc.getId(),plc.getUrl());
                    placeList.add(plc);
                }
            }
            try {
                Thread.sleep(10000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        subCategory.setPlaces(placeList);
    }


    /*
    *Review linklerini place lere setleyen method
    * Place in icinde pagination var.
    * Eger bir yer hakkinda 10 dan fazla yorum yoksa degerlendirme yapmak gereksiz.
     * Eger bir yer hakkinda 5 den fazla ingilizce yorum yoksa degerlendirme yapmak gereksiz.
     * Methodun basinda place sayfasi getiriliyor dogal olarak ilk sayfanin birdaha getirilmesine gerek yok
     */

    private final void insertReviewsIntoPlaces(TripAdvisorThingToDo place){

        //Place hakkinda 10 den az yorum varsa dikkate almak gereksiz
        if(place.getNumOfReviews() > 10){

            List<String> reviewUrlList = new LinkedList<String>() ;
            Document placePage = connect(place.getUrl());
            Elements headingInfo = placePage.select("div.heading_details").first().children();
            place.setNeighborhood(headingInfo.first().text());
            place.setExtraCategories(headingInfo.get(1).text());
            place.setNumOfEnglishReviews(util.numberOfReviewFromLanguage(placePage.select("div.col.language.extraWidth").select("label").text(), "English"));
            place.setRanking(util.getIntFromString(placePage.select("b.rank_text.wrap").text()));

            if(!placePage.select("div.detail").select("b").isEmpty()){
                place.setSuggestedVisitingHour(placePage.select("div.detail").select("b").first().parent().text());
            }

            //Ingilizce yorum 5 den azsa ciddiye alma
            if(place.getNumOfEnglishReviews()>5) {
                place.createReviewPageUrls();

                for(String placeUrl : place.getPageUrlList()) {
                    //ilk sayfaysa tekrar baglanmanin mantigi yok
                    if (!placeUrl.equals(place.getUrl())) {
                        placePage = connect(placeUrl) ;
                    }

                    Elements reviews = placePage.select("div.reviewSelector");
                    //reviewUrlList = new LinkedList<String>();
                    for (Element review : reviews) {
                        if (!review.attr("id").equals("")) {
                            String reviewUrl = review.select("a[href]").attr("abs:href");
                            reviewUrlList.add(reviewUrl); //review url list e ekle
                        }
                    }
                    try {
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            place.setReviewLinksList(reviewUrlList);
        }
    }

    @Override
    public void collectData() {

        createMainCategories();

        for (ThingsToDoCategory thingsToDoCategory : thingsToDoCategoryList) {
            insertSubCategoriesIntoMain(thingsToDoCategory);
            for (SubThingsToDoCategory subCategory : thingsToDoCategory.getSubCategoryList()) {
                insertPlacesIntoSubCategory(subCategory);
                for (TripAdvisorThingToDo place : subCategory.getPlaces()) {
                    insertReviewsIntoPlaces(place);
                    List<Review> reviewList = new LinkedList<Review>();
                    for (String url : place.getReviewLinksList()) {

                        Document reviewPage = this.connect(url);
                        Elements reviewBodyProperty = reviewPage.select("p[property=reviewBody]");
                        Elements reviewItemInline = reviewPage.select("div.innerBubble").first().select("div.rating.reviewItemInline");

                        Review review = new Review();
                        review.setReviewId(reviewBodyProperty.attr("id"));
                        review.setReviewText(reviewBodyProperty.text());
                        review.setReviewTitle(reviewPage.select("div[property=name]").text());
                        review.setReviewRate(util.ratingFromString(reviewItemInline.select("img").first().attr("alt")));
                        review.setReviewDate(reviewItemInline.select("span.ratingDate").first().attr("content"));
                        review.setUrl(url);
                        reviewList.add(review);

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    place.setReviews(reviewList);

                }
            }
        }
    }

    private String CategoryTemplate(){
        return Constants.CATEGORY_TEMPLATE;
    }

}
