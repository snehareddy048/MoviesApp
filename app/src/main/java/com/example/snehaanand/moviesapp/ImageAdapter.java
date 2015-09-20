package com.example.snehaanand.moviesapp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.*;

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
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams((int) this.context.getResources().getDimension(R.dimen.height), (int) this.context.getResources().getDimension(R.dimen.width)));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(thumbIds.get(position).getDisplay_image());
        return imageView;
    }

}
