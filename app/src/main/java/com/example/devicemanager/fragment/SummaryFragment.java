package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerDeviceAdapter;
import com.example.devicemanager.adapter.RecyclerOtherAdapter;
import com.example.devicemanager.adapter.SummaryAdapter;
import com.example.devicemanager.view.SlidingTabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class SummaryFragment extends Fragment {
    private FloatingActionButton fabContainer, fabAll, fabLaptop, fabDevice, fabFurniture, fabOther;
    private boolean isFABOpen = false;
    private RecyclerView rvSummary;
    private SummaryAdapter summaryAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        fabContainer = rootView.findViewById(R.id.fabSearch);
        fabAll = rootView.findViewById(R.id.fabAll);
        fabLaptop = rootView.findViewById(R.id.fabLaptop);
        fabDevice = rootView.findViewById(R.id.fabDevice);
        fabFurniture = rootView.findViewById(R.id.fabFurniture);
        fabOther = rootView.findViewById(R.id.fabOther);

        fabContainer.setOnClickListener(onClickFABSearch);

        summaryAdapter = new SummaryAdapter(getContext());
        rvSummary = rootView.findViewById(R.id.rvSummary);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabAll.animate().translationY(0);
        fabLaptop.animate().translationY(0);
        fabDevice.animate().translationY(0);
        fabFurniture.animate().translationY(0);
        fabOther.animate().translationY(0);
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabAll.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_1));
        fabLaptop.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_2));
        fabDevice.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_3));
        fabFurniture.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_4));
        fabOther.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_5));
    }

    private View.OnClickListener onClickFABSearch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == fabContainer) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            } else if (view == fabAll) {

            } else if (view == fabLaptop) {

            } else if (view == fabDevice) {

            } else if (view == fabFurniture) {

            } else if (view == fabOther) {

            }
        }
    };
}
