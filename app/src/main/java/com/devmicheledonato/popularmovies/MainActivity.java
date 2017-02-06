package com.devmicheledonato.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_EXTRA = "MOVIE_EXTRA";

    private RecyclerView mMoviesListRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMoviesArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_progress_bar);
        initRecyclerView();

        getMovies(true);
    }

    private void getMovies(boolean popular) {
        if (NetworkUtils.isOnline(this)) {
            URL url = NetworkUtils.buildTMDBUrl(popular);
            new DownloadMoviesTask().execute(url);
        } else {
            showError(getString(R.string.no_connection));
        }
    }

    private void initRecyclerView() {
        mMoviesListRecyclerView = (RecyclerView) findViewById(R.id.movies_list_recycler_view);

        mMoviesListRecyclerView.setHasFixedSize(true);

        int spanCount = 2;
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

    private class DownloadMoviesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL requestUrl = urls[0];
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(TAG, "RESPONSE: " + response);

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (response != null && !response.equals("")) {
                try {
                    handleResponse(response);
                    mMoviesAdapter.setDataSet(mMoviesArrayList);
                    mMoviesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showError(getString(R.string.error_message));
            }
        }
    }

    private void handleResponse(String responseString) throws JSONException {
        JSONObject response = new JSONObject(responseString);
        JSONArray results = response.getJSONArray("results");
        mMoviesArrayList = new ArrayList<Movie>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJsonObject = results.getJSONObject(i);
            Movie movie = new Movie(movieJsonObject);
            mMoviesArrayList.add(movie);
        }
    }

    private void showError(String errorMessage) {
        Snackbar.make(findViewById(R.id.activity_main),
                errorMessage, Snackbar.LENGTH_LONG)
                .show();
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
                getMovies(true);
                return true;
            case R.id.top_rated_movies:
                getMovies(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
