package com.example.gogo.orar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.ProcessButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private TextView mDay;
    private ProcessButton mNext;
    private ProgressGenerator mProgresGenerator;
    private EditText mMaterie, mNumeProf, mSala, mTip, mStartHour, mFinishHour;
    private CheckBox mPar, mImpar;
    private FloatingActionButton mAdd;
    private List<Orar> lista;
    public static DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         db = new DatabaseHandler(this);



        initializeUI();
        parOrImparWeek();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void initializeUI() {

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

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"ADAUGAT",Toast.LENGTH_SHORT).show();
                boolean isPar = false;
                if(mPar.isChecked()){
                    isPar = true;
                }
                Orar orar = new Orar(mDay.getText().toString(),mMaterie.getText().toString(),mNumeProf.getText().toString()
                        ,mSala.getText().toString(),mTip.getText().toString()
                        ,mStartHour.getText().toString(),mFinishHour.getText().toString(),isPar);
                String date = orar.getOraInceput();
                if(!date.equals("")) {
                    int minsToAdd = 5;
                    Date date1 = new Date();
                    date1.setTime((((Integer.parseInt(date.split(":")[0])) * 60 + (Integer.parseInt(date.split(":")[1]))) + date1.getTimezoneOffset()) * 60000);
                    System.out.println(date1.getHours() + ":" + date1.getMinutes());
                    date1.setTime(date1.getTime() - minsToAdd * 60000);
                    Log.e("min", String.valueOf(date1.getHours()));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(orar.getZiua()));
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
                int id = db.addContact(orar);
                Log.e("id",id+"");

            }
        });


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressGenerator.OnCompleteListener m= new ProgressGenerator.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        String day = mDay.getText().toString();
                        switchDay(day);
                        mMaterie.setEnabled(true);
                        mNumeProf.setEnabled(true);
                        mSala.setEnabled(true);
                        mTip.setEnabled(true);
                        mStartHour.setEnabled(true);
                        mFinishHour.setEnabled(true);
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

    public int getDayOfWeek(String s){
        if(s.equals("LUNI")){
            return Calendar.MONDAY;
        }
       else if(s.equals("MARTI")){
            return Calendar.TUESDAY;
        }
       else if(s.equals("MIERCURI")){
            return Calendar.WEDNESDAY;
        }else if(s.equals("JOI")){
            return Calendar.THURSDAY;
        }
        else{
            return Calendar.FRIDAY;
        }

    }

    public void switchDay(String text) {
        switch (text) {
            case "LUNI":
                mDay.setText("MARTI");
                clean();
                break;
            case "MARTI":
                mDay.setText("MIERCURI");
                clean();
                break;
            case "MIERCURI":
                mDay.setText("JOI");
                clean();
                break;
            case "JOI":
                mDay.setText("VINERI");
                clean();
                break;
            case "VINERI":
                startActivity(new Intent(this,OrarActivity.class));

                break;
            default:
                break;
        }
    }
    public void clean(){
        mMaterie.setText("");
        mNumeProf.setText("");
        mSala.setText("");
        mTip.setText("");
        mStartHour.setText("");
        mFinishHour.setText("");
    }

    public static boolean parOrImparWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.WEEK_OF_YEAR);
        if(calendar.get(Calendar.WEEK_OF_YEAR)%2==0){
            return true;
        }else {
            return false;
        }
    }

}
