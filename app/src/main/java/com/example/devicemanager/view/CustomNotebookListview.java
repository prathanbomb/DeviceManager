package com.example.devicemanager.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;

public class CustomNotebookListview extends RecyclerView {
    TextView tvBrand, tvCount;

    public CustomNotebookListview(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomNotebookListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    public CustomNotebookListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_notebook_item, this);
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
