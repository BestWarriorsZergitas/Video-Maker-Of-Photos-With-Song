package com.videomaker.photowithsong.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.MyVideo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by DaiPhongPC on 8/28/2017.
 */

public class AdapterAblbumVideo extends RecyclerView.Adapter<AdapterAblbumVideo.ItemVideo> {
    private Context context;
    private ArrayList<MyVideo> lsMyvideo;

    public AdapterAblbumVideo(Context context, ArrayList<MyVideo> lsMyvideo) {
        this.context = context;
        this.lsMyvideo = lsMyvideo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<MyVideo> getLsMyvideo() {
        return lsMyvideo;
    }

    public void setLsMyvideo(ArrayList<MyVideo> lsMyvideo) {
        this.lsMyvideo = lsMyvideo;
    }

    @Override
    public ItemVideo onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new ItemVideo(itemView);
    }

    @Override
    public void onBindViewHolder(ItemVideo holder, int position) {
        holder.titlevideo.setText(lsMyvideo.get(position).getNamevideo());
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(lsMyvideo.get(position).getPathvideo(),
                MediaStore.Images.Thumbnails.MINI_KIND);
        int duration = 0;
        try {
            duration = MediaPlayer.create(context, Uri.fromFile(new File(lsMyvideo.get(position).getPathvideo()))).getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.prviewvideo.setImageBitmap(thumbnail);
        int hour = (int) (duration / 3600000);
        int minus = (int) (duration % 3600000) / 60000;
        int sec = (int) (duration - (hour * 3600000 + 60000 * minus)) / 1000;
        String time = hour + "h:" + minus + "m:" + sec + "s";
        holder.timevideo.setText(time);
        holder.prviewvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String type = "video/mp4";
                Uri uri = Uri.parse(lsMyvideo.get(position).getPathvideo());
                intent.setDataAndType(uri, type);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lsMyvideo.size();
    }


    public class ItemVideo extends RecyclerView.ViewHolder {
        ImageView prviewvideo;
        TextView titlevideo, timevideo;

        public ItemVideo(View itemView) {
            super(itemView);
            prviewvideo = (ImageView) itemView.findViewById(R.id.preview_video);
            titlevideo = (TextView) itemView.findViewById(R.id.titlevideo);
            timevideo = (TextView) itemView.findViewById(R.id.counttime);
        }
    }


}
