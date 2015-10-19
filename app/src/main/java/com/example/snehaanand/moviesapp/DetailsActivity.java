package com.example.snehaanand.moviesapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snehaanand.moviesapp.model.MovieClass;
import com.example.snehaanand.moviesapp.model.ReviewClass;
import com.example.snehaanand.moviesapp.model.TrailerClass;
import com.example.snehaanand.moviesapp.network.DownloadWebPageTask;
import com.example.snehaanand.moviesapp.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetailsActivity extends AppCompatActivity {
    List<ReviewClass> reviewDetails = new ArrayList<>();
    List<TrailerClass> trailerDetails = new ArrayList<>();
    ListViewAdapter lviewAdapter;
    ArrayList<String> author=new ArrayList<>();
    ArrayList<String> content=new ArrayList<>();

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
        Uri reviewUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().appendPath(Utils.PATH_MOVIE).
                appendPath(movieDetails.getId().toString()).appendPath(Utils.PATH_REVIEWS)
                .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
        String GET_REVIEWS_URL=reviewUri.toString();

        Uri trailerUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().appendPath(Utils.PATH_MOVIE).
                appendPath(movieDetails.getId().toString()).appendPath(Utils.PATH_VIDEOS)
                .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
        String GET_TRAILERS_URL=trailerUri.toString();

        try {
            JsonArray jsonArray = new DownloadWebPageTask().execute(GET_REVIEWS_URL).get();
            for (int i = 0; i < jsonArray.size(); i++)
            {
                ReviewClass reviewData = new Gson().fromJson(jsonArray.get(i), ReviewClass.class);
                reviewDetails.add(reviewData);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            JsonArray jsonArray = new DownloadWebPageTask().execute(GET_TRAILERS_URL).get();
            for (int i = 0; i < jsonArray.size(); i++)
            {
                TrailerClass trailerData = new Gson().fromJson(jsonArray.get(i), TrailerClass.class);
                trailerDetails.add(trailerData);
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
        for (ReviewClass review : reviewDetails)
        {
            author.add(review.getAuthor());
            content.add(review.getContent());
        }
        lviewAdapter = new ListViewAdapter(this,author,content);
        userReviews.setAdapter(lviewAdapter);

        TextView movieTrailer = (TextView) findViewById(R.id.trailers);
        if(trailerDetails!=null)
        movieTrailer.setText(trailerDetails.size()+"hii");
    }
}
