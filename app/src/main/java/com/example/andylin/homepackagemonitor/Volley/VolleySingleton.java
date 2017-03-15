package com.example.andylin.homepackagemonitor.Volley;

import android.content.Context;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

/**
 * Created by Andy Lin on 2017-03-13.
 */

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context){
        if (instance == null){
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
