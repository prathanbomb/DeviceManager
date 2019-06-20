package com.example.devicemanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.devicemanager.R;
import com.example.devicemanager.fragment.AddDeviceFragment;
import com.example.devicemanager.manager.Contextor;

public class AddDeviceActivity extends AppCompatActivity {

    private Button btnCancel, btnConfirm;
    private ImageView ivDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        initInstances();

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();

            bundle.putString("Path", getIntent().getStringExtra("Path"));
            bundle.putString("Serial", getIntent().getStringExtra("Serial"));
            AddDeviceFragment fragment = AddDeviceFragment.newInstances();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, fragment)
                    .commit();
        }
    }

    private void initInstances() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(onClickBtnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(onClickBtnConfirm);*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(int msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(Contextor.getInstance().getContext());
        String dialogMsg = getResources().getString(msg);

        builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Contextor.getInstance().getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Contextor.getInstance().getContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    View.OnClickListener onClickBtnCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener onClickBtnConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //showAlertDialog(R.string.dialog_msg_confirm);
            finish();
        }
    };
}
