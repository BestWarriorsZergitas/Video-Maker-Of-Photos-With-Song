package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;
import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.RecyclerListAdapter;
import com.videomaker.photowithsong.helper.OnStartDragListener;
import com.videomaker.photowithsong.helper.SimpleItemTouchHelperCallback;
import com.videomaker.photowithsong.objects.Image;
import com.videomaker.photowithsong.utils.Constant;

import java.io.File;
import java.util.ArrayList;

import ly.img.android.sdk.models.constant.Directory;
import ly.img.android.sdk.models.state.EditorLoadSettings;
import ly.img.android.sdk.models.state.EditorSaveSettings;
import ly.img.android.sdk.models.state.manager.SettingsList;
import ly.img.android.ui.activities.ImgLyIntent;
import ly.img.android.ui.activities.PhotoEditorBuilder;

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

        ivBack=(ImageView)findViewById(R.id.iv_back);
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
        startEditor(imageList.get(position).getPath());
    }

    public void startEditor(String path) {
        Log.e("TAG","ok");
        String myPicture = path;
        SettingsList settingsList = new SettingsList();
        settingsList.getSettingsModel(EditorLoadSettings.class)
                .setImageSourcePath(myPicture, true) // Load with delete protection true!

                .getSettingsModel(EditorSaveSettings.class)
                .setExportDir(Directory.DCIM, "")
                .setExportPrefix("result_")
                .setSavePolicy(
                        EditorSaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT
                );
        new PhotoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, CAMERA_PREVIEW_RESULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
            String resultPath =
                    data.getStringExtra(ImgLyIntent.RESULT_IMAGE_PATH);
            String sourcePath =
                    data.getStringExtra(ImgLyIntent.SOURCE_IMAGE_PATH);

            if (resultPath != null) {
                // Scan result file
                File file =  new File(resultPath);
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                scanIntent.setData(contentUri);
                sendBroadcast(scanIntent);
            }

            if (sourcePath != null) {
                // Scan camera file
                File file =  new File(sourcePath);
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                scanIntent.setData(contentUri);
                sendBroadcast(scanIntent);
            }
            for (int i=0;i<imageList.size();i++){
                if (imageList.get(i).getPath().equals(sourcePath)){
                    imageList.get(i).setPath(resultPath);
                    break;
                }
            }
            adapter.notifyDataSetChanged();

        }
    }

}
