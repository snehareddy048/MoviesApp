package com.example.snehaanand.moviesapp;

/**
 * Created by snehaanandyeluguri on 10/18/15.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter
{
    Activity context;
    String author[];
    String content[];

    public ListViewAdapter(Activity context, String[] author, String[] content) {
        super();
        this.context = context;
        this.author = author;
        this.content = content;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return author.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtViewAuthor;
        TextView txtViewContent;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;

        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item_row, null);
            holder = new ViewHolder();
            holder.txtViewAuthor = (TextView) convertView.findViewById(R.id.author);
            holder.txtViewContent = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewAuthor.setText(author[position]);
        holder.txtViewContent.setText(content[position]);

        return convertView;
    }

}