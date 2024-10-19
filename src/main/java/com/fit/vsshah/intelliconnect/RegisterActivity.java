package com.fit.vsshah.intelliconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit.vsshah.intelliconnect.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private boolean strong_passw=false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });

        binding.passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.passwordCheckerLl.setVisibility(View.VISIBLE);

                binding.passwordCheckerLl.setVisibility(View.VISIBLE);
                if (binding.passwordEt.getText().toString().length()>=8){
                    binding.checkBox1.setChecked(true);
                    binding.labelTv1.setTextColor(getResources().getColor(R.color.green));
                    binding.labelTv1.setPaintFlags(binding.labelTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    binding.checkBox1.setChecked(false);
                    binding.labelTv1.setTextColor(getResources().getColor(R.color.red));
                    binding.labelTv1.setPaintFlags(binding.labelTv1.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

                String passw = binding.passwordEt.getText().toString().trim();
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(passw);
                boolean b = m.find();
                if (b){
                    binding.checkBox2.setChecked(true);
                    binding.labelTv2.setTextColor(getResources().getColor(R.color.green));
                    binding.labelTv2.setPaintFlags(binding.labelTv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    binding.checkBox2.setChecked(false);
                    binding.labelTv2.setTextColor(getResources().getColor(R.color.red));
                    binding.labelTv2.setPaintFlags(binding.labelTv2.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

                Pattern p1 = Pattern.compile("[A-Za-z]", Pattern.CASE_INSENSITIVE);
                Matcher m1 = p1.matcher(passw);
                boolean b1 = m1.find();
                if (b1){
                    binding.checkBox3.setChecked(true);
                    binding.labelTv3.setTextColor(getResources().getColor(R.color.green));
                    binding.labelTv3.setPaintFlags(binding.labelTv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    binding.checkBox3.setChecked(false);
                    binding.labelTv3.setTextColor(getResources().getColor(R.color.red));
                    binding.labelTv3.setPaintFlags(binding.labelTv3.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

                if (binding.checkBox1.isChecked() && binding.checkBox2.isChecked() && binding.checkBox3.isChecked()){
                    strong_passw=true;
                    binding.labelMainTv.setTextColor(getResources().getColor(R.color.green));
                    binding.labelMainTv.setText("* Password is strong enough!");
                    binding.filledTextField3.setVisibility(View.VISIBLE);
                }
                else {
                    strong_passw=false;
                    binding.labelMainTv.setTextColor(getResources().getColor(R.color.red));
                    binding.labelMainTv.setText("* Password must contain at least:");
                    binding.filledTextField3.setVisibility(View.GONE);
                }
            }
        });
    }

    private void verifyData() {
        if (binding.nameEt.getText().toString().trim().isEmpty()){
            binding.nameEt.setError("Enter your name");
            return;
        }
        if (binding.emailEt.getText().toString().trim().isEmpty()){
            binding.emailEt.setError("Enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.getText().toString().trim()).matches()){
            binding.emailEt.setError("Invalid Email");
            return;
        }
        if (binding.passwordEt.getText().toString().trim().isEmpty()){
            binding.passwordEt.setError("Create a password");
            return;
        }
        if (!strong_passw){
            binding.passwordEt.setError("Create strong password");
            return;
        }
        if (binding.rePasswordEt.getText().toString().trim().isEmpty()){
            binding.rePasswordEt.setError("Re-enter password");
            return;
        }
        if (!binding.passwordEt.getText().toString().trim().equals(binding.rePasswordEt.getText().toString().trim())){
            binding.rePasswordEt.setError("Password mismatch");
            return;
        }
        if (strong_passw){
            registerUser();
        }
    }

    private void registerUser() {
        progressDialog.show();
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            saveData(email);
                        }else {
                            Toast.makeText(RegisterActivity.this, "Couldn't register! Try again later...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void saveData(String email) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("avatar", "img1");
        hashMap.put("name", ""+binding.nameEt.getText().toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}