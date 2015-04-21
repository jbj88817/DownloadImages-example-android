package com.bojie.downloadimages;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bojiejiang on 4/20/15.
 */

public class MyTask extends AsyncTask<String, Integer, Boolean> {

    private int contentLength = -1;
    private int counter = 0;
    private int calculatedProgress = 0;
    private Activity mActivity;

    public MyTask(Activity activity) {
        onAttach(mActivity);
    }

    public void onAttach(Activity activity) {
        mActivity = activity;

    }

    public void onDetach() {
        mActivity = null;
    }

    @Override
    protected void onPreExecute() {
        if (mActivity != null) {
            ((MainActivity) mActivity).showProgressBarBeforeDownloading();
        }

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

        if (mActivity == null) {
            L.m("Skipping Progress Update Since activity is null");
        } else {
            calculatedProgress = (int) ((double) values[0] / contentLength) * 100;
            ((MainActivity) mActivity).updateProgress(calculatedProgress);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (mActivity != null) {
            ((MainActivity) mActivity).hideProgressBarAfterDownloading();
        }
    }
}
