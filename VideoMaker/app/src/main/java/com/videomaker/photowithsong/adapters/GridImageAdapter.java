package com.videomaker.photowithsong.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/21/2017.
 */

public class GridImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Image> imageList;

    public GridImageAdapter(Context mContext, ArrayList<Image> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemGridView = LayoutInflater.from(mContext).inflate(R.layout.item_picked_image_card, parent, false);
        ImageView thumbnail = (ImageView) itemGridView.findViewById(R.id.thumbnail);
        ImageView ivCancel = (ImageView) itemGridView.findViewById(R.id.iv_cancel);
        Glide.with(mContext).load(new File(imageList.get(position).getPath())).into(thumbnail);
        return convertView;
    }
}
