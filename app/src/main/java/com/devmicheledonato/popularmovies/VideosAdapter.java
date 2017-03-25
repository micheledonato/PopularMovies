package com.devmicheledonato.popularmovies;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmicheledonato.popularmovies.Model.Video;

import java.util.ArrayList;

/**
 * Created by Michele on 25/03/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private static final String TAG = VideosAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Video> mDataSet;
    private static OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public VideosAdapter() {
    }

    public void setDataSet(ArrayList<Video> dataSet) {
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
                .inflate(R.layout.video_item_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mDataSet.get(position);
        holder.mNameTextView.setText(video.getName());

        if (video.getSite().equals("YouTube")) {
            holder.mLogoImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.logo_tube));
        }
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

        private ImageView mLogoImageView;
        private TextView mNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mLogoImageView = (ImageView) itemView.findViewById(R.id.logo_image_view);
            mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getLayoutPosition());
        }
    }
}
