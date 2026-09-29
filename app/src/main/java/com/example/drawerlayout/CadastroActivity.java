package com.example.drawerlayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drawerlayout.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCancelar.setOnClickListener(v -> finish());
    }
}