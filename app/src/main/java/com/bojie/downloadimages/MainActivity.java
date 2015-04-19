package com.bojie.downloadimages;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

    private EditText selectionText;
    private ListView chooseImagesList;
    private String[] listOfImages;
    private ProgressBar downloadImagesProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectionText = (EditText) findViewById(R.id.downLoadURL);
        chooseImagesList = (ListView) findViewById(R.id.urlList);
        chooseImagesList.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        downloadImagesProgress = (ProgressBar) findViewById(R.id.downloadProgress);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectionText.setText(listOfImages[position]);
    }

    public void downLoadImage(View view) {
        if (selectionText.getText().toString().length() > 0) {
            String url = selectionText.getText().toString();
            MyTask myTask = new MyTask();
            myTask.execute(url);
        }
    }

    class MyTask extends AsyncTask<String, Integer, Boolean> {

        private int contentLength = -1;
        private int counter = 0;
        private int calculatedProgress = 0;

        @Override
        protected void onPreExecute() {
            downloadImagesProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
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
                downloadURL = new URL(params[0]);
                connection = (HttpURLConnection) downloadURL.openConnection();
                contentLength = connection.getContentLength();
                inputStream = connection.getInputStream();

                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + Uri.parse(params[0]).getLastPathSegment());
                L.m("" + file.getAbsolutePath());
                fileOutputStream = new FileOutputStream(file);
                int read = -1;
                byte[] buffer = new byte[1024];
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                    counter = counter + read;
                  //  L.m("counter value is " + counter + " and contentLength is " + contentLength);
                    publishProgress(counter);
                }
                successful = true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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

        @Override
        protected void onProgressUpdate(Integer... values) {
            calculatedProgress = (int) ((double) values[0] / contentLength) * 100;
            downloadImagesProgress.setProgress(calculatedProgress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            downloadImagesProgress.setVisibility(View.GONE);
        }
    }
}
