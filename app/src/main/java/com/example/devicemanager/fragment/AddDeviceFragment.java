package com.example.devicemanager.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    private Spinner spType;
    private ImageView ivDevice;
    private EditText etOwnerName, etSerialNumber, etDeviceDetail, etDatePicker,
     etOwnerId, etBrand, etDeviceModel, etDevicePrice, etNote;
    private Button btnConfirm;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String selected, lastKey;
    private int path;
    ProgressBar progressBar ;
    View progressDialogBackground;
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
                etSerialNumber.setText(data.getStringExtra("serial"));
            }
        }
    }

    private void initInstances(View view, Bundle savedInstanceState) {

        spType = view.findViewById(R.id.spinnerDeviceType);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                R.array.device_types,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spType.setAdapter(spinnerAdapter);
        spType.setOnItemSelectedListener(onSpinnerSelect);

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

        calendar = Calendar.getInstance(TimeZone.getDefault());
        this.date = onDateSet;
        etDatePicker.setOnClickListener(onClickDate);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(clickListener);

        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) view.findViewById(R.id.view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");

        //String path = getArguments().getString("Path");
        /*if (path != null) {
            Uri uri = Uri.fromFile(new File(getArguments().getString("Path")));
            Glide.with(Contextor.getInstance().getContext())
                    .load(uri)
                    .into(ivDevice);
        }*/

        etSerialNumber.setOnTouchListener(onTouchScan);
        String serial = getArguments().getString("Serial");
        if (serial != null) {
            etSerialNumber.setText(serial);
        }
        getPath();
    }

    private void showAlertDialog(int msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String dialogMsg = getResources().getString(msg);

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
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

    private void saveData() {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // TODO: Add more item data
        DataItem item = new DataItem("ID", etOwnerId.getText().toString(), etOwnerName.getText().toString(),
                etBrand.getText().toString(), etSerialNumber.getText().toString(), etDeviceModel.getText().toString(),
                etDeviceDetail.getText().toString(), etDevicePrice.getText().toString(), etDatePicker.getText().toString(),
                etNote.getText().toString());

        if (lastKey != null){
            Log.d("test152", lastKey + "");
            databaseReference.child(lastKey).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Complete!", Toast.LENGTH_SHORT).show();

                        Log.d("test152", "Successful");
                        // TODO: Add Success SuccessDialog
                        progressDialogBackground.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intentBack = new Intent();
                        getActivity().setResult(RESULT_OK,intentBack);
                        getActivity().finish();
                    }
                    else {
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialogBackground.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINESE);
        etDatePicker.setText(sdf.format(calendar.getTime()));
    }

    private void getPath(){
        Query query = databaseReference.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    lastKey = s.getKey();
                    Log.d("test152", lastKey + "");
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

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*// TO DO: Save State in this condition & Fix stacked activity
    private View.OnClickListener onClickImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Contextor.getInstance().getContext(), CameraActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };*/


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnConfirm) {
                showAlertDialog(R.string.dialog_msg_confirm);
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
            selected = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            selected = "none";
        }
    };

}
