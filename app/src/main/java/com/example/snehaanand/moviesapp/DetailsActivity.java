package com.example.snehaanand.moviesapp;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView movieName = (TextView) findViewById(R.id.movieName);
        TextView userRating = (TextView) findViewById(R.id.userRating);
        TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        ImageView movieImage = (ImageView) findViewById(R.id.movieImage);
        ListView userReviews = (ListView) findViewById(R.id.userReviews);

        MovieClass movieDetails = getIntent().getParcelableExtra(MainActivity.MOVIE_DETAILS);
        movieName.setText(movieDetails.getOriginal_title());
        userRating.setText(movieDetails.getVote_average().toString());
        releaseDate.setText(movieDetails.getRelease_date());
        synopsis.setText(movieDetails.getOverview());
        movieImage.setImageBitmap(movieDetails.getDisplay_image());



    }


}
