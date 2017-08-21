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

import java.io.File;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/20/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ItemAlbum> {
    private Context mContext;
    private List<Album> albumList;
    private OnClickAlbum onClickAlbum;

    public AlbumAdapter(Context mContext, List<Album> albumList, OnClickAlbum onClickAlbum) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.onClickAlbum = onClickAlbum;
    }

    @Override
    public ItemAlbum onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album_card, parent, false);
        return new ItemAlbum(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAlbum holder, final int position) {
        holder.title.setText(albumList.get(position).getBucket());
        holder.size.setText(albumList.get(position).getArrImage().size()+"");
        Glide.with(mContext).load(new File(albumList.get(position).getArrImage().get(0).getPath())).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAlbum.onClickAlbum(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ItemAlbum extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title, size;

        public ItemAlbum(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            size = (TextView) itemView.findViewById(R.id.count);
        }
    }
    public interface OnClickAlbum{
        void onClickAlbum(int position);
    }

}
