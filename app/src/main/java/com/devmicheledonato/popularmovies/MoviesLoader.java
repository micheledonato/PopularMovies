package com.devmicheledonato.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import com.devmicheledonato.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Michele on 22/03/2017.
 */

public class MoviesLoader implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MoviesLoader.class.getSimpleName();

    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    private Context mContext;
    private LoaderManager mLoaderManager;
    private int mLoaderID;

    private OnMoviesLoaderActionsListener mActionsListener;

    public interface OnMoviesLoaderActionsListener {
        void onShowProgress();

        void onHideProgress();

        void onShowErrorMessage();

        void onHandleResponse(String data, int loaderID);
    }

    public void setActionsListener(OnMoviesLoaderActionsListener listener) {
        mActionsListener = listener;
    }

    public MoviesLoader(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
    }

    public void startLoader(int loaderID, String url) {

        mLoaderID = loaderID;

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, url);

        Loader<String> searchLoader = mLoaderManager.getLoader(loaderID);
        if (searchLoader == null) {
            mLoaderManager.initLoader(loaderID, queryBundle, this);
        } else {
            mLoaderManager.restartLoader(loaderID, queryBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {

            String mJson;

            @Override
            protected void onStartLoading() {
                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }

                mActionsListener.onShowProgress();

                if (mJson != null) {
                    deliverResult(mJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

                /* Extract the search query from the args using our constant */
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);

                /* If the user didn't enter anything, there's nothing to search for */
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }

                /* Parse the URL from the passed in String and perform the search */
                try {
                    URL queryUrl = new URL(searchQueryUrlString);
                    return NetworkUtils.getResponseFromHttpUrl(queryUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mActionsListener.onHideProgress();
                mJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, String data) {
        if (data == null) {
            mActionsListener.onShowErrorMessage();
        } else {
            Log.d(TAG, mLoaderID + ": " + data);
            mActionsListener.onHandleResponse(data, mLoaderID);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // Nothing to do
    }
}
