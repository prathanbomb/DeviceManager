package com.example.devicemanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;

public class CustomOtherListview extends RecyclerView {
    TextView tvBrand, tvCount;

    public CustomOtherListview(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomOtherListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    public CustomOtherListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_other_item, this);
    }

    private void initInstances() {
        // findViewById here
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvCount = (TextView) findViewById(R.id.tvcount);
    }

    public void setBrandText(String text) {
        tvBrand.setText(text);
    }

    public void setCountText(int count) {
        tvCount.setText(""+count);
    }

}
