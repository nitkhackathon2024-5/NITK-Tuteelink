package com.fit.vsshah.intelliconnect;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fit.vsshah.intelliconnect.databinding.ActivityQuizBinding;
import com.fit.vsshah.intelliconnect.databinding.ActivitySubjectDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetailsActivity extends AppCompatActivity {

    private ActivitySubjectDetailsBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySubjectDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String lbl = getIntent().getStringExtra("subject");
        List<String> dataset = new ArrayList<String>();
        switch (lbl){
            case "phy":
                binding.labelTv.setText("Physics");
                dataset.add("Mechanics-I");
                dataset.add("Mechanics-II");
                dataset.add("Thermodynamics");
                dataset.add("Electromagnetism");
                dataset.add("Optics");
                dataset.add("Modern Physics");
                break;
            case "chem":
                binding.labelTv.setText("Chemistry");
                dataset.add("Mole Concept");
                dataset.add("Atomic Structure");
                dataset.add("Solid State");
                dataset.add("S-Block Elements");
                dataset.add("P-Block Elements");
                dataset.add("Alcohols, Phenols & Ethers");
                break;
            case "math":
                binding.labelTv.setText("Mathematics");
                dataset.add("Progressions");
                dataset.add("Set Theory");
                dataset.add("Probability");
                dataset.add("Coordinate Geometry");
                dataset.add("Calculus");
                dataset.add("Vectors & 3-D");
                break;
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.physicsRv.setLayoutManager(llm);
        binding.physicsRv.setHasFixedSize(true);
        AdapterTopics adapter = new AdapterTopics(this, dataset);
        binding.physicsRv.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}