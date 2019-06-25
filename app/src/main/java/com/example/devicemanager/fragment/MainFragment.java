package com.example.devicemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.AddDeviceActivity;
import com.example.devicemanager.activity.ScanBarcodeActivity;
import com.example.devicemanager.activity.SearchActivity;
import com.example.devicemanager.activity.SummaryActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {

    private Button btnAdd, btnCheck, btnSummary;
    private FloatingActionButton floatingButton, floatingButton2, floatingButton3;
    private android.widget.SearchView searchView;
    private boolean isFABOpen = false;
    private TextView tvLogout;
    private FirebaseAuth mAuth;

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

        floatingButton = rootView.findViewById(R.id.fabSearch);
        floatingButton2 = rootView.findViewById(R.id.fabTag);
        floatingButton3 = rootView.findViewById(R.id.fabName);
        floatingButton3.setOnClickListener(onClickFABName);
        floatingButton.setOnClickListener(onClickFABSearch);

        tvLogout = rootView.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(onClickLogout);
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

    private void closeFABMenu() {
        isFABOpen = false;
        floatingButton2.animate().translationY(0);
        floatingButton3.animate().translationY(0).translationX(0);
    }

    private void showFABMenu() {
        isFABOpen = true;
        floatingButton2.setVisibility(View.VISIBLE);
        floatingButton3.setVisibility(View.VISIBLE);
        floatingButton2.animate()
                .translationY(-getResources().getDimension(R.dimen.transition_floating_y));
        floatingButton3.animate()
                .translationY(-getResources().getDimension(R.dimen.transition_floating_y))
                .translationX(-getResources().getDimension(R.dimen.transition_floating_x));
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnAdd) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                closeFABMenu();
                intent.putExtra("serial","null");
                startActivity(intent);
            } else if (view == btnCheck) {
                Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
                closeFABMenu();
                startActivity(intent);
            } else if (view == btnSummary) {
                Intent intent = new Intent(getActivity(), SummaryActivity.class);
                closeFABMenu();
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener onClickLogout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
        }
    };

    private View.OnClickListener onClickFABSearch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        }
    };

    private View.OnClickListener onClickFABName = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            closeFABMenu();
            startActivity(intent);
        }
    };
}
