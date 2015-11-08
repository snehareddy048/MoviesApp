package com.example.snehaanand.moviesapp.view;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snehaanand.moviesapp.R;
import com.example.snehaanand.moviesapp.model.MovieClass;
import com.example.snehaanand.moviesapp.model.ReviewClass;
import com.example.snehaanand.moviesapp.model.TrailerClass;
import com.example.snehaanand.moviesapp.network.DownloadWebPageTask;
import com.example.snehaanand.moviesapp.utils.Utils;
import com.example.snehaanand.moviesapp.view.MoviesProvider;
import com.example.snehaanand.moviesapp.view.adapter.ReviewAdapter;
import com.example.snehaanand.moviesapp.view.adapter.TrailerAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by snehaanandyeluguri on 10/31/15.
 */
public class DetailsActivityFragment extends Fragment {
    ArrayList<ReviewClass> reviewDetails = new ArrayList<>();
    ArrayList<TrailerClass> trailerDetails = new ArrayList<>();
    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;
    ArrayList<String> author=new ArrayList<>();
    ArrayList<String> content=new ArrayList<>();
    ArrayList<String> trailerName=new ArrayList<>();
    MovieClass movieDetails;
    static final String TRAILERS_KEY="trailers";
    static final String REVIEWS_KEY="reviews";
    static final String MOVIE_DETAILS_KEY="movie_details";
    Button favorite;
    static final String FAVORITE_TEXT="Favorite";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView movieName = (TextView) getActivity().findViewById(R.id.movieName);
        TextView userRating = (TextView) getActivity().findViewById(R.id.userRating);
        TextView releaseDate = (TextView) getActivity().findViewById(R.id.releaseDate);
        TextView synopsis = (TextView) getActivity().findViewById(R.id.synopsis);
        ImageView movieImage = (ImageView) getActivity().findViewById(R.id.movieImage);
        ListView userReviews = (ListView) getActivity().findViewById(R.id.userReviews);
        ListView trailerVideos = (ListView) getActivity().findViewById(R.id.trailerVideos);
        favorite = (Button) getActivity().findViewById(R.id.favorite);

        Bundle arguments = getArguments();
        if(savedInstanceState!=null)
        {
            movieDetails=savedInstanceState.getParcelable(MOVIE_DETAILS_KEY);
            reviewDetails=(ArrayList<ReviewClass>)savedInstanceState.get(REVIEWS_KEY);
            trailerDetails=(ArrayList<TrailerClass>)savedInstanceState.get(TRAILERS_KEY);
        }
        else if (arguments != null) {
            movieDetails = arguments.getParcelable(Utils.MOVIE_DETAILS);
            if(movieDetails!=null)
            {
                Uri reviewUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().appendPath(Utils.PATH_MOVIE).
                        appendPath(movieDetails.getId().toString()).appendPath(Utils.PATH_REVIEWS)
                        .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
                String GET_REVIEWS_URL = reviewUri.toString();

                Uri trailerUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().appendPath(Utils.PATH_MOVIE).
                        appendPath(movieDetails.getId().toString()).appendPath(Utils.PATH_VIDEOS)
                        .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
                String GET_TRAILERS_URL = trailerUri.toString();

                try {
                    JsonObject jsonObject = new DownloadWebPageTask().execute(GET_REVIEWS_URL).get();
                    JsonArray jsonArray = jsonObject.getAsJsonArray(Utils.RESULTS);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ReviewClass reviewData = new Gson().fromJson(jsonArray.get(i), ReviewClass.class);
                        reviewDetails.add(reviewData);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    JsonObject jsonObject = new DownloadWebPageTask().execute(GET_TRAILERS_URL).get();
                    JsonArray jsonArray = jsonObject.getAsJsonArray(Utils.RESULTS);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        TrailerClass trailerData = new Gson().fromJson(jsonArray.get(i), TrailerClass.class);
                        trailerDetails.add(trailerData);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
            if (movieDetails != null)
            {
                movieName.setText(movieDetails.getOriginal_title());
                userRating.setText(movieDetails.getVote_average().toString());
                releaseDate.setText(movieDetails.getRelease_date());
                synopsis.setText(movieDetails.getOverview());
                movieImage.setImageBitmap(movieDetails.getDisplay_image());
                Boolean favoriteMovie = arguments.getBoolean(Utils.FAVORITE_MOVIE_ID, false);
                final String movieId=movieDetails.getId().toString();
                if (favoriteMovie) {
                    favorite.setText(FAVORITE_TEXT);
                    unchecked(movieId);
                } else {
                    favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            favorite.setText(FAVORITE_TEXT);
                            ContentValues values = new ContentValues();
                            values.put(MoviesProvider._ID, movieDetails.getId());
                            Uri uri = getActivity().getContentResolver().insert(
                                    MoviesProvider.CONTENT_URI, values);
                            Toast.makeText(getActivity().getBaseContext(),
                                    uri.toString(), Toast.LENGTH_LONG).show();
                            unchecked(movieId);
                        }
                    });
                }

                //trailers
                for (TrailerClass trailer : trailerDetails) {
                    trailerName.add(trailer.getName());
                }
                trailerAdapter = new TrailerAdapter(getActivity(), trailerName);
                //code to add header  to listview
                LayoutInflater trailerInflator = getActivity().getLayoutInflater();
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
                        String movieId = trailerDetails.get(position - 1).getKey();
                        Uri movieUri = Uri.parse(Utils.YOUTUBE_BASE_URL).buildUpon().appendQueryParameter("v", movieId).build();
                        String trailerUrl = movieUri.toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                        startActivity(intent);
                    }
                });

                //reviews
                for (ReviewClass review : reviewDetails) {
                    author.add(review.getAuthor());
                    content.add(review.getContent());
                }
                reviewAdapter = new ReviewAdapter(getActivity(), author, content);
                //code to add header  to listview
                LayoutInflater inflater = getActivity().getLayoutInflater();
                ViewGroup reviewHeader = (ViewGroup) inflater.inflate(R.layout.header, userReviews,
                        false);
                TextView reviewHeaderValue = (TextView) reviewHeader.findViewById(R.id.header_text);
                reviewHeaderValue.setText(this.getString(R.string.reviews));
                userReviews.addHeaderView(reviewHeader, null, false);
                //code to add header  to listview

                userReviews.setAdapter(reviewAdapter);
            }
    }

    public void unchecked(final String movieId)
    {
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite.setText(getResources().getString(R.string.mark_favorite));
                getActivity().getContentResolver().delete(
                        MoviesProvider.CONTENT_URI, MoviesProvider._ID+"=?", new String[]{movieId});
            }
        });
    }

    @Override
     public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_DETAILS_KEY, movieDetails);
        outState.putParcelableArrayList(REVIEWS_KEY, reviewDetails);
        outState.putParcelableArrayList(TRAILERS_KEY,trailerDetails);
    }

}
