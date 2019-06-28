package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerDeviceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SummaryDeviceFragment extends Fragment {
    String[] type;
    int[] available, inUse,total;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        type = new String[]{"ACCESS POINT","BARCODE READER","CAMERA","CARD READER","CASH DRAWER","CLOTHES DRYERS","COMPUTER","DOCUMENT SHREDDER"
                ,"DOOR ACCESS","IMAC","IPAD","IPOD","LABEL PRINTER","LAPTOP","MOBILE PHONE","MODEM ROUTER","MONITOR","NETWORK SWITCH","POCKET WIFI"
                ,"PRINTER","ROUTER","SCANNER","SERVER","TABLET","TELEPHONE","WATCH"};
        inUse = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        available = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        total = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        DownloadData();
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
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDevice);
        recyclerView.setLayoutManager(layoutManager);


        recyclerDeviceAdapter = new RecyclerDeviceAdapter(getContext());
        recyclerDeviceAdapter.setBrand(type);
        recyclerDeviceAdapter.setCount(inUse);
        recyclerDeviceAdapter.setTotal(total);
        recyclerDeviceAdapter.setAvailable(available);
        recyclerView.setAdapter(recyclerDeviceAdapter);

    }
    private void DownloadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Summary");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inUse = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
                available = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
                total = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    String key = s.getKey().trim();
                    for(int i = 0 ; i<type.length ; i++){
                        if(key.matches(type[i])){
                            int getTotal = s.child("Total").getValue(Integer.class);
                            int getInuse = s.child("InUse").getValue(Integer.class);
                            int getAvailable = s.child("Available").getValue(Integer.class);
                            inUse[i] = getInuse;
                            available[i] = getAvailable;
                            total[i] = getTotal;
                        }
                    }
                }
                recyclerDeviceAdapter.setTotal(total);
                recyclerDeviceAdapter.setCount(inUse);
                recyclerDeviceAdapter.setAvailable(available);
                recyclerDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("inLoop", databaseError.toString());
            }
        });
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
