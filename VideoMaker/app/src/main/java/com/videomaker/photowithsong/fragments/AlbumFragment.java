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
import com.videomaker.photowithsong.objects.Album;
import com.videomaker.photowithsong.objects.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/20/2017.
 */

public class AlbumFragment extends Fragment {
    private RecyclerView recycleView;
    private AlbumAdapter albumAdapter;
    private List<Album> arrAlbum = new ArrayList<>();
    private List<Image> arrImage = new ArrayList<>();
    private List<String> arrBucketAlbum = new ArrayList<>();
    private Context mContext;
    private AlbumAdapter.OnClickAlbum onClickAlbum;


    public void setOnClickAlbum(AlbumAdapter.OnClickAlbum onClickAlbum) {
        this.onClickAlbum = onClickAlbum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = getActivity().getBaseContext();
    }

    public void setImagesAlbum(List<Image> arrImage) {
        this.arrImage = arrImage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        arrAlbum.clear();
        for (int i = 0; i < arrImage.size(); i++) {
            getBucket(arrImage.get(i).getBucket(), arrBucketAlbum);
        }
        for (int i = 0; i < arrBucketAlbum.size(); i++) {
            Album album = new Album(arrBucketAlbum.get(i), arrImage.get(0).getPath(), new ArrayList<Image>());
            for (int j = 0; j < arrImage.size(); j++) {
                if (arrBucketAlbum.get(i).equals(arrImage.get(j).getBucket())) {
                    album.addImage(arrImage.get(j));
                }
            }
            arrAlbum.add(album);
        }
        recycleView = (RecyclerView) rootView.findViewById(R.id.rv_fr_album);
        recycleView.setLayoutManager(new GridLayoutManager(mContext, 2));
        albumAdapter = new AlbumAdapter(mContext, arrAlbum, onClickAlbum);
        recycleView.setAdapter(albumAdapter);
    }

    public void getBucket(String bucket, List<String> arrBucket) {
        boolean isAdd = false;
        for (int i = 0; i < arrBucket.size(); i++) {
            if (bucket.equals(arrBucket.get(i))) {
                isAdd = true;
            }
        }
        if (!isAdd) {
            arrBucketAlbum.add(bucket);
        }
    }

    public List<Album> getArrAlbum() {
        return arrAlbum;
    }
}
