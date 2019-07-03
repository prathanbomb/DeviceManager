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
import com.example.devicemanager.model.DataItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

public class CheckDeviceFragment extends Fragment {

    private Spinner spType;
    private TextView tvSerialNumber, tvOwnerName, tvDeviceDetail,
            tvLastUpdate, tvAddedDate;
    private static String serial;
    private Button btnConfirm, btnEdit;
    private ProgressBar progressBar;
    private View progressDialogBackground;
    private String itemStatus;

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
        tvDeviceDetail = view.findViewById(R.id.tvDeviceDetail);
        tvAddedDate = view.findViewById(R.id.tvAddedDate);
        tvLastUpdate = view.findViewById(R.id.tvLastUpdate);

        tvSerialNumber.setText(serial);

        btnEdit = view.findViewById(R.id.btnEdit);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) view.findViewById(R.id.view);

        btnEdit.setOnClickListener(clickListener);
        btnConfirm.setOnClickListener(clickListener);

        if (serial.contains("DGO")) {
            getData(serial, "unnamed2");
        } else {
            getData(serial, "serialNo");
        }
    }

    private void getData(String serialNew, String parameter) {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");
        Query query = databaseReference.orderByChild(parameter).equalTo(serialNew);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    hideDialog();
                    Toast.makeText(getActivity(), "Cannot find this item on Database.", Toast.LENGTH_SHORT).show();
                    showAlertDialog(R.string.dialog_msg_add, "add");
                }
                else {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        DataItem item = s.getValue(DataItem.class);
                        if (item != null) {
                            hideDialog();
//                            tvSerialNumber.setText(item.getUnnamed2());
                            tvOwnerName.setText(item.getPlaceName());
                            tvDeviceDetail.setText(item.getDetail());
                            tvLastUpdate.setText(getResources().getString(R.string.last_check) + " : " + "-");
                            String productAddedDateSubString;
                            if(item.getPurchasedDate().length()>=12){
                                String date = item.getPurchasedDate().substring(8, 10);
                                String month = item.getPurchasedDate().substring(4, 7);
                                String year = item.getPurchasedDate().substring(11, 15);
                                productAddedDateSubString = date + " " + month + " " + year;
                            }
                            else {
                                productAddedDateSubString = item.getPurchasedDate();
                            }
                            tvAddedDate.setText(getResources().getString(R.string.added_date) + " : " + productAddedDateSubString);

                        } else {
                            hideDialog();
                            Toast.makeText(getActivity(), "Cannot find this item on Database.", Toast.LENGTH_SHORT).show();
                            showAlertDialog(R.string.dialog_msg_add, "add");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideDialog();
                Toast.makeText(getActivity(), "Try again later", Toast.LENGTH_SHORT).show();
            }
        });
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
