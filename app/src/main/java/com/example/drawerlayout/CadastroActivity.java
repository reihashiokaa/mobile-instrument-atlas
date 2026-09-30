package com.example.drawerlayout;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.drawerlayout.databinding.ActivityCadastroBinding;

import java.io.File;
import java.io.IOException;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private CadastroViewModel viewModel;
    private boolean modoEdicao;

    private Uri photoUri;
    private File photoFile;
    private String caminhoFotoConfirmada;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    abrirCamera();
                } else {
                    Toast.makeText(this, getString(R.string.cadastro_erro_permissao), Toast.LENGTH_LONG).show();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), sucesso -> {
                if (sucesso && photoFile != null && photoFile.exists() && photoFile.length() > 0) {
                    viewModel.carregarFoto(photoFile.getAbsolutePath());
                    caminhoFotoConfirmada = photoFile.getAbsolutePath();
                } else {
                    Toast.makeText(this, getString(R.string.cadastro_cancelou_captura), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        modoEdicao = getIntent().getBooleanExtra(ContratoApp.EXTRA_MODO_EDICAO, false);

        if (modoEdicao) {
            binding.textTituloCadastro.setText(getString(R.string.cadastro_titulo_editar));
            binding.textAjudaSenha.setVisibility(View.VISIBLE);
            binding.buttonCamera.setText(getString(R.string.cadastro_btn_trocar_foto));
        }

        viewModel = new ViewModelProvider(this).get(CadastroViewModel.class);
        configurarObservadores();
        viewModel.iniciar(modoEdicao);

        binding.buttonCancelar.setOnClickListener(v -> finish());

        binding.buttonSalvar.setOnClickListener(v -> {
            String nome = binding.editNome.getText().toString();
            String email = binding.editEmail.getText().toString();
            String senha = binding.editSenha.getText().toString();
            viewModel.salvar(nome, email, senha);
        });

        binding.buttonCamera.setOnClickListener(v -> requestPermissionLauncher.launch(Manifest.permission.CAMERA));
    }

    private void configurarObservadores() {
        viewModel.getEstadoInicializacao().observe(this, estado -> {
            if (estado != null && estado.status == EstadoOperacao.Status.ERRO && "SEM_SESSAO".equals(estado.codigo)) {
                Toast.makeText(this, estado.mensagem, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        viewModel.getUsuarioParaEdicao().observe(this, usuario -> {
            if (usuario != null && binding.editNome.getText().toString().isEmpty()) {
                binding.editNome.setText(usuario.nome);
                binding.editEmail.setText(usuario.email);
            }
        });

        viewModel.getFoto().observe(this, bytesFoto -> {
            if (bytesFoto != null) {
                Bitmap bitmap = FotoUtils.decodificar(bytesFoto);
                if (bitmap != null) {
                    binding.imageFotoPerfil.setImageBitmap(bitmap);
                }
            }
        });

        viewModel.getEstadoSalvar().observe(this, estado -> {
            if (estado == null || estado.status == EstadoOperacao.Status.OCIOSO) return;

            if (estado.status == EstadoOperacao.Status.PROCESSANDO) {
                binding.progressCadastro.setVisibility(View.VISIBLE);
                bloquearCampos(true);
            } else if (estado.status == EstadoOperacao.Status.SUCESSO) {
                binding.progressCadastro.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.cadastro_sucesso), Toast.LENGTH_SHORT).show();
                viewModel.limparEstadoSalvar();
                finish();
            } else if (estado.status == EstadoOperacao.Status.ERRO) {
                binding.progressCadastro.setVisibility(View.GONE);
                bloquearCampos(false);
                Toast.makeText(this, estado.mensagem, Toast.LENGTH_LONG).show();
                viewModel.limparEstadoSalvar();
            }
        });
    }

    private void bloquearCampos(boolean bloquear) {
        binding.buttonSalvar.setEnabled(!bloquear);
        binding.editNome.setEnabled(!bloquear);
        binding.editEmail.setEnabled(!bloquear);
        binding.editSenha.setEnabled(!bloquear);
        binding.buttonCamera.setEnabled(!bloquear);
    }

    private void abrirCamera() {
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (storageDir == null) return;
            photoFile = File.createTempFile("FOTO_", ".jpg", storageDir);
            String authority = getPackageName() + ".fileprovider";
            photoUri = FileProvider.getUriForFile(this, authority, photoFile);
            takePictureLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.cadastro_erro_arquivo), Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.cadastro_erro_sem_camera), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (photoFile != null) outState.putString("caminho_pendente", photoFile.getAbsolutePath());
        if (caminhoFotoConfirmada != null) outState.putString("caminho_confirmado", caminhoFotoConfirmada);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String caminhoPendente = savedInstanceState.getString("caminho_pendente");
        if (caminhoPendente != null) photoFile = new File(caminhoPendente);
        caminhoFotoConfirmada = savedInstanceState.getString("caminho_confirmado");
    }
}