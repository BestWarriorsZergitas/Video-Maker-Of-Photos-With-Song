package com.videomaker.photowithsong.utils;

import android.os.Environment;

import com.videomaker.photowithsong.objects.MusicMP3;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DaiPhongPC on 8/23/2017.
 */

public class SongMusic {
    // SDCard Path
    //choose your path for me i choose sdcard
    final String MEDIA_PATH = new String(Environment.getExternalStorageDirectory().getAbsolutePath());
    private ArrayList<MusicMP3> songsList = new ArrayList<>();

    // Constructor
    public SongMusic() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<MusicMP3> getPlayList() {
        File home = new File(MEDIA_PATH);

        for (File file: home.listFiles()){
            if (file.isDirectory()){
                for (File file1:file.listFiles(new FileExtensionFilter())){
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_name", file1.getName());
                    song.put("file_path", file1.getPath());
                    // Adding each song to SongList
                    songsList.add(new MusicMP3(false,song.get("file_name"),"", song.get("file_path")));
                }
            }
            else {
                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".MP3")){
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_name", file.getName());
                    song.put("file_path", file.getPath());
                    // Adding each song to SongList
                    songsList.add(new MusicMP3(false,song.get("file_name"),"", song.get("file_path")));
                }
            }
        }

        // return songs list array
        return songsList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    //you can choose the filter for me i put .mp3
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
