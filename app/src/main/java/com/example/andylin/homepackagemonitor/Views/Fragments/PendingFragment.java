package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.andylin.homepackagemonitor.Presenter.PendingTabPresenter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Utils.ImageUtils;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Views.Interfaces.PendingTabView;

/**
 * Created by Andy Lin on 2017-03-17.
 */

public class PendingFragment extends Fragment implements PendingTabView{
    private static final String TAG = "PendingFragment";
    private PendingTabPresenter mPendingTabPresenter;
    private MainActivity mMainActivity;

    private LinearLayout mNoPendingRequestLayout;
    private CardView mCardView;
    private ImageView mImageView;
    private Button mAcceptButton;
    private Button mDeclineButton;

    @Override
    public void setActionBarTitle(String title) {
        mMainActivity.getSupportActionBar().setTitle(title);
    }

    @Override
    public void setNavDrawerSelectedItem(int resID) {
        mMainActivity.setNavigationDrawerCheckedItem(resID);
    }

    @Override
    public void showPendingRequest(String imageBytes) {
        mCardView.setVisibility(View.VISIBLE);
        mNoPendingRequestLayout.setVisibility(View.GONE);
        byte[] cameraByteArray = ImageUtils.hexStringToByteArray(imageBytes.substring(10));
        Bitmap bmp = BitmapFactory.decodeByteArray(cameraByteArray, 0, cameraByteArray.length);
        mImageView.setImageBitmap(bmp);
    }

    @Override
    public void showNoPendingRequest() {
        mNoPendingRequestLayout.setVisibility(View.VISIBLE);
        mCardView.setVisibility(View.GONE);
    }

    @Override
    public void hidePendingRequest() {
        mCardView.setVisibility(View.GONE);
        mNoPendingRequestLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPendingTabPresenter.setActionBar();
        mPendingTabPresenter.setNavDrawerSelectedItem();
        mPendingTabPresenter.checkPendingRequests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_pending_tab_fragment, container, false);
        this.mPendingTabPresenter = new PendingTabPresenter(getActivity(), this);
        this.mMainActivity = ((MainActivity) getActivity());

        mImageView = (ImageView) view.findViewById(R.id.pending_image);
        mNoPendingRequestLayout = (LinearLayout) view.findViewById(R.id.no_pending_request_layout);
        mCardView = (CardView) view.findViewById(R.id.card_view);

        mAcceptButton = (Button) view.findViewById(R.id.accept_button);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPendingTabPresenter.acknowledge(true);
            }
        });

        mDeclineButton = (Button) view.findViewById(R.id.decline_button);
        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPendingTabPresenter.acknowledge(false);
            }
        });
        return view;
    }
}
