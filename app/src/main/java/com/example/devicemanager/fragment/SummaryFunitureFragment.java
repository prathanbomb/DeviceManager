package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerFunitureAdapter;
import com.example.devicemanager.adapter.RecyclerOtherAdapter;

import java.util.ArrayList;

public class SummaryFunitureFragment extends Fragment {
    ArrayList count,type;
    RecyclerView recyclerView;
    RecyclerFunitureAdapter recyclerOtherAdapter;
    RecyclerView.LayoutManager layoutManager;


    public static SummaryFunitureFragment newInstance() {
        SummaryFunitureFragment fragment = new SummaryFunitureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary_funiture, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvFuniture);
        recyclerView.setLayoutManager(layoutManager);
        type = new ArrayList<String>();
        type.add("Chair");
        type.add("Funiture Set");
        type.add("Carpet");
        type.add("Table");
        type.add("White Board");
        type.add("Television");
        type.add("Cabinet");

        count = new ArrayList<Integer>();
        count.add(97);
        count.add(2);
        count.add(1);
        count.add(57);
        count.add(5);
        count.add(4);
        count.add(8);

        recyclerOtherAdapter = new RecyclerFunitureAdapter(getContext());
        recyclerOtherAdapter.setBrand(type);
        recyclerOtherAdapter.setCount(count);
        recyclerView.setAdapter(recyclerOtherAdapter);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
