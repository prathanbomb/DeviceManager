package com.example.devicemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.devicemanager.R;
import com.example.devicemanager.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())
                    .commit();
        }
        initInstance();
    }

    private void initInstance() {
    }

}
