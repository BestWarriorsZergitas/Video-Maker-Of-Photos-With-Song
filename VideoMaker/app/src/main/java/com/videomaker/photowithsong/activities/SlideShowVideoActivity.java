package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.MusicMP3;
import com.videomaker.photowithsong.utils.Constant;
import com.videomaker.photowithsong.utils.VideoUilt;

import org.mp4parser.Container;
import org.mp4parser.IsoFile;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class SlideShowVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoUilt video;
    private VideoView videoView;
    private MediaController mediaController;
    private TextView txtmusis, txtsong;
    private ImageView imgcontrolermusic, imgmusic;
    private MusicMP3 musicMP3;
    private LinearLayout lnpro;
    private ArrayList<Bitmap> lsBitmap;
    private ArrayList<String> paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        init();
        creatFolder();
        creatFile("test");
        imgcontrolermusic.setOnClickListener(this);
        loadbitmap();
        new AsynMakeVideo().execute(lsBitmap);

    }

    public void loadbitmap() {
        Intent data = getIntent();
        paths = data.getStringArrayListExtra(Constant.IMAGE_ARR);
        lsBitmap = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            lsBitmap.add(getBitmapFromLocalPath(paths.get(i), 1));
        }
    }


    public void init() {
        videoView = (VideoView) findViewById(R.id.showvideo);
        txtmusis = (TextView) findViewById(R.id.txtnamemusic);
        txtsong = (TextView) findViewById(R.id.txtsong);
        imgmusic = (ImageView) findViewById(R.id.iconmusic);
        imgcontrolermusic = (ImageView) findViewById(R.id.img_controlmusic);
        lnpro = (LinearLayout) findViewById(R.id.lnprocess);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d("DEBUG", videoView.getDuration() + "");
            }
        });

    }


    public void startIntentMusic() {
        Intent intent = new Intent(this, LoadMusicActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                musicMP3 = (MusicMP3) data.getSerializableExtra("music");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_controlmusic: {
                startIntentMusic();
                break;
            }
        }
    }

    public class AsynMakeVideo extends AsyncTask<ArrayList<Bitmap>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<Bitmap>... arrayLists) {
            ArrayList<Bitmap> bitmaps = arrayLists[0];
            video = new VideoUilt(getBaseContext(), bitmaps, Constant.PATH_TEMP + "test.mp4");
            String pavideo = video.makeVideo();
            addaudiovideo(pavideo, Constant.PATH_TEMP + "ring.aac");
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

    public void addaudiovideo(String pathvideo, String pathaudio) {
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

            MediaMuxer muxer = new MediaMuxer(pathvideo,
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
            int sampleSize = 256 * 1024;
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


                }
            }
            muxer.stop();
            muxer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void settingVideo(int widthVideo, int heightVideo, int duration) {
        VideoUilt.VIDEO_WIDTH = widthVideo;
        VideoUilt.VIDEO_HEIGHT = heightVideo;
        VideoUilt.maxFrame = duration * 30;
    }

    public void creatFolder() {
        File file = new File(Constant.PATH);
        if (!file.exists()) {
            file.mkdirs();
            File filetemp = new File(Constant.PATH_TEMP);
            File filevideo = new File(Constant.PATH_VIDEO);
            filetemp.mkdirs();
            filevideo.mkdirs();
        }

    }

    public String creatFile(String name) {
        File filevideo = new File(Constant.PATH_TEMP + name + ".mp4");
        try {
            FileOutputStream out = new FileOutputStream(filevideo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filevideo.getPath();
    }
}
