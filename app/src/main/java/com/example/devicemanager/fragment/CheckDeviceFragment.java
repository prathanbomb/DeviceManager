package com.example.devicemanager.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.AddDeviceActivity;
import com.example.devicemanager.manager.Contextor;
import com.example.devicemanager.manager.LoadData;
import com.example.devicemanager.model.DataItem;
import com.example.devicemanager.room.ItemEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CheckDeviceFragment extends Fragment {

    private TextView tvSerialNumber, tvOwnerName, tvDeviceDetail,
            tvLastUpdate, tvAddedDate,tvType;
    private static String serial;
    private Button btnConfirm, btnEdit;
    private ProgressBar progressBar;
    private View progressDialogBackground;
    private String itemStatus;
    private LoadData loadData;


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

        loadData = new LoadData(getContext());

        tvSerialNumber = view.findViewById(R.id.tvSerialNumber);
        tvOwnerName = view.findViewById(R.id.tvOwnerName);
        tvDeviceDetail = view.findViewById(R.id.tvDeviceDetail);
        tvAddedDate = view.findViewById(R.id.tvAddedDate);
        tvLastUpdate = view.findViewById(R.id.tvLastUpdate);
        tvType = view.findViewById(R.id.tvType);

        tvSerialNumber.setText(serial);

        btnEdit = view.findViewById(R.id.btnEdit);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) view.findViewById(R.id.view);

        btnEdit.setOnClickListener(clickListener);
        btnConfirm.setOnClickListener(clickListener);

        getData(serial);

    }

    private void getData(String serialNew) {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        List<ItemEntity> itemEntity = loadData.selectData(serialNew);
        if(itemEntity.size() == 0){
            hideDialog();
            showAlertDialog(R.string.dialog_msg_confirm, "add");
            return;
        }
        tvSerialNumber.setText(itemEntity.get(0).getUnnamed2());
        tvOwnerName.setText(itemEntity.get(0).getPlaceName());
        tvDeviceDetail.setText(itemEntity.get(0).getDetail());
        tvLastUpdate.setText(getResources().getString(R.string.last_check) + " : " + "-");
        String productAddedDateSubString;
        if (itemEntity.get(0).getPurchasedDate().length() >= 15) {
            String date = itemEntity.get(0).getPurchasedDate().substring(8, 10);
            String month = itemEntity.get(0).getPurchasedDate().substring(4, 7);
            String year = itemEntity.get(0).getPurchasedDate().substring(11, 15);
            productAddedDateSubString = date + " " + month + " " + year;
        } else {
            productAddedDateSubString = itemEntity.get(0).getPurchasedDate();
        }
        tvAddedDate.setText(getResources().getString(R.string.added_date) + " : " + productAddedDateSubString);
        tvType.setText("Type : "+itemEntity.get(0).getType());
        hideDialog();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnEdit) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra("Serial", serial);
                startActivityForResult(intent, 11111);
            } else if (view == btnConfirm) {
                showAlertDialog(R.string.dialog_msg_confirm, "confirm");
            }
        }
    };

    private void hideDialog() {
        progressDialogBackground.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showAlertDialog(int msg, final String state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String dialogMsg = getResources().getString(msg);

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (state.matches("confirm")) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                } else if (state.matches("add")) {
                    Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                    intent.putExtra("Serial", serial);
                    startActivity(intent);
                }
                getActivity().finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                if (state.matches("add")) {
                    getActivity().finish();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11111) {
            if (resultCode == RESULT_OK) {
                getActivity().finish();
            }
        }
    }
}
