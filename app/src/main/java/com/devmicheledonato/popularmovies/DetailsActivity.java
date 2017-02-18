package com.devmicheledonato.popularmovies;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmicheledonato.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Log.d(TAG, "onCreate");

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(MainActivity.MOVIE_EXTRA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
            initLayout(mMovie);
        } else {
            showError(getString(R.string.error_details));
        }
    }

    private void initLayout(Movie movie) {
        ImageView poster = (ImageView) findViewById(R.id.detail_poster_image_view);
        Picasso.with(this).load(movie.getPosterUri()).into(poster);
        TextView title = (TextView) findViewById(R.id.detail_title_text_view);
        title.setText(movie.getOriginalTitle());
        TextView date = (TextView) findViewById(R.id.detail_date_text_view);
        date.setText(movie.getReleaseDate());
        TextView vote = (TextView) findViewById(R.id.detail_vote_text_view);
        vote.setText(String.valueOf(movie.getVoteAverage()));
        TextView overview = (TextView) findViewById(R.id.detail_overview_text_view);
        overview.setText(movie.getOverview());
    }

    private void showError(String errorMessage) {
        Snackbar.make(findViewById(R.id.activity_details),
                errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }
}
