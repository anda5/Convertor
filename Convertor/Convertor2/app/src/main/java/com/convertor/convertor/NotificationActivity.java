package com.convertor.convertor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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
import java.net.URI;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NotificationActivity extends BroadcastReceiver {
    private String Euro[] = new String[2] ;
    private String Dolar[] = new String[2] ;
    private Double meuro;
    private Double mdolar;
    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        new JsonParser().execute();
        ctx = context;


    }
    class JsonParser extends AsyncTask<String, String, Void> {


        InputStream inputStream = null;
        String result = "";




        @Override
        protected Void doInBackground(String... params) {

            URL url= new URL();
            String euro = url.formatURL("EUR","RON");
            String dolar = url.formatURL("USD","RON");
            setUrl(euro,Euro);
            setUrl(dolar,Dolar);
            return null;
        }

        public void setUrl(String url,String r[]){

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
                r[0] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        protected void onPostExecute(Void v) {
            meuro = Double.parseDouble(Euro[0]);
            Log.e("euroo",mdolar+"");
            mdolar = Double.parseDouble(Dolar[0]);
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent repatingIntent = new Intent(ctx,AlarmReceiver.class);
            repatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,100,repatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(ctx)
                    .setContentIntent(pendingIntent)
                    .setSound(alarmSound)
                    .setSmallIcon(R.mipmap.currency1)
                    .setContentTitle("Currency Converter")
                    .setContentText("Euro:"+meuro+" Dolar:"+mdolar).setAutoCancel(true);
            notificationManager.notify(100,builder.build());

        }
    }
}
