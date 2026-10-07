package com.example.drawerlayout;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class DetalheActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private DetalheViewModel detalheViewModel;

    private Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalhe);

        ImageView imageDetail = findViewById(R.id.imageDetail);

        TextView textTitle = findViewById(R.id.textTitle);

        TextView textDescription = findViewById(R.id.textDescription);

        Button buttonClose = findViewById(R.id.buttonClose);

        buttonPlay = findViewById(R.id.buttonPlay);

        long instrumentoId = getIntent().getLongExtra(ContratoApp.EXTRA_INSTRUMENTO_ID, -1L);

        if (instrumentoId <= 0) {

            Toast.makeText(this, "Instrumento inválido.", Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        detalheViewModel = new ViewModelProvider(this).get(DetalheViewModel.class);

        detalheViewModel
                .getUsuarioAtivo()
                .observe(
                        this,
                        usuario -> {
                            if (usuario == null) {

                                liberarMediaPlayer();

                                finish();
                            }
                        });

        buttonPlay.setVisibility(View.GONE);

        detalheViewModel
                .getInstrumento()
                .observe(
                        this,
                        instrumento -> {
                            if (instrumento == null) {

                                Toast.makeText(
                                                this,
                                                "Instrumento não encontrado.",
                                                Toast.LENGTH_SHORT)
                                        .show();

                                liberarMediaPlayer();

                                finish();

                                return;
                            }

                            textTitle.setText(instrumento.nome);

                            textDescription.setText(instrumento.detalhes);

                            MidiaUtils.carregarImagem(imageDetail, instrumento.imagemUri);

                            if (instrumento.audioUri == null || instrumento.audioUri.isEmpty()) {

                                buttonPlay.setVisibility(View.GONE);

                                liberarMediaPlayer();

                                return;
                            }

                            buttonPlay.setVisibility(View.VISIBLE);

                            buttonPlay.setOnClickListener(
                                    view -> {
                                        liberarMediaPlayer();

                                        try {

                                            mediaPlayer =
                                                    MediaPlayer.create(
                                                            this, Uri.parse(instrumento.audioUri));

                                            if (mediaPlayer == null) {

                                                Toast.makeText(
                                                                this,
                                                                "Áudio indisponível.",
                                                                Toast.LENGTH_SHORT)
                                                        .show();

                                                return;
                                            }

                                            mediaPlayer.setOnCompletionListener(
                                                    player -> liberarMediaPlayer());

                                            mediaPlayer.start();

                                        } catch (Exception e) {

                                            liberarMediaPlayer();

                                            Toast.makeText(
                                                            this,
                                                            "Não foi possível reproduzir o áudio.",
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                        });

        detalheViewModel.carregarInstrumento(instrumentoId);

        buttonClose.setOnClickListener(view -> finish());
    }

    private void liberarMediaPlayer() {

        if (mediaPlayer != null) {

            try {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

            } catch (Exception e) {
                // O player pode já ter sido liberado.
            }

            mediaPlayer.release();

            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {

        super.onStop();

        liberarMediaPlayer();
    }

    @Override
    protected void onDestroy() {

        liberarMediaPlayer();

        super.onDestroy();
    }
}
