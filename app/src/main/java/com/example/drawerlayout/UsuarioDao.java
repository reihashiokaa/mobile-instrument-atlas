package com.example.drawerlayout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long inserir(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    Usuario buscarPorId(long id);

    @Query("SELECT * FROM usuarios WHERE sessao_ativa = 1 LIMIT 1")
    LiveData<Usuario> observarUsuarioAtivo();

    @Query("UPDATE usuarios SET sessao_ativa = 0 WHERE sessao_ativa = 1")
    void desativarSessoes();

    @Query("UPDATE usuarios SET sessao_ativa = 1 WHERE id = :id")
    int ativarSessao(long id);

    @Query(
            "UPDATE usuarios "
                    + "SET nome = :nome, "
                    + "email = :email, "
                    + "senha_hash = :senhaHash, "
                    + "foto = :foto "
                    + "WHERE id = :id AND sessao_ativa = 1")
    int atualizarPerfil(long id, String nome, String email, String senhaHash, byte[] foto);

    @Query("SELECT * FROM usuarios WHERE sessao_ativa = 1 LIMIT 1")
    Usuario buscarUsuarioAtivo();
}
