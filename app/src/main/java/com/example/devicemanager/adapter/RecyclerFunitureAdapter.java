package com.example.devicemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.SummaryListDetailActivity;

import java.util.ArrayList;

public class RecyclerFunitureAdapter extends RecyclerView.Adapter<RecyclerFunitureAdapter.Holder> {
    ArrayList<String> brand = new ArrayList<String>();

    ArrayList<Integer> available = new ArrayList<>();
    Context context;

    public RecyclerFunitureAdapter(Context context){
        this.context = context;
    }
    public void setBrand(ArrayList<String> brand) {
        this.brand = brand;
    }

    public void setCount(ArrayList<Integer> count) {
        this.count = count;
    }

    public void setAvailable(ArrayList<Integer> available) {
        this.available = available;
    }

    ArrayList<Integer> count = new ArrayList<Integer>();

    @NonNull
    @Override
    public RecyclerFunitureAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_other_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.setItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SummaryListDetailActivity.class);
                intent.putExtra("Type",brand.get(position));
                intent.putExtra("Count",count.get(position));
                intent.putExtra("Available", available.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return brand.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvBrand,tvCount, tvAvailable;

        public Holder(View itemView) {
            super(itemView);
            tvBrand = (TextView) itemView.findViewById(R.id.tvBrand);
            tvCount = (TextView) itemView.findViewById(R.id.tvcount);
            tvAvailable = itemView.findViewById(R.id.tvAvailable);
        }
        public void setItem(int position) {
            tvBrand.setText(brand.get(position));
            tvCount.setText(count.get(position)+"");
            tvAvailable.setText(available.get(position) + "");
        }

    }
}
