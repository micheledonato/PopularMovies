package com.devmicheledonato.popularmovies.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.devmicheledonato.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Michele on 04/02/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * TMDB URL
     */
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";

    private static final String PARAM_QUERY = "api_key";

    /**
     * IMAGE URL
     */
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    public NetworkUtils() {
    }

    public static URL buildUrlWithId(int movieId, String path){
        return buildTMDBUrl(Integer.toString(movieId), path);
    }

    public static URL buildUrl(String path){
        return buildTMDBUrl("", path);
    }

    private static URL buildTMDBUrl(String id, String typePath) {

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(typePath)
                .appendQueryParameter(PARAM_QUERY, BuildConfig.TMDB_API_KEY)
                .build();

        Log.d(TAG, "Built TMDB Uri: " + builtUri.toString());

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Uri buildImageUri(String posterPath) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(posterPath)
                .build();

        Log.d(TAG, "Built Image Uri: " + builtUri.toString());
        return builtUri;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
