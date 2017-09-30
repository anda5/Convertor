package com.convertor.convertor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private TextView mUsd;
    private TextView mInr;
    private TextView mEur;
    private EditText mEdit;
    private Button mButton;
    private Spinner mSpinner;
    private Spinner mSpinner1;
    public  int mIndex, mIndex2;
    public  double mValue;
    PagerAdapter adaptere;
    private String theResult[] = new String[2] ;

    private String convertFrom, convertTo;
    private List<String> array = new ArrayList<String>();
    public static Context context;
    ViewPager viewPager;
    boolean firstCreate = false;
    String euro, dolar, yen, lira,liraS,franc;

    Handler handler1 = new Handler() {
        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };
    Runnable runnable = new Runnable() {
        public void run() {

        }
    };
    String[] rank;
    int[] flag;
     Handler handler;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            euro = bundle.getString("euro");
            Log.e("CACA",euro+"");
            dolar = bundle.getString("dolar");
            yen = bundle.getString("yen");
            lira = bundle.getString("lira");
            liraS = bundle.getString("liraS");
            franc = bundle.getString("franc");
        }

        mButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.edit);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mUsd = (TextView) findViewById(R.id.usd);


        mSpinner1 = (Spinner) findViewById(R.id.spinner2);
        CurrencySymbol cs = new CurrencySymbol();
        context = getApplicationContext();

        Map currencies = cs.getAvailableCurrencies();
        for (Object country : currencies.keySet()) {
            String currencyCode = (String) currencies.get(country);
            array.add(currencyCode+"-"+country);

        }
        Collections.sort(array);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mSpinner.setAdapter(dataAdapter);
        mSpinner1.setAdapter(dataAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIndex = position;
                String split[] =  parent.getItemAtPosition(mIndex).toString().split("-");
                convertFrom = split[0];
                Log.e("convert",convertFrom);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIndex2 = position;
                String split[] =  parent.getItemAtPosition(mIndex2).toString().split("-");
                convertTo = split[0];
                mUsd.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = mEdit.getText().toString();
                if(value.isEmpty() || value==null){
                    Toast.makeText(getApplicationContext(),"Please insert some value!",Toast.LENGTH_SHORT).show();
                }else{
                    mValue = Double.parseDouble(value);
                }
                if(isInternetAvailable()) {

                    new JsonParser().execute();
                }else {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection!",Toast.LENGTH_LONG);
                }

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
      //notification
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,00);
        Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

// Generate sample data

        rank = new String[] { euro+"",dolar+"",lira+"",yen+"",liraS+"", franc+""};
        flag = new int[] { R.mipmap.euro, R.mipmap.dolar,
                R.mipmap.lira, R.mipmap.yen,R.drawable.lira1,R.drawable.franc
        };
        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);
        // Pass results to ViewPagerAdapter Class
        adaptere = new ViewPagerAdapter(MainActivity.this, rank, flag);
        viewPager.setAdapter(adaptere);
        viewPager.setCurrentItem(viewPager.getCurrentItem(), true);
        adaptere.notifyDataSetChanged();

        // Binds the Adapter to the ViewPager
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    class JsonParser extends AsyncTask<String, String, Void> {


        InputStream inputStream = null;
        String result = "";




        @Override
        protected Void doInBackground(String... params) {

            URL url= new URL();
            String url_select = url.formatURL(convertFrom,convertTo);
            String euro = url.formatURL("EUR","RON");
            String dolar = url.formatURL("USD","RON");
            setUrl(url_select,theResult);

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
                if(jObj!=null){
                r[0] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");
            }else{
                Toast.makeText(getApplicationContext(),"Please check your internet connection!",Toast.LENGTH_LONG);
            }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        protected void onPostExecute(Void v) {



                Double d = Double.parseDouble(theResult[0]);
                mUsd.setText(d * mValue + "");

        }
    }


}
