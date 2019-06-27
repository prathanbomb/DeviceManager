package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerFunitureAdapter;
import com.example.devicemanager.adapter.RecyclerOtherAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SummaryFunitureFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerFunitureAdapter recyclerFunitureAdapter;
    RecyclerView.LayoutManager layoutManager;
    int[] inUse,available;
    String[] type;


    public static SummaryFunitureFragment newInstance() {
        SummaryFunitureFragment fragment = new SummaryFunitureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        type = new String[]{"AIR CONDITIONER", "CABINET", "CARPET", "CART", "CHAIR", "COFFEE MACHINE", "COUNTER", "CURTAIN", "DRAWER", "FAN", "FURNITURE"
                , "GAS STOVE", "JUICE BLENDER", "KITCHEN", "LAMP", "LOCKER", "MIRCROWAVE", "REFRIGERATOR", "RICE COOKER", "SHELVES", "SINK"
                , "SOFA", "STOOL", "TABLE", "TELEVISION", "WASHING MACHINE", "WATER HEATER", "SWING"};
        inUse = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        available = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};

        DownloadData();
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

        recyclerFunitureAdapter = new RecyclerFunitureAdapter(getContext());
        recyclerFunitureAdapter.setBrand(type);
        recyclerFunitureAdapter.setCount(inUse);
        recyclerFunitureAdapter.setAvailable(available);
        recyclerView.setAdapter(recyclerFunitureAdapter);

    }

    private void DownloadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    String typeProduct = s.child("type").getValue(String.class).trim();
                    String status = s.child("place").getValue(String.class).trim();
                    for (int i = 0; i < type.length; i++) {
                        if (type[i].matches(typeProduct)) {
                            if (status.matches("-")) {
                                available[i] = available[i] + 1;
                            } else {
                                inUse[i] = inUse[i] + 1;

                            }
                        }
                    }
                }
                recyclerFunitureAdapter.notifyDataSetChanged();
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
