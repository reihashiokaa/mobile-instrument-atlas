package com.example.drawerlayout;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class DetailActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private DetalheViewModel detalheViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ImageView imageDetail =
                findViewById(R.id.imageDetail);

        TextView textTitle =
                findViewById(R.id.textTitle);

        TextView textDescription =
                findViewById(R.id.textDescription);

        Button buttonClose =
                findViewById(R.id.buttonClose);

        Button buttonPlay =
                findViewById(R.id.buttonPlay);

        // Pega o ID do instrumento enviado pelo fragment
        long instrumentoId =
                getIntent().getLongExtra(
                        ContratoApp.EXTRA_INSTRUMENTO_ID,
                        -1L
                );

        // Cria o ViewModel da tela de detalhes
        detalheViewModel =
                new ViewModelProvider(this)
                        .get(DetalheViewModel.class);

        // Observa o instrumento carregado pelo Room
        detalheViewModel.getInstrumento().observe(
                this,
                instrumento -> {

                    if (instrumento == null) {
                        return;
                    }

                    // Mostra o nome
                    textTitle.setText(instrumento.nome);

                    // Mostra os detalhes
                    textDescription.setText(
                            instrumento.detalhes
                    );

                    // Carrega a imagem
                    MidiaUtils.carregarImagem(
                            imageDetail,
                            instrumento.imagemUri
                    );

                    // Verifica se existe áudio
                    if (instrumento.audioUri == null
                            || instrumento.audioUri.isEmpty()) {

                        buttonPlay.setVisibility(View.GONE);

                    } else {

                        buttonPlay.setVisibility(View.VISIBLE);

                        buttonPlay.setOnClickListener(
                                view -> {

                                    // Libera uma reprodução anterior
                                    if (mediaPlayer != null) {
                                        mediaPlayer.release();
                                    }

                                    try {

                                        mediaPlayer =
                                                MediaPlayer.create(
                                                        this,
                                                        Uri.parse(
                                                                instrumento.audioUri
                                                        )
                                                );

                                        if (mediaPlayer != null) {
                                            mediaPlayer.start();
                                        }

                                    } catch (Exception e) {

                                        if (mediaPlayer != null) {
                                            mediaPlayer.release();
                                            mediaPlayer = null;
                                        }
                                    }
                                }
                        );
                    }
                }
        );

        // Carrega o instrumento pelo ID
        if (instrumentoId != -1L) {

            detalheViewModel.carregarInstrumento(
                    instrumentoId
            );
        }

        // Fecha a tela de detalhes
        buttonClose.setOnClickListener(
                view -> finish()
        );
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        // Libera o áudio quando a Activity é destruída
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}