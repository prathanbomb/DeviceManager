package com.example.devicemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerListDetailAdapter;
import com.example.devicemanager.model.DataItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class SummaryListDetailFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerListDetailAdapter recyclerListDetailAdapter;
    RecyclerView.LayoutManager layoutManager;
    String type;
    ArrayList<String> brand = new ArrayList<String>();
    ArrayList<String> detail = new ArrayList<String>();
    ArrayList<String> owner = new ArrayList<String>();
    ArrayList<String> addedDate = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();
    ProgressBar progressBar;
    View progressDialogBackground;

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
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerListDetailAdapter = new RecyclerListDetailAdapter(getContext());
        recyclerListDetailAdapter.setBrand(brand);
        recyclerListDetailAdapter.setDetail(detail);
        recyclerListDetailAdapter.setOwner(owner);
        recyclerListDetailAdapter.setAddedDate(addedDate);
        recyclerListDetailAdapter.setStatus(status);

        progressBar = (ProgressBar) rootView.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) rootView.findViewById(R.id.view);

        DownloadData();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvListDetail);
        recyclerView.setLayoutManager(layoutManager);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    private void DownloadData() {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        type = getArguments().getString("Type").trim();
        Query databaseReference = FirebaseDatabase.getInstance().getReference().child("Data")
                .orderByChild("type").equalTo(type);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                brand = new ArrayList<String>();
                detail = new ArrayList<String>();
                owner = new ArrayList<String>();
                addedDate = new ArrayList<String>();
                status = new ArrayList<String>();
                key = new ArrayList<String>();

                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    DataItem dataItem = s.getValue(DataItem.class);
                    if (dataItem != null) {
                        String productType = dataItem.getType().trim();
                        String productBrand = dataItem.getBrand().trim();
                        String productDetail = dataItem.getDetail().trim();
                        String productAddedDate = dataItem.getPurchasedDate().trim();
                        int subStringPosition = productAddedDate.indexOf("T");
                        String productAddedDateSubString = productAddedDate.substring(0, subStringPosition);
                        String productOwner = dataItem.getPlaceName().trim();
                        String productStatus;
                        if (productOwner.matches("-")) {
                            productStatus = "Available";
                        } else {
                            productStatus = "Active";
                        }
                        String productKey = dataItem.getUnnamed2().trim();

                        brand.add(productBrand);
                        detail.add(productDetail);
                        owner.add(productOwner);
                        addedDate.add(productAddedDateSubString);
                        status.add(productStatus);
                        key.add(productKey);
                    }
                }
                recyclerListDetailAdapter.setBrand(brand);
                recyclerListDetailAdapter.setDetail(detail);
                recyclerListDetailAdapter.setOwner(owner);
                recyclerListDetailAdapter.setAddedDate(addedDate);
                recyclerListDetailAdapter.setStatus(status);
                recyclerListDetailAdapter.setKey(key);
                recyclerListDetailAdapter.notifyDataSetChanged();
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


}
