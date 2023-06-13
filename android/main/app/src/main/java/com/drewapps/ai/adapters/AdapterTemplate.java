package com.drewapps.ai.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ItemTemplateBinding;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class AdapterTemplate extends RecyclerView.Adapter<AdapterTemplate.MyViewHolder> {

    Context context;
    ClickListener<Template> clickListener;
    ClickListener<Template> clickListenerStar;

    List<Template> templatesList;

    public AdapterTemplate(Context context, ClickListener<Template> clickListener, ClickListener<Template> clickListenerStar) {
        this.context = context;
        this.clickListener = clickListener;
        this.clickListenerStar = clickListenerStar;
    }

    public void setTemplateData(List<Template> templatesList) {
        this.templatesList = templatesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTemplateBinding binding = ItemTemplateBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.setTemplate(templatesList.get(position));
        holder.itemView.setOnClickListener(view -> {
            clickListener.onClick(templatesList.get(position));
        });

        if (templatesList.get(position).isFavorite) {
            holder.binding.ivStar.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.yellow_A700)));
        } else {
            holder.binding.ivStar.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.grey_80)));
        }

        holder.binding.ivStar.setOnClickListener(v -> {
            clickListenerStar.onClick(templatesList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (templatesList != null && templatesList.size() > 0) {
            return templatesList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTemplateBinding binding;

        public MyViewHolder(ItemTemplateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
