package com.drewapps.ai.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.databinding.ItemPurchaseBinding;
import com.drewapps.ai.items.PurchaseHistory;
import com.drewapps.ai.listener.ClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.MyViewHolder> {

    Context context;
    ClickListener<PurchaseHistory> clickListener;

    List<PurchaseHistory> purchaseHistoryList;

    public PurchaseAdapter(Context context, ClickListener<PurchaseHistory> clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setPurchaseHistoryList(List<PurchaseHistory> purchaseHistoryList) {
        this.purchaseHistoryList = purchaseHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPurchaseBinding binding = ItemPurchaseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String dateStr = "2023-03-17";

        SimpleDateFormat curFormater = new SimpleDateFormat("dd MMM, yy", Locale.ENGLISH);
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(purchaseHistoryList.get(position).date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);

        String newDateStr = postFormater.format(dateObj);
        holder.binding.tvDate.setText(newDateStr);
        holder.binding.setPurchase(purchaseHistoryList.get(position));
        holder.itemView.setOnClickListener(v -> {
            clickListener.onClick(purchaseHistoryList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (purchaseHistoryList != null && purchaseHistoryList.size() > 0) {
            return purchaseHistoryList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPurchaseBinding binding;

        public MyViewHolder(ItemPurchaseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
