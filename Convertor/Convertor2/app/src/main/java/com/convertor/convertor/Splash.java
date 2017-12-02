package com.convertor.convertor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Acer on 9/30/2017.
 */

public class Splash extends Activity {
    Double euro, dolar, yen, lira,liraS,franc;
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private String Euro[] = new String[2];
    private String Dolar[] = new String[2];
    private String Yen[] = new String[2];
    private String LiraS[] = new String[2];
    private String Franc[] = new String[2];
    private String Lira[] = new String[2];//set your time here......

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOnline()) {
            //  setContentView(R.layout.splash);


            Thread welcomeThread = new Thread() {

                @Override
                public void run() {
                    try {
                        super.run();
                        sleep(30000);  //Delay of 10 seconds
                    } catch (Exception e) {

                    } finally {

                        new JsonParser3().execute();


                    }
                }
            };
            welcomeThread.start();
        }else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

                alertDialog.show();
            } catch (Exception e) {
                Log.d("--", "Show Dialog: " + e.getMessage());
            }
        }

    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){

            return false;
        }
        return true;
    }
    class JsonParser3 extends AsyncTask<String, String, Void> {


        InputStream inputStream = null;
        String result = "";


        @Override
        protected Void doInBackground(String... params) {

            URL url = new URL();
            String euro = url.formatURL("EUR", "RON");
            String dolar = url.formatURL("USD", "RON");
            String yen = url.formatURL("JPY", "RON");
            String lira = url.formatURL("TRY", "RON");
            String liraS = url.formatURL("GBP", "RON");
            String franc = url.formatURL("CHF", "RON");

            setUrl(euro, Euro);
            setUrl(dolar, Dolar);
            setUrl(yen, Yen);
            setUrl(lira, Lira);
            setUrl(liraS, LiraS);
            setUrl(franc, Franc);
            return null;
        }


        public void setUrl(String url, String r[]) {

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString() + "");
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }

            JSONObject jObj = null;
            try {

                jObj = new JSONObject(result);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                if(jObj!=null) {
                    r[0] = jObj.getJSONObject("query")
                            .getJSONObject("results").getJSONObject("rate")
                            .getString("Rate");
                    Log.e("Euro",r[0]+"");
                }else{
                    Toast.makeText(getApplicationContext(),"Please check your internet connection!",Toast.LENGTH_LONG);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        protected void onPostExecute(Void v) {

            euro = Double.parseDouble(Euro[0]);
            dolar = Double.parseDouble(Dolar[0]);
            yen = Double.parseDouble(Yen[0]);
            lira = Double.parseDouble(Lira[0]);
            liraS = Double.parseDouble(LiraS[0]);
            franc = Double.parseDouble(Franc[0]);
            Intent i = new Intent(Splash.this,
                    MainActivity.class);
            Log.e("intraaa",euro+"");
            i.putExtra("euro",euro+"");
            i.putExtra("dolar",dolar+"");
            i.putExtra("yen",yen+"");
            i.putExtra("lira",lira+"");
            i.putExtra("liraS",liraS+"");
            i.putExtra("franc",franc+"");
            startActivity(i);
            finish();

        }

    }
}
