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

import com.fit.vsshah.intelliconnect.databinding.ActivityMainBinding;
import com.fit.vsshah.intelliconnect.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private FirebaseAuth firebaseAuth;
    private boolean quiz_ended=false;

    private String name, avatar;

    private String ans1="c", ans2="c", ans3="a", ans4="b", selected;
    private int ques=0, score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        showQues1();

        binding.optionATv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.submitBtn.setVisibility(View.VISIBLE);
                selected="a";
            }
        });

        binding.optionBTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.submitBtn.setVisibility(View.VISIBLE);
                selected="b";
            }
        });

        binding.optionCTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.submitBtn.setVisibility(View.VISIBLE);
                selected="c";
            }
        });

        binding.optionDTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.submitBtn.setVisibility(View.VISIBLE);
                selected="d";
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ques){
                    case 1: showQues2(); break;
                    case 2: showQues3(); break;
                    case 3: showQues4(); break;
                    case 4:
                        getData();
                        Toast.makeText(QuizActivity.this,"You've completed the quiz", Toast.LENGTH_SHORT).show();
                        QuizActivity.super.onBackPressed(); break;
                }
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ques){
                    case 1: if (Objects.equals(selected, ans1)) {
                        score++;
                        showCorrect(selected);
                    }else{
                        showWrong(selected);
                    }break;
                    case 2: if (Objects.equals(selected, ans2)) {
                        score++;
                        showCorrect(selected);
                    }else{
                        showWrong(selected);
                    }break;
                    case 3: if (Objects.equals(selected, ans3)) {
                        score++;
                        showCorrect(selected);
                    }else{
                        showWrong(selected);
                    }break;
                    case 4: if (Objects.equals(selected, ans4)) {
                        score++;
                        showCorrect(selected);
                    }else{
                        showWrong(selected);
                    }break;
                }
                binding.nextBtn.setVisibility(View.VISIBLE);
                binding.submitBtn.setVisibility(View.GONE);
            }
        });
    }

    private void showWrong(String selected) {
        switch (selected){
            case "a": binding.optionATv.setBackgroundResource(R.drawable.edittext_bg_wrong); break;
            case "b": binding.optionBTv.setBackgroundResource(R.drawable.edittext_bg_wrong); break;
            case "c": binding.optionCTv.setBackgroundResource(R.drawable.edittext_bg_wrong); break;
            case "d": binding.optionDTv.setBackgroundResource(R.drawable.edittext_bg_wrong); break;
        }
    }

    private void showCorrect(String selected) {
        switch (selected){
            case "a": binding.optionATv.setBackgroundResource(R.drawable.edittext_bg_correct); break;
            case "b": binding.optionBTv.setBackgroundResource(R.drawable.edittext_bg_correct); break;
            case "c": binding.optionCTv.setBackgroundResource(R.drawable.edittext_bg_correct); break;
            case "d": binding.optionDTv.setBackgroundResource(R.drawable.edittext_bg_correct); break;
        }
    }

    private void showQues1() {
        ques++;
        binding.optionATv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionBTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionCTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionDTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.quesLblTv.setText("Question 1");
        binding.questionTv.setText("A light unstretchable string passing over a smooth light pulley connects two blocks of masses m1 and m2. If the acceleration of the system is g/8, then the ratio of the masses m2/m1 is:");
        binding.optionATv.setText("5 : 3");
        binding.optionBTv.setText("8 : 1");
        binding.optionCTv.setText("9 : 7");
        binding.optionDTv.setText("4 : 3");
        binding.submitBtn.setVisibility(View.GONE);
        binding.nextBtn.setVisibility(View.GONE);
    }

    private void showQues2() {
        ques++;
        binding.optionATv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionBTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionCTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionDTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.quesLblTv.setText("Question 2");
        binding.questionTv.setText("A player caught a cricket ball of mass 150 g moving at a speed of 20 m/s. If the catching process is completed in 0.1 s, the magnitude of force exerted by the ball on the hand of the player is:");
        binding.optionATv.setText("150 N");
        binding.optionBTv.setText("3 N");
        binding.optionCTv.setText("30 N");
        binding.optionDTv.setText("300 N");
        binding.submitBtn.setVisibility(View.GONE);
        binding.nextBtn.setVisibility(View.GONE);
    }

    private void showQues3() {
        ques++;
        binding.optionATv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionBTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionCTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionDTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.quesLblTv.setText("Question 3");
        binding.questionTv.setText("A body of weight 200 N is suspended from a tree branch through a chain of mass 10 kg. The branch pulls the chain by a force equal to:");
        binding.optionATv.setText("300 N");
        binding.optionBTv.setText("100 N");
        binding.optionCTv.setText("150 N");
        binding.optionDTv.setText("200 N");
        binding.submitBtn.setVisibility(View.GONE);
        binding.nextBtn.setVisibility(View.GONE);
    }

    private void showQues4() {
        ques++;
        binding.optionATv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionBTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionCTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.optionDTv.setBackgroundResource(R.drawable.edittext_bg);
        binding.quesLblTv.setText("Question 4");
        binding.questionTv.setText("A person is standing in an elevator. In which situation, he experiences weight loss when the elevator moves:");
        binding.optionATv.setText("up with constant accn.");
        binding.optionBTv.setText("down with constant accn.");
        binding.optionCTv.setText("up with uniform velocity");
        binding.optionDTv.setText("down with uniform velocity");
        binding.submitBtn.setVisibility(View.GONE);
        binding.nextBtn.setVisibility(View.GONE);
    }

    private void getData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = "" + snapshot.child("name").getValue();
                avatar = ""+snapshot.child("avatar").getValue();
                saveData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveData() {
        final int min = 50;
        final int max = 100;
        final int random = new Random().nextInt((max - min) + 1) + min;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("avatar", avatar);
        hashMap.put("score", ""+random);
        hashMap.put("id", firebaseAuth.getCurrentUser().getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Leaderboard");
        reference.child("Physics").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(QuizActivity.this, ""+random, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (quiz_ended){
            super.onBackPressed();
        }else {
            Toast.makeText(this, "Submit the Test to exit...", Toast.LENGTH_SHORT).show();
        }
    }
}