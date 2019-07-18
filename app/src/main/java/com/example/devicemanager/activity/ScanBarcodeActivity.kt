package com.example.devicemanager.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.devicemanager.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException

class ScanBarcodeActivity : AppCompatActivity() {
    internal val RequestCameraPermissionID = 1001
    internal lateinit var cameraView: SurfaceView
    internal lateinit var textView: TextView
    internal lateinit var cameraSource: CameraSource
    internal var doing: Boolean? = true
    internal var callback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

            try {
                if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this@ScanBarcodeActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            RequestCameraPermissionID)
                    return
                }
                cameraSource.start(cameraView.holder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

        }

        override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
            cameraSource.stop()
        }
    }
    internal var processor: Detector.Processor<TextBlock> = object : Detector.Processor<TextBlock> {
        override fun release() {}

        override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
            val items = detections.detectedItems

            if (items.size() != 0 && doing!!) {
                textView.post {
                    val stringBuilder = StringBuilder()

                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        stringBuilder.append(item.value)
                        stringBuilder.append("\n")
                    }

                    val split = stringBuilder.toString().split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var text = ""

                    for (s in split) {
                        text = text + s
                    }
                    textView.text = text

                    if (checkId(text)) {
                        doing = false
                        showAlertDialog(text)
                    }
                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == RequestCameraPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                try {
                    cameraSource.start(cameraView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)

        initInstances()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun initInstances() {
        cameraView = findViewById(R.id.surfaceView)
        textView = findViewById(R.id.text_view)

        val textRecognizer = TextRecognizer.Builder(applicationContext).build()
        if (!textRecognizer.isOperational) {
            Toast.makeText(this, "Detector dependencies are not yet available", Toast.LENGTH_SHORT).show()
        } else {
            cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(5.0f)
                    .setAutoFocusEnabled(true)
                    .build()
            cameraView.holder.addCallback(callback)

            textRecognizer.setProcessor(processor)
        }

    }

    private fun showAlertDialog(text: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Serial No : $text").setPositiveButton("Yes") { dialog, which ->
            val intent = Intent(this@ScanBarcodeActivity, DeviceDetailActivity::class.java)
            intent.putExtra("serial", text)
            startActivity(intent)
            finish()
        }.setNegativeButton("No") { dialog, which ->
            Toast.makeText(this@ScanBarcodeActivity, "Scan Again", Toast.LENGTH_SHORT).show()
            doing = true
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun checkId(text: String): Boolean {
        return if (!text.contains("DGO")) {
            false
        } else if (text.trim { it <= ' ' }.length != 14) {
            false
        } else {
            text.substring(11).matches("\\d+".toRegex())
        }
    }

}
