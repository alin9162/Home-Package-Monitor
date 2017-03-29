package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andylin.homepackagemonitor.Adapters.PageAdapter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.View.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lin on 2017-03-14.
 */

public class HomeFragment extends Fragment{
    private static final String TAG = "HomeFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main_fragment, container, false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        final List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new PendingTabFragment());
        fragmentList.add(new HistoryTabFragment());
        fragmentList.add(new StatusTabFragment());
        final PageAdapter mAdapter = new PageAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
                if (position == 2){
                    ((StatusTabFragment) mAdapter.getItem(position)).checkBluetoothEnabled();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
