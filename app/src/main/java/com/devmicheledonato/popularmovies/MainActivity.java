package com.devmicheledonato.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.devmicheledonato.popularmovies.Model.Movie;
import com.devmicheledonato.popularmovies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesLoader.OnMoviesLoaderActionsListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_EXTRA = "MOVIE_EXTRA";

    private static final int MOVIES_SEARCH_LOADER = 1000;

    private RecyclerView mMoviesListRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private MoviesLoader mMoviesLoader;

    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMoviesArrayList = null;

    private static final String PATH = "path_state";
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_progress_bar);
        initRecyclerView();

        mMoviesLoader = new MoviesLoader(this, getSupportLoaderManager());
        mMoviesLoader.setActionsListener(this);

        if (savedInstanceState != null) {
            mPath = savedInstanceState.getString(PATH);
            getMovies(mPath);
        } else {
            getMovies(NetworkUtils.POPULAR);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PATH, mPath);
        super.onSaveInstanceState(outState);
    }

    private void getMovies(String path) {
        mPath = path;
        if (NetworkUtils.isOnline(this)) {
            URL url = NetworkUtils.buildUrl(path);
            mMoviesLoader.startLoader(MOVIES_SEARCH_LOADER, url.toString());
        } else {
            showError(getString(R.string.no_connection));
        }
    }

    private void initRecyclerView() {
        mMoviesListRecyclerView = (RecyclerView) findViewById(R.id.movies_list_recycler_view);

        mMoviesListRecyclerView.setHasFixedSize(true);

        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }
        int orientation = GridLayoutManager.VERTICAL;
        boolean reverseLayout = false;
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, spanCount, orientation, reverseLayout);
        mMoviesListRecyclerView.setLayoutManager(mLayoutManager);

        mMoviesAdapter = new MoviesAdapter();
        mMoviesAdapter.setListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra(MOVIE_EXTRA, mMoviesArrayList.get(position));
                startActivity(i);
            }
        });
        mMoviesListRecyclerView.setAdapter(mMoviesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular_movies:
                getMovies(NetworkUtils.POPULAR);
                return true;
            case R.id.top_rated_movies:
                getMovies(NetworkUtils.TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showError(String errorMessage) {
        Snackbar.make(findViewById(R.id.activity_main),
                errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onShowProgress() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onShowErrorMessage() {
        showError(getString(R.string.error_message));
    }

    @Override
    public void onHandleResponse(String data, int loaderID) {
        try {
            JSONObject response = new JSONObject(data);
            JSONArray results = response.getJSONArray("results");
            mMoviesArrayList = new ArrayList<Movie>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJsonObject = results.getJSONObject(i);
                Movie movie = new Movie(movieJsonObject);
                mMoviesArrayList.add(movie);
            }
            mMoviesAdapter.setDataSet(mMoviesArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
            showError(getString(R.string.error_message));
        }
    }
}
