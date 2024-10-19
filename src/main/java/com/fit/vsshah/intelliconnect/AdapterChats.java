package com.fit.vsshah.intelliconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AdapterChats extends RecyclerView.Adapter<AdapterChats.HolderChats>{

    private Context context;
    private ArrayList<ModelChats> list;

    public AdapterChats(Context context, ArrayList<ModelChats> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderChats onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_chat, parent, false);
        return new AdapterChats.HolderChats(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderChats holder, int position) {
        ModelChats modelChats = list.get(position);
        String id = modelChats.getId();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(id).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                String avatar = "" + snapshot.child("avatar").getValue();

                holder.nameTv.setText(name);
                switch (avatar) {
                    case "img1":
                        holder.avatarIv.setImageResource(R.drawable.img1);
                        break;
                    case "img2":
                        holder.avatarIv.setImageResource(R.drawable.img2);
                        break;
                    case "img3":
                        holder.avatarIv.setImageResource(R.drawable.img3);
                        break;
                    case "img4":
                        holder.avatarIv.setImageResource(R.drawable.img4);
                        break;
                    case "img5":
                        holder.avatarIv.setImageResource(R.drawable.img5);
                        break;
                    case "img6":
                        holder.avatarIv.setImageResource(R.drawable.img6);
                        break;
                    case "img7":
                        holder.avatarIv.setImageResource(R.drawable.img7);
                        break;
                    case "img8":
                        holder.avatarIv.setImageResource(R.drawable.img8);
                        break;
                    case "img9":
                        holder.avatarIv.setImageResource(R.drawable.img9);
                        break;
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatDetailsActivity.class);
                        intent.putExtra("id", id);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class HolderChats extends RecyclerView.ViewHolder{

        ImageView avatarIv;
        TextView nameTv;

        public HolderChats(@NonNull View itemView) {
            super(itemView);

            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
        }
    }

}
