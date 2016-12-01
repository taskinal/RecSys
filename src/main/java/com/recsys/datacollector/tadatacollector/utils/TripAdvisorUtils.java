package com.recsys.datacollector.tadatacollector.utils;

import utils.StringUtilities;

import java.util.Arrays;

/**
 * Created by alimert on 27.11.2016.
 */
public final class TripAdvisorUtils extends StringUtilities{


    public static String getSubCategoryIdFromUrl(String str){

        try{
            return str.replaceAll("[^?0-9]+", " ").trim().split(" ")[2];
        }
        catch(Exception e ){
            return e.getMessage();
        }
    }

    public static int getRankingFromRankingString(String rankString){


        return Integer.parseInt(rankString.replaceAll("[^?0-9]+", " ").trim().split(" ")[0]);

    }

    public static int numberOfReviewFromLanguage(String langAndNums, String lang){

        String[] langAndNumbers = langAndNums.trim().split(" ");
        int numberOfEngIdx =  Arrays.asList(langAndNumbers).indexOf(lang) + 1;
        return Integer.parseInt(langAndNumbers[numberOfEngIdx].replaceAll("[^?0-9]+", ""));
    }

    public static double ratingFromString(String rating){

        return Double.parseDouble(rating.substring(0,rating.indexOf(" ")));

    }



}
