package com.example.drawerlayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ImageView imageDetail = findViewById(R.id.imageDetail);
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textDescription = findViewById(R.id.textDescription);
        Button buttonClose = findViewById(R.id.buttonClose);


        // recebe os dados enviados pelo fragment
        String nome = getIntent().getStringExtra("nome");
        String descricao = getIntent().getStringExtra("descricao");
        int imagem = getIntent().getIntExtra("imagem", 0);

        // coloca os dados na tela
        textTitle.setText(nome);
        textDescription.setText(descricao);

        if (imagem != 0) {
            imageDetail.setImageResource(imagem);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // fecha a tela de detalhes e volta para a tela principal
        buttonClose.setOnClickListener(view -> finish());
    }
}