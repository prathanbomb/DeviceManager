package com.example.devicemanager.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.AddDeviceActivity;
import com.example.devicemanager.activity.ScanBarcodeActivity;
import com.example.devicemanager.activity.SearchActivity;
import com.example.devicemanager.activity.SummaryActivity;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {
    Button btnAdd, btnCheck, btnSummary;
    LinearLayout linearLayout;
    android.widget.SearchView searchView;

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
        //setHasOptionsMenu(true);

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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (android.widget.SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }*/

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCheck = (Button) rootView.findViewById(R.id.btnCheck);
        btnSummary = (Button) rootView.findViewById(R.id.btnSummary);

        btnAdd.setOnClickListener(clickListener);
        btnCheck.setOnClickListener(clickListener);
        btnSummary.setOnClickListener(clickListener);

        linearLayout = rootView.findViewById(R.id.layout_search);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnAdd) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra("serial","null");
                startActivity(intent);
            } else if (view == btnCheck) {
                Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
                startActivity(intent);
            } else if (view == btnSummary) {
                Intent intent = new Intent(getActivity(), SummaryActivity.class);
                startActivity(intent);
            }
        }
    };

    android.widget.SearchView.OnQueryTextListener queryTextListener = new android.widget.SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.i("onQueryTextChange", query);

            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.i("onQueryTextSubmit", newText);

            return true;
        }
    };
}
