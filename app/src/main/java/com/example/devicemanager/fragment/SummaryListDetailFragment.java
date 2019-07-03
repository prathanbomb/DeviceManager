package com.example.devicemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.adapter.RecyclerListDetailAdapter;
import com.example.devicemanager.manager.Contextor;
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
    Spinner spFilter, spSortBy;
    ArrayList<String> filterStatus, filterBrand, filterDetail, filterOwner, filterAddedDate, filterKey;


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
        spFilter = rootView.findViewById(R.id.spinnerFilter);
        spSortBy = rootView.findViewById(R.id.spinnerSortBy);

        ArrayAdapter<CharSequence> spinnerFilterAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                R.array.filter,
                R.layout.spinner_item_list_detail);
        spinnerFilterAdapter.setDropDownViewResource(R.layout.spinner_item_list_detail);

        ArrayAdapter<CharSequence> spinnerSortByAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                R.array.sort_by,
                R.layout.spinner_item_list_detail);
        spinnerSortByAdapter.setDropDownViewResource(R.layout.spinner_item_list_detail);

        spFilter.setAdapter(spinnerFilterAdapter);
        spSortBy.setAdapter(spinnerSortByAdapter);

//        spFilter.setOnItemSelectedListener(onSpinnerSelect);
//        spSortBy.setOnItemSelectedListener(onSpinnerSelect);

        setSpinnerDefault(spinnerFilterAdapter, spFilter);
        setSpinnerDefault(spinnerSortByAdapter, spSortBy);

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

    private void setSpinnerDefault(ArrayAdapter<CharSequence> spinnerFilterAdapter, Spinner spinner) {
        if (spinner == spFilter) {
            int spinnerPosition = spinnerFilterAdapter.getPosition("All");
            spinner.setSelection(spinnerPosition);
        }
        else if (spinner == spSortBy) {
            int spinnerPosition = spinnerFilterAdapter.getPosition("Purchased Date");
            spinner.setSelection(spinnerPosition);
        }
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
                        String date = productAddedDate.substring(8, 10);
                        String month = productAddedDate.substring(4, 7);
                        String year = productAddedDate.substring(11, 15);
                        String productAddedDateSubString = date+" "+month+" "+year;

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

    private AdapterView.OnItemSelectedListener onSpinnerSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView == spFilter) {
                String filter = adapterView.getItemAtPosition(i).toString();
                switch (filter) {
                    case "All":
                        recyclerListDetailAdapter.setBrand(brand);
                        recyclerListDetailAdapter.setDetail(detail);
                        recyclerListDetailAdapter.setOwner(owner);
                        recyclerListDetailAdapter.setAddedDate(addedDate);
                        recyclerListDetailAdapter.setStatus(status);
                        recyclerListDetailAdapter.setKey(key);
                        recyclerListDetailAdapter.notifyDataSetChanged();
                        break;
                    case "Available":
                        spinnerSetRecyclerview(filter);
                        break;
                    case "Active":
                        spinnerSetRecyclerview(filter);
                        break;
                }
            }
            if (adapterView == spSortBy) {
                String sortBy = adapterView.getItemAtPosition(i).toString();
                switch (sortBy) {
                    case "All":
                        Toast.makeText(getContext(), "All", Toast.LENGTH_SHORT).show();
                        break;
                    case "Name":
                        Toast.makeText(getContext(), "Name", Toast.LENGTH_SHORT).show();
                        break;
                    case "Purchased Date":
                        Toast.makeText(getContext(), "Purchased Date", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void spinnerSetRecyclerview(String spinnerStatus) {
        filterStatus = new ArrayList<String>();
        filterBrand = new ArrayList<String>();
        filterDetail = new ArrayList<String>();
        filterOwner = new ArrayList<String>();
        filterAddedDate = new ArrayList<String>();
        filterKey = new ArrayList<String>();

        for (int position = 0; position < status.size(); position++) {
            if (status.get(position).matches(spinnerStatus)) {
                filterStatus.add(status.get(position));
                filterBrand.add(brand.get(position));
                filterDetail.add(detail.get(position));
                filterOwner.add(owner.get(position));
                filterAddedDate.add(addedDate.get(position));
                filterKey.add(key.get(position));
            }
        }

        recyclerListDetailAdapter.setBrand(filterBrand);
        recyclerListDetailAdapter.setDetail(filterDetail);
        recyclerListDetailAdapter.setOwner(filterOwner);
        recyclerListDetailAdapter.setAddedDate(filterAddedDate);
        recyclerListDetailAdapter.setStatus(filterStatus);
        recyclerListDetailAdapter.setKey(filterKey);
        recyclerListDetailAdapter.notifyDataSetChanged();
    }


}
