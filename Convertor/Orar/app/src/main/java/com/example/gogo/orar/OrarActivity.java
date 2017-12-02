package com.example.gogo.orar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrarActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adaptere;
    Button adauga;

    @Override
    protected void onResume() {
        super.onResume();
        if(ViewPagerAdapter.listView!=null) {
            ((CustomAdapter) adaptere.listView.getAdapter()).notifyDataSetChanged();
            adaptere.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adauga = (Button)findViewById(R.id.adauga);
        // Pass results to ViewPagerAdapter Class
        List<Orar> orarList = new ArrayList<>();

        adaptere = new ViewPagerAdapter(OrarActivity.this);
        viewPager.setAdapter(adaptere);
        viewPager.setCurrentItem(viewPager.getCurrentItem(), true);
        adaptere.notifyDataSetChanged();


        adauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrarActivity.this,EditActivity.class);
                intent.putExtra("adauga",true);
                intent.putExtra("day",viewPager.getCurrentItem());
                startActivity(intent);

            }
        });

    }


}
