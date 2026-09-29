package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    // Mantido temporariamente para as telas antigas continuarem compilando.
    private final MutableLiveData<String> familiaSelecionada =
            new MutableLiveData<>();

    // Novo contrato do catálogo.
    private final MutableLiveData<List<Familia>> familias =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Long> familiaSelecionadaId =
            new MutableLiveData<>(ContratoApp.FAMILIA_INICIAL_ID);

    private final MutableLiveData<List<Instrumento>> instrumentos =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<List<Instrumento>> variacoes =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<EstadoOperacao> estadoCatalogo =
            new MutableLiveData<>(
                    EstadoOperacao.erro(
                            "NAO_IMPLEMENTADO",
                            "Catálogo ainda não conectado ao banco."
                    )
            );

    // Compatibilidade temporária com o código antigo.

    public LiveData<String> getFamiliaSelecionada() {
        return familiaSelecionada;
    }

    public void setFamiliaSelecionada(String familia) {
        familiaSelecionada.setValue(familia);
    }

    // Novo contrato.

    public LiveData<List<Familia>> getFamilias() {
        return familias;
    }

    public LiveData<Long> getFamiliaSelecionadaId() {
        return familiaSelecionadaId;
    }

    public LiveData<List<Instrumento>> getInstrumentos() {
        return instrumentos;
    }

    public LiveData<List<Instrumento>> getVariacoes() {
        return variacoes;
    }

    public LiveData<EstadoOperacao> getEstadoCatalogo() {
        return estadoCatalogo;
    }

    public void selecionarFamilia(long familiaId) {
        familiaSelecionadaId.setValue(familiaId);
    }
}