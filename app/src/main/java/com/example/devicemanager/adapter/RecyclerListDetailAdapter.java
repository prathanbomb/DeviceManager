package com.example.devicemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.activity.CheckDeviceActivity;
import com.example.devicemanager.activity.SummaryListDetailActivity;

import java.util.Random;

public class RecyclerListDetailAdapter extends RecyclerView.Adapter<RecyclerListDetailAdapter.Holder> {
    Context context;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;
    public RecyclerListDetailAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CheckDeviceActivity.class);
                intent.putExtra("serial","oX02ASCD023xxxxx");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView tvDetail, tvLocation, tvLastUpdate,tvSerial;

        public Holder(View itemView) {
            super(itemView);
            tvDetail        = (TextView) itemView.findViewById(R.id.tvDetail);
            tvLocation      = (TextView) itemView.findViewById(R.id.tvLocation);
            tvLastUpdate    = (TextView) itemView.findViewById(R.id.tvLastUpdate);
            tvSerial    = (TextView) itemView.findViewById(R.id.tvSerial);

        }

        public void setItem(int position) {
            Random rand = new Random();
            int n = rand.nextInt(543)+1;
            tvDetail.setText("Detail :ITEM"+position);
            tvLocation.setText("Location : USER"+n);
            tvLastUpdate.setText("Update Time : 21/06/2019");
            tvSerial.setText("Serial : oX02ASCD023"+position*n);
        }

    }
}
