package com.example.gogo.orar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.gogo.orar.MainActivity.db;

/**
 * Created by GoGo on 11/15/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    public  List<Orar> orar;
     static ListView listView;
    LayoutInflater inflater;
    public static CustomAdapter adapter;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        TextView textView;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.view_pager_item, container,
                false);

        listView = (ListView) itemView.findViewById(R.id.list);
        textView = (TextView) itemView.findViewById(R.id.ziua);


        List<Orar> dataModels1 = db.getAllContacts();


        if(position ==0) {
            textView.setText("LUNI");
            adapter = new CustomAdapter(getListByDay("LUNI",dataModels1), itemView.getContext());
        }else if(position==1){
            textView.setText("MARTI");
            adapter = new CustomAdapter(getListByDay("MARTI",dataModels1), itemView.getContext());
        }else if(position==2){
            textView.setText("MIERCURI");
            adapter = new CustomAdapter(getListByDay("MIERCURI",dataModels1), itemView.getContext());
        }else if(position==3){
            textView.setText("JOI");
            adapter = new CustomAdapter(getListByDay("JOI",dataModels1), itemView.getContext());
        }else{
            textView.setText("VIENRI");
            adapter = new CustomAdapter(getListByDay("VINERI",dataModels1), itemView.getContext());
        }
        listView.setAdapter(adapter);


        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    public ArrayList<Orar> getListByDay(String day,List<Orar> dl) {
        List<Orar> newOrar = new ArrayList<Orar>();
        boolean isPar  =  MainActivity.parOrImparWeek();
        for (Orar orar : dl) {
            if (orar.getZiua().equals(day)) {
                if(isPar) {
                    if(orar.getPara()) {
                        newOrar.add(orar);
                    }
                }else {
                    if(!orar.getPara()){
                        newOrar.add(orar);
                    }
                }
            }
        }
       return (ArrayList<Orar>) newOrar;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
