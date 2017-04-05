package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andylin.homepackagemonitor.Presenter.HistoryTabPresenter;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Views.Adapters.GridImageAdapter;
import com.example.andylin.homepackagemonitor.Views.Adapters.HistoryListAdapter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Interfaces.HistoryTabView;

/**
 * Created by Andy Lin on 2017-03-18.
 */

public class HistoryTabFragment extends Fragment implements View.OnClickListener, HistoryTabView{
    private static final String TAG = "HistoryTabFragment";
    private HistoryTabPresenter mHistoryTabPresenter;
    private MainActivity mMainActivity;

    private GridView mGridView;
    private ImageButton mGridDisplayButton;
    private ImageButton mListDisplayButton;
    private TextView mViewTypeTextView;
    private RecyclerView mRecyclerView;

    @Override
    public void setActionBarTitle(String title) {
        mMainActivity.getSupportActionBar().setTitle(title);
    }

    @Override
    public void setNavDrawerSelectedItem(int resID) {
        mMainActivity.setNavigationDrawerCheckedItem(resID);
    }

    @Override
    public void showGridView(GridImageAdapter gridImageAdapter) {
        mGridView.setAdapter(gridImageAdapter);
    }

    @Override
    public void showListView(HistoryListAdapter historyListAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(historyListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHistoryTabPresenter.setActionBar();
        mHistoryTabPresenter.setNavDrawerSelectedItem();
        mHistoryTabPresenter.getCameraImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_history_tab_fragment, container, false);
        this.mHistoryTabPresenter = new HistoryTabPresenter(getActivity(), this);
        this.mMainActivity = ((MainActivity) getActivity());

        mViewTypeTextView = (TextView) view.findViewById(R.id.view_type_text);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mGridDisplayButton = (ImageButton) view.findViewById(R.id.grid_display_button);
        mGridDisplayButton.setOnClickListener(this);

        mListDisplayButton = (ImageButton) view.findViewById(R.id.list_display_button);
        mListDisplayButton.setOnClickListener(this);

        mGridView = (GridView) view.findViewById(R.id.gridview);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.grid_display_button:
                mViewTypeTextView.setText("Grid View");
                mGridView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                break;
            case R.id.list_display_button:
                mViewTypeTextView.setText("List View");
                mGridView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
