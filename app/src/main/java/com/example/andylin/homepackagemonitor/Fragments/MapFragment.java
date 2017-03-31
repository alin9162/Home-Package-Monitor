package com.example.andylin.homepackagemonitor.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andylin.homepackagemonitor.R;

/**
 * Created by Andy Lin on 2017-03-30.
 */

public class MapFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_map_fragment, container, false);
        return view;
    }


}
