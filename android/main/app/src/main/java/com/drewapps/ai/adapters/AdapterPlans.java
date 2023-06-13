package com.drewapps.ai.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ItemPlansBinding;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.listener.ClickListener;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.utils.ViewAnimation;

import java.util.List;

public class AdapterPlans extends RecyclerView.Adapter<AdapterPlans.MyViewHolder> {

    Context context;
    ClickListener<ItemSubsPlan> clickListener;
    List<ItemSubsPlan> plans;
    int selectedPosition = 0;

    public AdapterPlans(Context context, ClickListener<ItemSubsPlan> clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setPlans(List<ItemSubsPlan> planList) {
        this.plans = planList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlansBinding binding = ItemPlansBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvTitle.setText(plans.get(position).planName);
        holder.binding.tvPrice.setText("" + MyApplication.prefManager().getString(Constants.CURRENCY) + " " + plans.get(position).planPrice);
        holder.binding.tvWords.setText("" + plans.get(position).includeWords + " Words Lifetime");
        holder.binding.tvImages.setText("" + plans.get(position).includeImages + " Images Lifetime");

        if (plans.get(position).mostPopular == 1){
            holder.binding.ivBestOffer.setVisibility(View.VISIBLE);
        }else {
            holder.binding.ivBestOffer.setVisibility(View.GONE);
        }

        if(selectedPosition == position){
            holder.binding.clMain.setBackground(context.getDrawable(R.drawable.bg_doc_created));
//            holder.binding.tvTitle.setTextColor(ColorStateList.valueOf(context.getColor(R.color.colorSecondary)));
            Shader shader = Util.generateGradient(new String[]{"#00E0AF", "#008FC9"},
                    holder.binding.tvTitle);
            holder.binding.tvTitle.getPaint().setShader(shader);

        }else {
            holder.binding.clMain.setBackground(context.getDrawable(R.drawable.bg_template));
            holder.binding.tvTitle.getPaint().setShader(null);
            holder.binding.tvTitle.setTextColor(ColorStateList.valueOf(context.getColor(R.color.white)));
        }

        holder.itemView.setOnClickListener(v->{
            setSelected(position);
            ViewAnimation.fadeOutIn(v);
            clickListener.onClick(plans.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (plans != null && plans.size() > 0) {
            return plans.size();
        }
        return 0;
    }

    private void setSelected(int position) {
        int oldPos = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPos);
        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPlansBinding binding;

        public MyViewHolder(@NonNull ItemPlansBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
