package com.example.devicemanager.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.ScanBarCodeAddDeviceActivity;
import com.example.devicemanager.manager.Contextor;
import com.example.devicemanager.manager.LoadData;
import com.example.devicemanager.model.DataItem;
import com.example.devicemanager.room.ItemEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class AddDeviceFragment extends Fragment {

    private Spinner spType, spTypeList, spBranch;
    private EditText etOwnerName, etSerialNumber, etDeviceDetail, etDatePicker,
            etOwnerId, etBrand, etDeviceModel, etDevicePrice, etNote, etQuantity,
            etPurchasePrice, etForwardDepreciation, etDepreciationRate, etDepreciationinYear,
            etAccumulateDepreciation, etForwardedBudget, etWarranty;
    private Button btnConfirm;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private String selected, lastKey, itemId, serial, serialState, abbreviation, type, unnamed2, YY;
    private int path, category, branch, order, countDevice = 1, quntity = 1, updatedKey = 0;
    private ProgressBar progressBar;
    private View progressDialogBackground;
    private DatabaseReference databaseReference;
    private TextView tvItemId, tvQuantity, tvClickToShow;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private LoadData loadData;
    private LinearLayout moreData;
    private Boolean clickMore = false;
    List<ItemEntity> itemEntity;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

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
                etSerialNumber.setText(serial);
            }
        }
    }

    private void initInstances(View view, Bundle savedInstanceState) {
        loadData = new LoadData(getContext());

        spBranch = view.findViewById(R.id.spinnerBranch);
        spType = view.findViewById(R.id.spinnerDeviceType);
        spTypeList = view.findViewById(R.id.spinnerDeviceTypeList);

        sp = getContext().getSharedPreferences("DownloadStatus", Context.MODE_PRIVATE);
        editor = sp.edit();

        setSpinner(R.array.branch, spBranch);
        setSpinner(R.array.device_types, spType);

        tvItemId = view.findViewById(R.id.tvItemId);
        etOwnerName = view.findViewById(R.id.etOwnerName);
        etSerialNumber = view.findViewById(R.id.etSerialNumber);
        etDeviceDetail = view.findViewById(R.id.etDeviceDetail);
        etDatePicker = view.findViewById(R.id.etPurchaseDate);
        etOwnerId = view.findViewById(R.id.etOwnerId);
        etBrand = view.findViewById(R.id.etDeviceBrand);
        etDeviceModel = view.findViewById(R.id.etDeviceModel);
        etDevicePrice = view.findViewById(R.id.etPrice);
        etNote = view.findViewById(R.id.etNote);
        etPurchasePrice = view.findViewById(R.id.etDevicePurchasePrice);
        etQuantity = view.findViewById(R.id.etQuantity);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvClickToShow = view.findViewById(R.id.tvClickToShow);
        moreData = view.findViewById(R.id.hidedLayout);
        etForwardDepreciation = view.findViewById(R.id.etForwardDepreciation);
        etDepreciationRate = view.findViewById(R.id.etDepreciationRate);
        etDepreciationinYear = view.findViewById(R.id.etDepreciationinYear);
        etAccumulateDepreciation = view.findViewById(R.id.etAccumulateDepreciation);
        etForwardedBudget = view.findViewById(R.id.etForwardedBudget);
        etWarranty = view.findViewById(R.id.etWarranty);

        tvClickToShow.setOnClickListener(clickListener);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        this.date = onDateSet;
        etDatePicker.setOnClickListener(onClickDate);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(clickListener);

        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        progressDialogBackground = (View) view.findViewById(R.id.view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");

        etSerialNumber.setOnTouchListener(onTouchScan);

        itemId = getArguments().getString("Serial");
        if (itemId != null) {
            tvQuantity.setText(getResources().getString(R.string.quantity) + ":1");
            etQuantity.setVisibility(View.INVISIBLE);
            lastKey = "" + loadData.selectData(itemId).get(0).getAutoId();
            setData();
        }
    }

    private void setData() {
        tvItemId.setText(itemId);
        setSpinnerPosition(R.array.branch, spBranch, Integer.parseInt(itemId.substring(5, 6)), null);
        setSpinnerPosition(R.array.device_types, spType, Integer.parseInt(itemId.substring(6, 7)), null);
        String spinnerName;
        spinnerName = itemId.substring(8, 11);
        switch (Integer.parseInt(itemId.substring(6, 7))) {
            case 1:
                setSpinnerPosition(R.array.building, spTypeList, -1, spinnerName);
                break;
            case 2:
                setSpinnerPosition(R.array.device_and_accessory, spTypeList, -1, spinnerName);
                break;
            case 3:
                setSpinnerPosition(R.array.furniture, spTypeList, -1, spinnerName);
                break;
            case 4:
                setSpinnerPosition(R.array.other, spTypeList, -1, spinnerName);
                break;
        }
        itemEntity = loadData.selectData(itemId);
        if (itemEntity.size() == 0) {
            return;
        }
        etOwnerId.setText(itemEntity.get(0).getPlaceId());
        etOwnerName.setText(itemEntity.get(0).getPlaceName());
        etBrand.setText(itemEntity.get(0).getBrand());
        etSerialNumber.setText(itemEntity.get(0).getSerialNo());
        etDeviceDetail.setText(itemEntity.get(0).getDetail());
        etDeviceModel.setText(itemEntity.get(0).getModel());
        etDevicePrice.setText(itemEntity.get(0).getPrice());
        etPurchasePrice.setText(itemEntity.get(0).getPurchasedPrice());
        etDatePicker.setText(itemEntity.get(0).getPurchasedDate().substring(0, 10));
        etNote.setText(itemEntity.get(0).getNote());
        etForwardDepreciation.setText(itemEntity.get(0).getForwardDepreciation());
        etDepreciationRate.setText(itemEntity.get(0).getDepreciationRate());
        etDepreciationinYear.setText(itemEntity.get(0).getDepreciationYear());
        etAccumulateDepreciation.setText(itemEntity.get(0).getAccumulatedDepreciation());
        etForwardedBudget.setText(itemEntity.get(0).getForwardedBudget());
        etWarranty.setText(itemEntity.get(0).getWarrantyDate());
    }

    private void setSpinner(int spinnerlist, Spinner spinner) {
        spinnerAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().getContext(),
                spinnerlist,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(onSpinnerSelect);
    }

    private void setSpinnerPosition(int spinerlist, Spinner spinner, int position, String spinerName) {
        if (position == -1) {
            spinnerAdapter = ArrayAdapter.createFromResource(
                    Contextor.getInstance().getContext(),
                    spinerlist,
                    R.layout.spinner_item);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(spinnerAdapter);
            String[] array = getResources().getStringArray(spinerlist);
            List<String> list = Arrays.asList(array);
            int spinnerPosition = 0;

            for (String str : list) {
                if (str.contains(spinerName)) {
                    spinnerPosition = list.indexOf(str);
                }
            }
            spinner.setSelection(spinnerPosition, true);
        } else {
            spinner.setSelection(position - 1);
        }
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

        if (itemId == null) {
            lastKey = getLastKey();
        }

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type.matches("save") && checkForm()) {
                    if (tvItemId.getText().toString().matches("Item Id")) {

                        YY = etDatePicker.getText().toString().substring(8, 10);
                        order = 1;
                        String form = "DGO" + YY + branch + category;

                        List<ItemEntity> itemEntity = loadData.getItem();
                        for (int i = 0; i < itemEntity.size(); i++) {
                            if (itemEntity.get(i).getUnnamed2().toString().contains(form))
                                order++;
                        }
                        int count = 0;
                        try {
                            count = Integer.parseInt(etQuantity.getText().toString());
                            quntity = count;
                        } catch (NumberFormatException num) {
                            Toast.makeText(getContext(), "" + num, Toast.LENGTH_SHORT).show();
                        }
                        for (int i = 0; i < count; i++) {
                            setUpdatedId(lastKey);
                            saveData();
                            int key = Integer.parseInt(lastKey) + 1;
                            lastKey = key + "";
                            order++;
                        }

                    } else {
                        progressDialogBackground.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        unnamed2 = tvItemId.getText().toString();
                        updateData();
                    }

                } else if (type.matches("serial")) {
                    Toast.makeText(getActivity(), "Intent to Detail", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/
                }
            }
        }).

                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateData() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        final Date dateCheck = new Date();
        databaseReference.child(lastKey).child("placeName").setValue(etOwnerName.getText().toString());
        itemEntity.get(0).setPlaceName(etOwnerName.getText().toString());

        databaseReference.child(lastKey).child("lastUpdated").setValue(dateFormat.format(dateCheck));
        itemEntity.get(0).setLastUpdated(dateFormat.format(dateCheck));

        databaseReference.child(lastKey).child("detail").setValue(etDeviceDetail.getText().toString());
        itemEntity.get(0).setDetail(etDeviceDetail.getText().toString());

        databaseReference.child(lastKey).child("serialNo").setValue(etSerialNumber.getText().toString());
        itemEntity.get(0).setSerialNo(etSerialNumber.getText().toString());

        databaseReference.child(lastKey).child("placeId").setValue(etOwnerId.getText().toString());
        itemEntity.get(0).setPlaceId(etOwnerId.getText().toString());

        databaseReference.child(lastKey).child("purchasedPrice").setValue(etPurchasePrice.getText().toString());
        itemEntity.get(0).setPurchasedPrice(etPurchasePrice.getText().toString());

        databaseReference.child(lastKey).child("price").setValue(etDevicePrice.getText().toString());
        itemEntity.get(0).setPrice(etDevicePrice.getText().toString());

        databaseReference.child(lastKey).child("unnamed2").setValue(tvItemId.getText().toString());
        itemEntity.get(0).setUnnamed2(tvItemId.getText().toString());

        databaseReference.child(lastKey).child("note").setValue(etNote.getText().toString());
        itemEntity.get(0).setNote(etNote.getText().toString());

        databaseReference.child(lastKey).child("forwardDepreciation").setValue(etForwardDepreciation.getText().toString());
        itemEntity.get(0).setForwardDepreciation(etForwardDepreciation.getText().toString());

        databaseReference.child(lastKey).child("depreciationRate").setValue(etDepreciationRate.getText().toString());
        itemEntity.get(0).setDepreciationRate(etDepreciationRate.getText().toString());

        databaseReference.child(lastKey).child("depreciationYear").setValue(etDepreciationinYear.getText().toString());
        itemEntity.get(0).setDepreciationYear(etDepreciationinYear.getText().toString());

        databaseReference.child(lastKey).child("accumulatedDepreciation").setValue(etAccumulateDepreciation.getText().toString());
        itemEntity.get(0).setAccumulatedDepreciation(etAccumulateDepreciation.getText().toString());

        databaseReference.child(lastKey).child("forwardedBudget").setValue(etForwardedBudget.getText().toString());
        itemEntity.get(0).setForwardBudget(etForwardedBudget.getText().toString());

        databaseReference.child(lastKey).child("warrantyDate").setValue(etWarranty.getText().toString());
        itemEntity.get(0).setWarrantyDate(etWarranty.getText().toString());

        setUpdatedId(lastKey);
        databaseReference.child(lastKey).child("model").setValue(etDeviceModel.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    itemEntity.get(0).setModel(etDeviceModel.getText().toString());
                    editor.putBoolean("downloadStatus", true);
                    editor.commit();
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

    private void saveData() {
        progressDialogBackground.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String date = setDate(etDatePicker.getText().toString());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        final Date dateCheck = new Date();
        // TODO: Add more item data
        //TODO:รหัสทรัพสิน ID
        DataItem item = new DataItem("ID", etOwnerId.getText().toString(), etOwnerName.getText().toString(),
                etBrand.getText().toString(), etSerialNumber.getText().toString(), etDeviceModel.getText().toString(),
                etDeviceDetail.getText().toString(), etDevicePrice.getText().toString(), etPurchasePrice.getText().toString(),
                date, etNote.getText().toString(), type, getUnnamed2(), etForwardDepreciation.getText().toString(),
                etDepreciationRate.getText().toString(), etDepreciationinYear.getText().toString(),
                etAccumulateDepreciation.getText().toString(), etForwardedBudget.getText().toString(), "" + YY,
                getUnnamed2().substring(3), "" + category, "" + branch, "-",
                "-", "" + dateFormat.format(dateCheck).toString(), "" + order,
                "" + abbreviation, "-", "DGO", etWarranty.getText().toString());
        final ItemEntity itemSave = new ItemEntity(loadData.getItem().size(),getUnnamed2(),type,etDeviceDetail.getText().toString(),
                etSerialNumber.getText().toString(),etOwnerName.getText().toString(),etDatePicker.getText().toString(),
                etNote.getText().toString(),"-",etOwnerId.getText().toString(),getUnnamed2().substring(3),
                etDevicePrice.getText().toString(),etDeviceModel.getText().toString(),etDepreciationRate.getText().toString(),
                "ID",etBrand.getText().toString(),abbreviation,order+"","-",YY+"","DGO","-",etForwardedBudget.getText().toString(),
                etAccumulateDepreciation.getText().toString(),etWarranty.getText().toString(),etDepreciationinYear.getText().toString(),
                branch+"",""+category,etPurchasePrice.getText().toString(),etForwardedBudget.getText().toString(),
                etForwardDepreciation.getText().toString(),dateFormat.format(dateCheck));
        if (lastKey != null) {
            databaseReference.child(lastKey).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (countDevice == quntity) {
                            loadData.insert(itemSave);
                            Toast.makeText(getActivity(), "Complete!", Toast.LENGTH_SHORT).show();
                            progressDialogBackground.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intentBack = new Intent();
                            getActivity().setResult(RESULT_OK, intentBack);
                            getActivity().finish();
                        }
                        countDevice++;
                    } else {
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialogBackground.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private void getUpdateKey() {
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Updated");
        Query query = databaseReference.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    updatedKey = Integer.parseInt(s.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                updatedKey = -1;
            }
        });
    }

    private void setUpdatedId(String lastKey) {
        FirebaseDatabase.getInstance().getReference().child("Updated")
                .child(updatedKey + "").child("id").setValue(lastKey)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Complete!", Toast.LENGTH_SHORT).show();

                            // TODO: Add Success SuccessDialog
                            progressDialogBackground.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        updatedKey++;
    }

    private String setDate(String inputDate) {
        if (inputDate.contains("GMT")) {
            inputDate = inputDate.substring(0, inputDate.indexOf("GMT")).trim();
        }
        String inputFormat = "dd/MM/yyyy";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(
                inputFormat, Locale.ENGLISH);
        String outputFormat = "EEE MMM dd yyyy HH:mm:ss";
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(
                outputFormat, Locale.ENGLISH);

        Date date;
        String str = inputDate;

        try {
            date = inputDateFormat.parse(inputDate);
            str = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;

    }

    private String getUnnamed2() {
        if (unnamed2 == null) {
            String date = etDatePicker.getText().toString();
            YY = date.substring(8, 10);
            String num;
            if (order < 1) {
                num = "001";
                order++;
            } else if (order < 10) {
                num = "00" + String.valueOf(order);
            } else if (order < 100) {
                num = "0" + String.valueOf(order);
            } else {
                num = "" + order;
            }
            String generateSerial = "DGO" + YY + branch + category + "-" + abbreviation + num;
            return generateSerial;
        }
        return unnamed2;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        etDatePicker.setText(sdf.format(calendar.getTime()));
    }

    private String getLastKey() {
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
                lastKey = "";
            }
        });
        return lastKey;
    }

    private void checkSerial() {
        List<ItemEntity> itemEntities = loadData.getItem();
        for (int i = 0; i < itemEntities.size(); i++) {
            if (itemEntities.get(i).getSerialNo().matches(serial)) {
                showAlertDialog("serial");
            }
        }
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
            etSerialNumber.setText("-");
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
        if (TextUtils.isEmpty(etForwardDepreciation.getText())) {
            etForwardDepreciation.setText("-");
        }
        if (TextUtils.isEmpty(etDepreciationRate.getText())) {
            etDepreciationRate.setText("-");
        }
        if (TextUtils.isEmpty(etDepreciationinYear.getText())) {
            etDepreciationinYear.setText("-");
        }
        if (TextUtils.isEmpty(etAccumulateDepreciation.getText())) {
            etAccumulateDepreciation.setText("-");
        }
        if (TextUtils.isEmpty(etForwardedBudget.getText())) {
            etForwardedBudget.setText("-");
        }
        if (TextUtils.isEmpty(etWarranty.getText())) {
            etWarranty.setText("-");
        }

        if (TextUtils.isEmpty(etDevicePrice.getText()) && TextUtils.isEmpty(etPurchasePrice.getText())) {
            Toast.makeText(getContext(), "Please input price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etDevicePrice.getText())) {
            etDevicePrice.setText("-");
        }
        if (TextUtils.isEmpty(etPurchasePrice.getText())) {
            etPurchasePrice.setText("-");
        }
        if (TextUtils.isEmpty(etDatePicker.getText())) {
            Toast.makeText(getContext(), "Please input purchased date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etQuantity.getText())) {
            etDeviceModel.setText("1");
            Toast.makeText(getContext(), "1 piece", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnConfirm) {
                hideKeyboardFrom(Contextor.getInstance().getContext(), view);
                getUpdateKey();
                showAlertDialog("save");
            } else if (view == tvClickToShow) {
                if (!clickMore) {
                    clickMore = true;
                    moreData.setVisibility(View.VISIBLE);
                } else {
                    clickMore = false;
                    moreData.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private View.OnClickListener onClickDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (itemId != null) {
                String[] d = etDatePicker.getText().toString().split("-");
                new DatePickerDialog(getActivity(),
                        date, Integer.parseInt(d[0]),
                        Integer.parseInt(d[1]) - 1,
                        Integer.parseInt(d[2])).show();
            } else {
                new DatePickerDialog(getActivity(),
                        date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
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
            if (adapterView == spType && itemId == null) {
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
            } else if (adapterView == spTypeList) {
                selected = adapterView.getItemAtPosition(i).toString();
                abbreviation = selected.toUpperCase().substring(0, 3);
                type = selected.toUpperCase();
            } else if (adapterView == spBranch) {
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
