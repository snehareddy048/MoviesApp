package com.example.snehaanand.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;
    ArrayList<String> author=new ArrayList<>();
    ArrayList<String> content=new ArrayList<>();
    ArrayList<String> trailerName=new ArrayList<>();

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
        ListView trailerVideos = (ListView) findViewById(R.id.trailerVideos);
        Button favorite = (Button) findViewById(R.id.favorite);

        final MovieClass movieDetails = getIntent().getParcelableExtra(Utils.MOVIE_DETAILS);
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
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MoviesProvider._ID,movieDetails.getId());
                Uri uri = getContentResolver().insert(
                        MoviesProvider.CONTENT_URI, values);
                Toast.makeText(getBaseContext(),
                        uri.toString(), Toast.LENGTH_LONG).show();
            }
        });

        //trailers
        for (TrailerClass trailer : trailerDetails)
        {
            trailerName.add(trailer.getName());
        }
        trailerAdapter = new TrailerAdapter(this,trailerName);
        //code to add header  to listview
        LayoutInflater trailerInflator = getLayoutInflater();
        ViewGroup trailerHeaderValue = (ViewGroup) trailerInflator.inflate(R.layout.header, trailerVideos,
                false);
        TextView headerValue = (TextView) trailerHeaderValue.findViewById(R.id.header_text);
        headerValue.setText(this.getString(R.string.trailers));
        trailerVideos.addHeaderView(trailerHeaderValue, null, false);
        //code to add header  to listview
        trailerVideos.setAdapter(trailerAdapter);
        trailerVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String movieId=trailerDetails.get(position-1).getKey();
                Uri movieUri = Uri.parse(Utils.YOUTUBE_BASE_URL).buildUpon().appendQueryParameter("v", movieId).build();
                String trailerUrl=movieUri.toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                startActivity(intent);
            }
        });

        //reviews
        for (ReviewClass review : reviewDetails)
        {
            author.add(review.getAuthor());
            content.add(review.getContent());
        }
        reviewAdapter = new ReviewAdapter(this,author,content);
        //code to add header  to listview
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup reviewHeader = (ViewGroup) inflater.inflate(R.layout.header, userReviews,
                false);
        TextView reviewHeaderValue = (TextView) reviewHeader.findViewById(R.id.header_text);
        reviewHeaderValue.setText(this.getString(R.string.reviews));
        userReviews.addHeaderView(reviewHeader, null, false);
        //code to add header  to listview

        userReviews.setAdapter(reviewAdapter);
    }
}
