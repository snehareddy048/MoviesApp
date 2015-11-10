package com.example.snehaanand.moviesapp.view;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.snehaanand.moviesapp.R;
import com.example.snehaanand.moviesapp.model.MovieClass;
import com.example.snehaanand.moviesapp.network.DownloadWebPageTask;
import com.example.snehaanand.moviesapp.utils.Utils;
import com.example.snehaanand.moviesapp.view.adapter.ImageAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by snehaanandyeluguri on 10/31/15.
 */
public class MainActivityFragment extends Fragment {
    ArrayList<MovieClass> movieDetails = new ArrayList<>();
    ArrayList<Integer> movieIds = new ArrayList<>();
    GridView gridview;
    public final String FAVORITE_MOVIES = "favorite_movies";
    public final String MOVIE_KEY = "movie_list_key";
    Uri movies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private class GetImageTask extends AsyncTask<JsonArray, Void, List> {
        @Override
        protected List<MovieClass> doInBackground(JsonArray... jsonArray) {
            return parseResult(jsonArray[0]);
        }

        private List<MovieClass> parseResult(JsonArray jsonArray) {
            for (int i = 0; i < jsonArray.size(); i++) {
                MovieClass data = new Gson().fromJson(jsonArray.get(i), MovieClass.class);
                try {
                    URL url = new URL("http://image.tmdb.org/t/p/w185/" + data.getPoster_path());
                    data.setDisplay_image(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                    movieDetails.add(data);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return movieDetails;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageAdapter imageAdapter = new ImageAdapter(getActivity(), movieDetails);
        String URL = "content://" + Utils.CONTENT_BASE_URL + "/" + Utils.MOVIES_TEXT;
        movies = Uri.parse(URL);

        gridview = (GridView) getActivity().findViewById(R.id.gridView);
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String sortType = sharedPrefs.getString(
                getString(R.string.pref_sort_key), "popularity");

        gridview.setAdapter(imageAdapter);
        if (savedInstanceState != null) {
            movieIds = savedInstanceState.getIntegerArrayList(FAVORITE_MOVIES);
            movieDetails = (ArrayList<MovieClass>) savedInstanceState.get(MOVIE_KEY);
            imageAdapter.setGridData(movieDetails);
        } else if (!isNetworkAvailable()) {
            Toast.makeText(getActivity().getBaseContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
        } else if (sortType.equalsIgnoreCase("favorite")) {


            Cursor c = getActivity().getContentResolver().query(movies, null, null, null, MoviesProvider._ID);

            if (c != null && c.moveToFirst()) {
                do {
                    Integer movieId = c.getInt(c.getColumnIndex(MoviesProvider._ID));
                    movieIds.add(movieId);
                } while (c.moveToNext());
            }

            for (Integer movieId : movieIds) {
                Uri builtUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().
                        appendPath(Utils.PATH_MOVIE).appendPath(movieId.toString())
                        .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
                String MOVIE_DB_URL = builtUri.toString();
                JsonArray movieJsonArray = new JsonArray();

                try {
                    JsonObject jsonObject = new DownloadWebPageTask().execute(MOVIE_DB_URL).get();
                    movieJsonArray.add(jsonObject);
                    movieDetails = (ArrayList<MovieClass>) new GetImageTask().execute(movieJsonArray).get();
                    if (movieDetails != null) {
                        imageAdapter.setGridData(movieDetails);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Uri builtUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().appendPath("discover").
                    appendPath(Utils.PATH_MOVIE).appendQueryParameter("sort_by", sortType + ".desc")
                    .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
            String MOVIE_DB_URL = builtUri.toString();

            try {
                JsonObject jsonObject = new DownloadWebPageTask().execute(MOVIE_DB_URL).get();
                JsonArray jsonArray = jsonObject.getAsJsonArray(Utils.RESULTS);
                movieDetails = (ArrayList<MovieClass>) new GetImageTask().execute(jsonArray).get();
                if (movieDetails != null) {
                    imageAdapter.setGridData(movieDetails);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MovieClass movieClass = movieDetails.get(position);
                Cursor cursor = getActivity().getContentResolver().query(movies, null, MoviesProvider._ID+"=?", new String[]{movieClass.getId().toString()}, MoviesProvider._ID);
                boolean favoriteSetting=false;
                cursor.moveToFirst();
                if(cursor!=null&cursor.getCount()>0){
                    favoriteSetting=true;
                }
                PaneSelection paneSelection = (MainActivityFragment.PaneSelection) getActivity();
                paneSelection.onItemSelection(movieClass, favoriteSetting);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface PaneSelection {
        void onItemSelection(MovieClass movieClass, Boolean favoriteSetting);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(FAVORITE_MOVIES, movieIds);
        outState.putParcelableArrayList(MOVIE_KEY, movieDetails);
    }
}
