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


import org.json.JSONObject;

import java.io.IOException;
import java.net.ProtocolException;

public class MainActivity extends AppCompatActivity {

    private TextView mUsd;
    private TextView mInr;
    private TextView mEur;
    private EditText mEdit;
    private Button mButton;
    private Spinner mSpinner;
    private int mIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.edit);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mUsd = (TextView) findViewById(R.id.usd);
        mInr = (TextView) findViewById(R.id.inr);
        mEur = (TextView) findViewById(R.id.eur);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = mEdit.getText().toString();
                Double mValue = Double.parseDouble(value);


                new JsonParser().execute();
            }
        });


    }



}
