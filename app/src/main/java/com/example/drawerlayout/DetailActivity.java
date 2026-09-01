package com.example.drawerlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ImageView imageDetail = findViewById(R.id.imageDetail);
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textDescription = findViewById(R.id.textDescription);
        Button buttonClose = findViewById(R.id.buttonClose);
        Button buttonPlay = findViewById(R.id.buttonPlay);


        // recebe os dados enviados pelo fragment
        String nome = getIntent().getStringExtra("nome");
        String descricao = getIntent().getStringExtra("descricao");
        String detalhes = getIntent().getStringExtra("detalhes");
        int imagem = getIntent().getIntExtra("imagem", 0);
        int som = getIntent().getIntExtra("som", 0);
        // esconde o botão quando o item não possui áudio
        if (som == 0) {
            buttonPlay.setVisibility(View.GONE);
        }

        // coloca os dados na tela
        textTitle.setText(nome);
        if (detalhes != null) {
            textDescription.setText(detalhes);
        } else {
            textDescription.setText(descricao);
        }

        if (imagem != 0) {
            imageDetail.setImageResource(imagem);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // reproduz o som do instrumento
        buttonPlay.setOnClickListener(view -> {

            if (som != 0) {

                // libera uma reprodução anterior, caso exista
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                mediaPlayer = MediaPlayer.create(this, som);
                mediaPlayer.start();
            }
        });

        // fecha a tela de detalhes e volta para a tela principal
        buttonClose.setOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // interrompe e libera o áudio ao fechar a Activity
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}