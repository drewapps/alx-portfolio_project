package com.drewapps.ai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.ItemMagicArtBinding;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class MagicArtAdapter extends RecyclerView.Adapter<MagicArtAdapter.MyViewHolder> {

    Context context;
    ClickListener<ItemMagicArt> clickListener;

    List<ItemMagicArt> magicArt;
    private int itemWidth = 0;
    int column;
    float width;

    public MagicArtAdapter(Context context, ClickListener<ItemMagicArt> clickListener, int column, float width) {
        this.context = context;
        this.clickListener = clickListener;
        this.column = column;
        this.width = width;
        itemWidth = MyApplication.getColumnWidth(column, width);
    }

    public void setMagicArt(List<ItemMagicArt> magicArtList){
        this.magicArt = magicArtList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMagicArtBinding binding = ItemMagicArtBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GlideBinding.bindImage(holder.binding.ivImage, magicArt.get(position).image);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.ivImage.getLayoutParams();
        params.width = itemWidth;
        params.height = itemWidth;

        holder.binding.ivImage.requestLayout();
        holder.binding.ivImage.setLayoutParams(params);
        holder.itemView.setOnClickListener(v->{
            clickListener.onClick(magicArt.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if(magicArt != null && magicArt.size() > 0){
            return magicArt.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ItemMagicArtBinding binding;
        public MyViewHolder(ItemMagicArtBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
