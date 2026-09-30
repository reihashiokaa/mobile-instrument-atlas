package com.example.drawerlayout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class DetalheViewModel extends AndroidViewModel {

    private final InstrumentoDao instrumentoDao;
    private final LiveData<Usuario> usuarioAtivo;

    private final MediatorLiveData<Instrumento> instrumento =
            new MediatorLiveData<>();

    private LiveData<Instrumento> fonteInstrumento;

    public DetalheViewModel(@NonNull Application application) {
        super(application);

        AppDatabase banco =
                AppDatabase.getInstance(application);

        instrumentoDao = banco.instrumentoDao();
        usuarioAtivo = banco.usuarioDao().observarUsuarioAtivo();
    }

    public void carregarInstrumento(long instrumentoId) {
        if (fonteInstrumento != null) {
            instrumento.removeSource(fonteInstrumento);
        }

        fonteInstrumento =
                instrumentoDao.observarPorId(instrumentoId);

        instrumento.addSource(
                fonteInstrumento,
                instrumento::setValue
        );
    }

    public LiveData<Instrumento> getInstrumento() {
        return instrumento;
    }

    public LiveData<Usuario> getUsuarioAtivo() {
        return usuarioAtivo;
    }
}