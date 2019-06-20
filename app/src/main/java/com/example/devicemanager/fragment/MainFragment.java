package com.example.devicemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.AddDeviceActivity;
import com.example.devicemanager.activity.CameraActivity;
import com.example.devicemanager.activity.ScanBarcodeActivity;
import com.example.devicemanager.activity.SearchActivity;
import com.example.devicemanager.activity.SummaryActivity;
import com.example.devicemanager.manager.Contextor;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {
    Button btnAdd, btnCheck, btnSummary;
    LinearLayout linearLayout;

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
                Intent intent = new Intent(Contextor.getInstance().getContext(), SearchActivity.class);
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
                Intent intent = new Intent(getActivity(), CheckDeviceActivity.class);
                intent.putExtra("serial","09845236214".toString());
                startActivity(intent);
            } else if (view == btnSummary) {
                Intent intent = new Intent(getActivity(), SummaryActivity.class);
                startActivity(intent);
            }
        }
    };
}
