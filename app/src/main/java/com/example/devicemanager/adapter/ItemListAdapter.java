package com.example.devicemanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicemanager.R;
import com.example.devicemanager.manager.Contextor;
import com.example.devicemanager.manager.DataManager;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.Holder> {

    private List<String> brand, list;
    private Context context;

    public ItemListAdapter(Context context, List<String> brand) {
        this.context = context;
        this.list = brand;
        this.brand = new ArrayList<>(brand);
    }

    @NonNull
    @Override
    public ItemListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Contextor.getInstance().getContext())
                .inflate(R.layout.list_item_search, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (list.size()> 0 && list.get(position) != null) {
            holder.setText(list.get(position));
        }
        else {
            holder.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return brand.size();
    }

    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(brand);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (int i = 0; i < brand.size(); i++) {
                    if (brand.get(i).toLowerCase().contains(filterPattern)) {
                        filteredList.add(brand.get(i));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List<String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvSearchItem;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvSearchItem = itemView.findViewById(R.id.tvSearchItem);
        }

        public void setText(String text) {
            tvSearchItem.setText(text);
        }
    }
}
