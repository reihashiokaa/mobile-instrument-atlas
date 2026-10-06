package com.example.drawerlayout;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.Locale;

public class CadastroViewModel extends AndroidViewModel {

    private final UsuarioDao usuarioDao;

    private final MutableLiveData<Usuario> usuarioParaEdicao = new MutableLiveData<>();

    private final MutableLiveData<byte[]> foto = new MutableLiveData<>();

    private final MutableLiveData<EstadoOperacao> estadoInicializacao =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private final MutableLiveData<EstadoOperacao> estadoFoto =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private final MutableLiveData<EstadoOperacao> estadoSalvar =
            new MutableLiveData<>(EstadoOperacao.ocioso());

    private boolean iniciado;
    private boolean modoEdicao;
    private volatile boolean processandoFoto;
    private volatile boolean salvando;

    private long idUsuarioEmEdicao = -1L;

    public CadastroViewModel(@NonNull Application application) {
        super(application);

        AppDatabase banco = AppDatabase.getInstance(application);

        usuarioDao = banco.usuarioDao();
    }

    public void iniciar(boolean modoEdicao) {
        if (iniciado) {
            return;
        }

        iniciado = true;
        this.modoEdicao = modoEdicao;

        estadoInicializacao.setValue(EstadoOperacao.processando());

        if (!modoEdicao) {
            foto.setValue(null);

            estadoInicializacao.setValue(EstadoOperacao.sucesso());

            return;
        }

        AppExecutors.IO.execute(
                () -> {
                    try {
                        Usuario usuario = usuarioDao.buscarUsuarioAtivo();

                        if (usuario == null) {
                            estadoInicializacao.postValue(
                                    EstadoOperacao.erro(
                                            "SEM_SESSAO", "Nenhum usuário está conectado."));

                            return;
                        }

                        idUsuarioEmEdicao = usuario.id;

                        usuarioParaEdicao.postValue(usuario);

                        foto.postValue(usuario.foto == null ? null : usuario.foto.clone());

                        estadoInicializacao.postValue(EstadoOperacao.sucesso());

                    } catch (Exception e) {
                        estadoInicializacao.postValue(
                                EstadoOperacao.erro(
                                        "FALHA_INTERNA", "Não foi possível carregar o perfil."));
                    }
                });
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
        if (processandoFoto || salvando) {
            return;
        }

        if (caminhoArquivo == null || caminhoArquivo.trim().isEmpty()) {

            estadoFoto.setValue(
                    EstadoOperacao.erro("FOTO_INVALIDA", "Não foi possível carregar a foto."));

            return;
        }

        processandoFoto = true;

        estadoFoto.setValue(EstadoOperacao.processando());

        AppExecutors.IO.execute(
                () -> {
                    try {
                        byte[] bytes = FotoUtils.lerFotoCompactada(caminhoArquivo);

                        if (bytes == null || bytes.length == 0 || bytes.length > 200 * 1024) {

                            throw new IllegalArgumentException("Foto inválida.");
                        }

                        foto.postValue(bytes);

                        estadoFoto.postValue(EstadoOperacao.sucesso());

                    } catch (Exception e) {
                        estadoFoto.postValue(
                                EstadoOperacao.erro(
                                        "FOTO_INVALIDA", "Não foi possível processar a foto."));

                    } finally {
                        processandoFoto = false;
                    }
                });
    }

    public void salvar(String nome, String email, String senha) {
        if (salvando) {
            return;
        }

        if (processandoFoto) {
            estadoSalvar.setValue(
                    EstadoOperacao.erro("DADOS_INVALIDOS", "Aguarde o processamento da foto."));

            return;
        }

        String nomeNormalizado = nome == null ? "" : nome.trim();

        String emailNormalizado = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);

        String senhaInformada = senha == null ? "" : senha;

        byte[] fotoAtual = foto.getValue();

        if (fotoAtual != null) {
            fotoAtual = fotoAtual.clone();
        }

        if (nomeNormalizado.isEmpty() || emailNormalizado.isEmpty()) {

            estadoSalvar.setValue(EstadoOperacao.erro("DADOS_INVALIDOS", "Preencha nome e email."));

            return;
        }

        if (!modoEdicao && senhaInformada.isEmpty()) {
            estadoSalvar.setValue(EstadoOperacao.erro("DADOS_INVALIDOS", "Informe uma senha."));

            return;
        }

        if (!modoEdicao && fotoAtual == null) {
            estadoSalvar.setValue(
                    EstadoOperacao.erro("DADOS_INVALIDOS", "Tire uma foto para continuar."));

            return;
        }

        salvando = true;

        estadoSalvar.setValue(EstadoOperacao.processando());

        final byte[] fotoParaSalvar = fotoAtual;

        AppExecutors.IO.execute(
                () -> {
                    try {
                        if (modoEdicao) {
                            salvarEdicao(
                                    nomeNormalizado,
                                    emailNormalizado,
                                    senhaInformada,
                                    fotoParaSalvar);
                        } else {
                            salvarCadastro(
                                    nomeNormalizado,
                                    emailNormalizado,
                                    senhaInformada,
                                    fotoParaSalvar);
                        }

                    } catch (SQLiteConstraintException e) {
                        estadoSalvar.postValue(
                                EstadoOperacao.erro(
                                        "EMAIL_EM_USO", "Este email já está cadastrado."));

                    } catch (Exception e) {
                        estadoSalvar.postValue(
                                EstadoOperacao.erro(
                                        "FALHA_INTERNA", "Não foi possível salvar os dados."));

                    } finally {
                        salvando = false;
                    }
                });
    }

    private void salvarCadastro(String nome, String email, String senha, byte[] foto) {
        Usuario existente = usuarioDao.buscarPorEmail(email);

        if (existente != null) {
            estadoSalvar.postValue(
                    EstadoOperacao.erro("EMAIL_EM_USO", "Este email já está cadastrado."));

            return;
        }

        Usuario usuario = new Usuario();

        usuario.nome = nome;
        usuario.email = email;
        usuario.senhaHash = SenhaUtils.gerarHash(senha);
        usuario.foto = foto;
        usuario.sessaoAtiva = false;

        usuarioDao.inserir(usuario);

        estadoSalvar.postValue(EstadoOperacao.sucesso());
    }

    private void salvarEdicao(String nome, String email, String senha, byte[] foto) {
        Usuario usuarioAtivo = usuarioDao.buscarUsuarioAtivo();

        if (usuarioAtivo == null || usuarioAtivo.id != idUsuarioEmEdicao) {

            estadoSalvar.postValue(
                    EstadoOperacao.erro("SEM_SESSAO", "A sessão não está mais ativa."));

            return;
        }

        Usuario emailExistente = usuarioDao.buscarPorEmail(email);

        if (emailExistente != null && emailExistente.id != usuarioAtivo.id) {

            estadoSalvar.postValue(
                    EstadoOperacao.erro("EMAIL_EM_USO", "Este email já está cadastrado."));

            return;
        }

        String senhaHash = usuarioAtivo.senhaHash;

        if (!senha.isEmpty()) {
            senhaHash = SenhaUtils.gerarHash(senha);
        }

        byte[] fotoFinal = foto != null ? foto : usuarioAtivo.foto;

        int atualizados =
                usuarioDao.atualizarPerfil(usuarioAtivo.id, nome, email, senhaHash, fotoFinal);

        if (atualizados != 1) {
            estadoSalvar.postValue(
                    EstadoOperacao.erro("SEM_SESSAO", "A sessão não está mais ativa."));

            return;
        }

        estadoSalvar.postValue(EstadoOperacao.sucesso());
    }

    public void limparEstadoSalvar() {
        estadoSalvar.setValue(EstadoOperacao.ocioso());
    }

    public void limparEstadoFoto() {
        estadoFoto.setValue(EstadoOperacao.ocioso());
    }
}
