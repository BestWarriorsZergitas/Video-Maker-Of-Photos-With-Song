package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.MusicMP3;
import com.videomaker.photowithsong.utils.Constant;
import com.videomaker.photowithsong.utils.VideoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SlideShowVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoUtils video;
    private VideoView videoView;
    private MediaController mediaController;
    private TextView txtmusis, txtsong, textsave, txttitle;
    private ImageView imgcontrolermusic, imgmusic;
    private MusicMP3 musicMP3;
    private LinearLayout lnpro;
    private ImageView back, next;
    private ArrayList<Bitmap> lsBitmap;
    private ArrayList<String> paths;
    private AlertDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slide_show);
        init();

        creatFolder();
        loadbitmap();
        new AsynMakeVideo().execute(paths);

    }

    public void loadbitmap() {
        Intent data = getIntent();
        paths = data.getStringArrayListExtra(Constant.IMAGE_ARR);
        lsBitmap = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            lsBitmap.add(getBitmapFromLocalPath(paths.get(i), 1));
        }
        lsBitmap.add(lsBitmap.get(lsBitmap.size() - 1));

    }

    public void init() {
        back = (ImageView) findViewById(R.id.iv_back);
        next = (ImageView) findViewById(R.id.iv_next);
        videoView = (VideoView) findViewById(R.id.showvideo);
        txtmusis = (TextView) findViewById(R.id.txtnamemusic);
        txttitle = (TextView) findViewById(R.id.titleappbar);
        txtsong = (TextView) findViewById(R.id.txtsong);
        imgmusic = (ImageView) findViewById(R.id.iconmusic);
        imgcontrolermusic = (ImageView) findViewById(R.id.img_controlmusic);
        lnpro = (LinearLayout) findViewById(R.id.lnprocess);
        textsave = (TextView) findViewById(R.id.tv_next);
        textsave.setText(getString(R.string.save));
        txttitle.setText(getString(R.string.slide_video));
        imgcontrolermusic.setOnClickListener(this);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d("DEBUG", videoView.getDuration() + "");
            }
        });
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        txttitle.setOnClickListener(this);
        textsave.setOnClickListener(this);

    }


    public void startIntentMusic() {
        Intent intent = new Intent(this, LoadMusicActivity.class);
        startActivityForResult(intent, 500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_controlmusic: {
                startIntentMusic();
                break;
            }
            case R.id.iv_back:
            case R.id.titleappbar: {
                finish();
                break;
            }
            case R.id.iv_next:
            case R.id.tv_next: {
                createDiaglog();
            }
        }
    }

    public class AsynMakeVideo extends AsyncTask<ArrayList<String>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> bitmaps = arrayLists[0];
            video = new VideoUtils(getBaseContext(), bitmaps, Constant.PATH_TEMP + "test.mp4");
            String pavideo = video.makeVideo_();
            coppyFile(pavideo, Constant.PATH_TEMP + "test1.mp4");
            return pavideo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lnpro.setVisibility(View.INVISIBLE);
            showVideo(s);
        }

        private void showVideo(String path) {
            videoView.setVideoPath(path);
            videoView.start();
        }

    }

    public class AsynAddAudio extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arrayLists) {

            String pathaudio = arrayLists[0];
            if (pathaudio == null) {
                return null;
            } else {
                addaudiovideo(Constant.PATH_TEMP + "test.mp4", pathaudio, Constant.PATH_TEMP + "test1.mp4");
                return Constant.PATH_TEMP + "test1.mp4";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lnpro.setVisibility(View.INVISIBLE);
            showVideo(s);
        }

        private void showVideo(String path) {
            videoView.setVideoPath(path);
            videoView.start();
        }

    }

    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }
        return null;
    }

    public void addaudiovideo(String pathvideo, String pathaudio, String output) {
        MediaExtractor videoExtractor = new MediaExtractor();
        try {
            videoExtractor.setDataSource(pathvideo);
            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(pathaudio);
            Log.d("DEBUG",
                    "Video Extractor Track Count "
                            + videoExtractor.getTrackCount());
            Log.d("DEBUG",
                    "Audio Extractor Track Count "
                            + audioExtractor.getTrackCount());

            MediaMuxer muxer = new MediaMuxer(output,
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            videoExtractor.selectTrack(0);
            MediaFormat videoFormat = videoExtractor.getTrackFormat(0);
            int videoTrack = muxer.addTrack(videoFormat);

            audioExtractor.selectTrack(0);
            MediaFormat audioFormat = audioExtractor.getTrackFormat(0);
            int audioTrack = muxer.addTrack(audioFormat);

            Log.d("DEBUG", "Video Format " + videoFormat.toString());
            Log.d("DEBUG", "Audio Format " + audioFormat.toString());

            boolean sawEOS = false;
            int frameCount = 0;
            int offset = 100;
            int sampleSize = 1024 * 1024;
            ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
            ByteBuffer audioBuf = ByteBuffer.allocate(sampleSize);
            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

            muxer.start();

            while (!sawEOS) {
                videoBufferInfo.offset = offset;
                audioBufferInfo.offset = offset;
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf,
                        offset);
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuf,
                        offset);
                int i = 0;
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    Log.d("DEBUG", "saw input EOS.");
                    sawEOS = true;
                    videoBufferInfo.size = 0;
                    audioBufferInfo.size = 0;
                } else {
                    try {
                        videoBufferInfo.presentationTimeUs = videoExtractor
                                .getSampleTime();
                        videoBufferInfo.flags = videoExtractor.getSampleFlags();
                        muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo);
                        videoExtractor.advance();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        audioBufferInfo.presentationTimeUs = audioExtractor
                                .getSampleTime();
                        audioBufferInfo.flags = audioExtractor.getSampleFlags();
                        muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo);
                        audioExtractor.advance();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    frameCount++;
                    Log.d("DEBUG", "franecount " + frameCount);


                }
            }
            muxer.stop();
            muxer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void creatFolder() {
        File file = new File(Constant.PATH);
        if (!file.exists()) {
            file.mkdirs();
            File filetemp = new File(Constant.PATH_TEMP);
            if (!filetemp.exists()) {
                filetemp.mkdirs();
                Log.d("DEBUG", "filetemp not creat");
            }
            File filevideo = new File(Constant.PATH_VIDEO);
            if (!filevideo.exists()) {
                filevideo.mkdirs();
                Log.d("DEBUG", "filevideo notcreate");
            }
        }
        creatFile();

    }

    public void creatFile() {
        File filevideo = new File(Constant.PATH_TEMP + "test.mp4");
        File filevideoaudio = new File(Constant.PATH_TEMP + "test1.mp4");
        if (filevideo.exists() && filevideoaudio.exists()) {
            Log.d("DEBUG", "file created");
        } else {
            try {
                FileOutputStream out = new FileOutputStream(filevideo);
                FileOutputStream out1 = new FileOutputStream(filevideoaudio);
                Log.d("DEBUG", "file created success");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void coppyFile(String inputPath, String outputPath) {

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

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500) {
            if (data != null) {
                lnpro.setVisibility(View.VISIBLE);
                musicMP3 = (MusicMP3) data.getSerializableExtra("music");
                Log.d("DEBUG", musicMP3.getPath());
                txtmusis.setText(musicMP3.getNamemusic());
                txtsong.setText(musicMP3.getNamesong());
                new AsynAddAudio().execute(musicMP3.getPath());

            }
        }
    }


    private void createDiaglog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.diaglog_save_video, null);
        TextView btsave, btcancel;
        EditText edtxt;
        edtxt = (EditText) v.findViewById(R.id.edit_save);
        btsave = (TextView) v.findViewById(R.id.bt_save);
        btcancel = (TextView) v.findViewById(R.id.bt_cancel);
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtxt != null) {
                    moveFile(Constant.PATH_TEMP + "test1.mp4", Constant.PATH_VIDEO + edtxt.getText().toString() + ".mp4");
                    mdialog.dismiss();
                    Intent intent = new Intent(SlideShowVideoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SlideShowVideoActivity.this, getString(R.string.input_name), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SlideShowVideoActivity.this, "huy", Toast.LENGTH_SHORT).show();
                mdialog.dismiss();
            }
        });
        dialog.setView(v);
        mdialog = dialog.create();
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.show();
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
