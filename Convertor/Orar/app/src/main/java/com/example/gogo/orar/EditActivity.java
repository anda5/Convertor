package com.example.gogo.orar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.ProcessButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private TextView mDay;
    private ProcessButton mNext;
    boolean isAdauga = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private ProgressGenerator mProgresGenerator;
    private EditText mMaterie, mNumeProf, mSala, mTip, mStartHour, mFinishHour;
    private CheckBox mPar, mImpar;
    private FloatingActionButton mAdd;
    private List<Orar> lista;
    public String day1,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        if(intent.getExtras().get("id")!=null) {
             day1 = intent.getExtras().get("day").toString();
             id = intent.getExtras().get("id").toString();
        }

        if(intent.getExtras().get("adauga")!=null){
            isAdauga = intent.getExtras().getBoolean("adauga");
            switch (intent.getExtras().getInt("day")) {
                case 0:
                     day1 = "LUNI";
                     break;
                case 1:
                    day1 = "MARTI";
                    break;
                case 2:
                    day1 ="MIERCURI";
                    break;
                case 3:
                    day1 = "JOI";
                    break;
                case 4:
                    day1 ="VINERI";
                    break;
            }
        }


        mDay = (TextView) findViewById(R.id.day);
        mNext = (ProcessButton) findViewById(R.id.btnWithText);
        mMaterie = (EditText) findViewById(R.id.materia);
        mNumeProf = (EditText) findViewById(R.id.numeProfesor);
        mSala = (EditText) findViewById(R.id.sala);
        mTip = (EditText) findViewById(R.id.tip);
        mStartHour = (EditText) findViewById(R.id.oraInceput);
        mFinishHour = (EditText)findViewById(R.id.oraSfarsit);
        mPar = (CheckBox) findViewById(R.id.saptPara);
        mImpar = (CheckBox) findViewById(R.id.saptImpara);
        mAdd = (FloatingActionButton) findViewById(R.id.add);
        lista = new ArrayList<>();
        mDay.setText(day1);
        if(isAdauga){
            mNext.setText("Adauga");
        }

        mPar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mPar.isChecked()){
                    mImpar.setChecked(false);
                }else{
                    mImpar.setChecked(true);
                }
            }
        });

        mImpar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mImpar.isChecked()){
                    mPar.setChecked(false);
                }else{
                    mPar.setChecked(true);
                }

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressGenerator.OnCompleteListener m= new ProgressGenerator.OnCompleteListener() {
                    @Override
                    public void onComplete() throws ParseException {
                        boolean isPar = false;
                        if(mPar.isChecked()){
                            isPar = true;
                        }
                        Orar orar = new Orar(mDay.getText().toString(),mMaterie.getText().toString(),mNumeProf.getText().toString()
                                ,mSala.getText().toString(),mTip.getText().toString()
                                ,mStartHour.getText().toString(),mFinishHour.getText().toString(),isPar);
                        if(isAdauga){
                            Toast.makeText(getApplicationContext(),"ADAUGAT",Toast.LENGTH_SHORT).show();
                             String date = orar.getOraInceput();
                             if(!date.equals("")) {
                                 int minsToAdd = 5;
                                 Date date1 = new Date();
                                 date1.setTime((((Integer.parseInt(date.split(":")[0])) * 60 + (Integer.parseInt(date.split(":")[1]))) + date1.getTimezoneOffset()) * 60000);
                                 System.out.println(date1.getHours() + ":" + date1.getMinutes());
                                 date1.setTime(date1.getTime() - minsToAdd * 60000);
                                 Log.e("min", String.valueOf(date1.getHours()));
                                 Calendar calendar = Calendar.getInstance();
                                 calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                 calendar.set(Calendar.HOUR_OF_DAY, date1.getHours());
                                 calendar.set(Calendar.MINUTE, date1.getMinutes());
                                 calendar.set(Calendar.SECOND, 00);
                                 Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                                 intent.putExtra("curs", orar.getMaterie());
                                 intent.putExtra("sala", orar.getSala());
                                 intent.putExtra("tip", orar.getTip());
                                 PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                 AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                             }
                            int id = MainActivity.db.addContact(orar);

                            Log.e("id",id+"");
                        }else{
                            MainActivity.db.updateContact(orar,id);
                        }
                        finish();
                    }
                };
                mProgresGenerator = new ProgressGenerator(m);
                mProgresGenerator.start(mNext);
                mMaterie.setEnabled(false);
                mNumeProf.setEnabled(false);
                mSala.setEnabled(false);
                mTip.setEnabled(false);
                mStartHour.setEnabled(false);
                mFinishHour.setEnabled(false);

            }
        });
    }





}
