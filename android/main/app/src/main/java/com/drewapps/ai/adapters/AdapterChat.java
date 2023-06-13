package com.drewapps.ai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.ItemMsgAdminBinding;
import com.drewapps.ai.databinding.ItemMsgMeBinding;
import com.drewapps.ai.items.Message;
import com.drewapps.ai.listener.ClickListener;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int CHAT_ME = 100;
    private final int CHAT_ADMIN = 200;

    Context context;
    ClickListener<Message> clickListener;

    List<Message> messageList;

    public AdapterChat(Context context, ClickListener<Message> clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == CHAT_ME){
            ItemMsgMeBinding binding = ItemMsgMeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MeViewHolder(binding);
        }else {
            ItemMsgAdminBinding binding2 = ItemMsgAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new AdminViewHolder(binding2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MeViewHolder){
           MeViewHolder viewHolder = ((MeViewHolder) holder);
           viewHolder.binding.textContent.setText(messageList.get(position).message);
           viewHolder.binding.tvDate.setText(messageList.get(position).date);
           if(!messageList.get(position).image.equals("")){
               GlideBinding.bindImage(viewHolder.binding.ivContent, messageList.get(position).image);
               viewHolder.binding.ivContent.setVisibility(View.VISIBLE);
               int width = MyApplication.getScreenWidth()/2;
               viewHolder.binding.ivContent.getLayoutParams().width = width;
               viewHolder.binding.ivContent.getLayoutParams().height = width;

           }
        }else {
            AdminViewHolder viewHolder = ((AdminViewHolder) holder);
            viewHolder.binding2.textContent.setText(messageList.get(position).message);
            viewHolder.binding2.tvDate.setText(messageList.get(position).date);
            if(!messageList.get(position).image.equals("")){
                GlideBinding.bindImage(viewHolder.binding2.ivContent, messageList.get(position).image);
                viewHolder.binding2.ivContent.setVisibility(View.VISIBLE);
                int width = MyApplication.getScreenWidth()/2;
                viewHolder.binding2.ivContent.getLayoutParams().width = width;
                viewHolder.binding2.ivContent.getLayoutParams().height = width;

            }
        }

    }

    @Override
    public int getItemCount() {
        if(messageList!=null && messageList.size()>0){
            return messageList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList!=null) {
            if (!this.messageList.get(position).type.equals("assistant")) {
                return CHAT_ME;
            } else {
                return CHAT_ADMIN;
            }
        }
        return super.getItemViewType(position);
    }

    public class MeViewHolder extends RecyclerView.ViewHolder {

        ItemMsgMeBinding binding;
        public MeViewHolder(ItemMsgMeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {

        ItemMsgAdminBinding binding2;
        public AdminViewHolder(ItemMsgAdminBinding binding2) {
            super(binding2.getRoot());
            this.binding2 = binding2;
        }
    }
}
