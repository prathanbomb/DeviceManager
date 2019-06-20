package com.example.devicemanager.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.devicemanager.R;
import com.example.devicemanager.manager.Contextor;

import java.io.File;

public class CheckDeviceFragment extends Fragment {

    private Spinner spType;
    TextView tvSerialNumber, tvOwnerName, etDeviceDetail;
    private static String serial;

    public static CheckDeviceFragment newInstances(String barcode) {
        CheckDeviceFragment fragment = new CheckDeviceFragment();
        serial = barcode;
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
        View view = inflater.inflate(R.layout.fragment_detail_device, container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {

        spType = view.findViewById(R.id.spinnerDeviceType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                R.array.device_types,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spType.setAdapter(spinnerAdapter);
        spType.setSelection(0);


        /*ivDevice = view.findViewById(R.id.ivDevice);
        Uri uri = Uri.fromFile(new File(getArguments().getString("Path")));

        Glide.with(Contextor.getInstance().getContext())
                .load(uri)
                .into(ivDevice);*/
        tvSerialNumber = (TextView) view.findViewById(R.id.tvSerialNumber);
        tvOwnerName = (TextView) view.findViewById(R.id.tvOwnerName);
        etDeviceDetail = (TextView) view.findViewById(R.id.etDeviceDetail);

        tvSerialNumber.setText(serial.toString());
        tvOwnerName.setText("Mr.Natthapat Phatthana");
        etDeviceDetail.setText("Macbook Pro 14");

    }
}
