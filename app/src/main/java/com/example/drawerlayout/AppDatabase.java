package com.example.drawerlayout;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {Usuario.class, Familia.class, Instrumento.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    private static final MutableLiveData<EstadoOperacao> estadoInicializacao =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    public abstract UsuarioDao usuarioDao();

    public abstract FamiliaDao familiaDao();

    public abstract InstrumentoDao instrumentoDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {

                    Context applicationContext = context.getApplicationContext();

                    AppDatabase banco =
                            Room.databaseBuilder(
                                            applicationContext,
                                            AppDatabase.class,
                                            ContratoApp.BANCO_NOME)
                                    .build();

                    INSTANCE = banco;

                    estadoInicializacao.postValue(EstadoOperacao.processando());

                    AppExecutors.IO.execute(
                            () -> {
                                try {
                                    DadosIniciais.popular(applicationContext, banco);

                                    estadoInicializacao.postValue(EstadoOperacao.sucesso());

                                } catch (Exception e) {
                                    estadoInicializacao.postValue(
                                            EstadoOperacao.erro(
                                                    "FALHA_INTERNA",
                                                    "Não foi possível carregar o catálogo."));
                                }
                            });
                }
            }
        }

        return INSTANCE;
    }

    public static LiveData<EstadoOperacao> getEstadoInicializacao() {
        return estadoInicializacao;
    }
}
