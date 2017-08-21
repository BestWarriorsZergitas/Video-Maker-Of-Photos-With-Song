package com.videomaker.photowithsong.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.fragments.AlbumFragment;
import com.videomaker.photowithsong.objects.Image;
import com.videomaker.photowithsong.utils.ManagerGalary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/17/2017.
 */

public class ImagePickerActivity extends AppCompatActivity{
    private List<Image> arrImagesAlbum;
    private List<String> imagePaths;
    private ManagerGalary managerGalary;
    private AlbumFragment albumFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        initUI();
    }

    private void initUI() {
        managerGalary=new ManagerGalary(this);
        arrImagesAlbum=managerGalary.getArrImage();
        imagePaths=managerGalary.getImagePaths();
        albumFragment=new AlbumFragment();
        albumFragment.setImagesAlbum(arrImagesAlbum);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, albumFragment);
        t.commit();
    }
}
