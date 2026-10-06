package com.example.drawerlayout;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    private static final String CHAVE_FAMILIA_ID = "familia_selecionada_id";

    private final FamiliaDao familiaDao;

    private final SavedStateHandle savedStateHandle;

    private final LiveData<List<Familia>> familias;
    private final LiveData<EstadoOperacao> estadoCatalogo;

    private final MediatorLiveData<List<Instrumento>> instrumentos = new MediatorLiveData<>();

    private final MediatorLiveData<List<Instrumento>> variacoes = new MediatorLiveData<>();

    private LiveData<FamiliaComInstrumentos> fonteFamiliaAtual;

    // Compatibilidade temporária com o código antigo.
    private final MediatorLiveData<String> familiaSelecionada = new MediatorLiveData<>();

    public SharedViewModel(@NonNull Application application, SavedStateHandle savedStateHandle) {
        super(application);

        this.savedStateHandle = savedStateHandle;

        if (!savedStateHandle.contains(CHAVE_FAMILIA_ID)) {
            savedStateHandle.set(CHAVE_FAMILIA_ID, ContratoApp.FAMILIA_INICIAL_ID);
        }

        AppDatabase banco = AppDatabase.getInstance(application);

        familiaDao = banco.familiaDao();

        familias = familiaDao.observarFamilias();

        estadoCatalogo = AppDatabase.getEstadoInicializacao();

        instrumentos.setValue(new ArrayList<>());
        variacoes.setValue(new ArrayList<>());

        configurarFamiliaSelecionada();
    }

    private void configurarFamiliaSelecionada() {
        LiveData<Long> familiaId =
                savedStateHandle.getLiveData(CHAVE_FAMILIA_ID, ContratoApp.FAMILIA_INICIAL_ID);

        instrumentos.addSource(
                familiaId,
                id -> {
                    if (id != null) {
                        observarFamilia(id);
                    }
                });

        familiaSelecionada.addSource(familiaId, id -> familiaSelecionada.setValue(nomeFamilia(id)));

        familiaSelecionada.addSource(
                familias,
                lista -> {
                    Long id = familiaId.getValue();

                    if (id != null) {
                        familiaSelecionada.setValue(nomeFamilia(id));
                    }
                });
    }

    private void observarFamilia(long familiaId) {
        if (fonteFamiliaAtual != null) {
            instrumentos.removeSource(fonteFamiliaAtual);
            variacoes.removeSource(fonteFamiliaAtual);
        }

        fonteFamiliaAtual = familiaDao.observarFamiliaComInstrumentos(familiaId);

        instrumentos.addSource(
                fonteFamiliaAtual,
                familiaComInstrumentos -> atualizarListas(familiaComInstrumentos));

        variacoes.addSource(
                fonteFamiliaAtual,
                familiaComInstrumentos -> atualizarListas(familiaComInstrumentos));
    }

    private void atualizarListas(FamiliaComInstrumentos familiaComInstrumentos) {
        List<Instrumento> principais = new ArrayList<>();

        List<Instrumento> listaVariacoes = new ArrayList<>();

        if (familiaComInstrumentos != null && familiaComInstrumentos.instrumentos != null) {

            for (Instrumento instrumento : familiaComInstrumentos.instrumentos) {

                if (instrumento.variacao) {
                    listaVariacoes.add(instrumento);
                } else {
                    principais.add(instrumento);
                }
            }
        }

        principais.sort(Comparator.comparingInt(instrumento -> instrumento.ordem));

        listaVariacoes.sort(Comparator.comparingInt(instrumento -> instrumento.ordem));

        instrumentos.setValue(principais);
        variacoes.setValue(listaVariacoes);
    }

    public LiveData<List<Familia>> getFamilias() {
        return familias;
    }

    public LiveData<Long> getFamiliaSelecionadaId() {
        return savedStateHandle.getLiveData(CHAVE_FAMILIA_ID, ContratoApp.FAMILIA_INICIAL_ID);
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
        savedStateHandle.set(CHAVE_FAMILIA_ID, familiaId);
    }

    // Métodos antigos mantidos temporariamente.

    public LiveData<String> getFamiliaSelecionada() {
        return familiaSelecionada;
    }

    public void setFamiliaSelecionada(String familia) {
        if (familia == null) {
            return;
        }

        switch (familia.toLowerCase()) {
            case "corda":
            case "cordas":
                selecionarFamilia(1L);
                break;

            case "sopro":
                selecionarFamilia(2L);
                break;

            case "percussão":
            case "percussao":
                selecionarFamilia(3L);
                break;
        }
    }

    private String nomeFamilia(Long id) {
        List<Familia> lista = familias.getValue();

        if (lista != null) {
            for (Familia familia : lista) {
                if (familia.id == id) {
                    return familia.nome;
                }
            }
        }

        if (id == 1L) {
            return "Corda";
        }

        if (id == 2L) {
            return "Sopro";
        }

        if (id == 3L) {
            return "Percussão";
        }

        return "";
    }
}
