package com.example.devicemanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.AddDeviceActivity;
import com.example.devicemanager.manager.Contextor;

public class CheckDeviceFragment extends Fragment {

    private Spinner spType;
    private TextView tvSerialNumber, tvOwnerName, etDeviceDetail;
    private static String serial;
    private Button btnConfirm,btnCancel,btnEdit;

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

        tvSerialNumber = view.findViewById(R.id.tvSerialNumber);
        tvOwnerName = view.findViewById(R.id.tvOwnerName);
        etDeviceDetail = view.findViewById(R.id.etDeviceDetail);

        tvSerialNumber.setText(serial.toString());
        tvOwnerName.setText("Mr.Natthapat Phatthana");
        etDeviceDetail.setText("Macbook Pro 14");

        btnEdit = view.findViewById(R.id.btnEdit);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        btnEdit.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
        btnConfirm.setOnClickListener(clickListener);

    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnEdit) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra("Serial", "09845236214");
                startActivity(intent);
                getActivity().finish();
            }
            else if(view == btnCancel){
                showAlertDialog(R.string.dialog_msg_cancel);
            }
            else if (view == btnConfirm) {
                showAlertDialog(R.string.dialog_msg_confirm);
            }
        }
    };

    private void showAlertDialog(int msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String dialogMsg = getResources().getString(msg);

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
