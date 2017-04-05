package com.example.andylin.homepackagemonitor.Presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Adapters.PageAdapter;
import com.example.andylin.homepackagemonitor.Views.Fragments.HistoryTabFragment;
import com.example.andylin.homepackagemonitor.Views.Fragments.PendingTabFragment;
import com.example.andylin.homepackagemonitor.Views.Fragments.StatusTabFragment;
import com.example.andylin.homepackagemonitor.Views.Interfaces.MainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public class HomePresenter {
    private Activity mActivity;
    private MainView mMainView;
    private final String TAG = "HomePresenter";

    public HomePresenter(Activity activity, MainView mainView){
        this.mActivity = activity;
        this.mMainView = mainView;
    }

    public void setActionBar(){
        mMainView.setActionBarTitle("Home");
    }

    public void setNavDrawerSelectedItem(){
        mMainView.setNavDrawerSelectedItem(R.id.nav_home);
    }

    public void setViewPager(FragmentManager fragmentManager){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new PendingTabFragment());
        fragmentList.add(new HistoryTabFragment());
        fragmentList.add(new StatusTabFragment());
        PageAdapter mAdapter = new PageAdapter(fragmentManager, fragmentList);
        mMainView.showViewPager(mAdapter);
    }
}
