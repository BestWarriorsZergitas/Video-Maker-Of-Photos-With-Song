package com.videomaker.photowithsong.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.videomaker.photowithsong.R;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PreviewVideo extends AppCompatActivity {
    private ArrayList<Bitmap> lsBitmap, ls1;
    private AndroidSequenceEncoder enc;
    private Button pressok, next;
    private ImageView img;
    private TextView txt;
    private int i = 0;
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        turnPermiss();
        init();
        pressok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        makeVideo(ls1);

                    }
                });
                thread.start();
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i < ls1.size()) {
                    img.setImageBitmap(ls1.get(i));
                    i++;
                } else {
                    i = 0;
                }
            }
        });
        video.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Make Video/test.mp4");
        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        video.start();
    }

    public ArrayList<Bitmap> reduceBitmap() {
        ArrayList<Bitmap> ls = new ArrayList<>();
        for (Bitmap bitmap : lsBitmap) {
            ls.add(getResizedBitmap(bitmap, 400));
        }
        return ls;
    }

    public void init() {
        pressok = (Button) findViewById(R.id.pressok);
        next = (Button) findViewById(R.id.pressnek);
        txt = (TextView) findViewById(R.id.txt);
        img = (ImageView) findViewById(R.id.img);
        video = (VideoView) findViewById(R.id.video);
        lsBitmap = new ArrayList<>();
        ls1 = new ArrayList<>();
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundsinhnhat));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundvalentine));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundvalentine1));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.nennoel));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.nennoel1));
        ls1 = reduceBitmap();
    }

    public File creatFile(String namefile) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Make Video");
        dir.mkdir();
        File file = new File(dir, namefile + ".mp4");
        return file;
    }

    public void makeVideo(ArrayList<Bitmap> ls) {
        File file = creatFile("test");
        try {
            FileOutputStream out = new FileOutputStream(file);
            enc = new AndroidSequenceEncoder(NIOUtils.writableChannel(file),
                    new Rational(100, 100));

            //25 100 duration 4s
            for (Bitmap bitmap : ls) {
                enc.encodeImage(bitmap);
            }
            enc.finish();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void turnPermiss() {
        try {
            //kiểm tra phiên bản SDK của máy với phiên bản android studio
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, 100);
                }
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
