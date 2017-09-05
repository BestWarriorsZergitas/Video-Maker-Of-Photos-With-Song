package com.videomaker.photowithsong.objects;

import java.io.Serializable;

/**
 * Created by DaiPhongPC on 8/24/2017.
 */

public class MusicMP3 implements Serializable {
    private boolean check;
    private String namemusic;
    private String namesong;
    private String path;


    public MusicMP3(boolean check, String namemusic, String namesong, String path) {
        this.check = check;
        this.namemusic = namemusic;
        this.namesong = namesong;
        this.path = path;
    }

    public MusicMP3() {

    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getNamemusic() {
        return namemusic;
    }

    public void setNamemusic(String namemusic) {
        this.namemusic = namemusic;
    }

    public String getNamesong() {
        return namesong;
    }

    public void setNamesong(String namesong) {
        this.namesong = namesong;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
