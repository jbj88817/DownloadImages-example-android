package com.bojie.downloadimages;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by bojiejiang on 4/16/15.
 */
public class L {

    public static void m(String message){
        Log.d("Bojie", message);
    }

    public static void s(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
