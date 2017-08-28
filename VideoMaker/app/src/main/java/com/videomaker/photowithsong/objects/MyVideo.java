package com.videomaker.photowithsong.objects;

/**
 * Created by DaiPhongPC on 8/28/2017.
 */

public class MyVideo {
    private String namevideo;
    private String pathvideo;

    public MyVideo(String namevideo, String pathvideo) {
        this.namevideo = namevideo;
        this.pathvideo = pathvideo;
    }

    public String getNamevideo() {
        return namevideo;
    }

    public void setNamevideo(String namevideo) {
        this.namevideo = namevideo;
    }

    public String getPathvideo() {
        return pathvideo;
    }

    public void setPathvideo(String pathvideo) {
        this.pathvideo = pathvideo;
    }
}
