package com.example.devicemanager.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class SummaryFunitureFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerFunitureAdapter recyclerFunitureAdapter;
    RecyclerView.LayoutManager layoutManager;
    int[] inUse,available,total;
    String[] type;
    ProgressBar progressBar ;
    View progressDialogBackground;

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
                , "SOFA", "STOOL", "TABLE", "TELEVISION", "WASHING MACHINE","WATER PUMP","WATER HEATER","WHITE BOARD", "SWING"};

            inUse = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0};
            available = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0};
            total = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0};
        if (savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
        }
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

         progressBar = (ProgressBar) rootView.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) rootView.findViewById(R.id.view);

        DownloadData();

        recyclerFunitureAdapter = new RecyclerFunitureAdapter(getContext());
        recyclerFunitureAdapter.setBrand(type);
        recyclerFunitureAdapter.setCount(inUse);
        recyclerFunitureAdapter.setTotal(total);
        recyclerFunitureAdapter.setAvailable(available);
        recyclerView.setAdapter(recyclerFunitureAdapter);

    }

    private void DownloadData() {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Summary");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inUse = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0};
                available = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0};
                total = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0};
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
                recyclerFunitureAdapter.setTotal(total);
                recyclerFunitureAdapter.setCount(inUse);
                recyclerFunitureAdapter.setTotal(total);
                recyclerFunitureAdapter.setAvailable(available);
                recyclerFunitureAdapter.notifyDataSetChanged();
                progressDialogBackground.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("inLoop", databaseError.toString());
                progressDialogBackground.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
    }
}
