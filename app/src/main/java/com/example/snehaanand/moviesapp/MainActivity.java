package com.example.snehaanand.moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.snehaanand.moviesapp.model.MovieClass;
import com.example.snehaanand.moviesapp.network.DownloadWebPageTask;
import com.example.snehaanand.moviesapp.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    List<MovieClass> movieDetails = new ArrayList<>();
    List<Integer> movieIds=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridView);
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String sortType = sharedPrefs.getString(
                getString(R.string.pref_sort_key), "popularity");

        //favorite
        String URL = "content://"+Utils.CONTENT_BASE_URL+"/students";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, MoviesProvider._ID);

        if (c!=null&&c.moveToFirst()) {
            do{
                Integer movieId=c.getInt(c.getColumnIndex(MoviesProvider._ID));
                movieIds.add(movieId);
            } while (c.moveToNext());
        }
        //

        if(sortType.equalsIgnoreCase("favorite")){
            for(Integer movieId:movieIds) {
                Uri builtUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().
                        appendPath(Utils.PATH_MOVIE).appendPath(movieId.toString())
                        .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
                String MOVIE_DB_URL = builtUri.toString();
                JsonArray movieJsonArray = new JsonArray();

                try {
                    JsonObject jsonObject = new DownloadWebPageTask().execute(MOVIE_DB_URL).get();
                    movieJsonArray.add(jsonObject);
                    movieDetails = new GetImageTask().execute(movieJsonArray).get();
                    if (movieDetails != null) {
                        gridview.setAdapter(new ImageAdapter(this, movieDetails));
                    } else {
                        Toast.makeText(getBaseContext(), R.string.no_internet_api, Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            Uri builtUri = Uri.parse(Utils.MOVIEDB_BASE_URL).buildUpon().appendPath("discover").
                    appendPath(Utils.PATH_MOVIE).appendQueryParameter("sort_by", sortType + ".desc")
                    .appendQueryParameter(Utils.QUERY_PARAMETER_API, Utils.API_KEY).build();
            String MOVIE_DB_URL = builtUri.toString();


            try {
                JsonObject jsonObject = new DownloadWebPageTask().execute(MOVIE_DB_URL).get();
                JsonArray jsonArray = jsonObject.getAsJsonArray(Utils.RESULTS);
                movieDetails = new GetImageTask().execute(jsonArray).get();
                if (movieDetails != null) {
                    gridview.setAdapter(new ImageAdapter(this, movieDetails));
                } else {
                    Toast.makeText(getBaseContext(), R.string.no_internet_api, Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                MovieClass movieClass=movieDetails.get(position);
                intent.putExtra(Utils.MOVIE_DETAILS, movieClass);
                Boolean favoriteSetting=movieIds.contains(movieClass.getId());
                if(favoriteSetting)
                {
                    intent.putExtra(Utils.FAVORITE_MOVIE_ID, true);
                }
                startActivity(intent);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
