package com.videomaker.photowithsong.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AlbumAdapter;
import com.videomaker.photowithsong.adapters.ImageAdapter;
import com.videomaker.photowithsong.objects.Album;
import com.videomaker.photowithsong.objects.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/20/2017.
 */

public class ImageFragment extends Fragment{
    private RecyclerView recycleView;
    private ImageAdapter imageAdapter;
    private List<Image> arrImage = new ArrayList<>();
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity().getBaseContext();
    }
    public void setImagesAlbum(List<Image> arrImage) {
        this.arrImage = arrImage;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_image,container,false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        recycleView = (RecyclerView) rootView.findViewById(R.id.rv_fr_image);
        recycleView.setLayoutManager(new GridLayoutManager(mContext, 3));
        imageAdapter = new ImageAdapter(arrImage,mContext);
        recycleView.setAdapter(imageAdapter);
    }
}
