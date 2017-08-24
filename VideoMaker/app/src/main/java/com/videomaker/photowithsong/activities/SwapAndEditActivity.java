package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.RecyclerListAdapter;
import com.videomaker.photowithsong.helper.OnStartDragListener;
import com.videomaker.photowithsong.helper.SimpleItemTouchHelperCallback;
import com.videomaker.photowithsong.objects.Image;
import com.videomaker.photowithsong.utils.Constant;

import java.io.File;
import java.util.ArrayList;

public class SwapAndEditActivity extends AppCompatActivity implements OnStartDragListener, RecyclerListAdapter.OnClickImageEdit {

    public static int CAMERA_PREVIEW_RESULT = 1;
    private ArrayList<Image> imageList;
    private RecyclerView recyclerView;
    private RecyclerListAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private TextView tvNext;
    private Image imageClick;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_swap_and_edit);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.IMAGE);
        imageList = bundle.getParcelableArrayList(Constant.IMAGE);
        recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RecyclerListAdapter(this, this, imageList, this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClickImageEdit(int position) {
        imageList = adapter.getmItems();
        Uri imageUri = Uri.fromFile(new File(imageList.get(position).getPath()));
    /* 2) Create a new Intent */
        Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
                .setData(imageUri)
                .build();
    /* 3) Start the Image Editor with request code 1 */
        startActivityForResult(imageEditorIntent, 1);
    }

}
