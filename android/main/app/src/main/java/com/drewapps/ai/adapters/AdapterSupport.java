package com.drewapps.ai.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ItemSupportReqBinding;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class AdapterSupport extends RecyclerView.Adapter<AdapterSupport.MyViewHolder> {

    Context context;
    ClickListener<ItemSupportReq> clickListener;
    List<ItemSupportReq> supportReqList;

    public AdapterSupport(Context context, ClickListener<ItemSupportReq> clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setSupportReq(List<ItemSupportReq> supportReqList) {
        this.supportReqList = supportReqList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSupportReqBinding binding = ItemSupportReqBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvTitle.setText(supportReqList.get(position).subject);
        holder.binding.tvCategory.setText(supportReqList.get(position).category);
        holder.binding.tvTicketID.setText(supportReqList.get(position).ticketId);
        holder.binding.tvDate.setText(supportReqList.get(position).lastUpdated);
        holder.binding.tvStatus.setText(supportReqList.get(position).status);

        ItemSupportReq item = supportReqList.get(position);
        if (item.status.equals("Open")) {
            holder.binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.blue_600)));
        } else if (item.status.equals("Replied")) {
            holder.binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.yellow_700)));
        } else if (item.status.equals("Pending")) {
            holder.binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.pink_700)));
        } else if (item.status.equals("Resolved")) {
            holder.binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.green_600)));
        } else if (item.status.equals("Closed")) {
            //Closed
            holder.binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.grey_80)));
            holder.itemView.setAlpha(0.4f);

        }

        if (item.priority.equals("High")) {
            holder.binding.ivPriority.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.red_700)));
        } else if (item.priority.equals("Normal")) {
            holder.binding.ivPriority.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.cyan_500)));
        } else if (item.priority.equals("Low")) {
            holder.binding.ivPriority.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.green_600)));
        }

        holder.itemView.setOnClickListener(view->{
            clickListener.onClick(supportReqList.get(position));
        });


    }

    @Override
    public int getItemCount() {
        if (supportReqList != null && supportReqList.size() > 0) {
            return supportReqList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSupportReqBinding binding;

        public MyViewHolder(ItemSupportReqBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
