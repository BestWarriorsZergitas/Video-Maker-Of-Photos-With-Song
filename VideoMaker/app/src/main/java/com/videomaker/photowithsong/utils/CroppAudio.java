package com.videomaker.photowithsong.utils;

import com.videomaker.photowithsong.soundfile.SoundFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by DaiPhongPC on 9/1/2017.
 */

public class CroppAudio {
    public static String croppAudio(SoundFile soundFile, File fileout,
                                    int durationStart, int durationEnd) {
        String path = "";
        try {
            soundFile.WriteFile(fileout, durationStart, durationEnd);
            path = fileout.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }
}
