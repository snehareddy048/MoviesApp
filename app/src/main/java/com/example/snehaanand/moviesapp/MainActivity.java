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

public class MainActivity extends AppCompatActivity implements MainActivityFragment.PaneSelection {
Boolean mTwoPane;
    String DETAILS_TAG="details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_popularMovies_detail) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            if (savedInstanceState == null) {//TODO:know use of this
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_popularMovies_detail, new DetailsActivityFragment(), DETAILS_TAG)
                        .commit();
            //}
        } else {
            mTwoPane = false;
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

    @Override
    public void onItemSelection(MovieClass movieClass,Boolean favoriteSetting) {
        if(mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putParcelable(Utils.MOVIE_DETAILS, movieClass);
            if (favoriteSetting)
                 arguments.putBoolean(Utils.FAVORITE_MOVIE_ID, true);

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_popularMovies_detail, fragment, DETAILS_TAG)
                    .commit();

        }
        else{
            Intent intent = new Intent(this, DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Utils.MOVIE_DETAILS, movieClass);

            //intent.putExtra(Utils.MOVIE_DETAILS, movieClass);
            if (favoriteSetting) {
                bundle.putBoolean(Utils.FAVORITE_MOVIE_ID, true);
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
