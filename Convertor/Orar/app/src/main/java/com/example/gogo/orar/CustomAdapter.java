package com.example.gogo.orar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GoGo on 11/23/2017.
 */

public class CustomAdapter extends ArrayAdapter<Orar> implements View.OnClickListener{

    private ArrayList<Orar> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView ziua;
        TextView oraInceput;
        TextView oraSfarsit;
        TextView materia;
        TextView profesor;
        TextView sala;
        TextView tip;
    }

    public CustomAdapter(ArrayList<Orar> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Orar dataModel = (Orar) object;


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Orar dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.oraInceput = (TextView) convertView.findViewById(R.id.ora);
            viewHolder.materia = (TextView) convertView.findViewById(R.id.matera);
            viewHolder.sala = (TextView) convertView.findViewById(R.id.sala);
            viewHolder.profesor = (TextView) convertView.findViewById(R.id.profesor);
            viewHolder.tip = (TextView) convertView.findViewById(R.id.tip);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                showChangeLangDialog(LayoutInflater.from(getContext()),getContext(),dataModel);
            return true;
            }
        });
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.oraInceput.setText(dataModel.getOraInceput());
        viewHolder.sala.setText(dataModel.getSala());
        viewHolder.materia.setText(dataModel.getMaterie());
        viewHolder.profesor.setText(dataModel.getProfesor());
        viewHolder.tip.setText(dataModel.getTip());

        // Return the completed view to render on screen
        return convertView;
    }

    public void showChangeLangDialog(LayoutInflater inflate, Context context, final Orar orar) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = inflate;
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView edit = (TextView) dialogView.findViewById(R.id.edit);
        final TextView delete = (TextView) dialogView.findViewById(R.id.delete);
        final AlertDialog b = dialogBuilder.create();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MainActivity.db.deleteContact(orar);
               remove(orar);
               notifyDataSetChanged();
               b.dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EditActivity.class);
                intent.putExtra("day",orar.getZiua());
                intent.putExtra("id",orar.getId());
                getContext().startActivity(intent);
                b.dismiss();
            }
        });
        dialogBuilder.setTitle("Editeaza sau sterge");
        dialogBuilder.setMessage("Doresti sa editezi sau sa stergi linia?");
        dialogBuilder.setNegativeButton("INCHIDE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              dialog.dismiss();
            }
        });
        b.show();
    }
}