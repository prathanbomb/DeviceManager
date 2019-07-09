package com.example.devicemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.SummaryListDetailActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerDeviceAdapter extends RecyclerView.Adapter<RecyclerDeviceAdapter.Holder> {
    private String[] brand;
    private Context context;
    private int[] available;
    private int[] count;
    private int[] total;

    public RecyclerDeviceAdapter(Context context) {
        this.context = context;
    }

    public void setBrand(String[] brand) {
        this.brand = brand;
    }

    public void setTotal(int[] total) {
        this.total = total;
    }

    public void setCount(int[] count) {
        this.count = count;
    }

    public void setAvailable(int[] available) {
        this.available = available;
    }


    @NonNull
    @Override
    public RecyclerDeviceAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.setItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SummaryListDetailActivity.class);
                intent.putExtra("Type", brand[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brand.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvBrand,tvTotal,tvAvailable,tvActive;

        Holder(View itemView) {
            super(itemView);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvAvailable = itemView.findViewById(R.id.tvAvailable);
            tvActive = itemView.findViewById(R.id.tvActive);
        }
        public void setItem(int position) {
            tvBrand.setText("" + brand[position]);
            tvTotal.setText("" + total[position]);
            tvAvailable.setText("" + available[position]);
            tvActive.setText("" + count[position]);
        }
    }
}
