package com.recsys.datacollector.tadatacollector.PlaceCollector;

import com.recsys.datacollector.Constants;
import com.recsys.datacollector.tadatacollector.TripAdvisorDataCollector;
import com.recsys.datacollector.tadatacollector.utils.TripAdvisorUtils;
import com.recsys.entities.*;
import com.recsys.service.Interfaces.*;
import dataobjects.datacollectordataobjects.Review;
import org.hibernate.HibernateException;
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
    TaThingsToDoCategoryService taThingsToDoCategoryService ;
    @Autowired
    TaThingsToDoService taThingsToDoService ;
    @Autowired
    TaThingsToDoSubCategoryService taThingsToDoSubCategoryService ;
    @Autowired
    TaReviewsService taReviewsService ;
    @Autowired
    TaPlaceService taPlaceService ;



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
                subCtg.setName(subCategory.children().select("span.filter_name").text());
                subCtg.setPlaceCount(util.getIntFromString(subCategory.children().select("span.filter_count").text()));
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
                    plc.setName(place.children().first().children().first().text());
                    plc.setUrl(place.children().first().children().first().absUrl("abs:href"));

                    //If blogu place hakkinda yorum olup olmadigini kontrol ediyor.
                    if (place.select("div.rs").size() != 0) {
                        plc.setRating(place.select("img").first().attr("alt"));
                        plc.setNumOfReviews(util.getIntFromString(place.getElementsContainingOwnText("review").text()));
                    }
                    else {
                        plc.setNumOfReviews(0);
                        plc.setRating("0 ");
                    }
                    placeMap.put(place.id(),plc.getUrl());
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

            if (headingInfo.size() < 2) {
                if (headingInfo.first().text().contains("Neighborhood")) {
                    place.setNeighborhood(headingInfo.first().text());
                } else {
                    place.setExtraCategories(headingInfo.first().text());
                }
            } else {
                place.setNeighborhood(headingInfo.first().text());
                place.setExtraCategories(headingInfo.get(1).text());
            }

            place.setNumOfEnglishReviews(util.numberOfReviewFromLanguage(placePage.select("div.col.language.extraWidth").select("label").text(), "English"));
            place.setRanking(util.getIntFromString(placePage.select("b.rank_text.wrap").text()));

            if(!placePage.select("div.detail").select("b").isEmpty()){
                place.setSuggestedVisitingHour(placePage.select("div.detail").select("b").first().parent().text());
            }

            //Ingilizce yorum 5 den azsa ciddiye alma
            if(place.getNumOfEnglishReviews()>0) {
                place.createReviewPageUrls();

                for(String placeUrl : place.getPageUrlList()) {
                    //ilk sayfaysa tekrar baglanmanin mantigi yok
                    if (!placeUrl.equals(place.getUrl())) {
                        placePage = connect(placeUrl) ;
                    }
                    if (placePage != null) {

                        Elements reviews = placePage.select("div.reviewSelector");
                        //reviewUrlList = new LinkedList<String>();
                        for (Element review : reviews) {
                            if (!review.attr("id").equals("") && !review.select("a[href]").attr("abs:href").contains("Amex")) {
                                String reviewUrl = review.select("a[href]").attr("abs:href");
                                reviewUrlList.add(reviewUrl); //review url list e ekle
                            }
                        }
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            place.setReviewLinksList(reviewUrlList);
        }
    }

    @Override
    public void collectData() {

        int ttdscBatchCounter=0, plcBatchCounter = 0, rewBatchCounter = 0 ;
        TaThingsToDoCategory ttdc = new TaThingsToDoCategory();
        TaThingsToDoSubCategory ttdsc = new TaThingsToDoSubCategory();
        TaPlace plc = new TaPlace();
        TaThingsToDo ttd = new TaThingsToDo();
        TaReviews rew = new TaReviews();

        createMainCategories();

        for (ThingsToDoCategory thingsToDoCategory : thingsToDoCategoryList) {

            ttdc.setId(thingsToDoCategory.getId().trim());
            ttdc.setName(thingsToDoCategory.getName());
            taThingsToDoCategoryService.saveOrUpdate(ttdc);

            insertSubCategoriesIntoMain(thingsToDoCategory);

            for (SubThingsToDoCategory subCategory : thingsToDoCategory.getSubCategoryList()) {
                ttdsc.setTtdId(ttdc);
                ttdsc.setId(subCategory.getId().trim());
                ttdsc.setName(subCategory.getName());
                ttdscBatchCounter++;
                if (ttdscBatchCounter % 50 == 0) {
                    taThingsToDoSubCategoryService.flush();
                    taThingsToDoSubCategoryService.clear();
                }
                taThingsToDoSubCategoryService.saveOrUpdate(ttdsc);
                insertPlacesIntoSubCategory(subCategory);

                for (TripAdvisorThingToDo place : subCategory.getPlaces()) {
                    insertReviewsIntoPlaces(place);

                    plc.setId(place.getId().trim());
                    plc.setName(place.getName());
                    plc.setRating(util.ratingFromString(place.getRating()));
                    plc.setRanking(place.getRanking());
                    plc.setNeighborhood(place.getNeighborhood());

                    ttd.setExtraCategories(place.getExtraCategories());
                    ttd.setSuggestedVisitingHour(place.getSuggestedVisitingHour());
                    ttd.setTtdId(ttdsc);
                    ttd.setPlaceId(plc);

                    plcBatchCounter++;
                    if (plcBatchCounter % 50 == 0) {
                        taPlaceService.flush();
                        taPlaceService.clear();
                        taThingsToDoService.flush();
                        taThingsToDoService.clear();
                    }
                    taPlaceService.saveOrUpdate(plc);
                    taThingsToDoService.add(ttd);
                    if (place.getReviewLinksList() != null) {
                        List<Review> reviewList = new LinkedList<Review>();
                        for (String url : place.getReviewLinksList()) {

                            Document reviewPage = this.connect(url);
                            if (reviewPage != null) {
                                Elements reviewBodyProperty = reviewPage.select("p[property=reviewBody]");
                                Elements reviewItemInline = reviewPage.select("div.innerBubble").first().select("div.rating.reviewItemInline");
                                Review review = new Review();
                                review.setReviewId(reviewBodyProperty.attr("id"));
                                review.setReviewText(reviewBodyProperty.text());
                                review.setReviewTitle(reviewPage.select("div[property=name]").text());
                                review.setReviewRate(util.ratingFromString(reviewItemInline.select("img").first().attr("alt")));
                                review.setReviewDate(reviewItemInline.select("span.ratingDate").first().attr("content"));
                                review.setUrl(url);

                                rew.setId(review.getReviewId().trim());
                                rew.setPlaceId(plc);
                                rew.setDate(review.getReviewDate());
                                rew.setText(review.getReviewText().replaceAll("[^\\x00-\\x7F]", ""));
                                rew.setTitle(review.getReviewTitle().replaceAll("[^\\x00-\\x7F]", ""));
                                rew.setRate(review.getReviewRate());

                                rewBatchCounter++;
                                if (rewBatchCounter % 50 == 0) {
                                    taReviewsService.flush();
                                    taReviewsService.clear();
                                }
                                taReviewsService.saveOrUpdate(rew);
                                reviewList.add(review);

                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        place.setReviews(reviewList);
                    }

                }

            }

        }
    }


    private String CategoryTemplate(){
        return Constants.CATEGORY_TEMPLATE;
    }
    public void writeToDb() {

        Document a = connect("https://www.tripadvisor.com/ShowUserReviews-g293974-d295194-r433534068-Galata_Tower-Istanbul.html#REVIEWS");
        int c = 5;

    }

}
