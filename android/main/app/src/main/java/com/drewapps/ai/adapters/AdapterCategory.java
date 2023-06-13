package com.drewapps.ai.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ItemCategoryBinding;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {

    Context context;
    ClickListener<String> clickListener;

    List<String> categories;

    int selectedPosition = 0;

    public AdapterCategory(Context context, ClickListener<String> clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
        selectedPosition = 0;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvCategory.setText(categories.get(position));
        if(selectedPosition == position){
            holder.binding.rlCategory.setBackground(context.getDrawable(R.drawable.bg_doc_created));
            holder.binding.tvCategory.setTextColor(ColorStateList.valueOf(context.getColor(R.color.colorSecondary)));
        }else {
            holder.binding.rlCategory.setBackground(context.getDrawable(R.drawable.bg_word_created));
            holder.binding.tvCategory.setTextColor(ColorStateList.valueOf(context.getColor(R.color.blue_700)));
        }

        holder.itemView.setOnClickListener(v -> {
            setSelected(position);
            clickListener.onClick(categories.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (categories != null && categories.size() > 0) {
            return categories.size();
        }
        return 0;
    }

    private void setSelected(int position) {
        int oldPos = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPos);
        notifyItemChanged(position);
    }

    public void setlectedCategory(String category) {
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).equals(category)){
                setSelected(i);
                break;
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        public MyViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
