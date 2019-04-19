package com.android.israr.myfirebasechatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    String receiptName;
    List<ChatMessage> chatMessageList;
    Context context;
    int resourceLayout;

    public ChatListAdapter(List<ChatMessage> chatMessageList,String receiptName, Context context, int resourceLayout) {
        this.chatMessageList = chatMessageList;
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.receiptName = receiptName;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(resourceLayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder viewHolder, int i) {
        ChatMessage listItem = chatMessageList.get(i);
        if(listItem.getSenderName().equals(receiptName)){
            viewHolder.tvTextReceive.setText(listItem.getMessage());
            viewHolder.tvTextSend.setVisibility(View.GONE);
        }else{
            viewHolder.tvTextSend.setText(listItem.getMessage());
            viewHolder.tvTextReceive.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTextSend,tvTextReceive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTextSend = itemView.findViewById(R.id.tvTextSend);
            tvTextReceive = itemView.findViewById(R.id.tvTextReceive);
        }
    }
}
