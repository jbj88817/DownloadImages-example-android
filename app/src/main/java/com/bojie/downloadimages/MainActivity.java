package com.bojie.downloadimages;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private EditText selectionText;
    private ListView chooseImagesList;
    private String[] listOfImages;
    private ProgressBar downloadImagesProgress;
    private NonUITaskFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectionText = (EditText) findViewById(R.id.downLoadURL);
        chooseImagesList = (ListView) findViewById(R.id.urlList);
        chooseImagesList.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        downloadImagesProgress = (ProgressBar) findViewById(R.id.downloadProgress);

        if (savedInstanceState == null) {
            mFragment = new NonUITaskFragment();
            getSupportFragmentManager().beginTransaction().add(mFragment, "TaskFragment").commit();
        } else {
            mFragment = (NonUITaskFragment) getSupportFragmentManager().findFragmentByTag("TaskFragment");
        }
        if (mFragment != null) {
            if (mFragment.myTask != null && mFragment.myTask.getStatus() == AsyncTask.Status.RUNNING) {
                downloadImagesProgress.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectionText.setText(listOfImages[position]);
    }

    public void downLoadImage(View view) {
        if (selectionText.getText().toString().length() > 0) {
            String url = selectionText.getText().toString();
            mFragment.beginTask(url);
        }
    }

    public void showProgressBarBeforeDownloading() {
        if (mFragment.myTask != null) {
            if (downloadImagesProgress.getVisibility() != View.VISIBLE
                    && downloadImagesProgress.getProgress() != downloadImagesProgress.getMax()) {
                downloadImagesProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideProgressBarAfterDownloading() {
        if (mFragment.myTask != null) {
            if (downloadImagesProgress.getVisibility() == View.VISIBLE) {
                downloadImagesProgress.setVisibility(View.GONE);
            }
        }
    }

    public void updateProgress(int progress) {
        downloadImagesProgress.setProgress(progress);
    }
}
