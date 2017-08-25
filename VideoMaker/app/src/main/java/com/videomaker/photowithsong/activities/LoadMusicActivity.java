package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AdapterMusic;
import com.videomaker.photowithsong.objects.MusicMP3;
import com.videomaker.photowithsong.utils.Constant;
import com.videomaker.photowithsong.utils.SongMusic;

import java.util.ArrayList;

public class LoadMusicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<MusicMP3> lsmusic;
    private ListView lsview;
    private AdapterMusic adapterMusic;
    private SongMusic songMusic;
    private LinearLayout lnpr;
    private MusicMP3 musicMP3;
    private MediaPlayer mediaPlayer;
    private TextView txtok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_music);
        lsview = (ListView) findViewById(R.id.lsview);
        lnpr = (LinearLayout) findViewById(R.id.lnpr);
        txtok = (TextView) findViewById(R.id.tv_next);
        lnpr.setVisibility(View.INVISIBLE);
        lsmusic = new ArrayList<>();
        lsmusic.add(new MusicMP3(false, "Ring.acc", "Author", Constant.PATH_TEMP + "ring.aac"));
        lsmusic.add(new MusicMP3(false, "Kiss the rain.acc", "Yurima",
                Constant.PATH_TEMP + "KissTheRain-Yiruma_.aac"));
//        songMusic = new SongMusic();
        adapterMusic = new AdapterMusic(lsmusic, this.getLayoutInflater());
        lsview.setAdapter(adapterMusic);
        lsview.setOnItemClickListener(this);
        txtok.setOnClickListener(this);
//        new loadMusic().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        for (int j = 0; j < adapterMusic.getCount(); j++) {
            adapterMusic.getItem(j).setCheck(false);
        }
        adapterMusic.getItem(i).setCheck(true);
        musicMP3 = adapterMusic.getItem(i);
        musicMP3.setCheck(true);
        adapterMusic.notifyDataSetChanged();
        playMusic();
    }

    @Override
    public void onClick(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        Intent intent = new Intent();
        if (musicMP3 != null) {
            intent.putExtra("music", musicMP3);
        }
        setResult(500, intent);
        finish();
    }

    public class loadMusic extends AsyncTask<Void, Void, ArrayList<MusicMP3>> {
        @Override
        protected ArrayList<MusicMP3> doInBackground(Void... voids) {
            return songMusic.getPlayList();
        }

        @Override
        protected void onPostExecute(ArrayList<MusicMP3> objectMusics) {
            adapterMusic.setLsmusic(objectMusics);
            adapterMusic.notifyDataSetChanged();
            lnpr.setVisibility(View.INVISIBLE);
            this.cancel(true);

        }

    }

    public void playMusic() {
        mediaPlayer = MediaPlayer.create(this, Uri.parse(musicMP3.getPath()));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
//        mediaPlayer = MediaPlayer.create(this, musicMP3.getId());
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
    }
}
