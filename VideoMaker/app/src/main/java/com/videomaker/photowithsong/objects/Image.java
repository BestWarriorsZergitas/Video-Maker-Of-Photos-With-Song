package com.videomaker.photowithsong.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Peih Gnaoh on 8/20/2017.
 */

public class Image implements Parcelable {
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private String date;
    private String bucket;
    private String path;
    private String width;
    private String height;
    private String size;
    private Bitmap imgEdited;
    private boolean isClicked;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public Image(String date, String bucket, String path, String width, String height, String size) {
        this.date = date;
        this.bucket = bucket;
        this.path = path;
        this.width = width;
        this.height = height;
        this.size = size;
    }

    protected Image(Parcel in) {
        date = in.readString();
        bucket = in.readString();
        path = in.readString();
    }

    public Bitmap getImgEdited() {
        return imgEdited;
    }

    public void setImgEdited(Bitmap imgEdited) {
        this.imgEdited = imgEdited;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(bucket);
        dest.writeString(path);
    }
}
