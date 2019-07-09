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
import com.example.devicemanager.manager.LoadData;
import com.example.devicemanager.room.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.Holder> {

    private ArrayList<ItemEntity> source;
    private static List<ItemEntity> list;
    private Context context;
    private Holder.ItemClickListener mClickListener;
    private LoadData loadData;
    private List<ItemEntity> filteredList = new ArrayList<>();

    public ItemListAdapter(Context context) {
        this.context = context;
        loadData = new LoadData(context);
        list = loadData.getItem();
        source = new ArrayList<>(list);
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
            filteredList.clear();
            boolean checkData = false;
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(loadData.getItem());
            } else {
                String[] filterPattern = charSequence.toString().toLowerCase().trim().split("\\s+");

                for (int i = 0; i < source.size(); i++) {
                    String brand = source.get(i).getBrand();
                    String type = source.get(i).getType();
                    String detail = source.get(i).getDetail();
                    String date = source.get(i).getPurchasedDate();
                    String place = source.get(i).getPlaceName();

                    String data = (brand + type + detail + date + place);

                    for (String s : filterPattern) {
                        checkData = data.toLowerCase().trim().contains(s);
                        if (!checkData) {
                            break;
                        }
                    }
                    if (checkData) {
                        filteredList.add(source.get(i));
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
            List<ItemEntity> posts;
            posts = (List<ItemEntity>) filterResults.values;
            if (posts != null) {
                list.addAll(posts);
            }
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
            tvSearchItem.setText(list.get(position).getBrand());
            tvSearchDetail.setText(list.get(position).getDetail());
            tvSearchName.setText(list.get(position).getPlaceName());
            //tvSearchSerial.setText(data.getSerialNo().get(position));
        }

        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }
}
