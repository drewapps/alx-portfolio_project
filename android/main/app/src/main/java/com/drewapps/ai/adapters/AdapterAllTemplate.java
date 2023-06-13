package com.drewapps.ai.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ItemTemplateDetBinding;
import com.drewapps.ai.databinding.ItemTemplateListBinding;
import com.drewapps.ai.items.AdapTemplateData;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class AdapterAllTemplate extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    boolean isAllTemplate = false;
    List<AdapTemplateData> allTemplate;
    List<Template> detailTemplate;

    ClickListener<String> category;
    ClickListener<Template> starClick;
    ClickListener<Template> templateClick;

    public AdapterAllTemplate(Context context, boolean isAllTemplate, ClickListener<String> category,
                              ClickListener<Template> starClick, ClickListener<Template> templateClick) {
        this.context = context;
        this.isAllTemplate = isAllTemplate;
        this.category = category;
        this.starClick = starClick;
        this.templateClick = templateClick;
    }

    public void setAllTemplate(List<AdapTemplateData> allTemplate) {
        this.allTemplate = allTemplate;
        notifyDataSetChanged();
    }

    public void setDetailTemplate(List<Template> detailTemplate) {
        this.detailTemplate = detailTemplate;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (isAllTemplate) {
            ItemTemplateListBinding binding = ItemTemplateListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new AllViewHolder(binding);
        } else {
            ItemTemplateDetBinding binding = ItemTemplateDetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new DetailViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (isAllTemplate) {
            ((AllViewHolder) holder).binding.tvCatTitle.setText(allTemplate.get(position).category);
            ((AllViewHolder) holder).adapterTemplate.setTemplateData(allTemplate.get(position).templateList);
            ((AllViewHolder) holder).binding.tvFaveViewAll.setOnClickListener(v -> {
                category.onClick(allTemplate.get(position).category);
            });
        } else {

            ((DetailViewHolder) holder).binding.setTemplate(detailTemplate.get(position));
            ((DetailViewHolder) holder).itemView.setOnClickListener(v -> {
                templateClick.onClick(detailTemplate.get(position));
            });

            if (detailTemplate.get(position).isFavorite) {
                ((DetailViewHolder) holder).binding.ivStar.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.yellow_A700)));
            } else {
                ((DetailViewHolder) holder).binding.ivStar.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.grey_80)));
            }

            ((DetailViewHolder) holder).binding.ivStar.setOnClickListener(v -> {
                starClick.onClick(detailTemplate.get(position));
            });
        }

    }

    @Override
    public int getItemCount() {
        if (isAllTemplate) {
            if (allTemplate != null && allTemplate.size() > 0) {
                return allTemplate.size();
            }
            return 0;
        } else {
            if (detailTemplate != null && detailTemplate.size() > 0) {
                return detailTemplate.size();
            }
            return 0;
        }
    }

    public class AllViewHolder extends RecyclerView.ViewHolder {

        ItemTemplateListBinding binding;
        AdapterTemplate adapterTemplate;

        public AllViewHolder(ItemTemplateListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            adapterTemplate = new AdapterTemplate(context, template -> {
                templateClick.onClick(template);
            }, template -> {
                starClick.onClick(template);
            });
            binding.rvTemplate.setAdapter(adapterTemplate);
        }
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        ItemTemplateDetBinding binding;

        public DetailViewHolder(ItemTemplateDetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
