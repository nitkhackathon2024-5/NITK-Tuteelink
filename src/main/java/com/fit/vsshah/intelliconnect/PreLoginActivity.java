package com.fit.vsshah.intelliconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit.vsshah.intelliconnect.databinding.ActivityPreLoginBinding;

public class PreLoginActivity extends AppCompatActivity {

    private ActivityPreLoginBinding binding;
    private int view_count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPreLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.flipper.setInAnimation(PreLoginActivity.this,R.anim.slide_right);
                binding.flipper.setOutAnimation(PreLoginActivity.this,R.anim.slide_left);
                binding.flipper.showNext();
                view_count++;

                switch (view_count){
                    case 2:
                        binding.holder1Iv.setBackgroundResource(R.drawable.holder2);
                        binding.holder2Iv.setBackgroundResource(R.drawable.holder1);break;
                    case 3:
                        binding.holder2Iv.setBackgroundResource(R.drawable.holder2);
                        binding.holder3Iv.setBackgroundResource(R.drawable.holder1);break;
                    case 4:
                        binding.holder3Iv.setBackgroundResource(R.drawable.holder2);
                        binding.holder4Iv.setBackgroundResource(R.drawable.holder1);break;
                    case 5:
                        binding.holder4Iv.setBackgroundResource(R.drawable.holder2);
                        binding.holder5Iv.setBackgroundResource(R.drawable.holder1);break;
                }

                if (view_count==5){
                    binding.joinBtn.setVisibility(View.VISIBLE);
                    binding.nextBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreLoginActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}