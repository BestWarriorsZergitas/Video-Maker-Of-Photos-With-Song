package com.videomaker.photowithsong.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AlbumAdapter;
import com.videomaker.photowithsong.adapters.ImageAdapter;
import com.videomaker.photowithsong.adapters.PickedImageAdapter;
import com.videomaker.photowithsong.fragments.AlbumFragment;
import com.videomaker.photowithsong.fragments.ImageFragment;
import com.videomaker.photowithsong.objects.Image;
import com.videomaker.photowithsong.utils.ManagerGalary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peih Gnaoh on 8/17/2017.
 */

public class ImagePickerActivity extends AppCompatActivity implements AlbumAdapter.OnClickAlbum, View.OnClickListener, ImageAdapter.OnClickImage ,PickedImageAdapter.OnClickCancel{
    private List<Image> arrImagesAlbum;
    private List<String> imagePaths;
    private ManagerGalary managerGalary;
    private AlbumFragment albumFragment;
    private ImageFragment imageFragment;
    private ImageView ivBack;
    private List<Image> imagesAlBum;
    private List<Image> imagesPicked;
    private RecyclerView rvPickedImage;
    private PickedImageAdapter pickedImageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_picker);
        initUI();
    }

    private void initUI() {
        rvPickedImage = (RecyclerView) findViewById(R.id.rv_picked_image);
        rvPickedImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesPicked = new ArrayList<>();
        pickedImageAdapter = new PickedImageAdapter(imagesPicked, this,this);
        pickedImageAdapter.setHasStableIds(true);
        rvPickedImage.setAdapter(pickedImageAdapter);
        managerGalary = new ManagerGalary(this);
        arrImagesAlbum = managerGalary.getArrImage();
        imagePaths = managerGalary.getImagePaths();
        albumFragment = new AlbumFragment();
        albumFragment.setOnClickAlbum(this);
        albumFragment.setImagesAlbum(arrImagesAlbum);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, albumFragment);
        t.commit();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                break;
            default:
                break;
        }
    }

    @Override
    public void onClickImage(int position) {
        imagesPicked.add(imagesAlBum.get(position));
        pickedImageAdapter.notifyDataSetChanged();
        rvPickedImage.smoothScrollToPosition(imagesPicked.size()-1);
        Log.e("dasdasd", imagesPicked.toString());

    }

    @Override
    public void onClickAlbum(int position) {
        imagesAlBum = albumFragment.getArrAlbum().get(position).getArrImage();
        imageFragment = new ImageFragment();
        imageFragment.setImagesAlbum(imagesAlBum);
        imageFragment.setOnClickImage(this);
        Log.e("ádas", "onClick" + position);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, imageFragment);
        t.commit();
    }

    @Override
    public void onClick(int position) {
        imagesPicked.remove(imagesPicked.get(position));
        pickedImageAdapter.notifyDataSetChanged();
    }
}
