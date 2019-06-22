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

    private List<String> name, list;
    private Context context;
    private static DataManager data = new DataManager();
    private Holder.ItemClickListener mClickListener;

    public ItemListAdapter(Context context) {
        this.context = context;
        list = data.getOwner();
        name = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ItemListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Contextor.getInstance().getContext())
                .inflate(R.layout.list_item_search, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        if (getItemCount() > 0 && list.get(position) != null) {
            holder.setText(position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(name);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (int i = 0; i < name.size(); i++) {
                    Log.d("innerrrr", name.get(i));
                    if (name.get(i).toLowerCase().contains(filterPattern)) {
                        Log.d("inner", name.get(i));
                        filteredList.add(name.get(i));
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

    public void setClickListener(Holder.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private TextView tvSearchItem, tvSearchDetail, tvSearchName, tvSearchSerial;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvSearchItem = itemView.findViewById(R.id.tvSearchItem);
            tvSearchDetail = itemView.findViewById(R.id.tvSearchDetail);
            tvSearchName = itemView.findViewById(R.id.tvSearchName);
            //tvSearchSerial = itemView.findViewById(R.id.tvSearchSerial);
        }

        public void setText(int position) {
            // TODO: Change 'Brand' to 'Type' and may be remove Serial No.
            tvSearchItem.setText(data.getBrand().get(position));
            tvSearchDetail.setText(data.getDetail().get(position));
            tvSearchName.setText(data.getOwner().get(position));
            //tvSearchSerial.setText(data.getSerialNo().get(position));
        }

        public interface ItemClickListener{
            void onItemClick(View view, int position);
        }
    }
}
