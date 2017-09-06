package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AdapterMusic;
import com.videomaker.photowithsong.objects.MusicMP3;
import com.videomaker.photowithsong.utils.Constant;

import java.util.ArrayList;

public class LoadMusicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<MusicMP3> lsmusic;
    private ListView lsview;
    private AdapterMusic adapterMusic;
    private LinearLayout lnpr;
    private MusicMP3 musicMP3;
    private MediaPlayer mediaPlayer;
    private TextView txtok, txttitle;
    private ImageView ivBack, ivNext;
    private RelativeLayout ads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_load_music);
        init();
        initMusic();
//        songMusic = new SongMusic();
        adapterMusic = new AdapterMusic(lsmusic, this.getLayoutInflater());
        lsview.setAdapter(adapterMusic);
        lsview.setOnItemClickListener(this);
        txtok.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        txttitle.setOnClickListener(this);

    }

    public void init() {
        lsview = (ListView) findViewById(R.id.lsview);
        ads = (RelativeLayout) findViewById(R.id.adslayout);
        lnpr = (LinearLayout) findViewById(R.id.lnpr);
        txtok = (TextView) findViewById(R.id.tv_next);
        txttitle = (TextView) findViewById(R.id.titleappbar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        ads = (RelativeLayout) findViewById(R.id.adslayout);
        txtok.setText(getString(R.string.next));
        txttitle.setText(getString(R.string.music_video));
        lnpr.setVisibility(View.INVISIBLE);
        Constant.showAds(this, ads);
    }

    public void initMusic() {
        lsmusic = new ArrayList<>();
        lsmusic.add(new MusicMP3(false, "Christmas-1", "Unknow", "NhacNenGiangSinh01.aac"));
        lsmusic.add(new MusicMP3(false, "Christmas-2", "Unknow", "NhacNenGiangSinh02.aac"));
        lsmusic.add(new MusicMP3(false, "Love-1", "Unknow", "NhacNenTinhYeu01.aac"));
        lsmusic.add(new MusicMP3(false, "Love-2", "Unknow", "NhacNenTinhYeu02.aac"));
        lsmusic.add(new MusicMP3(false, "Happy New Year", "Unknow", "NhacNenHappyNewYear01.aac"));
        lsmusic.add(new MusicMP3(false, "Birthday", "Unknow", "NhacNenSinhNhat01.aac"));
        lsmusic.add(new MusicMP3(false, "Hope You", "Unknow", "HopeYou.aac"));
        lsmusic.add(new MusicMP3(false, "Kiss the rain", "Yurima", "KissTheRain-Yiruma_.aac"));
        lsmusic.add(new MusicMP3(false, "Happy Day", "AyanaTaketatsu", "HappyDay-AyanaTaketatsu.aac"));
        lsmusic.add(new MusicMP3(false, "In Love", "July", "InLove-July.aac"));
        lsmusic.add(new MusicMP3(false, "TenshiTekiKenpouYonjou", "MatsumotoTama", "TenshiTekiKenpouYonjou-MatsumotoTama.aac"));

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
        switch (view.getId()) {
            case R.id.tv_next:
            case R.id.iv_next: {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
                Intent intent = new Intent();
                if (musicMP3 != null) {
                    intent.putExtra("music", musicMP3);
                }
                setResult(500, intent);
                finish();
                break;
            }
            case R.id.titleappbar:
            case R.id.iv_back: {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
                Intent intent = new Intent();
                setResult(500, intent);
                finish();
                break;
            }
        }

    }


    public void playMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd(musicMP3.getPath());
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
}
