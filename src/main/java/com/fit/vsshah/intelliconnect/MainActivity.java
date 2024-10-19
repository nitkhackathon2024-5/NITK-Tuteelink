package com.fit.vsshah.intelliconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit.vsshah.intelliconnect.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close);
        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavClick();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkUser();

        binding.chemistryLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubjectDetailsActivity.class);
                intent.putExtra("subject", "chem");
                startActivity(intent);
            }
        });

        binding.physicsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubjectDetailsActivity.class);
                intent.putExtra("subject", "phy");
                startActivity(intent);
            }
        });

        binding.mathematicsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubjectDetailsActivity.class);
                intent.putExtra("subject", "math");
                startActivity(intent);
            }
        });

        binding.physicsLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                intent.putExtra("subject", "phy");
                startActivity(intent);
            }
        });

        binding.chemistryLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                intent.putExtra("subject", "phy");
                startActivity(intent);
            }
        });

        binding.mathematicsLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                intent.putExtra("subject", "phy");
                startActivity(intent);
            }
        });
    }

    private void NavClick() {
        binding.vNV.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_account){
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }else if (item.getItemId() == R.id.nav_chats){
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }else if (item.getItemId() == R.id.nav_settings){
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            }else if (item.getItemId() == R.id.nav_logout){
                firebaseAuth.signOut();
                checkUser();
            }
            binding.myDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkUser() {
        if (firebaseAuth.getUid()==null){
            startActivity(new Intent(MainActivity.this, PreLoginActivity.class));
            finish();
        }else {
            loadUserData();
        }
    }

    private void loadUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                String firstName = name;
                if (name.contains(" ")) {
                    firstName = name.substring(0, name.lastIndexOf(' '));
                }
                binding.welcomeTv.setText("Welcome " + firstName + ",");
                binding.welcomeTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}