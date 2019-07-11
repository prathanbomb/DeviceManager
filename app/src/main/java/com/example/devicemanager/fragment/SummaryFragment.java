package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class SummaryFragment extends Fragment {
    private FloatingActionButton fabContainer, fabAll, fabLaptop, fabDevice, fabFurniture, fabOther;
    private boolean isFABOpen = false;
    private RecyclerView rvSummary;
    private SummaryAdapter summaryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout layoutAll, layoutDevice, layoutLaptop, layoutFurniture, layoutOther;

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
        fabContainer = rootView.findViewById(R.id.fabFilter);
        fabAll = rootView.findViewById(R.id.fabAll);
        fabLaptop = rootView.findViewById(R.id.fabLaptop);
        fabDevice = rootView.findViewById(R.id.fabDevice);
        fabFurniture = rootView.findViewById(R.id.fabFurniture);
        fabOther = rootView.findViewById(R.id.fabOther);

        layoutAll = rootView.findViewById(R.id.layoutAll);
        layoutDevice = rootView.findViewById(R.id.layoutDevice);
        layoutFurniture = rootView.findViewById(R.id.layoutFurniture);
        layoutLaptop = rootView.findViewById(R.id.layoutLaptop);
        layoutOther = rootView.findViewById(R.id.layoutOther);

        fabContainer.setOnClickListener(onClickListener);
        layoutAll.setOnClickListener(onClickListener);
        layoutOther.setOnClickListener(onClickListener);
        layoutDevice.setOnClickListener(onClickListener);
        layoutLaptop.setOnClickListener(onClickListener);
        layoutFurniture.setOnClickListener(onClickListener);

        summaryAdapter = new SummaryAdapter(getContext());
        rvSummary = rootView.findViewById(R.id.rvSummary);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        layoutAll.animate().translationY(0);
        layoutLaptop.animate().translationY(0);
        layoutDevice.animate().translationY(0);
        layoutFurniture.animate().translationY(0);
        layoutOther.animate().translationY(0);
        delayCloseFab();
    }

    private void showFABMenu() {
        isFABOpen = true;
        delayOpenFab();
        layoutAll.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_1));
        layoutLaptop.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_2));
        layoutDevice.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_3));
        layoutFurniture.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_4));
        layoutOther.animate().translationY(-getResources().getDimension(R.dimen.transition_floating_5));
    }

    private void delayCloseFab(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutAll.setVisibility(View.INVISIBLE);
                layoutDevice.setVisibility(View.INVISIBLE);
                layoutLaptop.setVisibility(View.INVISIBLE);
                layoutFurniture.setVisibility(View.INVISIBLE);
                layoutOther.setVisibility(View.INVISIBLE);
            }
        }, 200);
    }

    private void delayOpenFab(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutAll.setVisibility(View.VISIBLE);
                layoutLaptop.setVisibility(View.VISIBLE);
                layoutDevice.setVisibility(View.VISIBLE);
                layoutFurniture.setVisibility(View.VISIBLE);
                layoutOther.setVisibility(View.VISIBLE);
            }
        }, 200);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == fabContainer) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
           else if(view == layoutAll){
                Toast.makeText(getActivity(), "All", Toast.LENGTH_SHORT).show();
            }
            else if (view == layoutDevice){
                Toast.makeText(getActivity(), "Device", Toast.LENGTH_SHORT).show();
            }
            else if (view == layoutLaptop){
                Toast.makeText(getActivity(), "Laptop", Toast.LENGTH_SHORT).show();
            }
            else if (view == layoutFurniture){
                Toast.makeText(getActivity(), "Furniture", Toast.LENGTH_SHORT).show();
            }
            else if (view == layoutOther){
                Toast.makeText(getActivity(), "Other", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
