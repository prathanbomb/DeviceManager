package com.example.devicemanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.devicemanager.R;
import com.example.devicemanager.fragment.LoginFragment;
import com.example.devicemanager.fragment.MainFragment;
import com.example.devicemanager.fragment.SummaryFragment;
import com.example.devicemanager.manager.LoadData;
import com.example.devicemanager.room.ItemEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private LoadData loadData;
    private Boolean downloadStatus;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private View view;
    private ProgressBar progressBar;
    private Button btnSummary, btnDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initInstances();

        String logout = getIntent().getStringExtra("logout");

        if (logout != null && logout.matches("true")) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, LoginFragment.newInstance())
                    .commit();
        } else {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentContainer
                                        , SummaryFragment.newInstance()
                                        , "SummaryFragment")
                                .detach(SummaryFragment.newInstance())
                                .commit();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.contentContainer
                                        , MainFragment.newInstance()
                                        , "MainFragment")
                                .commit();
                    }
                }
            };
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    private void initInstances() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        btnDetail = findViewById(R.id.btnDetail);
        btnSummary = findViewById(R.id.btnSummary);

        btnDetail.setOnClickListener(onBtnClick);
        btnSummary.setOnClickListener(onBtnClick);

        sp = this.getSharedPreferences("DownloadStatus", Context.MODE_PRIVATE);
        editor = sp.edit();
        view = findViewById(R.id.view);
        progressBar = findViewById(R.id.spin_kit);
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        loadData = new LoadData(this);

        downloadStatus = sp.getBoolean("downloadStatus", true);
        if (downloadStatus) {
            loadData();
        } else {
            view.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadStatus = sp.getBoolean("downloadStatus", true);
        if (downloadStatus) {
            loadData();
        } else {
            view.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.clear();
        editor.commit();
    }

    private void loadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                loadData.deleteTable();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    ItemEntity item = s.getValue(ItemEntity.class);

                    if (item != null) {
                        if (!item.getPurchasedDate().matches("") &&
                                !item.getPurchasedDate().matches("-")) {
                            item.setPurchasedDate(setDate(item.getPurchasedDate()));
                        }
                        item.setAutoId(Integer.parseInt(s.getKey()));
                        loadData.insert(item);
                    }
                }
                editor.putBoolean("downloadStatus", false);
                editor.commit();
                view.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String setDate(String inputDate) {
        if (inputDate.contains("GMT")) {
            inputDate = inputDate.substring(0, inputDate.indexOf("GMT")).trim();
        }
        String inputFormat = "EEE MMM dd yyyy HH:mm:ss";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(
                inputFormat, Locale.ENGLISH);
        String outputFormat = "yyyy-MM-dd";
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

    View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            MainFragment mainFragment = (MainFragment)
                    getSupportFragmentManager().findFragmentByTag("MainFragment");
            SummaryFragment secondFragment = (SummaryFragment)
                    getSupportFragmentManager().findFragmentByTag("SummaryFragment");

            if (view == btnDetail) {
                getSupportFragmentManager().beginTransaction()
                        .attach(mainFragment)
                        .detach(secondFragment)
                        .commit();
            } else if (view == btnSummary) {
                getSupportFragmentManager().beginTransaction()
                        .attach(secondFragment)
                        .detach(mainFragment)
                        .commit();
            }
        }
    };
}
