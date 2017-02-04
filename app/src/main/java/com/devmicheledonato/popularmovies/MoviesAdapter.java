package com.devmicheledonato.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Michele on 04/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Movie> mDataSet;
    private static OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public MoviesAdapter() {
    }

    public void setDataSet(ArrayList<Movie> dataSet) {
        this.mDataSet = dataSet;
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        mContext = parent.getContext();

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(mContext, v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        String posterPath = mDataSet.get(position).getPosterPath();
        Uri imageUri = NetworkUtils.buildImageUri(posterPath);
        Log.d(TAG, "ImageUrl: " + imageUri.toString());

        Picasso.with(mContext)
                .load(imageUri)
                .into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (mDataSet != null) {
            return mDataSet.size();
        }
        Log.d(TAG, "ERROR DATA SET NULL");
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPosterImageView;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.poster_image_view);
            Log.d(TAG, "Height:" + mPosterImageView.getHeight());
            mPosterImageView.getLayoutParams().height = getHalfHeightDisplay(context);
        }

        private int getHalfHeightDisplay(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            Log.d(TAG, "HeightPixels: " + height);
            return height / 2;
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(view, getLayoutPosition());
        }
    }
}
