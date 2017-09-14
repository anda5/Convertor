package com.convertor.convertor;

/**
 * Created by Acer on 9/11/2017.
 */

public  class  URL {

     public String formatURL(String str,String str2){
         String s =  "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+str+""+str2+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
         return s;
     }
}
