package com.videomaker.photowithsong.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.Album;
import com.videomaker.photowithsong.objects.Image;

import java.io.File;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/20/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemImage> {
    private List<Image> imageList;
    private Context mContext;

    public ImageAdapter(List<Image> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @Override
    public ItemImage onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(mContext).inflate(R.layout.item_image_card,parent,false);
        return new ItemImage(itemView);
    }

    @Override
    public void onBindViewHolder(ItemImage holder, int position) {
        Glide.with(mContext).load(new File(imageList.get(position).getPath())).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ItemImage extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        public ItemImage(View itemView) {
            super(itemView);
            thumbnail=(ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
