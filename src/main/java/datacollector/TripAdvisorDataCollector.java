package datacollector;

import dataobjects.Place;
import dataobjects.Review;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;


/**
 * Created by alimert on 10.11.2016.
 */
public class TripAdvisorDataCollector extends BaseCollector {

    //CONSTANTS
    private static String CATEGORY_TEMPLATE = "https://www.tripadvisor.com/Attractions-g293974-Activities-c%s-Istanbul.html" ;
    private Map placeMap = new HashMap();


    // VARIABLES
    private Document tripAdvisorHtml;
    private Map<Object, Map<Object,List<Object>>> linkMap = new HashMap<Object, Map<Object,List<Object>>>();
    private List categories = new LinkedList<Category>();

    // METHODS
    public void dataCollector(String baseUrl) {

        this.tripAdvisorHtml = this.getWebPageHtml(TRIP_ADVISOR_ISTANBUL_URL, USER_AGENT, REFERRER, TRIP_ADVISOR_REQUEST_TIMEOUT);
        String thingsToDoUrl = this.tripAdvisorHtml.select(".attractions").first().children().attr("abs:href");

        Document thingsToDoMainCategories = this.getWebPageHtml(thingsToDoUrl, USER_AGENT, REFERRER, TRIP_ADVISOR_REQUEST_TIMEOUT);
        Elements mainCategoryUrls = thingsToDoMainCategories.select("div.filter_xor");


        for (Element mainCategoryUrl : mainCategoryUrls) {
            if (mainCategoryUrl.hasAttr("id")) {

                Category category = new Category();
                category.setId(mainCategoryUrl.id().replaceAll("[^?0-9]+", " "));
                category.setName(mainCategoryUrl.select("span.filter_name").html());
                category.setUrl(String.format(CATEGORY_TEMPLATE, category.getId()).replaceAll("\\s+", ""));

                Document thingsToDoSub = this.getWebPageHtml(category.getUrl(), USER_AGENT, REFERRER, TRIP_ADVISOR_REQUEST_TIMEOUT);
                Elements subCategoryUrls = thingsToDoSub.select("div.filter_group").first().children();

                for (Element subCategoryUrl : subCategoryUrls) {
                    if (subCategoryUrl.children().hasAttr("href")) {

                        SubCategory subCategory = new SubCategory();
                        subCategory.setUrl(subCategoryUrl.children().attr("abs:href"));
                        subCategory.setName(subCategoryUrl.children().select("span.filter_name").html());
                        subCategory.setCount(Integer.parseInt(subCategoryUrl.children().select("span.filter_count").html().replaceAll("[^?0-9]+", "")));
                        subCategory.setId(subCategory.getUrl().replaceAll("[^?0-9]+", " ").trim().split(" ")[2]);
                        subCategory.createUrlList();

                        for(String url : subCategory.getUrlList()) {

                            Document subCategoryPage = this.getWebPageHtml(url, USER_AGENT, REFERRER, TRIP_ADVISOR_REQUEST_TIMEOUT);
                            Elements places = subCategoryPage.select("div.entry");

                            for (Element place : places) {
                            //Place daha once alindiysa baska bi listeye, ekleme
                                if (placeMap.get(place.id()) == null) {

                                    Place newPlace = new Place();
                                    newPlace.setId(place.id().replaceAll("[^?0-9]+", " "));
                                    newPlace.setName(place.children().first().children().first().html());
                                    newPlace.setUrl(place.children().first().children().first().absUrl("abs:href"));
                                    //rating div i yoksa ne yorum var ne rating kontrol et
                                    if(place.select("div.rs").size() != 0){
                                        newPlace.setRating(place.select("img").first().attr("alt"));
                                        newPlace.setNumOfReviews(Integer.parseInt(place.getElementsContainingOwnText("review").text().replaceAll("[^?0-9]+", "")));
                                    }
                                    else {
                                        newPlace.setNumOfReviews(0);
                                        newPlace.setRating("0");
                                    }

                                    //Yerin hakkinda 10 dan fazla yorum varsa yorum olan sayfalarin url lerini olustur yoksa sadece kaydet
                                    if(newPlace.getNumOfReviews() > 5) {

                                        List<String> reviewUrlList = new LinkedList<String>();//yorumlarin url lerini tutacak link

                                        Document reviewPage = this.getWebPageHtml(newPlace.getUrl(),USER_AGENT, REFERRER, TRIP_ADVISOR_REQUEST_TIMEOUT);//ilk tum review larin oldugu page

                                        String[] langAndNumbers = reviewPage.select("div.col.language.extraWidth").select("label").text().trim().split(" ");
                                        int numberOfEngIdx =  Arrays.asList(langAndNumbers).indexOf("English") + 1 ; // English in yaninda yazan sayi (=) li sekilde

                                        newPlace.setNumOfEnglishReviews(Integer.parseInt(langAndNumbers[numberOfEngIdx].replaceAll("[^?0-9]+", ""))) ;

                                        Elements reviews = reviewPage.select("div.reviewSelector");

                                        for(Element review : reviews) {
                                            if (!review.attr("id").equals("")) {
                                                String reviewUrl = review.select("a[href]").attr("abs:href");
                                                reviewUrlList.add(reviewUrl); //review url list e ekle
                                            }
                                        }
                                        //Ilk sayfa okundu eger 10 dan fazla ingilizce yorum yoksa baska sayfa yok url olusturmak mantiksiz
                                        //Kod tekrari !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                        if(newPlace.getNumOfEnglishReviews() > 10 ) {
                                            newPlace.creteReviewPageUrls();//diger sayfalarin url leri otomatik olarak olusturulur.
                                            for(String reviewPageUrl : newPlace.getPageUrlList()) {//Place in icindeki reviewlar birden fazla sayfada olabilir
                                                Document otherReviewPage = this.getWebPageHtml(reviewPageUrl, USER_AGENT, REFERRER, TRIP_ADVISOR_REQUEST_TIMEOUT);//hepsine git
                                                Elements otherReviews = otherReviewPage.select("div.reviewSelector");
                                                for(Element review : reviews) {
                                                    if (!review.attr("id").equals("")) {
                                                        String reviewUrl = review.select("a[href]").attr("abs:href");
                                                        reviewUrlList.add(reviewUrl); //review url list e ekle
                                                    }
                                                }
                                                /*
                                                try {
                                                    Thread.sleep(10000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                */
                                            }

                                        }
                                        newPlace.setReviewLinksList(reviewUrlList);

                                    }
                                    placeMap.put(newPlace.getId(), newPlace.getUrl());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // VARIABLE ACCESS METHODS

    private class Category {

        private String id ;
        private String name ;
        private String url ;
        private List subCategoryList = new LinkedList<SubCategory>() ;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List getSubCategoryList() {
            return subCategoryList;
        }

        public void setSubCategoryList(List subCategoryList) {
            this.subCategoryList = subCategoryList;
        }

        @Override
        public boolean equals(Object obj){

            Category category = (Category) obj ;

            if(category==null){
                return false ;
            }

            if(this.getUrl().equals( category.getUrl())){
                return true ;
            }
            return false ;
        }
    }

    private final class SubCategory extends Category{

        private static final int PLACES_ON_PAGE = 30 ;
        private int count ;
        private List<String> urlList = new LinkedList<String>();
        private List places = new LinkedList<Place>();

        public void createUrlList(){

            if(this.getUrl()!=null){

                this.urlList.add(this.getUrl());
            }

            for(int i = 1 ; i <= count/PLACES_ON_PAGE ; i++ ){
                String str  = this.getUrl();
                str  = new StringBuilder(str).insert(str.indexOf("Istanbul"),"oa"+Integer.toString(i*PLACES_ON_PAGE)+"-").toString() ;
                this.urlList.add(str);
            }
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<String> getUrlList() {
            return urlList;
        }

        public void setUrlList(List<String> urlList) {
            this.urlList = urlList;
        }

        public List getPlaces() {
            return places;
        }
        public void setPlaces(List places) {
            this.places = places;
        }



    }



}
