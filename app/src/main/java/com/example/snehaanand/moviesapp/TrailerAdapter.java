package com.example.snehaanand.moviesapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by snehaanandyeluguri on 10/22/15.
 */
public class TrailerAdapter extends BaseAdapter {

    Activity context;
    ArrayList<String> trailerName;

    public TrailerAdapter(Activity context, ArrayList<String> trailerName) {
        super();
        this.context = context;
        this.trailerName = trailerName;
    }

    public int getCount() {
        return trailerName.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView trailerName;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_trailer_row, null);
            holder = new ViewHolder();
            holder.trailerName = (TextView) convertView.findViewById(R.id.trailerName);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.trailerName.setText(trailerName.get(position));

        return convertView;
    }
}
