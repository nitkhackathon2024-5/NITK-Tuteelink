package com.fit.vsshah.intelliconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit.vsshah.intelliconnect.databinding.ActivityMainBinding;
import com.fit.vsshah.intelliconnect.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private String avatar, gender, exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        loadData();

        binding.avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseAvatarBS();
            }
        });

        binding.genderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseGender();
            }
        });

        binding.examTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseExam();
            }
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", ""+binding.nameTv.getText().toString().trim());
        hashMap.put("bio", ""+binding.bioTv.getText().toString().trim());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        Toast.makeText(ProfileActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showChooseExam() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.bs_choose_exam, null);
        bottomSheetDialog.setContentView(view);

        ImageButton backBtn = view.findViewById(R.id.backBtn);
        TextView jeeMainsTv = view.findViewById(R.id.jeeMainsTv);
        TextView jeeAdvTv = view.findViewById(R.id.jeeAdvTv);
        TextView neetTv = view.findViewById(R.id.neetTv);
        TextView upscTv = view.findViewById(R.id.upscTv);
        TextView ndaTv = view.findViewById(R.id.ndaTv);
        TextView kcetTv = view.findViewById(R.id.kcetTv);
        TextView comedkTv = view.findViewById(R.id.comedkTv);
        TextView gujcetTv = view.findViewById(R.id.gujcetTv);

        if (!(exam.isEmpty() || exam.equals("null"))){
            switch (exam){
                case "JEE Mains": jeeMainsTv.setBackgroundResource(R.drawable.button_bg); jeeMainsTv.setTextColor(getResources().getColor(R.color.white));break;
                case "JEE Advanced": jeeAdvTv.setBackgroundResource(R.drawable.button_bg); jeeAdvTv.setTextColor(getResources().getColor(R.color.white));break;
                case "NEET": neetTv.setBackgroundResource(R.drawable.button_bg); neetTv.setTextColor(getResources().getColor(R.color.white));break;
                case "UPSC": upscTv.setBackgroundResource(R.drawable.button_bg); upscTv.setTextColor(getResources().getColor(R.color.white));break;
                case "NDA": ndaTv.setBackgroundResource(R.drawable.button_bg); ndaTv.setTextColor(getResources().getColor(R.color.white));break;
                case "KCET": kcetTv.setBackgroundResource(R.drawable.button_bg); kcetTv.setTextColor(getResources().getColor(R.color.white));break;
                case "COMEDK": comedkTv.setBackgroundResource(R.drawable.button_bg); comedkTv.setTextColor(getResources().getColor(R.color.white));break;
                case "GUJCET": gujcetTv.setBackgroundResource(R.drawable.button_bg); gujcetTv.setTextColor(getResources().getColor(R.color.white));break;
            }
        }

        bottomSheetDialog.show();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        
        jeeMainsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("JEE Mains", bottomSheetDialog);
            }
        });
        
        jeeAdvTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("JEE Advanced", bottomSheetDialog);
            }
        });
        
        neetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("NEET", bottomSheetDialog);
            }
        });
        
        upscTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("UPSC", bottomSheetDialog);
            }
        });
        
        ndaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("NDA", bottomSheetDialog);
            }
        });
        
        kcetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("KCET", bottomSheetDialog);
            }
        });
        
        comedkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("COMEDK", bottomSheetDialog);
            }
        });
        
        gujcetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExam("GUJCET", bottomSheetDialog);
            }
        });
    }

    private void saveExam(String jeeMains, BottomSheetDialog bottomSheetDialog) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("exam", ""+jeeMains);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bottomSheetDialog.dismiss();
                        loadData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showChooseGender() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.bs_choose_gender, null);
        bottomSheetDialog.setContentView(view);

        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageView maleIv = view.findViewById(R.id.maleIv);
        ImageView femaleIv = view.findViewById(R.id.femaleIv);
        ImageView maleTick = view.findViewById(R.id.tickMale);
        ImageView femaleTick = view.findViewById(R.id.tickFemale);

        if (!(gender.isEmpty() || gender.equals("null"))){
            switch(gender){
                case "Male": maleIv.setBackgroundResource(R.drawable.edittext_bg); maleTick.setVisibility(View.VISIBLE); break;
                case "Female": femaleIv.setBackgroundResource(R.drawable.edittext_bg); femaleTick.setVisibility(View.VISIBLE); break;
            }
        }

        bottomSheetDialog.show();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        maleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGender("Male", bottomSheetDialog);
            }
        });

        femaleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGender("Female", bottomSheetDialog);
            }
        });
    }

    private void setGender(String male, BottomSheetDialog bottomSheetDialog) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gender", ""+male);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bottomSheetDialog.dismiss();
                        loadData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showChooseAvatarBS() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.bs_choose_avatar, null);
        bottomSheetDialog.setContentView(view);

        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageView img1 = view.findViewById(R.id.img1);
        ImageView img2 = view.findViewById(R.id.img2);
        ImageView img3 = view.findViewById(R.id.img3);
        ImageView img4 = view.findViewById(R.id.img4);
        ImageView img5 = view.findViewById(R.id.img5);
        ImageView img6 = view.findViewById(R.id.img6);
        ImageView img7 = view.findViewById(R.id.img7);
        ImageView img8 = view.findViewById(R.id.img8);
        ImageView img9 = view.findViewById(R.id.img9);
        ImageView tick1 = view.findViewById(R.id.tick1);
        ImageView tick2 = view.findViewById(R.id.tick2);
        ImageView tick3 = view.findViewById(R.id.tick3);
        ImageView tick4 = view.findViewById(R.id.tick4);
        ImageView tick5 = view.findViewById(R.id.tick5);
        ImageView tick6 = view.findViewById(R.id.tick6);
        ImageView tick7 = view.findViewById(R.id.tick7);
        ImageView tick8 = view.findViewById(R.id.tick8);
        ImageView tick9 = view.findViewById(R.id.tick9);

        if (!(avatar.isEmpty() || avatar.equals("null"))){
            switch(avatar){
                case "img1": img1.setBackgroundResource(R.drawable.edittext_bg); tick1.setVisibility(View.VISIBLE); break;
                case "img2": img2.setBackgroundResource(R.drawable.edittext_bg); tick2.setVisibility(View.VISIBLE); break;
                case "img3": img3.setBackgroundResource(R.drawable.edittext_bg); tick3.setVisibility(View.VISIBLE); break;
                case "img4": img4.setBackgroundResource(R.drawable.edittext_bg); tick4.setVisibility(View.VISIBLE); break;
                case "img5": img5.setBackgroundResource(R.drawable.edittext_bg); tick5.setVisibility(View.VISIBLE); break;
                case "img6": img6.setBackgroundResource(R.drawable.edittext_bg); tick6.setVisibility(View.VISIBLE); break;
                case "img7": img7.setBackgroundResource(R.drawable.edittext_bg); tick7.setVisibility(View.VISIBLE); break;
                case "img8": img8.setBackgroundResource(R.drawable.edittext_bg); tick8.setVisibility(View.VISIBLE); break;
                case "img9": img9.setBackgroundResource(R.drawable.edittext_bg); tick9.setVisibility(View.VISIBLE); break;
            }
        }

        bottomSheetDialog.show();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img1", bottomSheetDialog);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img2", bottomSheetDialog);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img3", bottomSheetDialog);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img4", bottomSheetDialog);
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img5", bottomSheetDialog);
            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img6", bottomSheetDialog);
            }
        });

        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img7", bottomSheetDialog);
            }
        });

        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img8", bottomSheetDialog);
            }
        });

        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileAvatar("img9", bottomSheetDialog);
            }
        });
    }

    private void setProfileAvatar(String img1, BottomSheetDialog bottomSheetDialog) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("avatar", img1);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bottomSheetDialog.dismiss();
                        loadData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                String bio = "" + snapshot.child("bio").getValue();
                avatar = ""+snapshot.child("avatar").getValue();
                gender = ""+snapshot.child("gender").getValue();
                exam = ""+snapshot.child("exam").getValue();
                binding.nameTv.setText(name);
                binding.bioTv.setText(bio);
                if (! (gender.isEmpty() || gender.equals("null"))) binding.genderTv.setText(gender);
                if (! (exam.isEmpty() || exam.equals("null"))) binding.examTv.setText(exam);
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
                Toast.makeText(ProfileActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}