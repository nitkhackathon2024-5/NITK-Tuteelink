package com.fit.vsshah.intelliconnect;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdapterChatDetail extends RecyclerView.Adapter<AdapterChatDetail.ViewHolder>{

    ArrayList<ModelChatDetail> chatArrayList;
    Context context;

    public AdapterChatDetail(ArrayList<ModelChatDetail> chatArrayList, Context context) {
        this.chatArrayList = chatArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rv_receiver, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelChatDetail modelChat = chatArrayList.get(position);
        String uid = modelChat.getId();
        String time = DateUtils.formatDateTime(context, Long.parseLong(modelChat.getTimestamp()), DateUtils.FORMAT_SHOW_TIME);

        if (uid.equals(FirebaseAuth.getInstance().getUid())){
            holder.smsgTv.setText(modelChat.getText());
            holder.sdateTv.setText(time);
            holder.senderRl.setVisibility(View.VISIBLE);
        }else {
            holder.rmsgTv.setText(modelChat.getText());
            holder.rdateTv.setText(time);
            holder.receiverRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rmsgTv, smsgTv, rdateTv, sdateTv;
        public RelativeLayout receiverRl, senderRl;

        public ViewHolder(View itemView) {
            super(itemView);

            rmsgTv = itemView.findViewById(R.id.rmsgTv);
            smsgTv = itemView.findViewById(R.id.smsgTv);
            rdateTv = itemView.findViewById(R.id.rdateTv);
            sdateTv = itemView.findViewById(R.id.sdateTv);
            receiverRl = itemView.findViewById(R.id.receiverRl);
            senderRl = itemView.findViewById(R.id.senderRl);
        }

    }
}
