package com.example.andylin.homepackagemonitor.Views.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.andylin.homepackagemonitor.Model.BoxHistory;
import com.example.andylin.homepackagemonitor.Utils.ImageUtils;

import java.util.List;

/**
 * Created by Andy Lin on 2017-03-17.
 */

public class GridImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<BoxHistory> boxHistoryList;

    public GridImageAdapter(Context c, List<BoxHistory> boxHistoryList) {
        mContext = c;
        this.boxHistoryList = boxHistoryList;
    }

    @Override
    public int getCount() {
        if (boxHistoryList == null){
            return 0;
        }
        else {
            return boxHistoryList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return boxHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return boxHistoryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        BoxHistory item = boxHistoryList.get(position);
        byte[] cameraByteArray = ImageUtils.hexStringToByteArray(item.getImageBytes());
        Bitmap bmp = BitmapFactory.decodeByteArray(cameraByteArray, 0, cameraByteArray.length);
        imageView.setImageBitmap(bmp);
        return imageView;
    }
}
