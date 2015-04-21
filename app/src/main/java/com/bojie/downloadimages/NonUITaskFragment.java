package com.bojie.downloadimages;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bojiejiang on 4/20/15.
 */
public class NonUITaskFragment extends Fragment {

    MyTask myTask;
    private Activity mActivity;

    public NonUITaskFragment() {
    }

    public void beginTask(String... arguments) {
        myTask = new MyTask(mActivity);
        myTask.execute(arguments);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (myTask != null) {
            myTask.onAttach(activity);
        }
        L.m("Fragment onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.m("Fragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.m("Fragment onCreateView");
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.m("Fragment onActivityCreated");
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        L.m("Fragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        L.m("Fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.m("Fragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.m("Fragment onStop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.m("Fragment onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        L.m("Fragment onViewStateRestored");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.m("Fragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.m("Fragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (myTask != null) {
            myTask.onDetach();
        }
        L.m("Fragment onDetach");
    }


}
