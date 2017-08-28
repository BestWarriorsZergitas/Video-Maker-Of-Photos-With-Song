package com.videomaker.photowithsong.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AdapterAblbumVideo;
import com.videomaker.photowithsong.objects.MyVideo;

import java.util.ArrayList;


public class MyVideoActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private AdapterAblbumVideo adapterAblbumVideo;
    private ArrayList<MyVideo> arrVideo= new ArrayList<>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        init();
        printNamesToLogCat(this);
    }
    public void init(){
        recycleView= (RecyclerView) findViewById(R.id.rv_fr_albumvideo);

    }
    public static void printNamesToLogCat(Context context) {

    }

}
