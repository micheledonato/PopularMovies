package com.devmicheledonato.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmicheledonato.popularmovies.Model.Movie;
import com.devmicheledonato.popularmovies.Model.Review;
import com.devmicheledonato.popularmovies.Model.Video;
import com.devmicheledonato.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity implements MoviesLoader.OnMoviesLoaderActionsListener {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private static final int TRAILERS_SEARCH_LOADER = 1001;
    private static final int REVIEWS_SEARCH_LOADER = 1002;

    private ProgressBar mLoadingIndicatorDetails;

    private MoviesLoader mVideosLoader;
    private RecyclerView mVideosRecyclerView;
    private VideosAdapter mVideosAdapter;
    private ArrayList<Video> mVideos;

    private MoviesLoader mReviewsLoader;
    private RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Log.d(TAG, "onCreate");

        mLoadingIndicatorDetails = (ProgressBar) findViewById(R.id.loading_indicator_progress_bar);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(MainActivity.MOVIE_EXTRA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
            initLayout(mMovie);

            mVideosLoader = new MoviesLoader(this, getSupportLoaderManager());
            mReviewsLoader = new MoviesLoader(this, getSupportLoaderManager());
            mVideosLoader.setActionsListener(this);
            mReviewsLoader.setActionsListener(this);
            makeSearchQuery(mVideosLoader, mMovie.getId(), NetworkUtils.VIDEOS, TRAILERS_SEARCH_LOADER);
            makeSearchQuery(mReviewsLoader, mMovie.getId(), NetworkUtils.REVIEWS, REVIEWS_SEARCH_LOADER);

            mVideosRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);
            mVideosRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mVideosLayoutManager = new LinearLayoutManager(this);
            mVideosRecyclerView.setLayoutManager(mVideosLayoutManager);
            mVideosAdapter = new VideosAdapter();
            mVideosAdapter.setListener(new VideosAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    String key = mVideos.get(position).getKey();
                    startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYouTubeUri(key)));
                }
            });
            mVideosRecyclerView.setAdapter(mVideosAdapter);

            mReviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
            mReviewsRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mReviewsLayoutManager = new LinearLayoutManager(this);
            mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
            mReviewsAdapter = new ReviewsAdapter();
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        } else {
            showError(getString(R.string.error_details));
        }
    }

    private void makeSearchQuery(MoviesLoader loader, int movieId, String path, int loaderId) {
        URL searchUrl = NetworkUtils.buildUrlWithId(movieId, path);
        loader.startLoader(loaderId, searchUrl.toString());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleReviews(String data) {
        try {
            JSONObject response = new JSONObject(data);
            JSONArray results = response.getJSONArray("results");
            ArrayList<Review> reviews = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject reviewJsonObject = results.getJSONObject(i);
                Review review = new Review(reviewJsonObject);
                reviews.add(review);
            }
            mMovie.setReviews(reviews);
            if (mReviewsAdapter != null) {
                mReviewsAdapter.setDataSet(reviews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showError(getString(R.string.error_message));
        }
    }

    private void handleVideos(String data) {
        try {
            JSONObject response = new JSONObject(data);
            JSONArray results = response.getJSONArray("results");
            mVideos = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject videosJsonObject = results.getJSONObject(i);
                Video video = new Video(videosJsonObject);
                mVideos.add(video);
            }
            mMovie.setVideos(mVideos);
            if (mVideosAdapter != null) {
                mVideosAdapter.setDataSet(mVideos);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showError(getString(R.string.error_message));
        }
    }


    /*** MoviesLoader.OnMoviesLoaderActionsListener METHOD ***/

    @Override
    public void onHandleResponse(String data, int loaderID) {
        switch (loaderID) {
            case TRAILERS_SEARCH_LOADER:
                handleVideos(data);
                break;
            case REVIEWS_SEARCH_LOADER:
                handleReviews(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown LoaderID");
        }
    }

    @Override
    public void onShowProgress() {
        mLoadingIndicatorDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        mLoadingIndicatorDetails.setVisibility(View.GONE);
    }

    @Override
    public void onShowErrorMessage() {
        showError(getString(R.string.error_details));
    }
}
