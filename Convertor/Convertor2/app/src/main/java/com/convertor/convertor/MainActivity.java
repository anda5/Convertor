package com.convertor.convertor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mUsd;
    private TextView mInr;
    private TextView mEur;
    private EditText mEdit;
    private Button mButton;
    private Spinner mSpinner;
    public  int mIndex;
    public  double mValue;
    private String theResult[] = new String[2] ;
    private String convertFrom;
    private List<String> array = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.edit);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mUsd = (TextView) findViewById(R.id.usd);
        mInr = (TextView) findViewById(R.id.inr);

        CurrencySymbol cs = new CurrencySymbol();

        Map currencies = cs.getAvailableCurrencies();
        for (Object country : currencies.keySet()) {
            String currencyCode = (String) currencies.get(country);
            array.add(currencyCode);
            System.out.println(country + " => " + currencyCode);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mSpinner.setAdapter(dataAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIndex = position;
               convertFrom = parent.getItemAtPosition(mIndex).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = mEdit.getText().toString();
                mValue = Double.parseDouble(value);


                new JsonParser().execute();
            }
        });


    }

    class JsonParser extends AsyncTask<String, String, Void> {


        InputStream inputStream = null;
        String result = "";




        @Override
        protected Void doInBackground(String... params) {

            URL url= new URL();
            String url_select = url.formatURL(convertFrom);

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url_select);
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
                theResult[0] = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void v) {


            Double d = Double.parseDouble(theResult[0]);
            mUsd.setText(d*mValue + "");

        }
    }

}
