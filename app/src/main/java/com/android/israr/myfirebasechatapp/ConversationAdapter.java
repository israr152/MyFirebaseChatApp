package com.android.israr.myfirebasechatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private Context context;
    private int listItemLayout;
    private List<Conversation> itemList;

    ConversationAdapter(Context context, int listItemLayout, List<Conversation> itemList) {
        this.context = context;
        this.listItemLayout = listItemLayout;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(listItemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Conversation listItem = itemList.get(i);
        TextView item = viewHolder.tvConUserName;
        final String fullname = listItem.getConversationName();
        final String username = listItem.getConversationUsername();
        item.setText(fullname);

        viewHolder.clickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),ChatActivity.class);
                intent.putExtra("targetUser",fullname);
                intent.putExtra("targetUsername",username);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvConUserName;
        RelativeLayout clickableLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tvConUserName = itemView.findViewById(R.id.tvConUserName);
            clickableLayout = itemView.findViewById(R.id.clickableLayout);
        }
    }
}
