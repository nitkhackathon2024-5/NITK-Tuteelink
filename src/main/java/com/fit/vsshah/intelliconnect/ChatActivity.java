package com.fit.vsshah.intelliconnect;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fit.vsshah.intelliconnect.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private ArrayList<ModelChats> chatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chatsList = new ArrayList<>();
        loadChats();
    }

    private void loadChats() {
        chatsList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatsList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelChats modelCart = ds.getValue(ModelChats.class);
                            chatsList.add(modelCart);
                        }

                        LinearLayoutManager llm = new LinearLayoutManager(ChatActivity.this);
                        binding.chatsRv.setLayoutManager(llm);
                        binding.chatsRv.setHasFixedSize(true);
                        AdapterChats adapterChats = new AdapterChats(ChatActivity.this, chatsList);
                        binding.chatsRv.setAdapter(adapterChats);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}