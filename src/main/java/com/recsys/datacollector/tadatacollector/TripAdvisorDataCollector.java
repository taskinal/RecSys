package com.recsys.datacollector.tadatacollector;

import com.recsys.datacollector.BaseCollector;
import com.recsys.datacollector.Constants;
import com.recsys.datacollector.tadatacollector.dataobjects.Category;
import com.recsys.datacollector.tadatacollector.dataobjects.SubCategory;
import dataobjects.Place;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import utils.StringUtilities;

import java.util.*;


/**
 * Created by alimert on 10.11.2016.
 */
public class TripAdvisorDataCollector extends BaseCollector {


    private Map placeMap ;
    private HashMap<String,String> urls ; //Eger daha sonra ekstra bir field crawl edilmek istenirse flighs hotels etc, map e attr name ve url eklenmesi yeterli
    private List<Category> categoryList ;

    @Autowired
    StringUtilities util;

    /*
    * Method takes the main page wanted to be scraped.
    * Hashmap stores main urls in main page in format of ('AttrName','AttrValue')
    * Method is called only once at the beggining of collectData Method
     */
    private final void createMainLinks(String baseUrl){

        urls = new HashMap();

        Document mainPage = this.connect(baseUrl);

        urls.put("thingsToDoUrl", mainPage.select(".attractions").first().children().attr("abs:href"));
        urls.put("restaurantsUrl", mainPage.select(".restaurants").first().children().attr("abs:href"));

    }

    /*
    * Method takes attribute name in link map
    * Finds the link and creates main category list
     */

    private final void createMainCategories(String attrName){

        placeMap = new HashMap<String,String>();

        String thingsToDoPageUrl = urls.get(attrName);

        Document thingsToDoPage = this.connect(thingsToDoPageUrl);//At page there are more than one main categories
        Elements mainCategoryElements = thingsToDoPage.select("div.filter_xor");//Main category elements
        categoryList = new LinkedList<Category>() ;

        for(Element mainCategory : mainCategoryElements){

            if(mainCategory.hasAttr("id")){
                Category category = new Category();
                category.setId(util.getNumberFromString(mainCategory.id()));
                category.setName(mainCategory.select("span.filter_name").html());
                category.setUrl(String.format(CategoryTemplate(), category.getId()).replaceAll("\\s+", ""));//Url formati hep ayni ve Constant da tanimli
                categoryList.add(category);//Main category listesi olusturuldu her category nin icine sub category listesi gelecek .
            }
        }
    }

    /*
    * Main category nin altindaki sub category ler alinir.
    * Main category e ait sub category listesi olusturulur.
     */
    private final void insertSubCategoriesIntoMain(Category mainCategory){

        List<SubCategory> subCategoryList = new LinkedList();
        Document categoryPage = this.connect(mainCategory.getUrl());
        Elements subCategoryElements = categoryPage.select("div.filter_group").first().children();
        for(Element subCategory : subCategoryElements){

            if(subCategory.children().hasAttr("href")){
                SubCategory subCtg = new SubCategory();
                subCtg.setUrl(subCategory.children().attr("abs:href"));
                subCtg.setName(subCategory.children().select("span.filter_name").html());
                subCtg.setCount(util.getIntFromString(subCategory.children().select("span.filter_count").html()));
                subCtg.setId(util.getSubCategoryIdFromUrl(subCtg.getUrl()));
                subCtg.createUrlList();//Sub Category sayfalarinda pagination var onlarin linki burda yaratiliyor.
                subCategoryList.add(subCtg);
            }
        }
        mainCategory.setSubCategoryList(subCategoryList);
    }

    private final void insertPlacesIntoSubCategory(SubCategory subCategory){

        for(String subCategoryUrl : subCategory.getUrlList()){

            Document subCategoryPage = connect(subCategoryUrl) ;
            Elements placeElements = subCategoryPage.select("div.entry");

            for(Element place : placeElements) {
                //Place daha once eklenmediyse
                //Yapilmasinin sebebi bir yerin birden fazla sub category e ait olabilmesi
                if (!placeMap.containsKey(place.id())) {
                    Place plc = new Place();
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
                }
            }
        }
    }

    private final void insertReviewsIntoPlaces(Place place){

        //Place hakkinda 10 den az yorum varsa dikkate almak gereksiz
        if(place.getNumOfReviews()>10){
            List<String> reviewUrlList = null ;
            place.createReviewPageUrls();
            place.getPageUrlList();
            for(String placeUrl : place.getPageUrlList()) {
                Document placePage = connect(placeUrl);
                //Ingilizce yorum sayisi daha once alindiysa alinmasin 0 sa zaten bir daha bu if e ugramayacak
                if (place.getNumOfEnglishReviews() == 0) {
                    place.setNumOfEnglishReviews(util.numberOfReviewFromLanguage(placePage.select("div.col.language.extraWidth").select("label").text(), "English"));
                }
                if(place.getNumOfEnglishReviews() == 0){
                    return ;
                }
                //English review sayisi 5 den buyukse dikkate al
                if (place.getNumOfEnglishReviews() > 5) {
                    Elements reviews = placePage.select("div.reviewSelector");
                    reviewUrlList = new LinkedList<String>();
                    for (Element review : reviews) {
                        if (!review.attr("id").equals("")) {
                            String reviewUrl = review.select("a[href]").attr("abs:href");
                            reviewUrlList.add(reviewUrl); //review url list e ekle
                        }
                    }

                }

            }
            place.setReviewLinksList(reviewUrlList);
        }

    }

    private final void initialize (String baseUrl){

        createMainLinks(baseUrl);
        createMainCategories("thingsToDoUrl");

        for(Category category : categoryList){
            insertSubCategoriesIntoMain(category);
            for(SubCategory subCategory : category.getSubCategoryList()) {
                insertPlacesIntoSubCategory(subCategory);
                for(Place place : subCategory.getPlaces()){
                    insertReviewsIntoPlaces(place);
                }
            }
        }
    }

    @Override
    public void collectData(String baseUrl) {

        Document mainPage = this.connect("https://www.tripadvisor.com/ShowUserReviews-g293974-d307883-r438220109-Kucuk_Ayasofya_Camii_Church_of_the_Saints_Sergius_and_Bacchus-Istanbul.html#REVIEWS");
        int a =5 ;
//mainPage.select("p[property=reviewBody]").text() review i aliyor
//mainPage.select("p[property=reviewBody]").attr("id") id yi aliyor
//puani al

    }

    //CONSTANTS
    private String CategoryTemplate(){
        return Constants.CATEGORY_TEMPLATE;
    }


}
