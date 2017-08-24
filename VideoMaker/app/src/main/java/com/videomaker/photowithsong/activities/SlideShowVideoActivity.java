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
    private int position = 0;
    private TextView txtmusis, txtsong;
    private ImageView imgcontrolermusic, imgmusic;
    private MusicMP3 musicMP3;
    private LinearLayout lnpro;
    private ArrayList<Bitmap> lsBitmap;
    private ArrayList<String> paths;
    private String pathvideo, pathaudio, pathoutput;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        init();
//        path=creatFile("test");

        imgcontrolermusic.setOnClickListener(this);
        loadbitmap();
        new AsynMakeVideo().execute(lsBitmap);

    }

    public void loadbitmap() {
        Intent data = getIntent();
        paths = data.getStringArrayListExtra(Constant.IMAGE_ARR);
        lsBitmap = new ArrayList<>();
//        for (int i=0;i<paths.size();i++){
//            lsBitmap.add(getBitmapFromLocalPath(paths.get(i),1));
//        }
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundsinhnhat));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.nennoel));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundvalentine));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.nennoel1));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundvalentine1));

    }

    public String creatFile(String name) {
        File filevideo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Make Video/" + name + ".mp4");
        try {
            FileOutputStream out = new FileOutputStream(filevideo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filevideo.getPath();
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
            video = new VideoUilt(getBaseContext(), bitmaps,
                    Environment.getExternalStorageDirectory().getPath() + "/Make Video/test.mp4");
            String pa = video.makeVideo();
            try {
                addaudiovideo();
//                addAudio(Environment.getExternalStorageDirectory().getPath() + "/Make Video/test.mp4",
//                        Environment.getExternalStorageDirectory().getPath() + "/Make Video/ring01.mp3",
//                        Environment.getExternalStorageDirectory().getPath() + "/Make Video/test1.mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pa;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lnpro.setVisibility(View.INVISIBLE);
//            showVideo(s);
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

    private String addAudio(String videoSource, String audioSource, String outputFile) throws IOException {
        File output = new File(outputFile);

        // Load video đầu vào
        org.mp4parser.muxer.Movie originalMovie = MovieCreator.build(videoSource);
        org.mp4parser.muxer.Movie audio = MovieCreator.build(audioSource);
        //get duration of video
        IsoFile isoFile = new IsoFile(videoSource);
        double lengthInSeconds = (double)
                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();

        Track track = originalMovie.getTracks().get(0);
        Track audioTrack = audio.getTracks().get(0);

        double startTime1 = 0;
        double endTime1 = lengthInSeconds;

        if (audioTrack.getSyncSamples() != null && audioTrack.getSyncSamples().length > 0) {
            startTime1 = correctTimeToSyncSample(audioTrack, startTime1, false);
            endTime1 = correctTimeToSyncSample(audioTrack, endTime1, true);
        }

        long currentSample = 0;
        double currentTime = 0;
        double lastTime = -1;
        long startSample1 = -1;
        long endSample1 = -1;

        for (int i = 0; i < audioTrack.getSampleDurations().length; i++) {
            long delta = audioTrack.getSampleDurations()[i];

            if (currentTime > lastTime && currentTime <= startTime1) {
                // current sample is still before the new start time
                startSample1 = currentSample;
            }
            if (currentTime > lastTime && currentTime <= endTime1) {
                // current sample is after the new start time and still before the new end time
                endSample1 = currentSample;
            }

            lastTime = currentTime;
            currentTime += (double) delta / (double) audioTrack.getTrackMetaData().getTimescale();
            currentSample++;
        }

//        // Crop audio với độ lớn phù hợp với video
//        CroppedTrack cropperAacTrack = new CroppedTrack(audioTrack, startSample1, endSample1);
//
        org.mp4parser.muxer.Movie movie = new org.mp4parser.muxer.Movie();

        movie.addTrack(track);
        // Add audio vào video
        movie.addTrack(audioTrack);

        Container mp4file = new DefaultMp4Builder().build(movie);

        FileChannel fc = new FileOutputStream(output).getChannel();
        mp4file.writeContainer(fc);
        fc.close();

        return output.getAbsolutePath();
    }

    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;

        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }

    public void addaudiovideo() throws IOException {
        MediaExtractor videoExtractor = new MediaExtractor();
        videoExtractor.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/Make Video/test.mp4");

        MediaExtractor audioExtractor = new MediaExtractor();
        audioExtractor.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/Make Video/ring.aac");
        Log.d("DEBUG",
                "Video Extractor Track Count "
                        + videoExtractor.getTrackCount());
        Log.d("DEBUG",
                "Audio Extractor Track Count "
                        + audioExtractor.getTrackCount());

        MediaMuxer muxer = new MediaMuxer(Environment.getExternalStorageDirectory().getPath() + "/Make Video/test1.mp4",
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

    }
}
