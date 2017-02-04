package com.devmicheledonato.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";

    private static final String PARAM_QUERY = "api_key";


    public NetworkUtils() {
    }

    /**
     * @param popular true for popular
     *                false for top_rated
     * @return
     */

    public static URL buildTMDBUrl(Context context, boolean popular) {

        String typePath = TOP_RATED;
        if (popular) {
            typePath = POPULAR;
        }

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(typePath)
                .appendQueryParameter(PARAM_QUERY, context.getString(R.string.TMDB_API_KEY))
                .build();

        Log.d(TAG, "Build Uri: " + builtUri.toString());

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
                .appendPath(posterPath)
                .build();
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
}
