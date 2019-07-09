package com.example.devicemanager.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.AddDeviceActivity;
import com.example.devicemanager.activity.MainActivity;
import com.example.devicemanager.activity.ScanBarcodeActivity;
import com.example.devicemanager.activity.SearchActivity;
import com.example.devicemanager.activity.SummaryActivity;
import com.example.devicemanager.adapter.ItemListAdapter;
import com.example.devicemanager.manager.DataManager;
import com.example.devicemanager.manager.LoadData;
import com.example.devicemanager.room.AppDatabase;
import com.example.devicemanager.room.ItemEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment implements ItemListAdapter.Holder.ItemClickListener {

    private Button btnAdd, btnCheck, btnSummary;
    private FloatingActionButton floatingButton;
    private android.widget.SearchView searchView;
    private boolean isFABOpen = false;
    private TextView tvLogout;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private DataManager dataManager;
    private ItemListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private LoadData loadData;
    private Boolean downloadStatus;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private View view;
    private ProgressBar progressBar;
    AppDatabase database;

    public MainFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.expandActionView();

        final SearchView searchViewActionBar = (SearchView) menuItem.getActionView();
        searchViewActionBar.clearFocus();
        searchViewActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                view.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(query);
                view.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_scan) {
            Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        sp = getContext().getSharedPreferences("DownloadStatus", Context.MODE_PRIVATE);
        editor = sp.edit();

        floatingButton = rootView.findViewById(R.id.fabAdd);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        dataManager = new DataManager();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemListAdapter(getContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        view = rootView.findViewById(R.id.view);
        progressBar = rootView.findViewById(R.id.spin_kit);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SummaryActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
