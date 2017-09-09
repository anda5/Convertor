package com.convertor.convertor;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ProtocolException;

/**
 * Created by Acer on 9/9/2017.
 */

class JsonParser extends AsyncTask<String, String, String[]> {

    private int mIndex;
    private String theResult[];
    @Override
    protected String[] doInBackground(String... params) {
        if (mIndex == 0) {
            String s;
            try {
                s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USDINR,USDEUR%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
                JSONObject jObj;
                jObj = new JSONObject(s);
                theResult[0] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");
                theResult[1] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");

                System.out.println(theResult);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return theResult;

        } else if (mIndex == 1) {
            String s;
            try {
                s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22INRUSD,INREUR%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
                JSONObject jObj;
                jObj = new JSONObject(s);
                theResult[0] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");
                theResult[1] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");

                System.out.println(theResult);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return theResult;
        } else if (mIndex == 2) {
            String s;
            try {
                s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22EURINR,EURUSD%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
                JSONObject jObj;
                jObj = new JSONObject(s);
                theResult[0] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");
                theResult[1] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");

                System.out.println(theResult);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }return theResult;

    }

    public String getJson(String url)throws IOException, ProtocolException {
        String response ="";

        try {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);


            HttpResponse responce = httpclient.execute(httppost);

            HttpEntity httpEntity = responce.getEntity();

            response = EntityUtils.toString(httpEntity);

            Log.d("response is", response);



        } catch (Exception ex) {

            ex.printStackTrace();

        }
        return response;
    }
}