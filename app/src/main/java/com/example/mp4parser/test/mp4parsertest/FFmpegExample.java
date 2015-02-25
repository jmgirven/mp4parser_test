package com.example.mp4parser.test.mp4parsertest;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;


import java.io.File;
import java.io.IOException;
import java.lang.String;

/**
 * Created by enrico on 23/02/15.
 */
public class FFmpegExample {

    private static final String TAG = FFmpegExample.class.getSimpleName();

    public static void startTrim(Context context, File src, File dst, int startMs, int endMs) throws IOException {
        Log.d(TAG, "Starting startTrim");
        File mFile = new File(Environment.getExternalStorageDirectory()+"/temp/");
        Log.d(TAG, "Created temp file");
        FfmpegController ffController = new FfmpegController(context, mFile);
        Log.d(TAG, "Created controller");
        Clip srcVideo = new Clip(src.getAbsolutePath());
        Log.d(TAG, "Created clip");
        srcVideo.startTime = String.valueOf(startMs/1000);
        srcVideo.duration  = (endMs - startMs)/1000;

        try {
            ffController.trim(srcVideo, true, dst.getAbsolutePath(), new ShellUtils.ShellCallback() {
                @Override
                public void shellOut(String s) {
                    Log.d(TAG, "Shell out" + s);
                }

                @Override
                public void processComplete(int i) {
                    Log.d(TAG, "Process complete" + i);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
