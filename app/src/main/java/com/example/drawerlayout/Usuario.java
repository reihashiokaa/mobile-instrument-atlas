package com.example.drawerlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "usuarios",
        indices = {
                @Index(value = {"email"}, unique = true)
        }
)
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String nome = "";

    @NonNull
    public String email = "";

    @ColumnInfo(name = "senha_hash")
    @NonNull
    public String senhaHash = "";

    @Nullable
    public byte[] foto;

    @ColumnInfo(name = "sessao_ativa")
    public boolean sessaoAtiva;
}