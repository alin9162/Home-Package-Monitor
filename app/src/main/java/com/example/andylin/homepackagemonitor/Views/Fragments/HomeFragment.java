package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andylin.homepackagemonitor.Presenter.HomePresenter;
import com.example.andylin.homepackagemonitor.Views.Adapters.PageAdapter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Views.Interfaces.MainView;

/**
 * Created by Andy Lin on 2017-03-14.
 */

public class HomeFragment extends Fragment implements MainView{
    private static final String TAG = "HomeFragment";
    private MainActivity mMainActivity;
    private HomePresenter mHomePresenter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public void setActionBarTitle(String title) {
        mMainActivity.getSupportActionBar().setTitle(title);
    }

    @Override
    public void setNavDrawerSelectedItem(int resID) {
        mMainActivity.setNavigationDrawerCheckedItem(resID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHomePresenter.setActionBar();
        mHomePresenter.setNavDrawerSelectedItem();
        mHomePresenter.setViewPager(getChildFragmentManager());
    }

    @Override
    public void showViewPager(PageAdapter pageAdapter) {
        mViewPager.setAdapter(pageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Give the TabLayout the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main_fragment, container, false);
        mHomePresenter = new HomePresenter(getActivity(), this);
        this.mMainActivity = ((MainActivity) getActivity());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        return view;
    }
}
