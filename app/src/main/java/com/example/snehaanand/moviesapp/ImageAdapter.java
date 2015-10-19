package com.example.snehaanand.moviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.snehaanand.moviesapp.model.MovieClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snehaanandyeluguri on 9/6/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<MovieClass> thumbIds = new ArrayList();

    public ImageAdapter(Context c, List<MovieClass> movieBitmaps) {
        context = c;
        thumbIds = movieBitmaps;
    }

    public ImageAdapter(Context c, List<String> list,List<MovieClass> movieBitmaps) {
        context = c;
        thumbIds = movieBitmaps;
    }

    public int getCount() {
        return thumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(thumbIds.get(position).getDisplay_image());
        return imageView;
    }

}
