package com.fit.vsshah.intelliconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit.vsshah.intelliconnect.databinding.ActivityLeaderboardBinding;
import com.fit.vsshah.intelliconnect.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class LeaderboardActivity extends AppCompatActivity {

    private ActivityLeaderboardBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Player> topPlayersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        String lbl = getIntent().getStringExtra("subject");
        ActionBar actionBar = getSupportActionBar();
        switch (lbl){
            case "phy": actionBar.setTitle("Physics Leaderboard");break;
            case "chem": actionBar.setTitle("Chemistry Leaderboard");break;
            case "math": actionBar.setTitle("Maths Leaderboard");break;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadLeaderboard(lbl);

        binding.connect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToConnect(topPlayersList.get(0).getId());
            }
        });

        binding.connect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToConnect(topPlayersList.get(1).getId());
            }
        });

        binding.connect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToConnect(topPlayersList.get(2).getId());
            }
        });

        binding.connect4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToConnect(topPlayersList.get(3).getId());
            }
        });

        binding.connect5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToConnect(topPlayersList.get(4).getId());
            }
        });
    }

    private void sendRequestToConnect(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", firebaseAuth.getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(id).child("Chats").child(firebaseAuth.getCurrentUser().getUid()).setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap<String, Object> hashMap1 = new HashMap<>();
                        hashMap1.put("id", id);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(firebaseAuth.getCurrentUser().getUid()).child("Chats").child(id).setValue(hashMap1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(LeaderboardActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void loadLeaderboard(String lbl) {
        String subject="Physics";
        switch (lbl){
            case "phy": subject="Physics";break;
            case "chem": subject="Chemistry";break;
            case "math": subject="Mathematics";break;
        }

        topPlayersList = new ArrayList<>();
        Query topPlayersQuery = FirebaseDatabase.getInstance().getReference("Leaderboard").child(subject)
                .orderByChild("score").limitToLast(5);

        topPlayersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                topPlayersList.clear();

                // Retrieve the players and their scores
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = ""+snapshot.child("name").getValue();
                    String scoreString = ""+snapshot.child("score").getValue();
                    String id = ""+snapshot.child("id").getValue();
                    String avatar = ""+snapshot.child("avatar").getValue();
                    int score = Integer.parseInt(scoreString);

                    Player player = new Player(name, score, id, avatar);
                    topPlayersList.add(player);
                }

                Collections.sort(topPlayersList, new Comparator<Player>() {
                    @Override
                    public int compare(Player p1, Player p2) {
                        return Integer.compare(p2.getScore(), p1.getScore());
                    }
                });

                binding.name1.setText(topPlayersList.get(0).getName());
                binding.name2.setText(topPlayersList.get(1).getName());
                binding.name3.setText(topPlayersList.get(2).getName());
                binding.name4.setText(topPlayersList.get(3).getName());
                binding.name5.setText(topPlayersList.get(4).getName());
                binding.score1.setText(String.valueOf(topPlayersList.get(0).getScore()));
                binding.score2.setText(String.valueOf(topPlayersList.get(1).getScore()));
                binding.score3.setText(String.valueOf(topPlayersList.get(2).getScore()));
                binding.score4.setText(String.valueOf(topPlayersList.get(3).getScore()));
                binding.score5.setText(String.valueOf(topPlayersList.get(4).getScore()));

                loadAvatar(topPlayersList.get(0).getAvatar(), 1);
                loadAvatar(topPlayersList.get(1).getAvatar(), 2);
                loadAvatar(topPlayersList.get(2).getAvatar(), 3);
                loadAvatar(topPlayersList.get(3).getAvatar(), 4);
                loadAvatar(topPlayersList.get(4).getAvatar(), 5);

                if (topPlayersList.get(0).getId().equals(firebaseAuth.getUid())) binding.connect1.setVisibility(View.GONE);
                if (topPlayersList.get(1).getId().equals(firebaseAuth.getUid())) binding.connect2.setVisibility(View.GONE);
                if (topPlayersList.get(2).getId().equals(firebaseAuth.getUid())) binding.connect3.setVisibility(View.GONE);
                if (topPlayersList.get(3).getId().equals(firebaseAuth.getUid())) binding.connect4.setVisibility(View.GONE);
                if (topPlayersList.get(4).getId().equals(firebaseAuth.getUid())) binding.connect5.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void loadAvatar(String avatar, int i) {
        switch (i){
            case 1: switch (avatar){
                case "img1": binding.avatar1.setImageResource(R.drawable.img1);break;
                case "img2": binding.avatar1.setImageResource(R.drawable.img2);break;
                case "img3": binding.avatar1.setImageResource(R.drawable.img3);break;
                case "img4": binding.avatar1.setImageResource(R.drawable.img4);break;
                case "img5": binding.avatar1.setImageResource(R.drawable.img5);break;
                case "img6": binding.avatar1.setImageResource(R.drawable.img6);break;
                case "img7": binding.avatar1.setImageResource(R.drawable.img7);break;
                case "img8": binding.avatar1.setImageResource(R.drawable.img8);break;
                case "img9": binding.avatar1.setImageResource(R.drawable.img9);break;
            }
            case 2: switch (avatar){
                case "img1": binding.avatar2.setImageResource(R.drawable.img1);break;
                case "img2": binding.avatar2.setImageResource(R.drawable.img2);break;
                case "img3": binding.avatar2.setImageResource(R.drawable.img3);break;
                case "img4": binding.avatar2.setImageResource(R.drawable.img4);break;
                case "img5": binding.avatar2.setImageResource(R.drawable.img5);break;
                case "img6": binding.avatar2.setImageResource(R.drawable.img6);break;
                case "img7": binding.avatar2.setImageResource(R.drawable.img7);break;
                case "img8": binding.avatar2.setImageResource(R.drawable.img8);break;
                case "img9": binding.avatar2.setImageResource(R.drawable.img9);break;
            }
            case 3: switch (avatar){
                case "img1": binding.avatar3.setImageResource(R.drawable.img1);break;
                case "img2": binding.avatar3.setImageResource(R.drawable.img2);break;
                case "img3": binding.avatar3.setImageResource(R.drawable.img3);break;
                case "img4": binding.avatar3.setImageResource(R.drawable.img4);break;
                case "img5": binding.avatar3.setImageResource(R.drawable.img5);break;
                case "img6": binding.avatar3.setImageResource(R.drawable.img6);break;
                case "img7": binding.avatar3.setImageResource(R.drawable.img7);break;
                case "img8": binding.avatar3.setImageResource(R.drawable.img8);break;
                case "img9": binding.avatar3.setImageResource(R.drawable.img9);break;
            }
            case 4: switch (avatar){
                case "img1": binding.avatar4.setImageResource(R.drawable.img1);break;
                case "img2": binding.avatar4.setImageResource(R.drawable.img2);break;
                case "img3": binding.avatar4.setImageResource(R.drawable.img3);break;
                case "img4": binding.avatar4.setImageResource(R.drawable.img4);break;
                case "img5": binding.avatar4.setImageResource(R.drawable.img5);break;
                case "img6": binding.avatar4.setImageResource(R.drawable.img6);break;
                case "img7": binding.avatar4.setImageResource(R.drawable.img7);break;
                case "img8": binding.avatar4.setImageResource(R.drawable.img8);break;
                case "img9": binding.avatar4.setImageResource(R.drawable.img9);break;
            }
            case 5: switch (avatar){
                case "img1": binding.avatar5.setImageResource(R.drawable.img1);break;
                case "img2": binding.avatar5.setImageResource(R.drawable.img2);break;
                case "img3": binding.avatar5.setImageResource(R.drawable.img3);break;
                case "img4": binding.avatar5.setImageResource(R.drawable.img4);break;
                case "img5": binding.avatar5.setImageResource(R.drawable.img5);break;
                case "img6": binding.avatar5.setImageResource(R.drawable.img6);break;
                case "img7": binding.avatar5.setImageResource(R.drawable.img7);break;
                case "img8": binding.avatar5.setImageResource(R.drawable.img8);break;
                case "img9": binding.avatar5.setImageResource(R.drawable.img9);break;
            }
        }
    }

    public static class Player {
        private String name, id, avatar;
        private int score;

        public Player(String name, int score, String id, String avatar) {
            this.name = name;
            this.score = score;
            this.avatar = avatar;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Player() {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}