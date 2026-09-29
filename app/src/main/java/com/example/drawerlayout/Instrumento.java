package com.example.drawerlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "instrumentos",
        foreignKeys = {
                @ForeignKey(
                        entity = Familia.class,
                        parentColumns = "id",
                        childColumns = "familia_id",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index("familia_id")
        }
)
public class Instrumento {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "familia_id")
    public long familiaId;

    @NonNull
    public String nome = "";

    @NonNull
    public String descricao = "";

    @NonNull
    public String detalhes = "";

    @ColumnInfo(name = "imagem_uri")
    @NonNull
    public String imagemUri = "";

    @ColumnInfo(name = "audio_uri")
    @Nullable
    public String audioUri;

    public boolean variacao;

    public int ordem;
}