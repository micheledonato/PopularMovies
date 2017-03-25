package com.devmicheledonato.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devmicheledonato.popularmovies.Model.Review;

import java.util.ArrayList;

/**
 * Created by Michele on 25/03/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<Review> mDataSet;

    private static OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public ReviewsAdapter() {
    }

    public void setDataSet(ArrayList<Review> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        mContext = parent.getContext();

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.review_item_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mDataSet.get(position);

        holder.mContent.setText(review.getContent());
        holder.mAuthor.setText("[" + review.getAuthor() + "]");
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

        private TextView mContent;
        private TextView mAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mContent = (TextView) itemView.findViewById(R.id.content_text_view);
            mAuthor = (TextView) itemView.findViewById(R.id.author_text_view);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}
