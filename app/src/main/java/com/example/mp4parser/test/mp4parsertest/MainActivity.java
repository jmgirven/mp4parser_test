package com.example.mp4parser.test.mp4parsertest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ffmpeg.android;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button shortenButton;
    private EditText startET;
    private EditText endET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortenButton = (Button) findViewById(R.id.shorten_file_button);
        shortenButton.setOnClickListener(this);

        startET = (EditText) findViewById(R.id.start_edit_text);
        endET = (EditText) findViewById(R.id.end_edit_text);
    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");

        if ((startET.getText().toString().equals("")) || (endET.getText().toString().equals(""))) {
            Log.d(TAG, "onClick: Empty");
            return;
        }

        int startMS = Integer.parseInt(startET.getText().toString());
        int endMS = Integer.parseInt(endET.getText().toString());

        Log.d(TAG, "onClick: startMS: " + startMS + ", endMS: " + endMS);

        shortenButton.setEnabled(false);
        new ShortenAsync(startMS, endMS).execute();
    }


    private class ShortenAsync extends AsyncTask<Void, Void, Void> {

        private final int startMs;
        private final int endMs;

        public ShortenAsync(int startMs, int endMs) {
            this.startMs = startMs;
            this.endMs = endMs;
        }

        @Override
        protected Void doInBackground(Void... nothing) {
            File moviesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES
            );

            String filePrefix = "GOPR1008";
            String fileExtn = ".MP4";
            String fileName = filePrefix + fileExtn;

            try {
                InputStream inputStream = getAssets().open(fileName);
                File src = new File(moviesDir, fileName);

                storeFile(inputStream, src);

                File dest = new File(moviesDir, filePrefix + "_1" + fileExtn);

                Log.d(TAG, "startTrim: src: " + src.getAbsolutePath());
                Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
                Log.d(TAG, "startTrim: startMs: " + startMs);
                Log.d(TAG, "startTrim: endMs: " + endMs);
                SimpleShortenExample.startTrim(src, dest, startMs, endMs);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            Log.d(TAG, "onPostExecute");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, R.string.complete, Toast.LENGTH_SHORT).show();
                    if (shortenButton != null) shortenButton.setEnabled(true);
                }
            });
        }
    }


    private void storeFile(InputStream input, File file) {
        try {
            final OutputStream output = new FileOutputStream(file);
            try {
                try {
                    final byte[] buffer = new byte[1024];
                    int read;

                    while ((read = input.read(buffer)) != -1)
                        output.write(buffer, 0, read);

                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
