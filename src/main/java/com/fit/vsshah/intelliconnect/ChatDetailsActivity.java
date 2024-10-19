package com.fit.vsshah.intelliconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fit.vsshah.intelliconnect.databinding.ActivityChatDetailsBinding;
import com.fit.vsshah.intelliconnect.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatDetailsActivity extends AppCompatActivity {

    private ActivityChatDetailsBinding binding;
    private FirebaseAuth firebaseAuth;
    private String id;
    private ArrayList<ModelChatDetail> chatArrayList;
    private AdapterChatDetail adapterChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        id = getIntent().getStringExtra("id");

        loadUserInfo();
        loadMessages();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatDetailsActivity.this, "Coming Up in future updates...", Toast.LENGTH_SHORT).show();
            }
        });

        binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.msgEt.getText().toString().trim().isEmpty()){
                    sendMsg();
                }
            }
        });
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(id).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                String avatar = ""+snapshot.child("avatar").getValue();
                binding.nameTv.setText(name);
                switch (avatar) {
                    case "img1":
                        binding.avatarIv.setImageResource(R.drawable.img1);
                        break;
                    case "img2":
                        binding.avatarIv.setImageResource(R.drawable.img2);
                        break;
                    case "img3":
                        binding.avatarIv.setImageResource(R.drawable.img3);
                        break;
                    case "img4":
                        binding.avatarIv.setImageResource(R.drawable.img4);
                        break;
                    case "img5":
                        binding.avatarIv.setImageResource(R.drawable.img5);
                        break;
                    case "img6":
                        binding.avatarIv.setImageResource(R.drawable.img6);
                        break;
                    case "img7":
                        binding.avatarIv.setImageResource(R.drawable.img7);
                        break;
                    case "img8":
                        binding.avatarIv.setImageResource(R.drawable.img8);
                        break;
                    case "img9":
                        binding.avatarIv.setImageResource(R.drawable.img9);
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatDetailsActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {
        binding.messagesRv.setLayoutManager(new LinearLayoutManager(this));
        chatArrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Chats").child(id).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelChatDetail modelChat = dataSnapshot1.getValue(ModelChatDetail.class);
                    chatArrayList.add(modelChat);
                }
                adapterChat = new AdapterChatDetail(chatArrayList, ChatDetailsActivity.this);
                binding.messagesRv.setAdapter(adapterChat);
                binding.messagesRv.smoothScrollToPosition(chatArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMsg() {
        String timestamp = ""+System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("text", ""+binding.msgEt.getText().toString().trim());
        hashMap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("timestamp", timestamp);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Chats").child(id).child("Messages").child(timestamp).setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("text", ""+binding.msgEt.getText().toString().trim());
                        hashMap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("timestamp", timestamp);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(id).child("Chats").child(firebaseAuth.getUid()).child("Messages").child(timestamp).setValue(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.msgEt.setText("");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChatDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}