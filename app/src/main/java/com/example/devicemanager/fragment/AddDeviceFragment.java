package com.example.devicemanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.devicemanager.R;
import com.example.devicemanager.activity.CameraActivity;
import com.example.devicemanager.activity.ScanBarCodeAddDeviceActivity;
import com.example.devicemanager.manager.Contextor;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class AddDeviceFragment extends Fragment {

    private Spinner spType;
    private ImageView ivDevice;
    private EditText etOwnerName, etSerialNumber, etDeviceDetail;
    private Button btnConfirm;
    private Button btnScanBarcode;

    public static AddDeviceFragment newInstances() {
        AddDeviceFragment fragment = new AddDeviceFragment();
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
        initInstances(view, savedInstanceState);
        return view;
    }

    private void initInstances(View view, Bundle savedInstanceState) {

        spType = view.findViewById(R.id.spinnerDeviceType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                R.array.device_types,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spType.setAdapter(spinnerAdapter);

        ivDevice = view.findViewById(R.id.ivDevice);
        ivDevice.setOnClickListener(onClickImage);
        etOwnerName = (EditText) view.findViewById(R.id.etOwnerName);
        etSerialNumber = (EditText) view.findViewById(R.id.etSerialNumber);
        etDeviceDetail = (EditText) view.findViewById(R.id.etDeviceDetail);
        /*btnScanBarcode = (Button) view.findViewById(R.id.btnScanBarcode);
        btnScanBarcode.setOnClickListener(onClickBtnScan);*/
        etSerialNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getX() >= (etSerialNumber.getWidth()
                            - etSerialNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent intent = new Intent(getActivity(), ScanBarCodeAddDeviceActivity.class);
                        startActivityForResult(intent, 12345);
                        return true;
                    }
                }
                return false;
            }
        });

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(clickListener);

        String path = getArguments().getString("Path");
        String serial = getArguments().getString("Serial");

        if (path != null) {
            Uri uri = Uri.fromFile(new File(getArguments().getString("Path")));
            Glide.with(Contextor.getInstance().getContext())
                    .load(uri)
                    .into(ivDevice);
        }
        if (serial != null) {
            etSerialNumber.setText(serial);
            etDeviceDetail.setText("Macbook Pro 14");
            etOwnerName.setText("Mr.Natthapat Phatthana");
        }
    }

    private void showAlertDialog(int msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String dialogMsg = getResources().getString(msg);

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                Intent intentBack = new Intent();
                getActivity().setResult(RESULT_OK,intentBack);
                getActivity().finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345) {
            if (resultCode == RESULT_OK) {
                etSerialNumber.setText(data.getStringExtra("serial"));
            }
        }
    }

    // TODO: Save State in this condition & Fix stacked activity
    View.OnClickListener onClickImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Contextor.getInstance().getContext(), CameraActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnConfirm) {
                showAlertDialog(R.string.dialog_msg_confirm);
            }
        }
    };

    /*View.OnClickListener onClickBtnScan = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ScanBarCodeAddDeviceActivity.class);
            startActivityForResult(intent, 12345);
        }
    };*/
}
