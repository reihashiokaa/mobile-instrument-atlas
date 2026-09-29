package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CadastroViewModel extends ViewModel {

    private final MutableLiveData<Usuario> usuarioParaEdicao =
            new MutableLiveData<>(null);

    private final MutableLiveData<byte[]> foto =
            new MutableLiveData<>(null);

    private final MutableLiveData<EstadoOperacao> estadoInicializacao =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private final MutableLiveData<EstadoOperacao> estadoFoto =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private final MutableLiveData<EstadoOperacao> estadoSalvar =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private boolean iniciado = false;

    public void iniciar(boolean modoEdicao) {
        if (iniciado) {
            return;
        }

        iniciado = true;

        if (modoEdicao) {
            estadoInicializacao.setValue(
                    EstadoOperacao.erro(
                            "NAO_IMPLEMENTADO",
                            "Modo de edição ainda não implementado."
                    )
            );
        } else {
            estadoInicializacao.setValue(EstadoOperacao.sucesso());
        }
    }

    public LiveData<Usuario> getUsuarioParaEdicao() {
        return usuarioParaEdicao;
    }

    public LiveData<byte[]> getFoto() {
        return foto;
    }

    public LiveData<EstadoOperacao> getEstadoInicializacao() {
        return estadoInicializacao;
    }

    public LiveData<EstadoOperacao> getEstadoFoto() {
        return estadoFoto;
    }

    public LiveData<EstadoOperacao> getEstadoSalvar() {
        return estadoSalvar;
    }

    public void carregarFoto(String caminhoArquivo) {
        estadoFoto.setValue(
                EstadoOperacao.erro(
                        "NAO_IMPLEMENTADO",
                        "Processamento da foto ainda não implementado."
                )
        );
    }

    public void salvar(String nome, String email, String senha) {
        estadoSalvar.setValue(
                EstadoOperacao.erro(
                        "NAO_IMPLEMENTADO",
                        "Persistência ainda não implementada."
                )
        );
    }

    public void limparEstadoSalvar() {
        estadoSalvar.setValue(EstadoOperacao.ocioso());
    }

    public void limparEstadoFoto() {
        estadoFoto.setValue(EstadoOperacao.ocioso());
    }
}