package com.example.snehaanand.moviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "MainActivity";
    private static final String URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[Your API Key]";
    List<MovieClass> movieDetails = new ArrayList<>();
    public static final String MOVIE_DETAILS="MOVIE_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridView);

        try {
            movieDetails = new DownloadWebpageTask().execute(URL).get();
            gridview.setAdapter(new ImageAdapter(this, movieDetails));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(MOVIE_DETAILS,movieDetails.get(position));
                startActivity(intent);
            }
        });
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, List> {

        @Override
        protected List<MovieClass> doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();//TODO:show error to user
            }
            return null;
        }

        //TODO:display progress status in post execute
        @Override
        protected void onPostExecute(List result) {
            Toast.makeText(MainActivity.this, "server data:" + result.size(), Toast.LENGTH_LONG).show();
        }

        private List<MovieClass> downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line;
                String result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                return parseResult(result);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private List<MovieClass> parseResult(String jsonElements) {
            JsonElement jsonElement = new JsonParser().parse(jsonElements);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("results");
            for (int i = 0; i < jsonArray.size(); i++) {
                MovieClass data = new Gson().fromJson(jsonArray.get(i), MovieClass.class);
                //movieImages.add("http://image.tmdb.org/t/p/w185/"+data.getPoster_path());
                try {
                    URL url = new URL("http://image.tmdb.org/t/p/w185/" + data.getPoster_path());
                    data.setDisplay_image(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                    movieDetails.add(data);
                    //movieDetails.add();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
