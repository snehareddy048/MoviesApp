package com.example.snehaanand.moviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        MovieClass movieDetails = getIntent().getParcelableExtra(MainActivity.MOVIE_DETAILS);
        movieName.setText(movieDetails.getOriginal_title());
        userRating.setText(movieDetails.getVote_average().toString());
        releaseDate.setText(movieDetails.getRelease_date());
        synopsis.setText(movieDetails.getOverview());
        movieImage.setImageBitmap(movieDetails.getDisplay_image());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
