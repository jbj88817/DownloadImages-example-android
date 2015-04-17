package com.bojie.downloadimages;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private EditText mEditText;
    private ListView mListView;
    private String[] listOfImages;
    private ProgressBar mProgressBar;
    private LinearLayout loadingSection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.downLoadURL);
        mListView = (ListView) findViewById(R.id.urlList);
        mListView.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        mProgressBar = (ProgressBar) findViewById(R.id.downloadProgress);
        loadingSection = (LinearLayout) findViewById(R.id.loadingSection);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mEditText.setText(listOfImages[position]);
    }

    public void downLoadImage(View view) {

        String url = mEditText.getText().toString();
        Thread myThread = new Thread(new DownloadImageThread(url));
        myThread.start();
    }

    public boolean downloadImageUsingThreads(String url) {

        // 1.create the url object that represents the url
        // 2. open connection using that url object
        // 3. read data using input stream into a byte array
        // 4. open a file output stream to save data on sd card
        // 5. write data to the fileoutputstream
        // 6. close the connections

        boolean successful = false;
        URL downloadURL = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            downloadURL = new URL(url);
            connection = (HttpURLConnection) downloadURL.openConnection();
            inputStream = connection.getInputStream();

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment());
            L.m("" + file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }
            successful = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.GONE);
                }
            });
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return successful;

    }

    private class DownloadImageThread implements Runnable {

        private String url;
        public DownloadImageThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.VISIBLE);
                }
            });
            downloadImageUsingThreads(url);
        }
    }
}
