package com.example.drawerlayout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Locale;

public class AuthViewModel extends AndroidViewModel {

    private final AppDatabase banco;
    private final UsuarioDao usuarioDao;

    private final LiveData<Usuario> usuarioAtivo;

    private final MutableLiveData<EstadoOperacao> estadoLogin =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private final MutableLiveData<EstadoOperacao> estadoLogout =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private volatile boolean loginEmAndamento;
    private volatile boolean logoutEmAndamento;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        banco = AppDatabase.getInstance(application);
        usuarioDao = banco.usuarioDao();

        usuarioAtivo = usuarioDao.observarUsuarioAtivo();
    }

    public LiveData<Usuario> getUsuarioAtivo() {
        return usuarioAtivo;
    }

    public LiveData<EstadoOperacao> getEstadoLogin() {
        return estadoLogin;
    }

    public LiveData<EstadoOperacao> getEstadoLogout() {
        return estadoLogout;
    }

    public void login(String email, String senha) {
        if (loginEmAndamento) {
            return;
        }

        String emailNormalizado =
                email == null
                        ? ""
                        : email.trim().toLowerCase(Locale.ROOT);

        String senhaInformada =
                senha == null ? "" : senha;

        if (emailNormalizado.isEmpty()
                || senhaInformada.isEmpty()) {

            estadoLogin.setValue(
                    EstadoOperacao.erro(
                            "DADOS_INVALIDOS",
                            "Preencha email e senha."
                    )
            );

            return;
        }

        loginEmAndamento = true;

        estadoLogin.setValue(
                EstadoOperacao.processando()
        );

        AppExecutors.IO.execute(() -> {
            try {
                Usuario usuario =
                        usuarioDao.buscarPorEmail(
                                emailNormalizado
                        );

                if (usuario == null
                        || !SenhaUtils.verificar(
                        senhaInformada,
                        usuario.senhaHash
                )) {

                    estadoLogin.postValue(
                            EstadoOperacao.erro(
                                    "CREDENCIAIS_INVALIDAS",
                                    "Email ou senha inválidos."
                            )
                    );

                    return;
                }

                banco.runInTransaction(() -> {
                    Usuario usuarioConfirmado =
                            usuarioDao.buscarPorId(
                                    usuario.id
                            );

                    if (usuarioConfirmado == null) {
                        throw new IllegalStateException(
                                "Usuário não encontrado."
                        );
                    }

                    usuarioDao.desativarSessoes();

                    int alterados =
                            usuarioDao.ativarSessao(
                                    usuario.id
                            );

                    if (alterados != 1) {
                        throw new IllegalStateException(
                                "Não foi possível iniciar a sessão."
                        );
                    }
                });

                estadoLogin.postValue(
                        EstadoOperacao.sucesso()
                );

            } catch (Exception e) {
                estadoLogin.postValue(
                        EstadoOperacao.erro(
                                "FALHA_INTERNA",
                                "Não foi possível realizar o login."
                        )
                );

            } finally {
                loginEmAndamento = false;
            }
        });
    }

    public void logout() {
        if (logoutEmAndamento) {
            return;
        }

        logoutEmAndamento = true;

        estadoLogout.setValue(
                EstadoOperacao.processando()
        );

        AppExecutors.IO.execute(() -> {
            try {
                usuarioDao.desativarSessoes();

                estadoLogout.postValue(
                        EstadoOperacao.sucesso()
                );

            } catch (Exception e) {
                estadoLogout.postValue(
                        EstadoOperacao.erro(
                                "FALHA_INTERNA",
                                "Não foi possível encerrar a sessão."
                        )
                );

            } finally {
                logoutEmAndamento = false;
            }
        });
    }

    public void limparEstadoLogin() {
        estadoLogin.setValue(
                EstadoOperacao.ocioso()
        );
    }

    public void limparEstadoLogout() {
        estadoLogout.setValue(
                EstadoOperacao.ocioso()
        );
    }
}