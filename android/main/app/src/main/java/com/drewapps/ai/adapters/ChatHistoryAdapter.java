package com.drewapps.ai.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.databinding.ItemChatHistoryBinding;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder> {

    ClickListener<ItemChatHistory> clickListener;
    List<ItemChatHistory> chatHistoryList;

    public ChatHistoryAdapter(ClickListener<ItemChatHistory> clickListener) {
        this.clickListener = clickListener;
    }

    public void setChatHistoryList(List<ItemChatHistory> chatHistoryList) {
        this.chatHistoryList = chatHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatHistoryBinding binding = ItemChatHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvTitle.setText(chatHistoryList.get(position).chatItemList.get(0).text);
        holder.itemView.setOnClickListener(view -> {
            clickListener.onClick(chatHistoryList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (chatHistoryList != null && chatHistoryList.size() > 0) {
            return chatHistoryList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemChatHistoryBinding binding;

        public MyViewHolder(ItemChatHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
