package com.videomaker.photowithsong.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AdapterAblbumVideo;
import com.videomaker.photowithsong.objects.MyVideo;
import com.videomaker.photowithsong.utils.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class MyVideoActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private AdapterAblbumVideo adapterAblbumVideo;
    private ArrayList<MyVideo> arrVideo = new ArrayList<>();
    private Context mContext;
    private TextView textsave, titleappbar;
    private Dialog mdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_video);
        init();
        new loadVideo().execute();
        String data = "";
        try {
            data = getIntent().getStringExtra("DATA");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data.equals("check")) {
            createDiaglog();
        }

    }

    public void init() {
        recycleView = (RecyclerView) findViewById(R.id.rv_fr_albumvideo);
        textsave = (TextView) findViewById(R.id.tv_next);
        textsave.setVisibility(View.INVISIBLE);
        titleappbar = (TextView) findViewById(R.id.titleappbar);
        titleappbar.setText("My Video");
        arrVideo = new ArrayList<>();
        recycleView.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapterAblbumVideo = new AdapterAblbumVideo(getBaseContext(), arrVideo);
        recycleView.setAdapter(adapterAblbumVideo);

    }

    private void createDiaglog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.diaglog_save_video, null);
        Button btsave, btcancel;
        EditText edtxt;
        edtxt = (EditText) v.findViewById(R.id.edit_save);
        btsave = (Button) v.findViewById(R.id.bt_save);
        btcancel = (Button) v.findViewById(R.id.bt_cancel);
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtxt != null) {
                    moveFile(Constant.PATH_TEMP + "test1.mp4", Constant.PATH_VIDEO + edtxt.getText().toString() + ".mp4");
                    mdialog.dismiss();
                    new loadVideo().execute();
                    adapterAblbumVideo.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyVideoActivity.this, "Nhập tên video", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyVideoActivity.this, "huy", Toast.LENGTH_SHORT).show();
                mdialog.dismiss();
            }
        });
        dialog.setView(v);
        mdialog = dialog.create();
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.show();
    }

    public class loadVideo extends AsyncTask<Void, Void, ArrayList<MyVideo>> {
        @Override
        protected ArrayList<MyVideo> doInBackground(Void... voids) {
            return getPlayListvideo();
        }

        @Override
        protected void onPostExecute(ArrayList<MyVideo> objectMusics) {
            adapterAblbumVideo.setLsMyvideo(arrVideo);
            adapterAblbumVideo.notifyDataSetChanged();
            this.cancel(true);

        }

        public ArrayList<MyVideo> getPlayListvideo() {
            arrVideo.clear();
            File home = new File(Environment.getExternalStorageDirectory().getPath() + "/Make Video/My video");
            for (File file : home.listFiles()) {
                if (file.isDirectory()) {
                    for (File file1 : file.listFiles(new FileExtensionFilter())) {
                        HashMap<String, String> song = new HashMap<>();
                        song.put("file_name", file1.getName());
                        song.put("file_path", file1.getPath());
                        // Adding each song to SongList
                        arrVideo.add(new MyVideo(song.get("file_name"), song.get("file_path")));

                    }
                } else {
                    if (file.getName().endsWith(".mp4") || file.getName().endsWith(".MP4")) {
                        HashMap<String, String> song = new HashMap<>();
                        song.put("file_name", file.getName());
                        song.put("file_path", file.getPath());
                        // Adding each song to SongList
                        arrVideo.add(new MyVideo(song.get("file_name"), song.get("file_path")));
                    }
                }
            }

            // return songs list array
            return arrVideo;
        }

        /**
         * Class to filter files which are having .mp3 extension
         */
        //you can choose the filter for me i put .mp3
        class FileExtensionFilter implements FilenameFilter {
            public boolean accept(File dir, String name) {
                return (name.endsWith(".mp4") || name.endsWith(".MP4"));
            }
        }
    }

    private void moveFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath).delete();


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


}
