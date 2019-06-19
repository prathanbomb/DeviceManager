package com.example.devicemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.devicemanager.R;
import com.example.devicemanager.fragment.SummaryFragment;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, SummaryFragment.newInstance())
                    .commit();
        }
        initInstance();
    }

    private void initInstance() {
    }

}
