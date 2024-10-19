package com.fit.vsshah.intelliconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterTopics extends RecyclerView.Adapter<AdapterTopics.HolderTopics>{

    private Context context;
    private List<String> list;

    public AdapterTopics(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderTopics onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_topics, parent, false);
        return new HolderTopics(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTopics holder, int position) {

        holder.topicTv.setText(list.get(position));

        holder.topicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.topicTv.getText().toString().equals("Mechanics-I")) {
                    context.startActivity(new Intent(context, QuizActivity.class));
                }
                else {
                    Toast.makeText(context, "No Questions Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderTopics extends RecyclerView.ViewHolder{

        TextView topicTv;

        public HolderTopics(@NonNull View itemView) {
            super(itemView);

            topicTv = itemView.findViewById(R.id.topicsTv);
        }
    }

}
