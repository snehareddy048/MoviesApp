package com.example.snehaanand.moviesapp.view.adapter;

/**
 * Created by snehaanandyeluguri on 10/18/15.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.snehaanand.moviesapp.R;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter
{
    Activity context;
    ArrayList<String> author;
    ArrayList<String> content;

    public ReviewAdapter(Activity context, ArrayList<String> author, ArrayList<String> content) {
        super();
        this.context = context;
        this.author = author;
        this.content = content;
    }

    public int getCount() {
        return author.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtViewAuthor;
        TextView txtViewContent;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_review_row, null);
            holder = new ViewHolder();
            holder.txtViewAuthor = (TextView) convertView.findViewById(R.id.author);
            holder.txtViewContent = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewAuthor.setText(author.get(position));
        holder.txtViewContent.setText(content.get(position));

        return convertView;
    }

}
