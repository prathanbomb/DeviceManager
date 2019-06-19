package com.example.devicemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.manager.Contextor;

public class DeviceDetailFragment extends Fragment {

    Spinner spType ;

    public static DeviceDetailFragment newInstances(){
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_detail, container, false);
        initIntances(view,savedInstanceState);
        return view;
    }

    public void init(){

    }

    public void initIntances(View view, Bundle savedInstanceState){

        spType = view.findViewById(R.id.spinnerDeviceType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                R.array.device_types,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spType.setAdapter(spinnerAdapter);
    }
}
