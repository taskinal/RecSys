package utils;

import org.springframework.cache.support.NullValue;

import java.util.Arrays;

/**
 * Created by alimert on 13.11.2016.
 */
public class StringUtilities {


    /*
    * This method is used to get number from a string in format '##########11'
    * One number no blank
     */
    public static String getNumberFromString(String str) {

        try {
            return str.replaceAll("[^?0-9]+", " ");
        }
        catch (Exception e ) {
            return e.getMessage();
        }

    }

    public static int getIntFromString(String str){

        try{
            return Integer.parseInt(str.replaceAll("[^?0-9]+", "")) ;
        }
        catch(Exception e){
            e.getStackTrace();
        }

        return -1 ;
    }


}
