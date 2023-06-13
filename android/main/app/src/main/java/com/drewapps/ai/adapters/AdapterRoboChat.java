package com.drewapps.ai.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.ItemChatBotBinding;
import com.drewapps.ai.databinding.ItemChatUserBinding;
import com.drewapps.ai.databinding.ItemMsgAdminBinding;
import com.drewapps.ai.databinding.ItemMsgMeBinding;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.listener.ClickListener;
import com.drewapps.ai.utils.Constants;

import java.util.List;

public class AdapterRoboChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int CHAT_ME = 100;
    private final int CHAT_ADMIN = 200;

    Context context;
    ClickListener<ChatItem> clickListener;

    List<ChatItem> messageList;
    private int selectedPos = -1;
    View SELECTED_VIEW = null;

    public AdapterRoboChat(Context context, ClickListener<ChatItem> clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setMessageList(List<ChatItem> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CHAT_ME) {
            ItemChatUserBinding binding = ItemChatUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MeViewHolder(binding);
        } else {
            ItemChatBotBinding binding2 = ItemChatBotBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new AdminViewHolder(binding2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MeViewHolder) {
            MeViewHolder viewHolder = ((MeViewHolder) holder);
            viewHolder.binding.textContent.setText(Html.fromHtml(messageList.get(position).text));
            GlideBinding.bindImage(viewHolder.binding.roundedImageView, MyApplication.prefManager().getString(Constants.USER_IMAGE));
        } else {
            AdminViewHolder viewHolder = ((AdminViewHolder) holder);
            if (messageList.get(position).text.equals("LOADING")) {
                viewHolder.binding2.successAnimation.setVisibility(View.VISIBLE);
                viewHolder.binding2.textContent.setVisibility(View.GONE);
            } else {
                viewHolder.binding2.successAnimation.setVisibility(View.GONE);
                viewHolder.binding2.textContent.setVisibility(View.VISIBLE);
                viewHolder.binding2.textContent.setText(Html.fromHtml(messageList.get(position).text));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_VIEW = v;
                clickListener.onClick(messageList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (messageList != null && messageList.size() > 0) {
            return messageList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList != null) {
            if (!this.messageList.get(position).role.equals("assistant")) {
                return CHAT_ME;
            } else {
                return CHAT_ADMIN;
            }
        }
        return super.getItemViewType(position);
    }

    public class MeViewHolder extends RecyclerView.ViewHolder {

        ItemChatUserBinding binding;

        public MeViewHolder(ItemChatUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {

        ItemChatBotBinding binding2;

        public AdminViewHolder(ItemChatBotBinding binding2) {
            super(binding2.getRoot());
            this.binding2 = binding2;
        }
    }

    public View getSelectedItemView() {
       return SELECTED_VIEW;
    }
}
