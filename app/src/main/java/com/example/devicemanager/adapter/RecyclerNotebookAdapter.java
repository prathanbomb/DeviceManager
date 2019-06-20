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
import com.example.devicemanager.activity.MainActivity;
import com.example.devicemanager.fragment.SummaryFragment;
import com.example.devicemanager.manager.Contextor;

import java.util.ArrayList;

public class RecyclerNotebookAdapter extends RecyclerView.Adapter<RecyclerNotebookAdapter.Holder> {
    ArrayList<String> brand = new ArrayList<String>();
    public RecyclerNotebookAdapter(Context context){
        this.context = context;
    }


    Context context;


    public ArrayList<String> getBrand() {
        return brand;
    }

    public void setBrand(ArrayList<String> brand) {
        this.brand = brand;
    }

    public ArrayList<Integer> getCount() {
        return count;
    }

    public void setCount(ArrayList<Integer> count) {
        this.count = count;
    }

    ArrayList<Integer> count = new ArrayList<Integer>();

    @NonNull
    @Override
    public RecyclerNotebookAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notebook_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.setItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:intent summary item
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra("Type",brand.get(position));
//                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return brand.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tvBrand,tvCount;

        public Holder(View itemView) {
            super(itemView);
            tvBrand = (TextView) itemView.findViewById(R.id.tvBrand);
            tvCount = (TextView) itemView.findViewById(R.id.tvcount);
        }
        public void setItem(int position) {
            tvBrand.setText(brand.get(position));
            tvCount.setText(count.get(position)+"");
        }

    }
}
