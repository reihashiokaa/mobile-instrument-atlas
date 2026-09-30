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

public class DetailActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private DetalheViewModel detalheViewModel;

    private Button buttonPlay;

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

        buttonPlay =
                findViewById(R.id.buttonPlay);

        // Pega o ID do instrumento enviado pelo Fragment
        long instrumentoId =
                getIntent().getLongExtra(
                        ContratoApp.EXTRA_INSTRUMENTO_ID,
                        -1L
                );

        // Verifica se o ID é válido
        if (instrumentoId <= 0) {

            Toast.makeText(
                    this,
                    "Instrumento inválido.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Cria o ViewModel da tela de detalhes
        detalheViewModel =
                new ViewModelProvider(this)
                        .get(DetalheViewModel.class);

        // Inicialmente esconde o botão de áudio
        buttonPlay.setVisibility(View.GONE);

        // Observa o instrumento carregado pelo Room
        detalheViewModel.getInstrumento().observe(
                this,
                instrumento -> {

                    // Instrumento não encontrado
                    if (instrumento == null) {

                        Toast.makeText(
                                this,
                                "Instrumento não encontrado.",
                                Toast.LENGTH_SHORT
                        ).show();

                        liberarMediaPlayer();

                        finish();

                        return;
                    }

                    // Mostra o nome
                    textTitle.setText(
                            instrumento.nome
                    );

                    // Mostra os detalhes
                    textDescription.setText(
                            instrumento.detalhes
                    );

                    // Carrega a imagem
                    MidiaUtils.carregarImagem(
                            imageDetail,
                            instrumento.imagemUri
                    );

                    // Verifica se o instrumento possui áudio
                    if (instrumento.audioUri == null
                            || instrumento.audioUri.isEmpty()) {

                        buttonPlay.setVisibility(View.GONE);

                        return;
                    }

                    // Mostra o botão de áudio
                    buttonPlay.setVisibility(View.VISIBLE);

                    // Define o clique do botão Play
                    buttonPlay.setOnClickListener(
                            view -> {

                                // Libera um áudio anterior
                                liberarMediaPlayer();

                                try {

                                    mediaPlayer =
                                            MediaPlayer.create(
                                                    this,
                                                    Uri.parse(
                                                            instrumento.audioUri
                                                    )
                                            );

                                    // Verifica se o player foi criado
                                    if (mediaPlayer == null) {

                                        Toast.makeText(
                                                this,
                                                "Áudio indisponível.",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;
                                    }

                                    // Libera o player quando o áudio terminar
                                    mediaPlayer.setOnCompletionListener(
                                            player -> liberarMediaPlayer()
                                    );

                                    // Começa a reprodução
                                    mediaPlayer.start();

                                } catch (Exception e) {

                                    liberarMediaPlayer();

                                    Toast.makeText(
                                            this,
                                            "Não foi possível reproduzir o áudio.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    );
                }
        );

        // Carrega o instrumento pelo ID
        detalheViewModel.carregarInstrumento(
                instrumentoId
        );

        // Fecha a tela de detalhes
        buttonClose.setOnClickListener(
                view -> finish()
        );
    }

    // Libera o MediaPlayer com segurança
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

        // Interrompe o áudio quando a Activity deixa de estar visível
        liberarMediaPlayer();
    }

    @Override
    protected void onDestroy() {

        liberarMediaPlayer();

        super.onDestroy();
    }
}