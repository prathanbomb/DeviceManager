package com.example.devicemanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.devicemanager.R;
import com.example.devicemanager.fragment.AddDeviceFragment;
import com.example.devicemanager.fragment.SummaryFragment;
import com.example.devicemanager.fragment.SummaryListDetailFragment;

public class SummaryListDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_list_detail);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();

            bundle.putString("Type", getIntent().getStringExtra("Type"));
            bundle.putInt("Count", getIntent().getIntExtra("Count",0));

            SummaryListDetailFragment fragment = SummaryListDetailFragment.newInstance();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, fragment)
                    .commit();
        }
        initInstances();
    }

    private void initInstances() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
