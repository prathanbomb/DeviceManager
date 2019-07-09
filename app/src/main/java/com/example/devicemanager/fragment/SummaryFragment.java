package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerDeviceAdapter;
import com.example.devicemanager.adapter.RecyclerOtherAdapter;
import com.example.devicemanager.view.SlidingTabLayout;
import com.google.android.material.tabs.TabLayout;

public class SummaryFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        FragmentStatePagerAdapter viewPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return SummaryDeviceFragment.newInstance();
                    case 1:
                        return SummaryFunitureFragment.newInstance();
                    case 2:
                        return SummaryOtherFragment.newInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Device";
                    case 1:
                        return "Furniture";
                    case 2:
                        return "Other";
                    default:
                        return "";
                }
            }
        };
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
