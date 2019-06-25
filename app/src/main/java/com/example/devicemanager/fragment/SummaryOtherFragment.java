package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerOtherAdapter;

import java.util.ArrayList;

public class SummaryOtherFragment extends Fragment {
    ArrayList count,type, available;
    RecyclerView recyclerView;
    RecyclerOtherAdapter recyclerOtherAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static SummaryOtherFragment newInstance() {
        SummaryOtherFragment fragment = new SummaryOtherFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_summary_other, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvOther);
        recyclerView.setLayoutManager(layoutManager);
        type = new ArrayList<String>();
        type.add("SSD");
        type.add("Software");
        type.add("Program");
        type.add("Apple Care");
        type.add("Car");
        type.add("Film");

        count = new ArrayList<Integer>();
        count.add(24);
        count.add(18);
        count.add(12);
        count.add(28);
        count.add(1);
        count.add(1);

        available = new ArrayList<Integer>();
        available.add(10);
        available.add(4);
        available.add(2);
        available.add(10);
        available.add(4);
        available.add(5);
        available.add(0);

        recyclerOtherAdapter = new RecyclerOtherAdapter(getContext());
        recyclerOtherAdapter.setBrand(type);
        recyclerOtherAdapter.setCount(count);
        recyclerOtherAdapter.setAvailable(available);
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
