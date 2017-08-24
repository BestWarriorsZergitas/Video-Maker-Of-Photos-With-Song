package com.videomaker.photowithsong.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.MusicMP3;

import java.util.ArrayList;

/**
 * Created by DaiPhongPC on 8/24/2017.
 */

public class AdapterMusic extends BaseAdapter {
    private ArrayList<MusicMP3> lsmusic;
    private LayoutInflater inflater;

    public AdapterMusic(ArrayList<MusicMP3> lsmusic, LayoutInflater inflater) {
        this.lsmusic = lsmusic;
        this.inflater = inflater;
    }

    public ArrayList<MusicMP3> getLsmusic() {
        return lsmusic;
    }

    public void setLsmusic(ArrayList<MusicMP3> lsmusic) {
        this.lsmusic = lsmusic;
    }

    @Override
    public int getCount() {
        return lsmusic.size();
    }

    @Override
    public MusicMP3 getItem(int i) {
        return lsmusic.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    TextView txtnamemusic;
    ImageView imgcheck;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_music, null);
        }
        txtnamemusic = (TextView) view.findViewById(R.id.txtnamems);
        imgcheck = (ImageView) view.findViewById(R.id.imgcheck);
        MusicMP3 musicMP3 = getItem(i);
        if (musicMP3.isCheck()) {
            imgcheck.setImageResource(R.mipmap.ic_launcher);
        } else {
            imgcheck.setImageResource(R.mipmap.ic_launcher_round);
        }
        txtnamemusic.setText(musicMP3.getNamemusic());
        return view;
    }
}
