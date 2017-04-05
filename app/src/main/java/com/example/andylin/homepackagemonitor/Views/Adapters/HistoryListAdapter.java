package com.example.andylin.homepackagemonitor.Views.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andylin.homepackagemonitor.Model.BoxHistory;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Utils.ImageUtils;

import java.util.List;

/**
 * Created by Andy Lin on 2017-03-19.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder>{
    private List<BoxHistory> boxHistoryList;
    private Activity activity;

    public HistoryListAdapter(List<BoxHistory> boxHistoryList, Activity activity){
        this.boxHistoryList = boxHistoryList;
        this.activity = activity;
    }

    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_history_list, parent, false);
        HistoryListAdapter.ViewHolder vh = new HistoryListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryListAdapter.ViewHolder holder, final int position) {
        BoxHistory item = boxHistoryList.get(position);
        byte[] cameraByteArray = ImageUtils.hexStringToByteArray(item.getImageBytes());
        Bitmap bmp = BitmapFactory.decodeByteArray(cameraByteArray, 0, cameraByteArray.length);
        holder.boxStatusImage.setImageBitmap(bmp);

        if (item.getWasGranted()){
            holder.boxStatusAccessGranted.setText("Access Granted");
            holder.boxStatusAccessGranted.setTextColor(activity.getResources().getColor(R.color.colorAccessGranted));
        }
        else {
            holder.boxStatusAccessGranted.setText("Access Denied");
            holder.boxStatusAccessGranted.setTextColor(Color.RED);
        }

        holder.boxStatusDate.setText(item.getDateAccessed());
    }

    @Override
    public int getItemCount() {
        if (boxHistoryList != null){
            return boxHistoryList.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView boxStatusImage;
        public TextView boxStatusAccessGranted;
        public TextView boxStatusDate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.boxStatusImage = (ImageView) itemView.findViewById(R.id.history_image);
            this.boxStatusAccessGranted = (TextView) itemView.findViewById(R.id.history_access_status);
            this.boxStatusDate = (TextView) itemView.findViewById(R.id.history_access_date);
        }
    }
}
