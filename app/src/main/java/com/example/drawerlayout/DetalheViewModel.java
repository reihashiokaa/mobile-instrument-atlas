package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetalheViewModel extends ViewModel {

    private final MutableLiveData<Instrumento> instrumento =
            new MutableLiveData<>();

    private final MutableLiveData<Usuario> usuarioAtivo =
            new MutableLiveData<>();

    public void carregarInstrumento(long instrumentoId) {
        instrumento.setValue(null);
    }

    public LiveData<Instrumento> getInstrumento() {
        return instrumento;
    }

    public LiveData<Usuario> getUsuarioAtivo() {
        return usuarioAtivo;
    }
}