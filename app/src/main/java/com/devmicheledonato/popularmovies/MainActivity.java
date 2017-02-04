package com.devmicheledonato.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesListRecyclerView;
    private MoviesAdapter mMoviesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        getMovies();
        initRecyclerView();
    }

    private void getMovies(){
        URL url = NetworkUtils.buildTMDBUrl(this, true);
        new DownloadMoviesTask().execute(url);
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
//                Intent i = new Intent(this, detail);
//                startActivity(i);
            }
        });
        mMoviesListRecyclerView.setAdapter(mMoviesAdapter);
    }

    private class DownloadMoviesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {

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
        protected void onPostExecute(String s) {
            Log.d(TAG, "RESPONSE: " + s);


            //mMoviesAdapter.setDataSet();
            //mMoviesAdapter.notifyDataSetChanged();
        }
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

                return true;
            case R.id.top_rated_movies:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
