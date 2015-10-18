package com.example.snehaanand.moviesapp;

import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetailsActivity extends AppCompatActivity {
    List<ReviewClass> reviewDetails = new ArrayList<>();
    ListViewAdapter lviewAdapter;
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

        MovieClass movieDetails = getIntent().getParcelableExtra(Utils.MOVIE_DETAILS);
        Uri builtUri = Uri.parse(Utils.BASE_URL).buildUpon().appendPath(Utils.PATH_MOVIE).
                appendPath(movieDetails.getId().toString()).appendPath(Utils.PATH_REVIEWS)
                .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
        String GET_REVIEWS_URL=builtUri.toString();
        try {
            JsonArray jsonArray = new DownloadWebPageTask().execute(GET_REVIEWS_URL).get();
            for (int i = 0; i < jsonArray.size(); i++)
            {
                ReviewClass data = new Gson().fromJson(jsonArray.get(i), ReviewClass.class);
                reviewDetails.add(data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        movieName.setText(movieDetails.getOriginal_title());
        userRating.setText(movieDetails.getVote_average().toString());
        releaseDate.setText(movieDetails.getRelease_date());
        synopsis.setText(movieDetails.getOverview());
        movieImage.setImageBitmap(movieDetails.getDisplay_image());
        String month[] = {"January","February","March","April","May",
                "June","July","August","September","October","November","December"};

        String number[] = {"Month - 1", "Month - 2","Month - 3",
                "Month - 4","Month - 5","Month - 6",
                "Month - 7","Month - 8","Month - 9",
                "Month - 10","Month - 11","Month - 12"};
        lviewAdapter = new ListViewAdapter(this,month,number);
        userReviews.setAdapter(lviewAdapter);

    }
}
