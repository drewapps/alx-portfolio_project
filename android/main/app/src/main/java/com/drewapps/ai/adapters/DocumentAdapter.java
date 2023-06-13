package com.drewapps.ai.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ItemDocumentBinding;
import com.drewapps.ai.items.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {

    Context context;
    OnClick clickListener;

    List<Document> documentList;

    public DocumentAdapter(Context context, OnClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDocumentBinding binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String dateStr = "2023-03-17";

        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(documentList.get(position).lastChangeDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);

        String newDateStr = postFormater.format(dateObj);
        holder.binding.tvDate.setText(newDateStr);
        holder.binding.setDocument(documentList.get(position));
        holder.itemView.setOnClickListener(v -> {
            clickListener.onClick(documentList.get(position));
        });
        holder.binding.lvMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v, Gravity.NO_GRAVITY, 0,
                    R.style.popupMenu);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.menu_download) {
                        clickListener.onDownload(documentList.get(position));
                    } else if (item.getItemId() == R.id.menu_delete) {
                        clickListener.onDelete(documentList.get(position));
                    }

                    return true;
                }
            });
            popup.inflate(R.menu.doc_menu);
            popup.setForceShowIcon(true);
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        if (documentList != null && documentList.size() > 0) {
            return documentList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDocumentBinding binding;

        public MyViewHolder(ItemDocumentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClick {
        void onClick(Document document);

        void onDownload(Document document);

        void onDelete(Document document);
    }
}
