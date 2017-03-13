package com.example.andylin.homepackagemonitor.View.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andylin.homepackagemonitor.R;

/**
 * Created by Andy Lin on 2017-03-09.
 */

public class SignInFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in, container, false);
//        vendorListPresenter = new VendorListPresenter(getActivity(), this, getArguments());
//        this.mainActivity = ((MainActivity) getActivity());
//        this.recyclerView = (RecyclerView) view.findViewById(R.id.vendor_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
//        vendorListPresenter.setActionBar();
//        vendorListPresenter.setNavDrawerSelectedItem();
//        vendorListPresenter.populateVendorList();
    }
}
