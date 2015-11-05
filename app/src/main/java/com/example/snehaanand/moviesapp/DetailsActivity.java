package com.example.snehaanand.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.snehaanand.moviesapp.model.MovieClass;
import com.example.snehaanand.moviesapp.utils.Utils;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle arguments = new Bundle();

        Bundle bundle = getIntent().getExtras();
        MovieClass movieClass = bundle.getParcelable(Utils.MOVIE_DETAILS_ACTIVITY);
        Boolean favoriteSetting = bundle.getBoolean(Utils.FAVORITE_SETTING);
        arguments.putParcelable(Utils.MOVIE_DETAILS, movieClass);
        arguments.putBoolean(Utils.FAVORITE_MOVIE_ID,favoriteSetting);
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, fragment)
                .commit();
    }
}
