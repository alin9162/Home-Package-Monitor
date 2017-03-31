package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.View.Activities.MainActivity;

/**
 * Created by Andy Lin on 2017-03-14.
 */

public class SettingsFragment extends Fragment{
    private Button accountSettingsButton;
    private LinearLayout enableAccountLayout;
    private LinearLayout accountLayout;
    private boolean account_tab = false;
    private Button account_dropdown_image;
    private EditText phoneNumber;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_settings_fragment, container, false);

        enableAccountLayout = (LinearLayout) view.findViewById(R.id.enable_account_layout);
        accountLayout = (LinearLayout) view.findViewById(R.id.account_layout);
        accountSettingsButton = (Button) view.findViewById(R.id.account_button);

        phoneNumber = (EditText) view.findViewById(R.id.phone_number_entry);
        phoneNumber.setTypeface(Typeface.DEFAULT);

        accountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_tab == true) {
                    accountLayout.setVisibility(View.GONE);
                    account_tab = false;
                    accountSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.account_logo, 0, R.mipmap.dropdown_carrot, 0);
                } else {
                    accountLayout.setVisibility(View.VISIBLE);
                    account_tab = true;
                    accountSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.account_logo, 0, R.mipmap.upward_arrow, 0);
                }

            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Settings");
    }
}
