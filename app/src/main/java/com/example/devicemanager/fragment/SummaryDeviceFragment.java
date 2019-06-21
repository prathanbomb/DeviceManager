package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerDeviceAdapter;

import java.util.ArrayList;

public class SummaryDeviceFragment extends Fragment {
    ArrayList count,type;
    RecyclerView recyclerView;
    RecyclerDeviceAdapter recyclerDeviceAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static SummaryDeviceFragment newInstance() {
        SummaryDeviceFragment fragment = new SummaryDeviceFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_summary_device, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDevice);
        recyclerView.setLayoutManager(layoutManager);
        type = new ArrayList<String>();
        type.add("Laptop");
        type.add("Printer");
        type.add("Scanner");
        type.add("Tablet");
        type.add("Monitor");
        type.add("Adapter");

        count = new ArrayList<Integer>();
        count.add(40);
        count.add(4);
        count.add(4);
        count.add(4);
        count.add(15);
        count.add(30);

        recyclerDeviceAdapter = new RecyclerDeviceAdapter(getContext());
        recyclerDeviceAdapter.setBrand(type);
        recyclerDeviceAdapter.setCount(count);
        recyclerView.setAdapter(recyclerDeviceAdapter);

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
