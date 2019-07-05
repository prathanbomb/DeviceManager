package com.example.devicemanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicemanager.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class ScanBarcodeActivity extends AppCompatActivity {
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    final Handler handler = new Handler();
    Boolean doing = true;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initInstances();
    }

    private void initInstances() {
//        IntentIntegrator integrator = new IntentIntegrator(ScanBarcodeActivity.this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Scan");
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(false);
//        integrator.setBarcodeImageEnabled(false);
//        integrator.initiateScan();
        cameraView = (SurfaceView) findViewById(R.id.surfaceView);
        textView = (TextView) findViewById(R.id.text_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(this, "Detector dependencies are not yet available", Toast.LENGTH_SHORT).show();
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(callback);

            textRecognizer.setProcessor(processor);
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if(result.getContents() == null) {
//                Log.d("MainActivity", "Cancelled scan");
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//                finish();
//
//            } else {
//                Log.d("MainActivity", "Scanned");
//                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(ScanBarcodeActivity.this, CheckDeviceActivity.class);
//                intent.putExtra("serial",result.getContents());
//                startActivity(intent);
//                finish();
//            }
//        } else {
//            // This is important, otherwise the result will not be passed to the fragment
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ScanBarcodeActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCameraPermissionID);
                    return;
                }
                cameraSource.start(cameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            cameraSource.stop();
        }
    };
    Detector.Processor<TextBlock> processor = new Detector.Processor<TextBlock>() {
        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(Detector.Detections<TextBlock> detections) {

            final SparseArray<TextBlock> items = detections.getDetectedItems();
            if (items.size() != 0) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < items.size(); ++i) {
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }
                        String[] split = stringBuilder.toString().split("\\s+");
                        String text = "";
                        for (int i = 0; i < split.length; i++) {
                            text = text + split[i];
                        }
                        textView.setText(text);
                        if (text.contains("DGO") && text.trim().length() == 14) {
                            if (doing) {
                                doing = false;
                                showAlertDialog(text);
                            }
                        }
                    }
                });
            }
        }
    };
    private void showAlertDialog(final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Serial No : " + text).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ScanBarcodeActivity.this, CheckDeviceActivity.class);
                intent.putExtra("serial", textView.getText().toString());
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ScanBarcodeActivity.this, "Scan Again", Toast.LENGTH_SHORT).show();
                doing=true;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
