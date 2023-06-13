package com.drewapps.ai.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.databinding.ItemBackgroundBinding;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class AdapterBackground extends RecyclerView.Adapter<AdapterBackground.MyViewHolder> {

    Context context;
    List<Drawable> drawableList;
    ClickListener<Integer> clickListener;

    public AdapterBackground(Context context, List<Drawable> drawableList, ClickListener<Integer> clickListener) {
        this.context = context;
        this.drawableList = drawableList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBackgroundBinding binding = ItemBackgroundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.ivBg.setImageDrawable(drawableList.get(position));
        holder.itemView.setOnClickListener(v->{
            clickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return drawableList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBackgroundBinding binding;

        public MyViewHolder(ItemBackgroundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
