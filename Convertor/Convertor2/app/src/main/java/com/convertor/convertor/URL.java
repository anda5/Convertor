package com.convertor.convertor;

/**
 * Created by Acer on 9/11/2017.
 */

public  class  URL {

    public String FORMAT_ID = "1fd8f3760aec412995c0a738c4a84683";

     public String formatURL(String str,String str2){
     //  String s =  "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+str+""+str2+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
         String sNew = "https://openexchangerates.org/api/convert/19999.95/"+str+"/"+str2+"?app_id="+FORMAT_ID;
         return sNew;
     }
}
