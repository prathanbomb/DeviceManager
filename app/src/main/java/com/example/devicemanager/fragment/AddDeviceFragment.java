package com.example.devicemanager.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.CheckDeviceActivity;
import com.example.devicemanager.activity.ScanBarCodeAddDeviceActivity;
import com.example.devicemanager.manager.Contextor;
import com.example.devicemanager.model.DataItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class AddDeviceFragment extends Fragment {

    private Spinner spType, spTypeList,spBranch;
    private EditText etOwnerName, etSerialNumber, etDeviceDetail, etDatePicker,
            etOwnerId, etBrand, etDeviceModel, etDevicePrice, etNote,etQuantity;
    private Button btnConfirm;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String selected, lastKey, serial, serialState,abbreviation;
    //TODO:this is mock order
    private int path,category,branch,order =100;
    private ProgressBar progressBar;
    private View progressDialogBackground;
    private DatabaseReference databaseReference;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345) {
            if (resultCode == RESULT_OK) {
                serial = data.getStringExtra("serial");
                checkSerial();
                Log.d("serial537", serial);
                etSerialNumber.setText(serial);
            }
        }
    }

    private void initInstances(View view, Bundle savedInstanceState) {
        spBranch = view.findViewById(R.id.spinnerBranch);
        spType = view.findViewById(R.id.spinnerDeviceType);
        spTypeList = view.findViewById(R.id.spinnerDeviceTypeList);

        setSpinner(R.array.device_types, spType);
        setSpinner(R.array.branch, spBranch);

        spBranch.setOnItemSelectedListener(onSpinnerSelect);
        spType.setOnItemSelectedListener(onSpinnerSelect);
        spTypeList.setOnItemSelectedListener(onSpinnerSelect);

        etOwnerName = view.findViewById(R.id.etOwnerName);
        etSerialNumber = view.findViewById(R.id.etSerialNumber);
        etDeviceDetail = view.findViewById(R.id.etDeviceDetail);
        etDatePicker = view.findViewById(R.id.etPurchaseDate);
        etOwnerId = view.findViewById(R.id.etOwnerId);
        etBrand = view.findViewById(R.id.etDeviceBrand);
        etDeviceModel = view.findViewById(R.id.etDeviceModel);
        etDevicePrice = view.findViewById(R.id.etDevicePrice);
        etNote = view.findViewById(R.id.etNote);
        etDevicePrice = view.findViewById(R.id.etDevicePrice);
        etQuantity= view.findViewById(R.id.etQuantity);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        this.date = onDateSet;
        etDatePicker.setOnClickListener(onClickDate);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(clickListener);

        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) view.findViewById(R.id.view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");

        etSerialNumber.setOnTouchListener(onTouchScan);
        String serial = getArguments().getString("Serial");
        if (serial != null) {
            etSerialNumber.setText(serial);
        }
        getPath();
    }

    private void setSpinner(int spinnerlist, Spinner spinner) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                spinnerlist,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void showAlertDialog(final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int dialogMsg = 0;
        switch (type) {
            case "save":
                dialogMsg = R.string.dialog_msg_confirm;
                break;
            case "serial":
                dialogMsg = R.string.dialog_msg_check_serial;
                builder.setTitle(R.string.dialog_msg_head_check_serial);
                break;
        }

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type.matches("save") && checkForm()) {
                    for(int i = 0 ; i < Integer.parseInt(etQuantity.getText().toString()) ; i++) {
                        saveData();
                        int key = Integer.parseInt(lastKey)+1;
                        lastKey = key+"";
                        order++;
                    }
                } else if (type.matches("serial")) {
                    Toast.makeText(getActivity(), "Intent to Detail", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(getContext(), CheckDeviceActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getUnnamed2();
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveData() {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // TODO: Add more item data
        DataItem item = new DataItem("ID", etOwnerId.getText().toString(), etOwnerName.getText().toString(),
                etBrand.getText().toString(), etSerialNumber.getText().toString(), etDeviceModel.getText().toString(),
                etDeviceDetail.getText().toString(), etDevicePrice.getText().toString(), etDatePicker.getText().toString(),
                etNote.getText().toString(), "", getUnnamed2());

        if (lastKey != null) {
            databaseReference.child(lastKey).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Complete!", Toast.LENGTH_SHORT).show();

                        // TODO: Add Success SuccessDialog
                        progressDialogBackground.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intentBack = new Intent();
                        getActivity().setResult(RESULT_OK, intentBack);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialogBackground.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private String getUnnamed2() {
        String date = etDatePicker.getText().toString();
        String YY = date.substring(13);
        Log.d("getUnnamed2",""+branch+"-----"+category);
        String unnnamed2 = "DGO"+YY+branch+category+"-"+abbreviation+order;
        return unnnamed2;
    }

    private void updateLabel() {
        String myFormat = "E MMM dd yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        etDatePicker.setText(sdf.format(calendar.getTime()));
    }

    private void getPath() {
        Query query = databaseReference.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    lastKey = s.getKey();
                    path = Integer.parseInt(lastKey) + 1;
                    lastKey = String.valueOf(path);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Cannot Insert Data", Toast.LENGTH_SHORT).show();
                progressDialogBackground.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void checkSerial() {
        Query query = databaseReference.orderByChild("serialNo").equalTo(serial);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    showAlertDialog("serial");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean checkForm() {
        if (TextUtils.isEmpty(etDeviceDetail.getText())) {
            etDeviceDetail.setText("-");
        }
        if (TextUtils.isEmpty(etOwnerName.getText())) {
            etOwnerName.setText("-");
        }
        if (TextUtils.isEmpty(etDeviceDetail.getText())) {
            etDeviceDetail.setText("-");
        }
        if (TextUtils.isEmpty(etSerialNumber.getText())) {
            etSerialNumber.setText("=");
        }
        if (TextUtils.isEmpty(etBrand.getText())) {
            etBrand.setText("-");
        }
        if (TextUtils.isEmpty(etNote.getText())) {
            etNote.setText("-");
        }
        if (TextUtils.isEmpty(etOwnerId.getText())) {
            etOwnerId.setText("-");
        }
        if (TextUtils.isEmpty(etDeviceModel.getText())) {
            etDeviceModel.setText("-");
        }
        if (TextUtils.isEmpty(etDevicePrice.getText())) {
            Toast.makeText(getContext(), "Please input price per piece", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etDatePicker.getText())) {
            Toast.makeText(getContext(), "Please input purchased date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnConfirm) {
                getPath();
                hideKeyboardFrom(Contextor.getInstance().getContext(), view);
                showAlertDialog("save");
            }
        }
    };

    private View.OnClickListener onClickDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new DatePickerDialog(getActivity(),
                    date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private View.OnTouchListener onTouchScan = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int DRAWABLE_RIGHT = 2;

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getX() >= (etSerialNumber.getWidth()
                        - etSerialNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Intent intent = new Intent(getActivity(), ScanBarCodeAddDeviceActivity.class);
                    startActivityForResult(intent, 12345);
                    return true;
                }
            }
            return false;
        }
    };

    private AdapterView.OnItemSelectedListener onSpinnerSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView == spType) {
                selected = adapterView.getItemAtPosition(i).toString();
                switch (i) {
                    case 0:
                        setSpinner(R.array.building, spTypeList);
                        category = 1;
                        break;
                    case 1:
                        setSpinner(R.array.device_and_accessory, spTypeList);
                        category = 2;
                        break;
                    case 2:
                        setSpinner(R.array.furniture, spTypeList);
                        category = 3;
                        break;
                    case 3:
                        setSpinner(R.array.other, spTypeList);
                        category = 4;
                        break;
                }
            }
            else if (adapterView == spTypeList) {
                selected = adapterView.getItemAtPosition(i).toString();
                abbreviation = selected.toUpperCase().substring(0,3);
            }
            else if(adapterView == spBranch){
                switch (i) {
                    case 0:
                        branch = 1;
                        break;
                    case 1:
                        branch = 2;
                        break;
                    case 2:
                        branch = 3;
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            selected = "none";
        }
    };

}
