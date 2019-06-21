package com.example.devicemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerFunitureAdapter;
import com.example.devicemanager.adapter.RecyclerListDetailAdapter;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class SummaryListDetailFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerListDetailAdapter recyclerListDetailAdapter;
    RecyclerView.LayoutManager layoutManager;


    @SuppressWarnings("unused")
    public static SummaryListDetailFragment newInstance() {
        SummaryListDetailFragment fragment = new SummaryListDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        //setHasOptionsMenu(true);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary_list_detail, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvListDetail);
        recyclerView.setLayoutManager(layoutManager);
        String type = getArguments().getString("Type");
        Integer count = getArguments().getInt("Count");
        Log.d("countinsummary",""+count);
        recyclerListDetailAdapter = new RecyclerListDetailAdapter(getContext());
        recyclerListDetailAdapter.setCount(count);
        recyclerView.setAdapter(recyclerListDetailAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
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
