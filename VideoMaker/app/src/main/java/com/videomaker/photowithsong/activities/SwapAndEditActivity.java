package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.GridImageAdapter;
import com.videomaker.photowithsong.dynamicgridview.DynamicGridView;
import com.videomaker.photowithsong.objects.Image;
import com.videomaker.photowithsong.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class SwapAndEditActivity extends AppCompatActivity {
    private ArrayList<Image> imageList;
    private GridImageAdapter gridImageAdapter;
    private DynamicGridView dynamicGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_and_edit);
        Intent intent = getIntent();
        Bundle bundle=intent.getBundleExtra(Constant.IMAGE);
        imageList=bundle.getParcelableArrayList(Constant.IMAGE);
        gridImageAdapter = new GridImageAdapter(this, imageList);
        dynamicGridView=(DynamicGridView)findViewById(R.id.gv_image);
        dynamicGridView.setAdapter(gridImageAdapter);

    }
}
