package com.android.israr.myfirebasechatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<User> friendsList;
    private Context context;
    private int layoutResource;

    FriendsListAdapter(List<User> friendsList, Context context, int layoutResource) {
        this.friendsList = friendsList;
        this.context = context;
        this.layoutResource = layoutResource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(layoutResource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User listItem = friendsList.get(i);
        viewHolder.tvFriendName.setText(listItem.getFullName());
        viewHolder.clickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("What do you want to do?")
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context.getApplicationContext(),CallActivity.class);
                                String username = listItem.getUsername();
                                intent.putExtra("targetUsername",username);
                                context.startActivity(intent);
                            }
                        }).setNegativeButton("Chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context.getApplicationContext(),ChatActivity.class);
                        String username = listItem.getUsername();
                        String fullname = listItem.getFullName();
                        intent.putExtra("targetUsername",username);
                        intent.putExtra("targetUser",fullname);
                        context.startActivity(intent);
                    }
                }).create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        RelativeLayout clickableLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvConUserName);
            clickableLayout = itemView.findViewById(R.id.clickableLayout);
        }
    }
}
